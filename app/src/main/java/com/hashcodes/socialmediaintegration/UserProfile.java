package com.hashcodes.socialmediaintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    JSONObject response, profile_pic_data, profile_pic_url;
    Button logout;
    String jsondata;
    TextView user_name,user_email;
    CircleImageView user_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);




        user_name =  findViewById(R.id.UserName);
        user_picture =  findViewById(R.id.profilePic);
        user_email =  findViewById(R.id.email);
        logout=findViewById(R.id.logout);

        loaduserProfile(AccessToken.getCurrentAccessToken());
        checkLoginStatus();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disconnectFromFacebook();


            }
        });

    }


    public void disconnectFromFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).executeAsync();
    }


    private void loaduserProfile(AccessToken currentAccessToken){
        Intent intent = getIntent();
        jsondata = intent.getStringExtra("userProfile");

        FacebookSdk.sdkInitialize(getApplicationContext());
        try {
            response = new JSONObject(jsondata);
            user_email.setText(response.get("email").toString());
            user_name.setText(response.get("name").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Glide.with(this).load(profile_pic_url.getString("url"))
                    .into(user_picture);
//            Picasso.with(this).load(profile_pic_url.getString("url"))
//                    .into(user_picture);

            } catch(Exception e){
            e.printStackTrace();
        }
    }



    private void checkLoginStatus(){
        if (AccessToken.getCurrentAccessToken()!=null){
            loaduserProfile(AccessToken.getCurrentAccessToken());
        }
    }


}
