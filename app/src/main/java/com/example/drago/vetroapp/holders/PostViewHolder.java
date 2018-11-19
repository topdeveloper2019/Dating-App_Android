package com.example.drago.vetroapp.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drago.vetroapp.R;
import com.example.drago.vetroapp.models.Post;
import com.squareup.picasso.Picasso;

import static com.example.drago.vetroapp.R.id.sharedImageItem;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public TextView bodyView;
    public ImageView sharedImageView;

    public PostViewHolder(View itemView) {
        super(itemView);

        authorView = itemView.findViewById(R.id.authorNameItem);
        bodyView = itemView.findViewById(R.id.messageTextItem);
        sharedImageView = itemView.findViewById(sharedImageItem);
    }

    public void bindToPost(Context context, Post post,int width) {

        if(post.fileurl != null) {
            sharedImageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(post.fileurl).resize(width,(int)width*post.height/post.width)
                    .error(R.drawable.common_google_signin_btn_icon_dark)
                    .into(sharedImageView);
        }
        authorView.setText(post.author);

        bodyView.setText(post.body);

    }
}
