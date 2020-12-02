package com.example.shwiper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Code based on:
 * https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
 * https://www.youtube.com/watch?v=Vyqz_-sJGFk&t=1187s
 * Modified by Liam-Thomas Flynn on 21/09/2020
 */
public class adsLikedListAdapter extends RecyclerView.Adapter<adsLikedListAdapter.MyViewHolder> {

    private static final String TAG = "ListCourseAdapter";

    private List<Ad> likedAds;
    private Context mContext;
    private OnLikedAdsListener onLikedAdsListener;

    public adsLikedListAdapter(Context mContext, List<Ad> likedAds, OnLikedAdsListener onLikedAdsListener) {
        this.mContext = mContext;
        this.likedAds = likedAds;
        this.onLikedAdsListener = onLikedAdsListener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView adPicture;
        public TextView adTitle;
        public TextView priceValue;
        public RelativeLayout parentLayout;
        public OnLikedAdsListener onLikedAdsListener;
        public MyViewHolder(View itemView, OnLikedAdsListener onLikedAdsListener) {
            super(itemView);
            adPicture = itemView.findViewById(R.id.adPicture);
            adTitle = itemView.findViewById(R.id.adTitle);
            priceValue = itemView.findViewById(R.id.priceValue);
            parentLayout = itemView.findViewById(R.id.parentRelative);
            this.onLikedAdsListener = onLikedAdsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLikedAdsListener.onLikedAdsClick(getAdapterPosition());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public adsLikedListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relativelayout_likedadslist, parent, false);
        MyViewHolder vh = new MyViewHolder(v, onLikedAdsListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.adTitle.setText(likedAds.get(position).getTitle());
        holder.priceValue.setText(likedAds.get(position).getPrice());
        Picasso.get()
                .load(likedAds.get(position).getImage())
                .fit()
                .centerCrop()
                .into(holder.adPicture);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return likedAds.size();
    }

    //Virtual method to implement method when item is clicked
    public interface OnLikedAdsListener{
        void onLikedAdsClick(int position);
    }
}


