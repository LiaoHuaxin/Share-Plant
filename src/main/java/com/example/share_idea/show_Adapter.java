package com.example.share_idea;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class show_Adapter extends RecyclerView.Adapter<show_Adapter.show_Viewholder> {

    private final int PICK_Artitle_Content_REQUEST = 20;
    Home_fragment mhome_fragment;
    ImageView img;
    TextView title;
    LinearLayout layout;
    ArrayList<Article_Detail> arrayList;

    public show_Adapter(Home_fragment home_fragment, ArrayList steps_upload) {
        this.mhome_fragment = home_fragment;
        this.arrayList = steps_upload;


    }

    @NonNull
    @Override
    public show_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_container,parent,false);
        return new show_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull show_Viewholder holder, final int position) {
        Glide.with(img).load(arrayList.get(position).getTitle_img()).placeholder(R.drawable.cat).into(img);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //img.setImageURI(Uri.parse(arrayList.get(position).getTitle_img()));
        title.setText(arrayList.get(position).getTitle());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Artitle_Content.class);
                Bundle bundle = new Bundle();
                bundle.putString("title_img", arrayList.get(position).getTitle_img());
                bundle.putString("title",arrayList.get(position).getTitle());
                bundle.putString("Introduce_Plant",arrayList.get(position).getIntroduce_Plant());
                bundle.putString("requirements", arrayList.get(position).getRequirements());
                bundle.putString("content",arrayList.get(position).getContent());
                bundle.putString("notice_item",arrayList.get(position).getNotice_item());
                bundle.putSerializable("steps",arrayList.get(position).getSteps());
                intent.putExtra("Bundle",bundle);
                //intent.putExtra("steps", (Parcelable) arrayList.get(position).getSteps());
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class show_Viewholder extends RecyclerView.ViewHolder {
        public show_Viewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.show_img);
            title = itemView.findViewById(R.id.Show_title);
            layout = itemView.findViewById(R.id.show_layout);
        }
    }

}
