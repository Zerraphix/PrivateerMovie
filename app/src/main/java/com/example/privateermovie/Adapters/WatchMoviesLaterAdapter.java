package com.example.privateermovie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchMoviesLaterAdapter extends ArrayAdapter<MoviesModel.Result> {

    private Context mContext;
    private ArrayList<MoviesModel.Result> mResults;

    public WatchMoviesLaterAdapter(Context context, ArrayList<MoviesModel.Result> results) {
        super(context, 0, results);
        mContext = context;
        mResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        WatchMoviesLaterAdapter.ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new WatchMoviesLaterAdapter.ViewHolder();

            listItemView = inflater.inflate(R.layout.movie_later_list_item, parent, false);

            viewHolder.imageView = listItemView.findViewById(R.id.img_card_movie); // Example: Replace 'iv_imageView' with the id of your ImageView in grid item layout

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (WatchMoviesLaterAdapter.ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        MoviesModel.Result result = mResults.get(position);

        // Load backdrop image using Picasso
        if (result.getBackdrop_path() != null) {
            String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
            Picasso.get().load(imageUrl).into(viewHolder.imageView);
        }

        return listItemView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}