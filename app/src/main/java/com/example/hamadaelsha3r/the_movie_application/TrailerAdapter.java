package com.example.hamadaelsha3r.the_movie_application;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.trailer_view_holder> {

    private final Context context;

    final List<MovieTrailer> trailers;

    public TrailerAdapter(Context context, List<MovieTrailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public trailer_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false);


        return new trailer_view_holder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final trailer_view_holder holder, final int position) {

        holder.Tname.setText(trailers.get(position).trailer_name);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Youtube(context, trailers.get(position).trailer_key);

            }
        });


    }


    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class trailer_view_holder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        TextView Tname;

        trailer_view_holder(View itemView) {
            super(itemView);

            Tname = itemView.findViewById(R.id.tral_name);
            layout = itemView.findViewById(R.id.the_whole_layout);
        }
    }


    public static void Youtube(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

}
