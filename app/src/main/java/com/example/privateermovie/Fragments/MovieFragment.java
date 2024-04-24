package com.example.privateermovie.Fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.privateermovie.Adapters.MoviesAdapter;
import com.example.privateermovie.MainActivity;
import com.example.privateermovie.Models.CurrentPage;
import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.OnLoadMoreListener;
import com.example.privateermovie.R;
import com.google.gson.Gson;

import java.util.ArrayList;

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

        MainActivity mainActivity = new MainActivity();
        mainActivity.resetPageNumber();

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // You've reached the end of the GridView, load more data here
                    Activity activity = getActivity();
                    if (activity instanceof  OnLoadMoreListener){
                        ((OnLoadMoreListener)activity).onLoadMore();
                        updateGridViewWithNewData();
                    }
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

    // Method to update the GridView with new data
    public void updateGridViewWithNewData() {
        loadMoviesFromPreferences(); // Reload data from preferences
    }

    void loadMoreData() {
        Toast.makeText(getActivity(), "Loading more movies",
                Toast.LENGTH_LONG).show();
    }

}