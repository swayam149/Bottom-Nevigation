package com.example.bottomnevigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditprofileActivity extends AppCompatActivity {
    EditText editName,editEmail,editUsername,editPassword;
    Button saveButton;
    String nameUser,emailUser,usernameUser,passwordUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName= findViewById(R.id.edit_name);
        editEmail= findViewById(R.id.edit_email);
        editUsername= findViewById(R.id.edit_Username);
        editPassword= findViewById(R.id.edit_password);
        saveButton= findViewById(R.id.save_button);

        showData();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged()||isEmailChanged()||isPasswordChanged()){
                    Toast.makeText(EditprofileActivity.this, "saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditprofileActivity.this, "No Change Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isNameChanged(){
        if (!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        }else{
            return false;
        }
    }
    public boolean isEmailChanged(){
        if (!emailUser.equals(editEmail.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        }else{
            return false;
        }
    }
    public boolean isPasswordChanged(){
        if (!passwordUser.equals(editPassword.getText().toString())){
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        }else{
            return false;
        }
    }
    public void showData(){
        Intent intent = getIntent();
        nameUser =intent.getStringExtra("name");
        emailUser =intent.getStringExtra("email");
        usernameUser=intent.getStringExtra("username");
        passwordUser=intent.getStringExtra("password");


        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }
}