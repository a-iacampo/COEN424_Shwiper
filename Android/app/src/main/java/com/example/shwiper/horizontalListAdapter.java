package com.example.shwiper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class horizontalListAdapter extends RecyclerView.Adapter<horizontalListAdapter.MyViewHolder> {

    private static final String TAG = "ListCourseAdapter";

    private List<String> adImages;

    public horizontalListAdapter(List<String> adImages) {
        this.adImages = adImages;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView adPicture;
        public RelativeLayout parentLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            adPicture = itemView.findViewById(R.id.horizontal_item_image);
            parentLayout = itemView.findViewById(R.id.parentRelative);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_image_layout, parent, false);
        horizontalListAdapter.MyViewHolder vh = new horizontalListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get()
                .load(adImages.get(position))
                .fit()
                .centerCrop()
                .into(holder.adPicture);
    }

    @Override
    public int getItemCount() {
        return adImages.size();
    }
}
