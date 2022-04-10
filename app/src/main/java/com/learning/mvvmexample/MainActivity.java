package com.learning.mvvmexample;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learning.mvvmexample.adapters.RecyclerAdapter;
import com.learning.mvvmexample.models.NicePlace;
import com.learning.mvvmexample.viewmodels.MainActivityViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFab = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();
        mainActivityViewModel.getNicePlaces().observe(this, nicePlaces -> mAdapter.notifyDataSetChanged());

        mainActivityViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean) showProgressBar();
            else {
                hideProgressBar();
                mRecyclerView.smoothScrollToPosition(Objects.requireNonNull(mainActivityViewModel.getNicePlaces().getValue()).size() - 1);
            }
        });

        mFab.setOnClickListener(onClick -> {
            mainActivityViewModel.addNewPlace(new NicePlace(
                    "https://i.imgur.com/ZcLLrkY.jpg",
                    "Washington"
            ));
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerAdapter(this, mainActivityViewModel.getNicePlaces().getValue());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}