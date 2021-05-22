package com.example.share_idea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore firestore;
    ArrayList steps_Upload;
    show_Adapter adapter;
    public Search_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Search_Fragment newInstance(String param1, String param2) {
        Search_Fragment fragment = new Search_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firestore =FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
        steps_Upload = new ArrayList();

    }
    GridView gridView;
    List<ItemModel> itemModelList = new ArrayList<>();
    ItemAdapter itemAdapter;
    private AppCompatActivity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_, container, false);
        gridView = view.findViewById(R.id.gridView);
        //這裡放置搜尋後的 List
        steps_Upload.clear();
        itemAdapter = new ItemAdapter(this, steps_Upload);

        //adapter = new show_Adapter(this.getContext(), steps_Upload);
        gridView.setAdapter(itemAdapter);
        return view ;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //steps_Upload.clear();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //itemAdapter.getFilter().filter(newText);

                searchData(newText);
                return true;
            }
        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        Toolbar mToolbar = mActivity.findViewById(R.id.toolbar_me);
        mActivity.setSupportActionBar(mToolbar);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id= item.getItemId();
        if(id == R.id.search_view){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void searchData(String s){
        firestore.collection("Article").whereEqualTo("title",s.toLowerCase()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        steps_Upload.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            Article_Detail article = new Article_Detail(
                                    snapshot.getString("title_img")
                                    ,snapshot.getString("title")
                                    ,snapshot.getString("Introduce_Plant")
                                    ,snapshot.getString("requirements")
                                    ,snapshot.getString("content")
                                    ,snapshot.getString("notice_item")
                                    , (HashMap<String, Object>) snapshot.getData());
                            steps_Upload.add(article);
                            itemAdapter = new ItemAdapter(Search_Fragment.this, steps_Upload);
                            gridView.setAdapter(itemAdapter);

                        }
                    }
                });
    }

    private class ItemAdapter extends BaseAdapter  {

        private List<Article_Detail> itemModelList;
        private List<Article_Detail> itemModelsFilter;
        private Search_Fragment context;
        public ItemAdapter(Search_Fragment context, List<Article_Detail> itemModelList){
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
            final View view = getLayoutInflater().inflate(R.layout.filter_row_item,null);

            final ImageView imageView = view.findViewById(R.id.imageview);
            final TextView filterName = view.findViewById(R.id.filterName);
            final TextView filter_description = view.findViewById(R.id.filter_description);
            LinearLayout search_gridview = view.findViewById(R.id.search_gridview);

            search_gridview.setOnClickListener(new View.OnClickListener() {
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
            filter_description.setText(itemModelsFilter.get(position).getIntroduce_Plant());
            return view;
        }

//        @Override
//        public Filter getFilter() {
//            Filter filter = new Filter() {
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//
//                    FilterResults filterResults = new FilterResults();
//                    if(constraint == null || constraint.length() == 0){
//                        filterResults.count = itemModelList.size();
//                        filterResults.values = itemModelList;
//                    }else {
//                        String searchStr = constraint.toString().toLowerCase();
//                        List<ItemModel> resultData = new ArrayList<>();
//                        for(ItemModel itemModel : itemModelList){
//                            if(itemModel.getName().contains(searchStr) || itemModel.getDescription().contains(searchStr)){
//                                resultData.add(itemModel);
//                            }
//                            filterResults.count = resultData.size();
//                            filterResults.values = resultData;
//                        }
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    itemModelsFilter = (List<ItemModel>) results.values;
//                    notifyDataSetChanged();
//                }
//            };
//            return null;
//        }
    }
}