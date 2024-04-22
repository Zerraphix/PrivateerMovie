package com.example.privateermovie;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

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

    void initGui(){
        findViewById(R.id.btn_movies).setOnClickListener(v -> fragmentChanger(MovieFragment.class));
        findViewById(R.id.btn_series).setOnClickListener(view -> {

        });
        findViewById(R.id.btn_watchlater).setOnClickListener(view -> {

        });
        findViewById(R.id.btn_search).setOnClickListener(view -> {

        });

    }

    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();
    }
}