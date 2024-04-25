package com.example.privateermovie.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.privateermovie.Fragments.DetailedMovieFragment;
import com.example.privateermovie.Models.MoviesModel;
import com.example.privateermovie.R;
import com.google.gson.Gson;
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
        ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder();

            listItemView = inflater.inflate(R.layout.movie_list_item, parent, false);

            viewHolder.imageView = listItemView.findViewById(R.id.img_card);
            viewHolder.btn_thumb = listItemView.findViewById(R.id.btn_thumb); // Initialize btn_thumb here

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        MoviesModel.Result result = mResults.get(position);

        // Load backdrop image using Picasso
        if (result.getBackdrop_path() != null) {
            String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
            Picasso.get().load(imageUrl).into(viewHolder.imageView);
        }

        viewHolder.imageView.setOnClickListener(v -> {
            // Pass the selected movie directly to DetailedMovieFragment.newInstance()
            DetailedMovieFragment fragment = DetailedMovieFragment.newInstance(result);

            // Get the fragment manager and start the transaction
            FragmentManager fragmentManager = ((AppCompatActivity) this.mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("name")
                    .commit();
        });

        viewHolder.btn_thumb.setOnClickListener(v -> {
            String json = new Gson().toJson(result);
            SharedPreferences sharedPref = ((AppCompatActivity) mContext).getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("saved_movies", json);
            editor.apply();
            Toast.makeText(mContext, "Movie added to watch later", Toast.LENGTH_SHORT).show();
        });

        return listItemView;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageButton btn_thumb; // Declare btn_thumb ImageButton here
    }

}
