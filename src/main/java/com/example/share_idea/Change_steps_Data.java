package com.example.share_idea;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.example.share_idea.AddSteps.getImage;

public class Change_steps_Data extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    String Change_id;
    String Change_edit;
    byte[] Change_Img;

    TextView mChange_id;
    EditText mChange_edit;
    ImageView mChange_Img;
    Button mChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_steps__data);
        mChange_id = findViewById(R.id.Change_id);
        mChange_edit = findViewById(R.id.change_edit);
        mChange_Img = findViewById(R.id.change_Img);
        mChange_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        mChange = findViewById(R.id.Change);

        //get data
        getAndSetIntentData();


        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    DBHelper dbHelper = new DBHelper(Change_steps_Data.this);
                    Change_edit = mChange_edit.getText().toString().trim();
                    Change_Img = imageViewToByte(mChange_Img);
                    dbHelper.updateData(Change_id, Change_edit, Change_Img);
                    Intent intent = new Intent(Change_steps_Data.this, Add_Article.class);
                    startActivity(intent);
                    Toast.makeText(Change_steps_Data.this,"Change successful",Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("image_id") && getIntent().hasExtra("textContent") &&
                getIntent().hasExtra("image")){
            //Getting Data from Intent
            Change_id = getIntent().getStringExtra("image_id");
            Change_edit = getIntent().getStringExtra("textContent");
            Change_Img = getIntent().getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(Change_Img, 0, Change_Img.length);
            //Setting Intent Data
            mChange_id.setText(Change_id);
            mChange_edit.setText(Change_edit);
            mChange_Img.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            Uri FilePathUri = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                FilePathUri);
                //stepImg.setImageURI(FilePathUri);
                mChange_Img.setScaleType(ImageView.ScaleType.FIT_CENTER);//使圖片充滿控制元件大小
                mChange_Img.setImageBitmap(getImage(bitmap));
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}