package com.example.privateermovie;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.annotation.NonNull;


import com.example.privateermovie.Fragments.MenuFragment;
import com.example.privateermovie.Fragments.MovieFragment;
import com.example.privateermovie.Fragments.SerieFragment;
import com.example.privateermovie.Fragments.WatchLaterFragment;
import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.Models.CurrentPage;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements OnLoadMoreListener {

    private ArrayList<MoviesModel> movies;
    public CurrentPage currentPage = new CurrentPage();
    public int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getMovies();
        fragmentChanger(MenuFragment.class);

        initGui();
    }

    void initGui() {
        findViewById(R.id.btn_movies).setOnClickListener(v -> fragmentChanger(MovieFragment.class));
        findViewById(R.id.btn_series).setOnClickListener(v -> fragmentChanger(SerieFragment.class));
        findViewById(R.id.btn_watchlater).setOnClickListener(v -> fragmentChanger(WatchLaterFragment.class));
        findViewById(R.id.btn_search).setOnClickListener(v -> { });
    }

    public void getMovies() {
        OkHttpClient client = new OkHttpClient();

        page = page + 1;
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=" + page + "&sort_by=popularity.desc")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4Zjg3ZTVjYjhmNDY1ZTk0MjViMDc5ZWJmOWViZGVlOCIsInN1YiI6IjY2MjYwNGUyMjU4ODIzMDE3ZDkyOGUyMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.NPs6rE6gkzc4JubfcCr6ssuvfChImEWJzqVuhA3gc1A")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    // Convert JSON to a single MoviesModel object
                    convertToMoviesModel(json);
                    // Save the entire JSON response in SharedPreferences
                    saveMovies(json);
                } else {
                    Log.wtf("Nothing works!!", response.message());
                    // Handle unsuccessful response
                    // Maybe show an error message to the user
                }
            }
        });
    }

    void saveMovies(String data){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("movies", data);
        editor.apply();
    }

    void convertToMoviesModel(String json){
        // Parse the JSON string into a single MoviesModel object
        MoviesModel moviesModel = new Gson().fromJson(json, MoviesModel.class);
        // Process the MoviesModel object as needed
        // For example, you can extract the list of movies from moviesModel.getResults()
        // and use it to populate your UI
    }

    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            showCloseAppDialog();
        }
    }

    private void showCloseAppDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Going back further will close the app. Are you sure you want to continue?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Close the app
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    // Dismiss the dialog
                    dialogInterface.dismiss();
                })
                .show();
    }

    @Override
    public void onLoadMore() {
        getMovies();
    }
    public void resetPageNumber() {
        page = 0;
        Log.d(TAG, "resetPageNumber: " + page);
    }
}
