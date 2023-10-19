package com.example.bottomnevigation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bottomnevigation.Comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName,profileEmail,profileUsername,profilePassword,profileMobile_no;
    TextView titleName,titleUsername,AccountStatusTV;

    ImageView ivProfile_photo;

    String strUsername;
    ProgressBar pd;
    Button editProfileButton;

    SharedPreferences preferences; //temp database
    SharedPreferences.Editor editor;  //edit or modify the temp database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        editor = preferences.edit();
        strUsername = preferences.getString("username",null);
        Toast.makeText(this, "My User Name is "+strUsername, Toast.LENGTH_SHORT).show();

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        profileMobile_no = findViewById(R.id.profileMobile_no);
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfileButton =findViewById(R.id.editButton);


        getUserDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getUserDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", strUsername);
        client.post(Urls.getUserDetailsURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strname = jsonObject.getString("name");
                        String stremail_id = jsonObject.getString("email_id");
                        String strmobile_no = jsonObject.getString("mobile_no");
                        String strusername = jsonObject.getString("username");
                        String strpassword = jsonObject.getString("password");

                        profileName.setText(strname);
                        profileEmail.setText(stremail_id);
                        profileMobile_no.setText(strmobile_no);
                        profileUsername.setText(strusername);
                        profilePassword.setText(strpassword);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}