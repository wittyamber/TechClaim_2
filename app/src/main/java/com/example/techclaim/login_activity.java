package com.example.techclaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {
    private boolean passwordshowing = false;
    private ProgressBar progressBar;
    private  EditText login_email, login_password;
    private FirebaseAuth authProfile;
    private static final String TAG = "login_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final ImageView pass_icon = findViewById(R.id.pass_icon);
        final TextView signup_btn = findViewById(R.id.signup_btn);
        final AppCompatButton login_btn = findViewById(R.id.login_btn);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        //Login User
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login_activity.this, "Please enter your email. ", Toast.LENGTH_SHORT).show();
                    login_email.setError("Email is required");
                    login_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(login_activity.this, "Please re-enter your email. ", Toast.LENGTH_SHORT).show();
                    login_email.setError("Valid email is required");
                    login_email.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login_activity.this, "Please enter your password. ", Toast.LENGTH_SHORT).show();
                    login_password.setError("Password is required");
                    login_password.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginuser(email, password);
                }

            }
        });

        //sign up button or registration page
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signup(v);
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
    }

    private void loginuser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(login_activity.this, "You are successfully logged in.", Toast.LENGTH_LONG).show();

                    // Start the dashboard activity
                    Intent intent = new Intent(login_activity.this, Home.class);
                    startActivity(intent);

                    // Finish the login activity so the user cannot come back to it using the back button
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        login_email.setError("Email does not exist. Please register");
                        login_email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        login_email.setError("Invalid credentials, Kindly check and re-enter.");
                        login_email.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(login_activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void signup(View view) {
        Intent intent = new Intent(this, signup_activity.class);
        startActivity(intent);
    }
}


