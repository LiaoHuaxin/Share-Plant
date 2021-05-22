package com.example.share_idea;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Add_Article extends AppCompatActivity {

    Bitmap bitmap;
    RecyclerView stepRecycler;
    FloatingActionButton btnAdd;
    Button btnbrowse, btnupload;
    int counter;
    Uri uri;
    ImageView image_view,AddImage;
    Uri FilePathUri,FilePathUriAddView;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    int complete_Add;
    ProgressDialog progressDialog ;
    ArrayList<steps_Model> list;
    ArrayList steps_Upload;
    StepsListAdapter stepsListAdapter;
    // Uri indicates, where the image will be picked from
    // request code
    UserInfo muserInfo;
    Map<String,Object> steps;
    String path,mAddSteps;
    int mAddAteps_check;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PERMISSION_CODE = 1001;
    private final int PICK_IMAGE_REQUESTAddView = 21;
    List<Bitmap> increaseImage;
    public static DBHelper dbHelper;
    private static final String TABLE_NAME = "Step_storage";
    //Column Name
    private static final String KEY_ID = "ID";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_IMG = "image";
    EditText title_plant ,txtcontent,requirements,Introduce_Plant,notice_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__article);
        Counter mcounter = (Counter)getApplicationContext();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Add_your_steps");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Add_your_steps");//解釋成「連接到你的Firebase」
        auth = FirebaseAuth.getInstance();
        //下載用戶資訊
        downloadUserInfo();
        progressDialog = new ProgressDialog((Add_Article.this));// context name as per your project name

        title_plant = findViewById(R.id.title_plant);
        Introduce_Plant = findViewById(R.id.Introduce_Plant);
        requirements =findViewById(R.id.requirements);
        txtcontent = findViewById(R.id.methods_plant);
        notice_item = findViewById(R.id.notice_item);
        //選取圖片
        image_view = findViewById(R.id.image_view);
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        //SelectImage();
                        CropImage.startPickImageActivity(Add_Article.this);
                    }
                }
                else {
                    //SelectImage();
                    CropImage.startPickImageActivity(Add_Article.this);
                }

            }
        });

        //這個用於Intent回傳來之值，
//        Intent bundle = getIntent();
//        //get back content
//
//        //將返回物件設定到 RecycleView
//        if(bundle != null) {
//            //textContent.add(bundle.getStringExtra("stepcontent"));
//            //textContent.add(bundle.getStringExtra("stepcontent"));
//            //imageUriArray.add(bundle.getByteArrayExtra("Image"));
//            //Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArrayExtra("Image"), 0, bundle.getByteArrayExtra("Image").length);
//            //image_view.setImageBitmap(bitmap);
//
//            for(int i =0; i<imageUriArray.size();i++) {
//                //ArrayList.add(new StepData(bitmap, textContent.get(i)));
//            }
//        }
        //======================================
        //下列變數將做為修改用
        list = new ArrayList<>();
        steps_Upload = new ArrayList<>();
        mAddAteps_check = mcounter.getmAddAteps_check();
        mAddSteps = mcounter.getAddAteps();
