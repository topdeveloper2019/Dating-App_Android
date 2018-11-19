package com.example.drago.vetroapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.drago.vetroapp.fragments.ProfileDetailFragment;
import com.example.drago.vetroapp.fragments.StoryDetailFragment;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public ProfilePagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileDetailFragment.newInstance("","");
            case 1:
                return StoryDetailFragment.newInstance("","");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
