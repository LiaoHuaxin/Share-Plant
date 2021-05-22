package com.example.share_idea;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

class Article_Content_steps extends RecyclerView.Adapter<Article_Content_steps.ViewHolder>{

    private Activity mContext;
    private ArrayList steps;

    private ArrayList<HashMap<String, String>> msteps;
    public Article_Content_steps(Activity mContext, Object steps){
        this.mContext = mContext;
        this.msteps = (ArrayList<HashMap<String, String>>) steps;
    }
    @NonNull
    @Override
    public Article_Content_steps.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_addstep,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Article_Content_steps.ViewHolder holder, int position) {

        holder.id.setText(msteps.get(position).get("Id"));
        //holder.title.setText(msteps.get(position).get("steps_name").toString());

//        viewHolder.stepImg.setImageBitmap(Uri.parse(mList.get(position).getImage()));

        Glide.with(holder.stepImg).load(msteps.get(position).get("downloadURL").toString()).placeholder(R.drawable.cat).into(holder.stepImg);
        holder.stepImg.setScaleType(ImageView.ScaleType.FIT_XY);//使圖片充滿控制
    }

    @Override
    public int getItemCount() {
        return msteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,id;
        private ImageView stepImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.Add_Article_id);
            title = itemView.findViewById(R.id.Add_Article_layout);
            stepImg = itemView.findViewById(R.id.Add_Article_Img);


        }
    }
}
