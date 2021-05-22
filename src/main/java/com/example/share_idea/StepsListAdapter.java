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

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder> {

    private Activity mContext;
    private ArrayList<steps_Model> mList;



    public StepsListAdapter(Activity Context, ArrayList<steps_Model> list){
        this.mContext = Context;
        this.mList = list;


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_addstep,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.mid.setText(mList.get(position).toString());
        holder.mstepcontent.setText(mList.get(position).toString());

//        viewHolder.stepImg.setImageBitmap(Uri.parse(mList.get(position).getImage()));

        Glide.with(holder.stepImg).load(mList.get(position).toString()).placeholder(R.drawable.cat).into(holder.stepImg);
        holder.stepImg.setScaleType(ImageView.ScaleType.FIT_CENTER);//使圖片充滿控制


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mstepcontent,mid;
        private ImageView stepImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mid = itemView.findViewById(R.id.Add_Article_id);
            mstepcontent = itemView.findViewById(R.id.Add_Article_layout);
            stepImg = itemView.findViewById(R.id.Add_Article_Img);


        }
    }
}
