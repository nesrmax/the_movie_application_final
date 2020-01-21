package com.example.hamadaelsha3r.the_movie_application;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Activity_detail extends AppCompatActivity {

    private TextView title;
    private TextView rlease_date;
    CheckBox favoriteCheckBox;
    TextView vote;
    TextView overview;
    TextView num_of_rev;
    ImageView poster_details;
    String link, trailer_name, trailer_key, trailer_id,
            rev_author, rev_content, rev_id, rev_link, mov_name;
    public static final ArrayList<MovieTrailer> trailers = new ArrayList<>();
    public static final ArrayList<MovieReviews> reviews = new ArrayList<>();
    String key = "3eb6a7eb34657be719cf18315c117723";
    RecyclerView recyclerView_review, recyclerView_trailer;
    TrailerAdapter trail_adapter;
    ReviewsAdabter review_adapter;
    MovieDatabase movieDb;
    movie mMovie;
    // MenuItem item1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        poster_details = findViewById(R.id.Image_poster_detail);
        title = findViewById(R.id.title_detail_2);
        rlease_date = findViewById(R.id.release_date_detail_2);
        vote = findViewById(R.id.vote_average_detail_2);
        overview = findViewById(R.id.overview2);
        num_of_rev = findViewById(R.id.label_review1);
        favoriteCheckBox = findViewById(R.id.favorite_checkbox);
        recyclerView_trailer = findViewById(R.id.RecyclerView_Trailer);
        recyclerView_trailer.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_review = findViewById(R.id.RecyclerView_Reviews);
        recyclerView_review.setLayoutManager(new LinearLayoutManager(this));
        // item1 = findViewById(R.id.put_in_favorite_id);

        the_data_to_display();
        GetTrailers();
        GetReviews();
        movieDb = MovieDatabase.getsInstance(getApplicationContext());
        favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteCheckBox.isChecked()) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDb.movieDao().insertMovie(mMovie);
                        }
                    });
                    Toast toast = Toast.makeText
                            (Activity_detail.this, "Movie has been added to fav", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDb.movieDao().deleteMovie(mMovie);
                        }
                    });
                    Toast toast = Toast.makeText
                            (Activity_detail.this, "Movie has been removed from fav", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        checkIfFav(mMovie.getId());
    }

    private void checkIfFav(String id) {
        LiveData<movie> movie = movieDb.movieDao().loadMovie(id);
        movie.observe(this, new Observer<movie>() {
            @Override
            public void onChanged(@Nullable movie movies) {
                if (movies == null)
                    favoriteCheckBox.setChecked(false);
                else favoriteCheckBox.setChecked(true);
            }
        });
    }

    void the_data_to_display() {

        Bundle bundle = getIntent().getExtras();
        mMovie = new movie(
                bundle.getString("id"),
                bundle.getString("title"),
                bundle.getString("vote"),
                bundle.getString("image"),
                bundle.getString("date"),
                bundle.getString("overview"));

        link = getString(R.string.link) + Objects.requireNonNull(bundle).getString("id") + "/";
        mov_name = bundle.getString("title");
        title.setText(mMovie.getTitle());
        rlease_date.setText(mMovie.getReleaseDate());
        vote.setText(mMovie.getVoteAverage());
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500/" + bundle.getString("image")).into(poster_details);
        overview.setText(mMovie.getOverview());


    }


    public interface GET_json_BODY1 {


        @GET("videos")
        Call<ResponseBody> Get_The_trailer(@Query("api_key") String key);

        @GET("reviews")
        Call<ResponseBody> Get_The_review(@Query("api_key") String key);
    }

    void GetTrailers() {

        trailers.clear();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(link).build();

        GET_json_BODY1 TheTrailer = retrofit.create(GET_json_BODY1.class);


        TheTrailer.Get_The_trailer(key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray results = object.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {

                        JSONObject obj_result = results.getJSONObject(i);

                        trailer_name = obj_result.getString("name");
                        trailer_key = obj_result.getString("key");
                        trailer_id = obj_result.getString("id");

                        trailers.add(new MovieTrailer(trailer_name, trailer_key, trailer_id));

                    }

                    trail_adapter = new TrailerAdapter(Activity_detail.this, trailers);
                    recyclerView_trailer.setAdapter(trail_adapter);


                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });


    }


    void GetReviews() {

        reviews.clear();

        Retrofit retrofit;

        retrofit = new Retrofit.Builder().baseUrl(link).build();

        GET_json_BODY1 The_Review = retrofit.create(GET_json_BODY1.class);


        The_Review.Get_The_review(key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());


                    num_of_rev.setText(jsonObject.getString("total_results") + " reviews");


                    JSONArray array_of_rev = jsonObject.getJSONArray("results");


                    for (int i = 0; i < array_of_rev.length(); i++) {


                        JSONObject object1 = array_of_rev.getJSONObject(i);

                        rev_author = object1.getString("author");
                        rev_content = object1.getString("content");
                        rev_id = object1.getString("id");
                        rev_link = object1.getString("url");

                        reviews.add(new MovieReviews(rev_author, rev_content, rev_id, rev_link));

                    }

                    review_adapter = new ReviewsAdabter(Activity_detail.this, reviews);
                    recyclerView_review.setAdapter(review_adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                shareContent();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void shareContent() {

        Bitmap bitmap = getBitmapFromView(poster_details);
        try {
            File file = new File(this.getExternalCacheDir(), "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, mov_name);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }


}














