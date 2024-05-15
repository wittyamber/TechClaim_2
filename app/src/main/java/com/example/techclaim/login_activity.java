package com.example.techclaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {
    private boolean passwordshowing = false;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techclaim-c67ef-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final EditText login_id = findViewById(R.id.login_id);
        final EditText login_password = findViewById(R.id.login_password);
        final ImageView pass_icon = findViewById(R.id.pass_icon);
        final TextView signup_btn = findViewById(R.id.signup_btn);
        final AppCompatButton login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studID = login_id.getText().toString();
                final String password = login_password.getText().toString();

                if (studID.isEmpty() || password.isEmpty()){
                    Toast.makeText(login_activity.this,"Please enter Student ID or Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if the Student ID is already exist
                            if (snapshot.hasChild(studID)) {
                                final String getPassword = snapshot.child(studID).child("password").getValue(String.class);

                                if (getPassword.equals(password)) {
                                    Toast.makeText(login_activity.this,"Successfully Logged in", Toast.LENGTH_SHORT).show();

                                    //opens dashboard
                                    startActivity(new Intent(login_activity.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(login_activity.this,"Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(login_activity.this,"Incorrect Student ID", Toast.LENGTH_SHORT).show();                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking if password is showing or not
                if (passwordshowing){
                    passwordshowing = false;

                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass_icon.setImageResource(R.drawable.show_pass);
                }else{
                    passwordshowing = true;

                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass_icon.setImageResource(R.drawable.hide_pass);
                }

                //move cursor at last of the text
                login_password.setSelection(login_password.length());

            }
        });

        //open sign up page
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_activity.this, signup_activity.class));
            }
        });
    }

}
