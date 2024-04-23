package com.example.privateermovie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends ArrayAdapter<MoviesModel.Result> {

    private Context mContext;
    private ArrayList<MoviesModel.Result> mResults;

    public  MoviesAdapter( Context context, ArrayList<MoviesModel.Result> results){
        super(context,0, results);
        mContext = context;
        mResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
        }

        ImageView imageView = listItemView.findViewById(R.id.img_card);

        MoviesModel.Result result = mResults.get(position);

        // Load backdrop image using Picasso
        if (result.getBackdrop_path() != null) {
            String backdropUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
            Picasso.get().load(backdropUrl).into(imageView);
        }


        return listItemView;
    }

}
