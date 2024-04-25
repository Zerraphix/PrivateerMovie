package com.example.privateermovie.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.privateermovie.Fragments.DetailedSerieFragment;
import com.example.privateermovie.Models.SeriesModel;
import com.example.privateermovie.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeriesAdapter extends ArrayAdapter<SeriesModel.Result> {
    private Context mContext;
    private ArrayList<SeriesModel.Result> mResults;

    public  SeriesAdapter( Context context, ArrayList<SeriesModel.Result> results){
        super(context,0, results);
        mContext = context;
        mResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        SeriesAdapter.ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new SeriesAdapter.ViewHolder();

            listItemView = inflater.inflate(R.layout.movie_list_item, parent, false);

            viewHolder.imageView = listItemView.findViewById(R.id.img_card);
            viewHolder.btn_thumb = listItemView.findViewById(R.id.btn_thumb); // Initialize btn_thumb here

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (SeriesAdapter.ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        SeriesModel.Result result = mResults.get(position);

        // Load backdrop image using Picasso
        if (result.getBackdrop_path() != null) {
            String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
            Picasso.get().load(imageUrl).into(viewHolder.imageView);
        }

        viewHolder.imageView.setOnClickListener(v -> {
            // Pass the selected movie directly to DetailedMovieFragment.newInstance()
            DetailedSerieFragment fragment = DetailedSerieFragment.newInstance(result);

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
            editor.putString("saved_series", json);
            editor.apply();
            Toast.makeText(mContext, "Serie added to watch later", Toast.LENGTH_SHORT).show();
        });

        return listItemView;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageButton btn_thumb; // Declare btn_thumb ImageButton here
    }

}