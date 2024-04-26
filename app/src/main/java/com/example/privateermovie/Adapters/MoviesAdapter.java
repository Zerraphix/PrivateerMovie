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
import com.example.privateermovie.Models.SeriesModel;
import com.example.privateermovie.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            // Retrieve existing saved series data
            SharedPreferences sharedPref = ((AppCompatActivity) mContext).getPreferences(Context.MODE_PRIVATE);
            String existingJson = sharedPref.getString("saved_movies", "[]"); // Default to an empty array if no data is found

            // Convert existing JSON string to list of series objects
            List<MoviesModel.Result> savedMoviesList = new ArrayList<>();
            if (!existingJson.isEmpty()) {
                try {
                    JSONArray jsonArray = new JSONArray(existingJson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MoviesModel.Result series = new Gson().fromJson(jsonObject.toString(), MoviesModel.Result.class);
                        savedMoviesList.add(series);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Add the new series to the list
            savedMoviesList.add(result);

            // Serialize the list to JSON format
            String updatedJson = new Gson().toJson(savedMoviesList);

            // Save the updated JSON string to SharedPreferences
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("saved_movies", updatedJson);
            editor.apply();

            // Show a toast message
            Toast.makeText(mContext, "Movie added to watch later", Toast.LENGTH_SHORT).show();
        });

        return listItemView;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageButton btn_thumb; // Declare btn_thumb ImageButton here
    }

}
