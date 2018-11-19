package com.example.drago.vetroapp.fragments;


import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.drago.vetroapp.CountryFilterActivity;
import com.example.drago.vetroapp.NewMessageActivity;
import com.example.drago.vetroapp.R;
import com.example.drago.vetroapp.holders.PostViewHolder;
import com.example.drago.vetroapp.models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;


import java.util.Objects;


import static java.util.Objects.requireNonNull;

public abstract class StoryListFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "StoryListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_NEW_POST = 333;
    private View rootview;
//    @BindView(R.id.myprofileImage) ImageView _myprofileImage;
//    @BindView(R.id.newpostText)  TextView _newpostButton;
//    @BindView(R.id.filterImage) ImageView _filterImage;
    private TextView _newpostButton;

    public StoryListFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview = requireNonNull(inflater).inflate(R.layout.fragment_story_list, container, false);
//        ButterKnife.bind(this,rootview);
        mRecycler = rootview.findViewById(R.id.messagesList);
        ImageView _filterImage = rootview.findViewById(R.id.filterImage);
        ImageView _myprofileImage = rootview.findViewById(R.id.myprofileImage);
        _newpostButton = rootview.findViewById(R.id.newpostText);
        _filterImage.setOnClickListener(this);
        _newpostButton.setOnClickListener(this);
        _myprofileImage.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String userID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mRecycler.setHasFixedSize(true);


        return rootview;
   }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query postsQuery = getQuery(mDatabase);
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            int width = 0;


            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                DisplayMetrics displayMetrics = new DisplayMetrics();
                Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                width = displayMetrics.widthPixels;
                return new PostViewHolder(inflater.inflate(R.layout.story_list_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder viewHolder, int position,@NonNull final Post model) {
                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(getActivity(),model,width);
            }

        };
        mRecycler.setAdapter(mAdapter);
    }

    // [START post_stars_transaction]

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.filterImage:
                Intent intent = new Intent(getActivity(),CountryFilterActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                break;
            case R.id.myprofileImage:
                break;
            case R.id.newpostText:
                _newpostButton.setTextColor(Color.BLACK);
                Intent intent1 = new Intent(getActivity(),NewMessageActivity.class);
                startActivity(intent1);
                Objects.requireNonNull(getActivity()).finish();

                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        _newpostButton.setTextColor(getResources().getColor(R.color.iron));
//    }
    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        mRecycler.removeAllViews();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        Toast.makeText(getActivity(), "stop listening", Toast.LENGTH_SHORT).show();
    }
    public abstract Query getQuery(DatabaseReference databaseReference);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootview = null;
    }
}
