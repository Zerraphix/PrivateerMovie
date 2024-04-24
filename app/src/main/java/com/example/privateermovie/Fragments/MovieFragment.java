package com.example.privateermovie.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.privateermovie.Adapters.MoviesAdapter;
import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.OnLoadMoreListener;
import com.example.privateermovie.R;
import com.example.privateermovie.Secrets;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private ArrayList<MoviesModel.Result> results;
    private MoviesAdapter adapter;
    public static RequestQueue rq;
    public int page = 0;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        rq = Volley.newRequestQueue(getContext());
    }

    public OnLoadMoreListener onLoadMoreListener;

    // Method to set the listener
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        gridView = view.findViewById(R.id.movie_list);
        results = new ArrayList<>();
        adapter = new MoviesAdapter(getContext(), results);
        gridView.setAdapter(adapter);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // You've reached the end of the GridView, load more data here
                    getMovies();
                }
            }
        });

        return view;
    }

    private void loadMoviesFromPreferences() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("movies", "");
        MoviesModel moviesModel = gson.fromJson(json, MoviesModel.class);
        Log.v("json data", json);

        if (moviesModel != null && moviesModel.getResults() != null) {
            // Add new data to the existing list
            results.addAll(moviesModel.getResults());
            adapter.notifyDataSetChanged(); // Notify adapter of data change
        }
    }

    public void getMovies() {
        page = page + 1;
        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=" + page + "&sort_by=popularity.desc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            // Save the entire JSON response in SharedPreferences
            saveMovies(json);
            loadMoviesFromPreferences();
        }, error -> Log.e("Volley", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", Secrets.Token);
                return params;
            }
        };
        rq.add(request);
    }

    void saveMovies(String data){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("movies", data);
        editor.apply();
    }

}