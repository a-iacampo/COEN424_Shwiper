package com.example.shwiper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder>{

    private List<Ad> items;
    private Context context;
    private String url;

    public CardStackAdapter(List<Ad> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        url = items.get(position).getUrl();
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView image;
        TextView title, location, description, price;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewID);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_name);
            location = itemView.findViewById(R.id.item_location);
            description = itemView.findViewById(R.id.item_description);
            price = itemView.findViewById(R.id.item_price);

            cardView.setOnClickListener(cardListener);
        }

        void setData(Ad data) {
            if (!data.getImage().equals("")) {
                Picasso.get()
                        .load(data.getImage())
                        .fit()
                        .centerCrop()
                        .into(image);
                title.setText(data.getTitle());
                price.setText("$ " + data.getPrice());
                location.setText(data.getLocation());
                description.setText(data.getDescription());
            }
        }
    }

    private View.OnClickListener cardListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, OnCardClick.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    };

    public List<Ad> getItems() {
        return items;
    }

    public void setItems(List<Ad> items) {
        this.items = items;
    }

    public void openDialog(){
        cardOnClickDialog cardDialog = new cardOnClickDialog();
        cardDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "cardDialog");
    }

}