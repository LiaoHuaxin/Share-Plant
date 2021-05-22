package com.example.share_idea;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personal_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personal_Fragment extends Fragment {

    public Button edit_personal_data;
    public TextView Name;
    public TextView Introduce;
    public CircleImageView User_picture;
    FirebaseUser user;
    public Personal_Fragment() {
        // Required empty public constructor
    }
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    UserInfo info;
    String UserName;
    ArrayList User_ArticleList;
    selfAdapter mselfAdapter;
    GridView gridView;
    // TODO: Rename and change types and number of parameters
    public static Personal_Fragment newInstance(String param1, String param2) {
        Personal_Fragment fragment = new Personal_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserName = user.getDisplayName();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        User_ArticleList = new ArrayList();
        mselfAdapter = new Personal_Fragment.selfAdapter(this, User_ArticleList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_, container, false);



        edit_personal_data = view.findViewById(R.id.edit_personal_data);
        User_picture = view.findViewById(R.id.User_picture);
        Name = view.findViewById(R.id.Name);
        Introduce = view.findViewById(R.id.Introduce);
        edit_personal_data = view.findViewById(R.id.edit_personal_data);

        gridView = view.findViewById(R.id.personal_gridView);
        gridView.setAdapter(mselfAdapter);
        //設定個人資料



        edit_personal_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),personal_data_setting.class);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

            downloadUserInfo();

    }


    public void downloadUserInfo(){
        firestore.collection("UserInfo").whereEqualTo("name",auth.getCurrentUser().getDisplayName()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot snapshot : task.getResult()){
                            info = new UserInfo(
                                    snapshot.getString("id")
                                    ,snapshot.getString("name")
                                    ,snapshot.getString("email"));
                            if(info != null) {
                                Name.setText(info.getName());
                                Glide.with(User_picture).load(user.getPhotoUrl()).into(User_picture);
                                downloadUserArticle(info.getId());
                            }
                        }
                    }
                });
    }
    private void downloadUserArticle(String s){
        firestore.collection("Article").whereEqualTo("UserId",s.toLowerCase()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        User_ArticleList.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            Article_Detail article = new Article_Detail(
                                    snapshot.getString("title_img")
                                    ,snapshot.getString("title")
                                    ,snapshot.getString("Introduce_Plant")
                                    ,snapshot.getString("requirements")
                                    ,snapshot.getString("content")
                                    ,snapshot.getString("notice_item")
                                    , (HashMap<String, Object>) snapshot.getData());
                            User_ArticleList.add(article);
                            mselfAdapter = new Personal_Fragment.selfAdapter(Personal_Fragment.this, User_ArticleList);
                            mselfAdapter.notifyDataSetInvalidated();
                            gridView.setAdapter(mselfAdapter);

                        }
                    }
                });
    }
    private class selfAdapter extends BaseAdapter {

        private List<Article_Detail> itemModelList;
        private List<Article_Detail> itemModelsFilter;
        private Personal_Fragment context;

        public selfAdapter(Personal_Fragment context, List<Article_Detail> itemModelList) {
            this.itemModelList = itemModelList;
            this.itemModelsFilter = itemModelList;
        }

        @Override
        public int getCount() {
            return itemModelsFilter.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.personal_article_item, null);

            final ImageView imageView = view.findViewById(R.id.personal_imageview);
            final TextView filterName = view.findViewById(R.id.personal_Name);
            LinearLayout personal_gridview = view.findViewById(R.id.personal_gridview);

            personal_gridview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Artitle_Content.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title_img", itemModelsFilter.get(position).getTitle_img());
                    bundle.putString("title", itemModelsFilter.get(position).getTitle());
                    bundle.putString("Introduce_Plant", itemModelsFilter.get(position).getIntroduce_Plant());
                    bundle.putString("requirements", itemModelsFilter.get(position).getRequirements());
                    bundle.putString("content", itemModelsFilter.get(position).getContent());
                    bundle.putString("notice_item", itemModelsFilter.get(position).getNotice_item());
                    bundle.putSerializable("steps", itemModelsFilter.get(position).getSteps());
                    intent.putExtra("Bundle", bundle);
                    //intent.putExtra("steps", (Parcelable) arrayList.get(position).getSteps());
                    v.getContext().startActivity(intent);
                }
            });

            Glide.with(imageView).load(itemModelsFilter.get(position).getTitle_img()).placeholder(R.drawable.cat).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            filterName.setText(itemModelsFilter.get(position).getTitle());
            return view;
        }

    }

}