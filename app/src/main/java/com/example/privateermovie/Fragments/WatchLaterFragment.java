package com.example.privateermovie.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.privateermovie.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchLaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchLaterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WatchLaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchLaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchLaterFragment newInstance(String param1, String param2) {
        WatchLaterFragment fragment = new WatchLaterFragment();
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
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);
        fragmentChanger(WatchMovieLaterFragment.class);

        initGui(view);

        return view;
    }

    void initGui(View view) {
        view.findViewById(R.id.btn_movies_later).setOnClickListener(v -> fragmentChanger(WatchMovieLaterFragment.class));
        view.findViewById(R.id.btn_series_later).setOnClickListener(v -> fragmentChanger(WatchSerieLaterFragment.class));
    }


    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.watchlater_fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}