//
//        dbHelper = new DBHelper(Add_Article.this);
//        dbHelper.CreateTable();
//
//        ArrayList<StepData> ArrayList = new ArrayList<StepData>();
//        Cursor cursor = Add_Article.dbHelper.readAllData();
//        if(cursor.getCount() == 0) {}
//        else {
//            while (cursor.moveToNext()) {
////                image_id.add(cursor.getString(0));
////                mcontent.add(cursor.getString(1));
////                mimage.add(cursor.getBlob(2));
//
//                Integer id = (cursor.getInt(0));
//                String content = (cursor.getString(1));
//                byte[] image = (cursor.getBlob(2));
//                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//                ArrayList.add(new StepData(id, content, bitmap));
//
//                image_id.add(cursor.getString(0));
//                textContent.add(cursor.getString(1));
//                mimage.add(cursor.getBlob(2));
//            }
//        }


        //==========================================================================================


        //將資料取回

        //在此處設定 Adapter
        //確認ID只設一次
        if(mAddAteps_check == 0){
            mAddSteps = UUID.randomUUID().toString();
            mcounter.setAddAteps(mAddSteps);
            mAddAteps_check +=1;
            mcounter.setmAddAteps_check(mAddAteps_check);

        }
        stepRecycler = findViewById(R.id.stepRecycler);
        stepsListAdapter = new StepsListAdapter(this,list);//資料丟這裡

        stepRecycler.setHasFixedSize(true);
        stepRecycler.setLayoutManager(new LinearLayoutManager(this));

        stepRecycler.setAdapter(stepsListAdapter);





        showSteps();
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Article.this,AddSteps.class);
                startActivity(intent);
            }
        });
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    //返回建 - 觸發事件
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Upload_Article();
            Upload_Data();
        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void Upload_Data(){
        if (image_view.getDrawable().isFilterBitmap() && title_plant.getText().length()!=0 && Introduce_Plant.getText().length() !=0 &&
                requirements.getText().length()!=0 && txtcontent.getText().length()!=0 && notice_item.getText().length() !=0) {
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(Uri.parse(path)));
            storageReference2.putFile(Uri.parse(path))
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            //getDownloadUrl() -->異步的方式
                            //https://www.javaer101.com/en/article/32958380.html
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

//                                    @SuppressWarnings("VisibleForTests")


                                    //計數器歸零
//                                            Counter mcounter = (Counter) getApplicationContext();
//                                    mcounter.setCounter(0);
//                                    complete_Add = mcounter.getComplete_Add();
                                    //新增新的資料
//                                    String random = UUID.randomUUID().toString();
//                                    for (int i = 0; i < steps_Upload.size(); i++) {
//                                        firestore.collection("draft").document(random).collection("Steps").document(complete_Add + "").set(steps_Upload.get(i));
//                                        complete_Add += 1;
//                                    }
                                    steps_Upload.clear();
                                    steps = new HashMap<>();
                                    steps.put("title_img", uri.toString());
                                    steps.put("title", title_plant.getText().toString());
                                    steps.put("Introduce_Plant", Introduce_Plant.getText().toString());
                                    steps.put("requirements", requirements.getText().toString());
                                    steps.put("content", txtcontent.getText().toString());
                                    steps.put("notice_item", notice_item.getText().toString());
                                    steps.put("notice_item", notice_item.getText().toString());
                                    steps.put("UserId", muserInfo.getId());
                                    //steps.put("steps",list);
                                    steps_Upload.add(steps);
                                    //firestore.collection("draft").document(random).collection("Steps").document("Main_Title").set(steps_Upload.get(0));
                                    firestore.collection("Article").document(UUID.randomUUID().toString()).set(steps_Upload.get(0));

//                                    mcounter.setComplete_Add(0);
                                    //刪除原先 RecyclerView 的資料
//                                    for (int i = 0; i <= steps_Upload.size() + 50; i++) {
//                                        firestore.collection(mAddSteps+"").document(i + "").delete();
//                                    }
//                                    mcounter.setmAddAteps_check(0);
                                }
                            });
                        }
                    });
        }
        else{
            Toast.makeText(Add_Article.this, "資料還沒填完喔", Toast.LENGTH_LONG).show();

        }
    }
    public void showSteps(){

        firestore.collection(mAddSteps+"").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot : task.getResult()){
                            steps_Model steps_model = new steps_Model(snapshot.getString("Id")
                                    ,snapshot.getString("steps_name")
                            ,snapshot.getString("downloadURL"));
                            list.add(steps_model);
                            steps_Upload.add(steps_model);


                        }
                        stepsListAdapter.notifyDataSetChanged();
                        //Toast.makeText(Add_Article.this, "ok!!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_Article.this, "Failed !!",Toast.LENGTH_SHORT).show();
                    }
                });

    }
    // Select Image method
    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    //選擇完圖片後，做的動作
    //內容是，圖片資料路徑設定

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri)){
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
            else {
                startCrop(imageuri);
            }
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                try {
                    bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    result.getUri());
                    bitmap = compressImage(bitmap);
                    path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, UUID.randomUUID().toString() + ".jpeg", "upload");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                image_view.setImageBitmap(bitmap);
                image_view.setScaleType(ImageView.ScaleType.CENTER_CROP);//使圖片充滿控制元件大小
                Toast.makeText(this, "Image successfully !!!" ,Toast.LENGTH_SHORT).show();
            }
        }
