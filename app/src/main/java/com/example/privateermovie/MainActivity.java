package com.example.privateermovie;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;


import com.example.privateermovie.Fragments.MenuFragment;
import com.example.privateermovie.Fragments.MovieFragment;
import com.example.privateermovie.Fragments.SerieFragment;
import com.example.privateermovie.Fragments.WatchLaterFragment;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        fragmentChanger(MovieFragment.class);

        initGui();
    }

    void initGui() {
        findViewById(R.id.btn_movies).setOnClickListener(v -> fragmentChanger(MovieFragment.class));
        findViewById(R.id.btn_series).setOnClickListener(v -> fragmentChanger(SerieFragment.class));
        findViewById(R.id.btn_watchlater).setOnClickListener(v -> fragmentChanger(WatchLaterFragment.class));
        findViewById(R.id.btn_search).setOnClickListener(v -> { });
    }

    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            showCloseAppDialog();
        }
    }

    private void showCloseAppDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Going back further will close the app. Are you sure you want to continue?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Close the app
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    // Dismiss the dialog
                    dialogInterface.dismiss();
                })
                .show();
    }
}
