package com.example.techclaim;

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

public class login_activity extends AppCompatActivity {
    private boolean passwordshowing = false;
    private EditText login_email, login_password;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        email = password = "";
        final EditText login_email = findViewById(R.id.login_email);
        final EditText login_password = findViewById(R.id.login_password);
        final ImageView pass_icon = findViewById(R.id.pass_icon);
        final TextView signup_btn = findViewById(R.id.signup_btn);
        final AppCompatButton login_btn = findViewById(R.id.login_btn);

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

        //sign up button or registration page
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_activity.this, signup_activity.class));
            }
        });
    }


    public void signup(View view) {
        Intent intent = new Intent(this, signup_activity.class);
        startActivity(intent);
        finish();
    }
}