//        if (requestCode == PICK_IMAGE_REQUESTAddView
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//
//            // Get the Uri of data
//            FilePathUriAddView = data.getData();
//            try {
//                // Setting image on image view using Bitmap
//                Bitmap mbitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                getContentResolver(),
//                                FilePathUriAddView);
//                AddImage.setImageBitmap(mbitmap);
//                //reateImage(FilePathUriAddView);
//                //Log.e("myID: ", String.valueOf(findViewById(AddViewID.getId())));
//            }
//
//            catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
    }
    private void startCrop(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length >0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SelectImage();
                }
                else {
                    Toast.makeText(this, "Permission denied...! ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    /**
     * 图片按比例大小压缩方法(根据Bitmap图片压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap getImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }
        return (bitmap);// 压缩好比例大小后再进行质量压缩
    }
    public void downloadUserInfo(){
        firestore.collection("UserInfo").whereEqualTo("name",auth.getCurrentUser().getDisplayName()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot snapshot : task.getResult()){
                            muserInfo = new UserInfo(
                                    snapshot.getString("id")
                                    ,snapshot.getString("name")
                                    ,snapshot.getString("email"));

                        }
                    }
                });
    }

    //======================================================================

//    //新建 Step 視窗
//    @SuppressLint("ResourceAsColor")
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public LinearLayout CreateImage(Uri uri) {
//        //LinearLayout
//        LinearLayout mLinearLayout_img = new LinearLayout(Add_Article.this);
//        mLinearLayout_img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        mLinearLayout_img.setOrientation(LinearLayout.HORIZONTAL);
//        mLinearLayout_img.setPadding(10,10,10,10);
//
//        ImageView mimageView = new ImageView(Add_Article.this);
//        LinearLayout.LayoutParams settingmimageView = new LinearLayout.LayoutParams(200,200);
//        settingmimageView.leftMargin = 150;
//        mimageView.setLayoutParams(settingmimageView);
//        mimageView.setTag(R.id.Img1,mimageView);
//        Drawable drawablemimageView;
//        final Resources res = this.getResources();
//        drawablemimageView = res.getDrawable(R.drawable.ic_baseline_image_24, getTheme());
//        mimageView.setBackground(drawablemimageView);
//        mLinearLayout_img.addView(mimageView);
//
//        mimageView.setScaleType(ImageView.ScaleType.FIT_XY);//使圖片充滿控制元件大小
//
//        mimageView.setId(ViewCompat.generateViewId());
//        mimageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImageAddView();
//
//                //mimageView.setBackground(res.getDrawable(R.drawable.ic_twotone_list_24));
//
//            }
//        });
//
//        return mLinearLayout_img;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public LinearLayout CreateView(){
//        //LinearLayout
//        final LinearLayout mLinearLayout = new LinearLayout(Add_Article.this);
//        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        mLinearLayout.setPadding(10,10,10,10);
//
//
//        final TextView mtextView = new TextView(Add_Article.this);
//        LinearLayout.LayoutParams settingtextView = new LinearLayout.LayoutParams(100,100);
//        mtextView.setLayoutParams(settingtextView);
//        settingtextView.rightMargin = 22;
//        settingtextView.leftMargin = 20;
//        mtextView.setText(""+count);
//        Drawable drawable;
//        Resources res = this.getResources();
//        drawable = res.getDrawable(R.drawable.circlebackground, getTheme());
//        mtextView.setId(View.generateViewId());
//        mtextView.setBackground(drawable);
//        mtextView.setTextColor(Color.parseColor("#FFFFFF"));
//        mtextView.setGravity(Gravity.CENTER);
//        mtextView.setTextSize(15);
//        mtextView.setPadding(20,20,20,20);
//        mLinearLayout.addView(mtextView);
//
//        final  EditText meditText = new EditText(Add_Article.this);
//        LinearLayout.LayoutParams settingeditText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0);
//        meditText.setLayoutParams(settingeditText);
//        Drawable drawablemeditText;
//        drawablemeditText = res.getDrawable(R.drawable.circle20, getTheme());
//        meditText.setId(View.generateViewId());
//        meditText.setBackground(drawablemeditText);
//        meditText.setHint("輸入");
//        meditText.setPadding(20,20,0,20);
//        meditText.setInputType(InputType.TYPE_CLASS_TEXT);
//        mLinearLayout.addView(meditText);
//
//        final ActionMenuView mactionMenuView = new ActionMenuView(Add_Article.this);
//        LinearLayout.LayoutParams settingactionMenuView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0.2);
//        meditText.setLayoutParams(settingactionMenuView);
//        //Drawable drawablemspinner;
//        //drawablemspinner = res.getDrawable(R.drawable.ic_twotone_list_24, getTheme());
//        //將 Menu 分配給 ActionMenuView
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.action_menu,mactionMenuView.getMenu());
//        mactionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case (R.id.delete_menu):
//                        ViewGroup parent = (ViewGroup) mLinearLayout.getParent();
//                        parent.removeViewAt(0);
//                        parent.removeViewAt(0);//一行行消除View
//                        break;
//                }
//                return false;
//            }
//        });
//        //mactionMenuView.setBackground(drawablemspinner);
//        mactionMenuView.setLayoutMode(Gravity.CENTER);
//        mactionMenuView.setPadding(20,20,20,20);
//        mLinearLayout.addView(mactionMenuView);
//
//        return mLinearLayout;
//    }
//
//    //建立選單
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.completet_btn, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//    //選單內選項
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.complete:
//                UploadImage();
//                Intent intent = new Intent(Add_Article.this, MainActivity.class);
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 質量壓縮方法，這裡100表示不壓縮，把壓縮後的資料存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 300) { // 迴圈判斷如果壓縮後圖片是否大於100kb,大於繼續壓縮
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 這裡壓縮options%，把壓縮後的資料存放到baos中
            options -= 10;// 每次都減少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把壓縮後的資料baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream資料生成圖片
        return bitmap;
    }

    //===============================================================================
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Upload_Article() {

        if (image_view.getDrawable().isFilterBitmap() && title_plant.getText().length()!=0 && requirements.getText().length()!=0 && txtcontent.getText().length()!=0) {
            progressDialog.setTitle("Add your steps");
            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(Uri.parse(path)));
            storageReference2.putFile(Uri.parse(path))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            //getDownloadUrl() -->異步的方式
                            //https://www.javaer101.com/en/article/32958380.html
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    @SuppressWarnings("VisibleForTests")
                                    // Get the data from an ImageView as bytes
                                            Article uploadinfo = new Article( uri.toString()
                                            , title_plant.getText().toString(),requirements.getText().toString(),txtcontent.getText().toString());

                                    String key = databaseReference.push().getKey();
                                    databaseReference.child(key).setValue(uploadinfo).addOnSuccessListener((new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG,"Data Saved");
                                        }
                                    })).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"Data not Saved", e);
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
        else {

            Toast.makeText(Add_Article.this, "資料還沒填完喔", Toast.LENGTH_LONG).show();
        }
    }

}