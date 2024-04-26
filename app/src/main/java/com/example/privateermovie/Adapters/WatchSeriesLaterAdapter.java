package com.example.privateermovie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.privateermovie.Fragments.DetailedSerieFragment;
import com.example.privateermovie.Models.SeriesModel;
import com.example.privateermovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchSeriesLaterAdapter extends ArrayAdapter<SeriesModel.Result> {

    private Context mContext;
    private ArrayList<SeriesModel.Result> mResults;

    public WatchSeriesLaterAdapter(Context context, ArrayList<SeriesModel.Result> results) {
        super(context, 0, results);
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

            listItemView = inflater.inflate(R.layout.serie_later_list_item, parent, false);

            viewHolder.imageView = listItemView.findViewById(R.id.img_card_serie); // Example: Replace 'iv_imageView' with the id of your ImageView in grid item layout

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        SeriesModel.Result result = mResults.get(position);

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
