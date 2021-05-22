package com.example.share_idea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_fragment extends Fragment {

    private RecyclerView recyclerView;
    StorageReference storageReference;
    DatabaseReference databaseReference,UseraRef;
    private String  currrentID;
    private List<Article> list;
    private LinearLayoutManager linearLayoutManager;
    show_Adapter adapter;
    DatabaseReference ContacsRef,UserRef;
    private RecyclerView myContactsList;
    FirebaseRecyclerOptions<Article> options;
    ArrayList steps_Upload;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore firestore;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private  String currentUserID;
    SwipeRefreshLayout swipeRefreshLayout;
    public Home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_fragment newInstance(String param1, String param2) {
        Home_fragment fragment = new Home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設定資料同步
        firestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        steps_Upload = new ArrayList<>();
        Fetch_Firestore();
        adapter = new show_Adapter(this, steps_Upload);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        recyclerView = view.findViewById(R.id.contacts_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

         swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fetch_Firestore();
                recyclerView.invalidate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //adapter.notifyDataSetChanged();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        //Fetch();

        //adapter.startListening()
    }
    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void Fetch_Firestore(){
        steps_Upload.clear();
        firestore.collection("Article")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            Article_Detail article = new Article_Detail(
                                    snapshot.getString("title_img")
                                    ,snapshot.getString("title")
                                    ,snapshot.getString("Introduce_Plant")
                                    ,snapshot.getString("requirements")
                                    ,snapshot.getString("content")
                                    ,snapshot.getString("notice_item")
                                    , (HashMap<String, Object>) snapshot.getData());

                            steps_Upload.add(article);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);


                        }
                    }
                });
    }
    public void Fetch() {

        options = new FirebaseRecyclerOptions.Builder<Article>()
                .setQuery(UseraRef, new SnapshotParser<Article>() {
                    @NonNull
                    @Override
                    public Article parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Article(snapshot.child("mimage_view").getValue().toString(),
                                snapshot.child("mtxtdata").getValue().toString(),
                                snapshot.child("mcounts").getValue().toString(),
                                snapshot.child("mtxtcontent").getValue().toString());
                    }
                })
                .build();
    }








}