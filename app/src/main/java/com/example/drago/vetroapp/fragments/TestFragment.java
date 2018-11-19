package com.example.drago.vetroapp.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TestFragment extends StoryListFragment {
    public TestFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        Query recentPostsQuery = databaseReference.child("posts");
        return recentPostsQuery;
    }
}
