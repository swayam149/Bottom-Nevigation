package com.example.bottomnevigation;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottomnevigation.Comman.GoogleSignInModelClass;
import com.example.bottomnevigation.Comman.NetworkChangeListner;
import com.example.bottomnevigation.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText edit_login_username, edit_login_password;
    CheckBox login_checkbox;
    Button login_to_dashboard_btn, sign_in_button_google;
    TextView login_to_register;
    boolean doubletap = false;
    SharedPreferences preferences; //temp database
    SharedPreferences.Editor editor;  //edit or modify the temp database

    NetworkChangeListner changeListner = new NetworkChangeListner();

    GoogleSignInClient client;
    FirebaseAuth Auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();

        if (preferences.getBoolean("isLogin", false)) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        edit_login_username = findViewById(R.id.edit_login_username);
        edit_login_password = findViewById(R.id.edit_login_password);
        login_checkbox = findViewById(R.id.login_checkbox);
        login_to_dashboard_btn = findViewById(R.id.login_to_dashboard_btn);
        login_to_register = findViewById(R.id.login_to_register);
//        sign_in_button_google=findViewById(R.id.sign_in_button_google);


        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        client = GoogleSignIn.getClient(LoginActivity.this, options);
        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bottom-nevigation-default-rtdb.firebaseio.com/");
//                tv_term_condition = findViewById(R.id.tv_login_term_condition);
        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        login_to_dashboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()) {
//                }
//                if (edit_login_username.getText().toString().isEmpty()) {
//                    edit_login_username.setError("Please Enter Your Username");
//                } else if (edit_login_username.getText().toString().length() < 8) {
//                    edit_login_username.setError("Username must be greater then 8");
//                } else if (edit_login_password.getText().toString().isEmpty()) {
//                    edit_login_password.setError("Please Enter your password");
//                } else if (edit_login_password.getText().toString().length() < 8) {
//                    edit_login_password.setError("Password must be greater then 8");
//
                    Toast.makeText(LoginActivity.this, "Username or Password is not valid", Toast.LENGTH_SHORT).show();

                } else {
                    loginUserFromServer();
//                    loginUserfromFiebase();
                }
            }
        });


        login_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    public Boolean validateUsername() {
        String val = edit_login_username.getText().toString();
        if (val.isEmpty()) {
            edit_login_username.setError("Username cannot be empty");
            return true;
        } else {
            edit_login_username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = edit_login_password.getText().toString();
        if (val.isEmpty()) {
            edit_login_password.setError("Password cannot be empty");
            return true;
        } else {
            edit_login_password.setError(null);
            return true;
        }
    }

    private void loginUserfromFiebase() {

        String userUsername = edit_login_username.getText().toString().trim();
        String userPassword = edit_login_password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    edit_login_username.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        edit_login_username.setError(null);
                        Toast.makeText(LoginActivity.this, "Firebase Server Open", Toast.LENGTH_SHORT).show();
                        loginUserFromServer();
//                        pass the data using intent
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email_id").getValue(String.class);
                        String mobileNoFromDB = snapshot.child(userUsername).child("mobile_no").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                        intent.putExtra("name", nameFromDB);
//                        intent.putExtra("email_id", emailFromDB);
//                        intent.putExtra("mobile_no", mobileNoFromDB);
//                        intent.putExtra("username", usernameFromDB);
//                        intent.putExtra("password", passwordFromDB);
                        Auth.signInWithEmailAndPassword(emailFromDB, edit_login_password.getText().toString()).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (Auth.getCurrentUser().isEmailVerified()) {
                                                Toast.makeText(LoginActivity.this, "Login SuccessFully Done", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
//                                                intent.putExtra("isLogin", true);
//                                                intent.putExtra("name", nameFromDB);
//                                                intent.putExtra("mobile_no", mobileNoFromDB);
//                                                intent.putExtra("email_id", emailFromDB);
//                                                intent.putExtra("username", usernameFromDB);
//                                                intent.putExtra("password", passwordFromDB);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Please Verify your Email ", Toast.LENGTH_SHORT).show();
                                            }


                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                        startActivity(intent);
                    } else {
                        edit_login_password.setError("Invalid Credentials");
                        edit_login_password.requestFocus();
                    }
                } else {
                    edit_login_username.setError("User does not exist");
                    edit_login_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loginUserFromServer() {
        AsyncHttpClient client = new AsyncHttpClient(); //generate the request through network
        RequestParams params = new RequestParams(); //passing the data thr request

        params.put("username", edit_login_username.getText().toString());   //nikdroid23
        params.put("password", edit_login_password.getText().toString());

        client.post(Urls.loginUserURL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");

                            if (status.equals("1")) {
                                String Strname = response.getString("name");
                                String Strmobile_no = response.getString("mobile_no");
                                String Stremail_id = response.getString("email_id");
                                String Strusername = response.getString("username");
                                String Strpassword = response.getString("password");


                                Auth.signInWithEmailAndPassword(Stremail_id, edit_login_password.getText().toString()).
                                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (Auth.getCurrentUser().isEmailVerified()) {
                                                Toast.makeText(LoginActivity.this, "Login SuccessFully Done", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                                                editor.putBoolean("isLogin", true).commit();
                                                editor.putString("name", Strname).commit();
                                                editor.putString("mobile_no", Strmobile_no).commit();
                                                editor.putString("email_id", Stremail_id).commit();
                                                editor.putString("username", Strusername).commit();
                                                editor.putString("password", Strpassword).commit();
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Please Verify your Email ", Toast.LENGTH_SHORT).show();
                                            }


                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.e("Error is ", errorResponse.toString());
                        Toast.makeText(LoginActivity.this, "Server Error " + errorResponse, Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(changeListner, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(changeListner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            Auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = Auth.getCurrentUser();
                    GoogleSignInModelClass signInModelClass = new GoogleSignInModelClass();
                    assert user != null;
                    signInModelClass.setUserId(user.getUid());
                    signInModelClass.setUserName(user.getDisplayName());
                    signInModelClass.setUserEmail(user.getEmail());
                    signInModelClass.setUserProfilePhoto(user.getPhotoUrl().toString());
                    database.getReference().child("users").child(user.getUid()).setValue(signInModelClass);


                }
            });

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubletap) {
            super.onBackPressed();
        } else {
            Toast.makeText(LoginActivity.this, "press again to go back", Toast.LENGTH_SHORT).show();
            doubletap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubletap = false;
                }
            }, 2000);
        }
    }
}