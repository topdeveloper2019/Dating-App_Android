package com.example.drago.vetroapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drago.vetroapp.R;
import com.example.drago.vetroapp.holders.PostViewHolder;
import com.example.drago.vetroapp.models.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public  class PostsAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> mPostIds = new ArrayList<>();
    private List<Post> mPosts = new ArrayList<>();

    public PostsAdapter(Context mContext, DatabaseReference mDatabaseReference) {
        this.mContext = mContext;
        this.mDatabaseReference = mDatabaseReference;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                mPosts.add(post);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                String postKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int postIndex = mPostIds.indexOf(postKey);
                if (postIndex > -1) {
                    // Replace with the new data
                    mPosts.set(postIndex, post);

                    // Update the RecyclerView
                    notifyItemChanged(postIndex);
                } else {
                    Log.w("warning", "onChildChanged:unknown_child:" + postKey);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String postKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int postIndex = mPostIds.indexOf(postKey);
                if (postIndex > -1) {
                    // Remove data from the list
                    mPostIds.remove(postIndex);
                    mPosts.remove(postIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(postIndex);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post movedPost= dataSnapshot.getValue(Post.class);
                String postKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.story_list_item, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {

        Post post = mPosts.get(i);
        postViewHolder.authorView.setText(post.author);
        postViewHolder.bodyView.setText(post.body);
        if(post.fileurl != null) {
            Picasso.with(mContext)
                    .load(post.fileurl).resize(post.width,post.height)
                    .error(R.drawable.common_google_signin_btn_icon_dark)
                    .into(postViewHolder.sharedImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
