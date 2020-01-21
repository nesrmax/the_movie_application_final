package com.example.hamadaelsha3r.the_movie_application;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final ArrayList<movie> list1 = new ArrayList<>();
    String image;
    String title;
    private String relese_date;
    String vote;
    String overview;
    String mov_id;

    final String link = "http://api.themoviedb.org/3/movie/";
    String key = "3eb6a7eb34657be719cf18315c117723";
    String Saved_Insatance_Key = "the_key";
    Retrofit retrofit;
    RecyclerView recyclerView;
    private MovieDatabase movieDb;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        recyclerView = findViewById(R.id.RecyclerView1);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        if (savedInstanceState == null) {
            Log.d("ayasalah", "create");

            The_data(sharedPreferences);

        } else {

            if (savedInstanceState.containsKey(Saved_Insatance_Key)) {

                ArrayList<movie> list = savedInstanceState.getParcelableArrayList(Saved_Insatance_Key);

                movie_adabter adabter = new movie_adabter(MainActivity.this, list);

                recyclerView.setAdapter(adabter);
            }
        }
        movieDb = MovieDatabase.getsInstance(getApplicationContext());

    }

    private void showFavorites() {

        final LiveData<List<movie>> movie = movieDb.movieDao().loadAllMovies();
        movie.observe(this, new Observer<List<movie>>() {
            @Override
            public void onChanged(@Nullable List<movie> movies) {
                Log.d("DB", "DB CHANGED");
                if (movies != null && movies.size() != 0) {
                    movie_adabter adabter = new movie_adabter(MainActivity.this, movies);
                    recyclerView.setAdapter(adabter);
                } else {
                    Toast toast = Toast.makeText
                            (MainActivity.this, "There is no favorite movies to show!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Saved_Insatance_Key, list1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.setting_activity:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.favorite_list_id:
                if (item.isChecked()) {
                    The_data(sharedPreferences);
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    showFavorites();
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                }
           /* case R.id.high:
                top_rated();
                return true;

            case R.id.popular:
                popular_body();
                return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

// ____________________________________________________________________________________________________________________________

    public interface GET_json_BODY {

        @GET("popular")
        Call<ResponseBody> popular_movie(@Query("api_key") String key);

        @GET("top_rated")
        Call<ResponseBody> top_rated(@Query("api_key") String key);
    }

    public void popular_body() {

        list1.clear();

        retrofit = new Retrofit.Builder().baseUrl(link).build();
        GET_json_BODY popular_Body = retrofit.create(GET_json_BODY.class);
        popular_Body.popular_movie(key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    JSONObject the_whole_json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    JSONArray the_result = the_whole_json.getJSONArray("results");
                    for (int i = 0; i < the_result.length(); i++) {
                        JSONObject result_object = the_result.getJSONObject(i);
                        image = result_object.getString("poster_path");
                        title = result_object.getString("title");
                        relese_date = result_object.getString("release_date");
                        vote = result_object.getString("vote_average");
                        overview = result_object.getString("overview");
                        mov_id = result_object.getString("id");
                        list1.add(new movie(mov_id, title, vote, image, relese_date, overview));
                    }
                    movie_adabter adabter = new movie_adabter(MainActivity.this, list1);
                    recyclerView.setAdapter(adabter);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void top_rated() {

        list1.clear();

        retrofit = new Retrofit.Builder().baseUrl(link).build();

        GET_json_BODY rated_body = retrofit.create(GET_json_BODY.class);

        rated_body.top_rated(key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject the_whole_json = new JSONObject(response.body().string());
                    JSONArray the_result = the_whole_json.getJSONArray("results");
                    for (int i = 0; i < the_result.length(); i++) {
                        JSONObject result_object = the_result.getJSONObject(i);
                        image = result_object.getString("poster_path");
                        title = result_object.getString("title");
                        relese_date = result_object.getString("release_date");
                        vote = result_object.getString("vote_average");
                        overview = result_object.getString("overview");
                        mov_id = result_object.getString("id");
                        list1.add(new movie(mov_id, title, vote, image, relese_date, overview));
                    }
                    movie_adabter adabter = new movie_adabter(MainActivity.this, list1);
                    recyclerView.setAdapter(adabter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void The_data(SharedPreferences sharedPreferences) {
        String sort = sharedPreferences.getString(getString(R.string.sort_way_key), getString(R.string.popular_value));
        Log.d("ayasalah", "00");
        list1.clear();
        if (sort.equals("Popular")) {
            popular_body();

        } else  /*(sort.equals("TopRated"))*/ {
            top_rated();

        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        The_data(sharedPreferences);
//    }

    @Override
    protected void onDestroy() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        The_data(sharedPreferences);
    }

}