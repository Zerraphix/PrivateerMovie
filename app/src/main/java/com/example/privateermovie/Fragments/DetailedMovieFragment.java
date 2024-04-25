package com.example.privateermovie.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedMovieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MoviesModel.Result mSelectedMovie;

    public DetailedMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedMovieFragment newInstance(String param1, String param2) {
        DetailedMovieFragment fragment = new DetailedMovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailedMovieFragment newInstance(MoviesModel.Result selectedMovie) {
        DetailedMovieFragment fragment = new DetailedMovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedMovie", selectedMovie);
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
        if (getArguments() != null) {
            mSelectedMovie = (MoviesModel.Result) getArguments().getSerializable("selectedMovie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed_movie, container, false);

        // Display detailed information about the selected movie
        if (mSelectedMovie != null) {
            TextView titleTextView = view.findViewById(R.id.tv_detailTitle);
            ImageView posterImage = view.findViewById(R.id.iv_poster);
            TextView releaseDate = view.findViewById(R.id.tv_releaseDate);
            TextView overView = view.findViewById(R.id.tv_overview);

            titleTextView.setText(mSelectedMovie.getTitle());
            String imageUrl = "https://image.tmdb.org/t/p/original/" + mSelectedMovie.getBackdrop_path();
            Picasso.get().load(imageUrl).into(posterImage);
            releaseDate.setText(mSelectedMovie.getRelease_date());
            overView.setText(mSelectedMovie.getOverview());
        }

        return view;
    }
}