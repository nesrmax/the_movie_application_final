package com.example.hamadaelsha3r.the_movie_application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewsAdabter extends RecyclerView.Adapter<ReviewsAdabter.review_view_holder> {

    private final List<MovieReviews> reviews;

    public ReviewsAdabter(Context context, List<MovieReviews> reviews) {
        Context context1 = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public review_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);

        return new review_view_holder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull review_view_holder holder, int position) {

        holder.auther.setText(reviews.get(position).getAuthor());
        String url = reviews.get(position).getLink();
        holder.the_link.setText(url);
        Linkify.addLinks(holder.the_link,Linkify.WEB_URLS);

    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class review_view_holder extends RecyclerView.ViewHolder {


        TextView auther;
        final TextView the_link;

        public review_view_holder(View itemView) {
            super(itemView);

            auther = itemView.findViewById(R.id.rev_txt);
            the_link = itemView.findViewById(R.id.rev_txt1);
        }


    }

}
