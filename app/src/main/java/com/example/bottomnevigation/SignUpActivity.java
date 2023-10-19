package com.example.bottomnevigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottomnevigation.Comman.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    EditText edit_reg_name,edit_reg_email,edit_reg_mobile_no,edit_reg_username,edit_reg_password;
    Button reg_to_login_btn;
    TextView reg_to_login;
    CheckBox reg_checkbox;
    ProgressDialog pd;

    FirebaseAuth Auth;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_reg_name = findViewById(R.id.edit_reg_name);
        edit_reg_email = findViewById(R.id.edit_reg_email);
        edit_reg_mobile_no = findViewById(R.id.edit_reg_mobile_no);
        edit_reg_username = findViewById(R.id.edit_reg_username);
        edit_reg_password = findViewById(R.id.edit_reg_password);
        reg_to_login_btn =findViewById(R.id.reg_to_login_btn);
        reg_to_login =findViewById(R.id.reg_to_login);
        reg_checkbox = findViewById(R.id.reg_checkbox);

        Auth = FirebaseAuth.getInstance();

        reg_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_reg_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_reg_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

//        reg_to_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
//                startActivity(i);
//            }
//        });

        reg_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_reg_name.getText().toString().isEmpty()) {
                    edit_reg_name.setError("Please Enter Your Name");
                } else if (edit_reg_mobile_no.getText().toString().isEmpty()) {
                    edit_reg_mobile_no.setError("Please Enter Your Mobile Number");
                } else if (edit_reg_mobile_no.getText().toString().length() != 10) {
                    edit_reg_mobile_no.setError("Mobile Number Must be 10 Digits");
                } else if (edit_reg_email.getText().toString().isEmpty()) {
                    edit_reg_email.setError("Please Enter Your Email Id");
                } else if (!edit_reg_email.getText().toString().contains("@") ||
                        !edit_reg_email.getText().toString().contains(".com")) {
                    edit_reg_email.setError("Invalid Email id");
                } else if (edit_reg_username.getText().toString().isEmpty()) {
                    edit_reg_username.setError("Please Enter Your Username");
                } else if (edit_reg_username.getText().toString().length() < 8) {
                    edit_reg_username.setError("Username must be greater then 8");
                }
                else if (!edit_reg_username.getText().toString().matches("(.*[A-Z].*)")) {

                    edit_reg_username.setError("must contain 1 upper case char ");
                } else if (!edit_reg_username.getText().toString().matches("(.*[a-z].*)")) {

                    edit_reg_username.setError("must contan 1 lowercase char");
                } else if (!edit_reg_username.getText().toString().matches("(.*[@#&^=+$].*)")) {

                    edit_reg_username.setError("must contain 1 special symbol");

                } else if (!edit_reg_username.getText().toString().matches("(.*[0-9].*)")) {

                    edit_reg_username.setError("must contain 1 integer");
                } else if (edit_reg_password.getText().toString().isEmpty()) {
                    edit_reg_password.setError("Please Enter Your Password");
                } else if (edit_reg_password.getText().toString().length() < 8) {
                    edit_reg_password.setError("Password must be greater then 8");
                }
                else {

                    Auth.createUserWithEmailAndPassword(edit_reg_email.getText().toString(), edit_reg_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    pd = new ProgressDialog(SignUpActivity.this);  //init
                                                    pd.setTitle("Register User");
                                                    pd.setMessage("Please Wait......");
                                                    pd.setCancelable(false);
                                                    pd.setCanceledOnTouchOutside(true);
                                                    pd.show();
                                                    addRegisterUser();
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

    private void addRegisterUser() {
        AsyncHttpClient client = new AsyncHttpClient();   //generate the request throw network
        RequestParams params = new RequestParams(); // passing the data with request

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("users");

        String name = edit_reg_name.getText().toString();
        String email_id = edit_reg_email.getText().toString();
        String mobile_no = edit_reg_mobile_no.getText().toString();
        String username = edit_reg_username.getText().toString();
        String password = edit_reg_password.getText().toString();

        HelperClass helperClass = new HelperClass(name,email_id,mobile_no,username,password);
        reference.child(username).setValue(helperClass);

        Toast.makeText(SignUpActivity.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();

        params.put("name",edit_reg_name.getText().toString());    //harsh kuche
        params.put("mobile_no",edit_reg_mobile_no.getText().toString());
        params.put("email_id",edit_reg_email.getText().toString());
        params.put("username",edit_reg_username.getText().toString());
        params.put("password",edit_reg_password.getText().toString());

        client.post(Urls.registerUserURL,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");

                            if (status.equals("1"))
                            {
                                pd.dismiss();
                                Toast.makeText(SignUpActivity.this,"Registration Successfully Done",
                                        Toast.LENGTH_SHORT).show();
//                                Classname objectname = new Constructorname();
                                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                pd.dismiss();
                                Toast.makeText(SignUpActivity.this, "Already Registration Done",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        pd.dismiss();
                        Toast.makeText(SignUpActivity.this, "Sever Error ", Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    public void reg_to_login(View view) {
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }
}

//404 = page not found
//500 = Server not found
//200 = ok