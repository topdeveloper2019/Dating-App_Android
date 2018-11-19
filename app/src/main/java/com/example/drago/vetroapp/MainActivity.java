package com.example.drago.vetroapp;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.drago.vetroapp.adapters.ViewSwapperAdapter;
import com.example.drago.vetroapp.fragments.FriendStoryFragment;
import com.example.drago.vetroapp.fragments.ProfileDetailFragment;
import com.example.drago.vetroapp.fragments.ProfileFragment;
import com.example.drago.vetroapp.fragments.SearchFragment;
import com.example.drago.vetroapp.fragments.StoryDetailFragment;
import com.example.drago.vetroapp.fragments.StoryListFragment;

import org.buffer.adaptablebottomnavigation.view.AdaptableBottomNavigationView;
import org.buffer.adaptablebottomnavigation.view.ViewSwapper;

public class MainActivity extends AppCompatActivity implements ProfileDetailFragment.OnFragmentInteractionListener,StoryDetailFragment.OnFragmentInteractionListener, FriendStoryFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener,SearchFragment.OnFragmentInteractionListener {
    private AdaptableBottomNavigationView bottomNavigationView;
    private ViewSwapperAdapter viewSwapperAdapter;
    private ViewSwapper viewSwapper;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (AdaptableBottomNavigationView) findViewById(R.id.view_bottom_navigation);
        viewSwapper = (ViewSwapper) findViewById(R.id.view_swapper);
        viewSwapperAdapter = new ViewSwapperAdapter(getSupportFragmentManager());
//
        viewSwapper.setAdapter(viewSwapperAdapter);
        bottomNavigationView.setupWithViewSwapper(viewSwapper);
        final ProgressDialog progressDialog =  new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");

        progressDialog.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 5000);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }
}
