package com.example.share_idea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class Artitle_Content extends AppCompatActivity {
    ImageView title_img;
    TextView content_title,content,content_requirements,content_introduce,content_notice;
    Bundle bundle;
    HashMap article;
    ArrayList<steps_Model> steps;
    RecyclerView recyclerView;
    Article_Content_steps article_content_steps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artitle__content);


        title_img = findViewById(R.id.title_img);
        content_title = findViewById(R.id.content_title);
        content_introduce = findViewById(R.id.content_introduce);
        content_requirements = findViewById(R.id.content_requirements);
        content = findViewById(R.id.content);
        content_notice = findViewById(R.id.content_notice);
        recyclerView = findViewById(R.id.steps);



        Intent intent = getIntent();
        bundle = new Bundle();
        bundle = intent.getBundleExtra("Bundle");
        Glide.with(title_img).load(bundle.getString("title_img")).placeholder(R.drawable.cat).into(title_img);
        title_img.setScaleType(ImageView.ScaleType.FIT_XY);

        content_title.setText(bundle.getString("title"));
        content_introduce.setText(bundle.getString("Introduce_Plant"));
        content_requirements.setText(bundle.getString("requirements"));
        content.setText(bundle.getString("content"));
        content_notice.setText(bundle.getString("notice_item"));
        //article = (HashMap) bundle.get("steps");

        //article_content_steps = new Article_Content_steps(this,  article.get("steps"));
        //recyclerView.setAdapter(stepsListAdapter);
    }

}