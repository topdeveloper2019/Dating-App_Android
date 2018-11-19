package com.example.drago.vetroapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.drago.vetroapp.fragments.FriendStoryFragment;
import com.example.drago.vetroapp.fragments.ProfileFragment;
import com.example.drago.vetroapp.fragments.SearchFragment;
import com.example.drago.vetroapp.fragments.StoryListFragment;
import com.example.drago.vetroapp.fragments.TestFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.buffer.adaptablebottomnavigation.adapter.FragmentStateAdapter;

public class ViewSwapperAdapter extends FragmentStateAdapter {

    private static final int INDEX_BUFFER = 0;
    private static final int INDEX_RETREAT = 1;
    private static final int INDEX_VALUES = 2;
    private static final int INDEX_PROFILE = 3;
    public ViewSwapperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case INDEX_BUFFER:
                return new TestFragment();
            case INDEX_RETREAT:
                return SearchFragment.newInstance("","");
            case INDEX_VALUES:
                return FriendStoryFragment.newInstance("","");
            case INDEX_PROFILE:
                return ProfileFragment.newInstance("","");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

}