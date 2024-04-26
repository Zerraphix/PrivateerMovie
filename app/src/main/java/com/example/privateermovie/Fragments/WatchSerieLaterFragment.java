package com.example.privateermovie.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.privateermovie.Adapters.SeriesAdapter;
import com.example.privateermovie.Adapters.WatchSeriesLaterAdapter;
import com.example.privateermovie.Models.SeriesModel;
import com.example.privateermovie.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchSerieLaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchSerieLaterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private ArrayList<SeriesModel.Result> results;
    private WatchSeriesLaterAdapter adapter;

    public WatchSerieLaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchSerieLaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchSerieLaterFragment newInstance(String param1, String param2) {
        WatchSerieLaterFragment fragment = new WatchSerieLaterFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_serie_later, container, false);

        gridView = view.findViewById(R.id.serie_later_list);
        results = new ArrayList<>();
        adapter = new WatchSeriesLaterAdapter(getContext(), results);
        gridView.setAdapter(adapter);

        loadSeriesFromPreferences();

        return view;
    }

    private void loadSeriesFromPreferences() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("saved_series", "");
        Log.v("json data", json);
        SeriesModel.Result[] seriesArray = gson.fromJson(json, SeriesModel.Result[].class);
        if (seriesArray != null) {
            results.clear(); // Clear existing data
            results.addAll(Arrays.asList(seriesArray)); // Add new data to the existing list
            adapter.notifyDataSetChanged(); // Notify adapter of data change
        }
    }
}