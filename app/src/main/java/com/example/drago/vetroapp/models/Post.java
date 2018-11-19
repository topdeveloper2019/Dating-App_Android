package com.example.drago.vetroapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String body;
    public String filename;
    public String fileurl;
    public int width;
    public int height;
    public int starCount;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author,  String body,String uploadfileName,String fileurl,int width,int height) {
        this.uid = uid;
        this.author = author;
        this.body = body;
        this.filename = uploadfileName;
        this.fileurl = fileurl;
        this.width = width;
        this.height = height;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("body", body);
        result.put("filename",filename);
        result.put("fileurl",fileurl);
        result.put("width", width);
        result.put("height", height);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
