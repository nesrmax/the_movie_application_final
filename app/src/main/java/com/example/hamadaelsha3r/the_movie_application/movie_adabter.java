package com.example.hamadaelsha3r.the_movie_application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class movie_adabter extends RecyclerView.Adapter<movie_adabter.Movie_view_holder> {


    private final Context context1;
    List<movie> movie_list ;

    public movie_adabter(Context context, List<movie> results) {

        movie_list = results;
        context1=context;

    }


    @NonNull
    @Override
    public Movie_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("hassan","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");


        View row1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        Log.d("hassan","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


        Movie_view_holder viewHolder = new Movie_view_holder(row1);

        Log.d("hassan","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull Movie_view_holder holder, final int position) {

        Log.d("hassan","jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");


        Picasso.with(context1).load("http://image.tmdb.org/t/p/w185"
                + movie_list.get(position).getPosterPath()).into(holder.poster);

        Log.d("hassan","ppppppppppppppppppppppppppppppppppppppppppppppp");


        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("hassan","wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");


                Intent intent = new Intent(context1,Activity_detail.class);

                Bundle bundle = new Bundle();
                bundle.putString("title", movie_list.get(position).getTitle());
                bundle.putString("date", movie_list.get(position).getReleaseDate());
                bundle.putString("vote", movie_list.get(position).getVoteAverage());
                bundle.putString("image", movie_list.get(position).getPosterPath());
                bundle.putString("overview", movie_list.get(position).getOverview());
                bundle.putString("id",movie_list.get(position).getId());

                Log.d("hassan","eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");


                intent.putExtras(bundle);

                context1.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return movie_list.size();
    }

    class Movie_view_holder extends RecyclerView.ViewHolder {


        final ImageView poster;

        Movie_view_holder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
        }
    }

}
