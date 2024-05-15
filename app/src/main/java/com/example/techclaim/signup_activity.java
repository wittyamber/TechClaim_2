package com.example.techclaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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

public class signup_activity extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean conpasswordShowing = false;
    private EditText signup_studID, signup_name, signup_email, signup_password, signup_password_con;

    //create object of DatabaseReference class
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techclaim-c67ef-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        final ImageView pass_icon = findViewById(R.id.pass_icon_signup);
        final ImageView pass_iconcon = findViewById(R.id.pass_icon_con);
        final AppCompatButton signup_btn = findViewById(R.id.reg_btn);
        final TextView logintxt_btn = findViewById(R.id.logintxt_btn);

        signup_name = findViewById(R.id.signup_name);
        signup_studID = findViewById(R.id.signup_studID);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_password_con = findViewById(R.id.signup_password_con);

        pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordShowing) {
                    passwordShowing = false;

                    signup_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass_icon.setImageResource(R.drawable.show_pass);
                } else {
                    passwordShowing = true;

                    signup_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass_icon.setImageResource(R.drawable.hide_pass);
                }

                //move cursor at last of the text
                signup_password.setSelection(signup_password.length());

            }
        });

        pass_iconcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (conpasswordShowing) {
                    conpasswordShowing = false;

                    signup_password_con.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass_iconcon.setImageResource(R.drawable.show_pass);
                } else {
                    conpasswordShowing = true;

                    signup_password_con.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass_iconcon.setImageResource(R.drawable.hide_pass);
                }

                //move cursor at last of the text
                signup_password_con.setSelection(signup_password_con.length());

            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from EditTexts
                String studID, name, email, pass, pass_con;
                studID = signup_studID.getText().toString();
                name = signup_name.getText().toString();
                email = signup_email.getText().toString();
                pass = signup_password.getText().toString();
                pass_con = signup_password_con.getText().toString();

                //check if user fill all the fields

                if (studID.isEmpty() || name.isEmpty() || email.isEmpty() || pass.isEmpty() || pass_con.isEmpty()) {
                    Toast.makeText(signup_activity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                //check if the password is matching
                else if (!pass.equals(pass_con)){
                    Toast.makeText(signup_activity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
                else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if student id is already registered
                            if (snapshot.hasChild(studID)) {
                                Toast.makeText(signup_activity.this, "Student ID is already registered.", Toast.LENGTH_SHORT).show();
                            }else {
                                //showing data to firebase
                                databaseReference.child("users").child(studID).child("name").setValue(signup_name);
                                databaseReference.child("users").child(studID).child("email").setValue(signup_email);
                                databaseReference.child("users").child(studID).child("pass").setValue(signup_password);

                                Toast.makeText(signup_activity.this, "You are Successfully Registered.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                                                                                                });

                }

            }
        });

        logintxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
