package com.example.share_idea;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.share_idea.Add_Article.compressImage;

public class AddSteps extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    EditText mstepEdit;
    ImageView stepImg;
    Button mcomplete_step;
    private ArrayList<String> imageUriArray;
    private ArrayList<String> textContent;
    private Uri FilePathUri;
    public DBHelper StepSqlite;
    Bitmap mbitmap;
    String path;
    int counter;
    String mAddSteps;
    ProgressDialog progressDialog ;
    StorageReference storageReference;
    private FirebaseFirestore firestore;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_steps);

        mstepEdit = findViewById(R.id.stepEdit);
        stepImg = findViewById(R.id.stepImg);

        //creating database
        StepSqlite = new DBHelper(this);
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("AddSteps");
        //上面必須有宣告，然後在這裡實體化，並使用
        imageUriArray = new ArrayList<>();
        textContent = new ArrayList<>();
        //全域變數
        Counter mcounter = (Counter)getApplicationContext();
        counter = mcounter.getCounter();
        mAddSteps = mcounter.getAddAteps();
        mcomplete_step = findViewById(R.id.complete_step);
        mcomplete_step.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override//isFilterBitmap()
            public void onClick(View v) {
                if(mstepEdit.getText().toString().trim().length() !=0 && stepImg.getDrawable().isFilterBitmap()) {
                    try {
//                        StepSqlite.insertData(
//                                mstepEdit.getText().toString().trim(),
//                                imageViewToByte(stepImg));
//                        Toast.makeText(AddSteps.this, "新增成功", Toast.LENGTH_SHORT).show();
//                        mstepEdit.setText("");
//                        stepImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                        stepImg.setScaleType(ImageView.ScaleType.FIT_CENTER);//使圖片充滿控制元件大小
                        String mstepEdit_txt = mstepEdit.getText().toString();
                        Add_steps(mstepEdit_txt);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(AddSteps.this,"Yes",Toast.LENGTH_SHORT).show();
                }
                else {
                    //放入內容
                    Toast.makeText(AddSteps.this,"NO",Toast.LENGTH_SHORT).show();
                }

            }
        });


        stepImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageUriArray.clear();
                SelectImage();
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
//            Intent intent = new Intent(AddSteps.this, Add_Article.class);
//            intent.putExtra("Counter", counter);
//            setResult(requestCode_counter,intent);
//            startActivity(intent);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public void Add_steps(final String mstepEdit_txt){

        final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(Uri.parse(path)));
        storageReference2.putFile(Uri.parse(path))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //getDownloadUrl() -->異步的方式
                        //https://www.javaer101.com/en/article/32958380.html
                        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                @SuppressWarnings("VisibleForTests")
                                // Get the data from an ImageView as bytes

                                        //String Id = UUID.randomUUID().toString();
                                String Id= String.valueOf(counter);
                                Map<String,Object> steps = new HashMap<>();
                                steps.put("Id",Id);
                                steps.put("steps_name",mstepEdit_txt);
                                steps.put("downloadURL",uri.toString());
                                firestore.collection(mAddSteps+"").document(Id).set(steps)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AddSteps.this, "Data Saved !!",Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddSteps.this, "Failed !!",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSteps.this, "Failed !!",Toast.LENGTH_SHORT).show();
                    }
        });
        counter += 1;
        Counter mcounter = (Counter)getApplicationContext();
        mcounter.setCounter(counter);
        mstepEdit.setText("");
        stepImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
        stepImg.setScaleType(ImageView.ScaleType.FIT_CENTER);//使圖片充滿控制元件大小
        Toast.makeText(AddSteps.this, "新增成功", Toast.LENGTH_SHORT).show();

    }
    //左上角返回建  -  事件監聽
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            try {
//                StepSqlite.insertData(
//                        stepcontent.getText().toString().trim(),
//                        imageViewToByte(stepImg));
//                Toast.makeText(AddSteps.this, "新增成功", Toast.LENGTH_SHORT).show();
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //圖片壓縮
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析將inJustDecodeBounds設定為true，來獲取圖片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 呼叫上面定義的方法計算inSampleSize值
        //inSampleSize 任何小於等於1的值都與1相同。注意：*解碼器使用基於2的冪的最終值，任何其他值將*舍入到最接近的2的冪。
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用獲取到的inSampleSize值再次解析圖片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static Bitmap decodeBitmapById (@NonNull Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只测量image 不加载到内存
        BitmapFactory.decodeResource(res, resId, options);//测量image
        options.inPreferredConfig= Bitmap.Config.RGB_565;//设置565编码格式 省内存
        options.inSampleSize = calculateInSamplesizeByOptions(options, reqWidth, reqHeight);//获取压缩比 根据当前屏幕宽高去压缩图片
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);//按照Options配置去加载图片到内存
        ByteArrayOutputStream out=new ByteArrayOutputStream();//字节流输出
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);//压缩成JPEG格式 压缩像素质量为50%

        return bitmap;
    }

    //计算图片的压缩比
    public static int calculateInSamplesizeByOptions(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSamplesize   = 1;
        int width  = options.outWidth;//图片的宽度 单位1px 即像素点
        int height = options.outHeight;//图片的高度
        if (height > reqHeight || width > reqWidth) {
            int halfheight = height / 2;
            int halfwidth  = width  / 2;
            while (halfheight/inSamplesize >= reqHeight && halfwidth / inSamplesize >= reqWidth){
                inSamplesize*=2;
            }
            //inSamplesize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        //options.inJustDecodeBounds = false;
        return inSamplesize;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromFile(Resources res ,int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
         // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * 图片按比例大小压缩方法(根据Bitmap图片压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap getImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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



    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            FilePathUri = data.getData();
            try {
                // Setting image on image view using Bitmap
                mbitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                FilePathUri);

                mbitmap = compressImage(mbitmap);
                path = MediaStore.Images.Media.insertImage(this.getContentResolver(), mbitmap, UUID.randomUUID().toString() + ".jpeg", "upload");
                stepImg.setScaleType(ImageView.ScaleType.FIT_CENTER);//使圖片充滿控制元件大小
                stepImg.setImageBitmap(getImage(mbitmap));
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}