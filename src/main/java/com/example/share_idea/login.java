package com.example.share_idea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.service.controls.ControlsProviderService.TAG;

public class login extends AppCompatActivity {

    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private TextView Username;
    private CircleImageView mprofile;
    private LoginButton mlogin;
    private Button start;
    private AccessToken accessToken;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Article article;
    private ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_login);
        firestore = FirebaseFirestore.getInstance();
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


            Username = findViewById(R.id.User_name);
            mprofile = findViewById(R.id.profile);
            mlogin = findViewById(R.id.login);
            start = findViewById(R.id.start_program);
            ckeckloginstatus();

            mlogin.setReadPermissions(Arrays.asList("user_status"));

            //loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "user_photos"));
//            profileTracker = new ProfileTracker() {
//                @Override
//                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                    this.stopTracking();
//                    Profile.setCurrentProfile(currentProfile);
//
//                }
//            };
//            profileTracker.startTracking();

            accessTokenTracker.startTracking();
            //AccessToken.refreshCurrentAccessTokenAsync();
            mlogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                //登入成功

                @Override
                public void onSuccess(LoginResult loginResult) {

                    firebaseFacebook(loginResult.getAccessToken());

                    accessToken = loginResult.getAccessToken();
                    Log.d("FB", "access token got.");
                    //send request and call graph api
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {

                                //當RESPONSE回來的時候

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    //讀出姓名 ID FB個人頁面連結

                                    Log.d("FB", "complete");
                                    Log.d("FB", object.optString("name"));
                                    Log.d("FB", object.optString("email"));
                                    Log.d("FB", object.optString("id"));
                                    String id = object.optString("id");//?type=normal
                                    String image_url = "https://graph.facebook.com/v3.2/" + id + "/picture?type=normal";
                                    Uri profilePictureUri = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 500, 500);
                                    Uri photoUrl = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                                    Glide.with(mprofile).load(profilePictureUri).into(mprofile);

                                    //方法一 建立firebase 客戶資料
                                    HashMap user_data = new HashMap<>();
                                    user_data.put("id",object.optString("id"));
                                    user_data.put("name",object.optString("name"));
                                    user_data.put("email",object.optString("email"));
//                                    user_data.put("name",object.optString("user_gender"));
//                                    user_data.put("name",object.optString("user_friends"));
                                    firestore.collection("UserInfo").document(UUID.randomUUID().toString()).set(user_data);
                                    //方法二
//                                    if (user != null) {
//                                        // Name, email address, and profile photo Url
//                                        String name = user.getDisplayName();
//                                        String email = user.getEmail();
//                                        Uri mphotoUrl = user.getPhotoUrl();
//                                    }
                                    Username.setText(object.optString("name"));
                                    Intent intent = new Intent(login.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                    //包入你想要得到的資料 送出request

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                //登入取消
                @Override
                public void onCancel() {
                    // App code
                    LoginManager.getInstance().logOut();
                    auth.signOut();
                    Log.d("FB", "CANCEL");
                }
                //登入失敗
                @Override
                public void onError(FacebookException exception) {
                    // App code

                    Log.d("FB", exception.toString());
                }
            });
            //跳到主頁
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(login.this, MainActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        //printHashKey();
    }
    private void firebaseFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Register successfull",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Register failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Username.setText("");
                mprofile.setImageResource(0);
                start.setVisibility(View.INVISIBLE);
            }
            else {
                loginFB(currentAccessToken);
                start.setVisibility(View.VISIBLE);

            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        //確認有使用者就登入
        if(user != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    private void loginFB(AccessToken accessToken) {


                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

                            String id = object.getString("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                Log.d(TAG, "Facebook id:" + id);
                                Log.d(TAG, "Facebook name:" + name);
                                Log.d(TAG, "Facebook email:" + email);
                                String image_url = "https://graph.facebook.com/v3.2/" + id + "/picture?type=normal";
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.dontAnimate();

                                Uri profilePictureUri = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 500 , 500 );
                                Glide.with(mprofile).load(profilePictureUri).into(mprofile);
                                Username.setText(name);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // https://developers.facebook.com/docs/android/graph?locale=zh_TW
                // 如果要取得email，需透過添加參數的方式來獲取(如下)
                // 不添加只能取得id & name
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
    private void ckeckloginstatus(){
        if (AccessToken.getCurrentAccessToken()!=null) {
            loginFB(AccessToken.getCurrentAccessToken());
        }
    }

    //找回HashKey
    public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.share_idea",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(signature.toByteArray());
                Log.d("TEMPTAGHASH KEY:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}