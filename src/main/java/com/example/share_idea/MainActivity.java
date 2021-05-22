package com.example.share_idea;

//參考資料
//BottomNavigationView+ViewPager+Fragment 底部導航按鈕
//原文網址：https://kknews.cc/tech/x6o6q6q.html

//https://kknews.cc/zh-tw/tech/x6o6q6q.html
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //宣告元件
    BottomNavigationView bnView;
    ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Fragment
        //把剛剛建好的 Fragments 放入 FragmentAdapter 之中
        //再將 FragmentAdapter 和 viewPager，做setAdapter
        //將 bottom_nav_view 設定成監聽器，依照按到的位置，觸發 view_pager
        bnView = findViewById(R.id.bottom_nav_view);
        viewPager = findViewById(R.id.view_pager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Home_fragment());
        fragments.add(new Search_Fragment());
        fragments.add(new Add_Fragment());
        fragments.add(new Personal_Fragment());


        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //BottomNavigationView 點擊事件監聽

        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int menuId = menuItem.getItemId();
                // 跳轉指定頁面：Fragment
                switch (menuId){
                    case R.id.Home_Fragment:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.Search_Fragment:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.Add_Fragment:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.Personal_Fragment:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });


        // ViewPager 滑動事件監聽
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //將滑動到的頁面對應的 menu 設置為選中狀態
                bnView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    List<ItemModel> itemModelList = new ArrayList<>();
    ItemAdapter itemAdapter;
    //建立選單

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id= item.getItemId();
        if(id == R.id.search_view){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public final class FragmentAdapter extends FragmentPagerAdapter{
        private List<Fragment> mFragments;
        public FragmentAdapter(List<Fragment> fragments, FragmentManager fm)
        {
            super(fm);
            this.mFragments = fragments;
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
    private class ItemAdapter extends BaseAdapter implements Filterable {

        private List<ItemModel> itemModelList;
        private List<ItemModel> itemModelsFilter;
        private Context context;
        public ItemAdapter(Context context, List<ItemModel> itemModelList){
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.filter_row_item,null);
            ImageView imageView = view.findViewById(R.id.image_view);
            TextView filterName = view.findViewById(R.id.filterName);
            TextView filter_description = view.findViewById(R.id.filter_description);

            Glide.with(imageView).load(itemModelsFilter.get(position).getImg()).placeholder(R.drawable.cat).into(imageView);
            filterName.setText(itemModelsFilter.get(position).getName());
            filter_description.setText(itemModelsFilter.get(position).getDescription());
            return null;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    if(constraint == null || constraint.length() == 0){
                        filterResults.count = itemModelList.size();
                        filterResults.values = itemModelList;
                    }else {
                        String searchStr = constraint.toString().toLowerCase();
                        List<ItemModel> resultData = new ArrayList<>();
                        for(ItemModel itemModel : itemModelList){
                            if(itemModel.getName().contains(searchStr) || itemModel.getDescription().contains(searchStr)){
                                resultData.add(itemModel);
                            }
                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }
                    }
                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    itemModelsFilter = (List<ItemModel>) results.values;
                    notifyDataSetChanged();
                }
            };
            return null;
        }
    }
}