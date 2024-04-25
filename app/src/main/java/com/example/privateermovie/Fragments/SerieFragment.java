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
import com.example.privateermovie.Adapters.SeriesAdapter;
import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.Models.SeriesModel;
import com.example.privateermovie.R;
import com.example.privateermovie.Secrets;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SerieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SerieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private ArrayList<SeriesModel.Result> results;
    private SeriesAdapter adapter;
    private static RequestQueue rq;
    private int page = 0;

    public SerieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SerieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SerieFragment newInstance(String param1, String param2) {
        SerieFragment fragment = new SerieFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie, container, false);

        gridView = view.findViewById(R.id.serie_list);
        results = new ArrayList<>();
        adapter = new SeriesAdapter(getContext(), results);
        gridView.setAdapter(adapter);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // You've reached the end of the GridView, load more data here
                    getSeries();
                }
            }
        });

        return view;
    }

    private void loadSeriesFromPreferences() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("series", "");
        SeriesModel seriesModel = gson.fromJson(json, SeriesModel.class);
        Log.v("json data", json);

        if (seriesModel != null && seriesModel.getResults() != null) {
            // Add new data to the existing list
            results.addAll(seriesModel.getResults());
            adapter.notifyDataSetChanged(); // Notify adapter of data change
        }
    }

    public void getSeries() {
        page = page + 1;
        String url = "https://api.themoviedb.org/3/discover/tv?include_adult=false&include_video=false&language=en-US&page=" + page + "&sort_by=popularity.desc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            // Save the entire JSON response in SharedPreferences
            saveSeries(json);
            loadSeriesFromPreferences();
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

    void saveSeries(String data){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("series", data);
        editor.apply();
    }
}