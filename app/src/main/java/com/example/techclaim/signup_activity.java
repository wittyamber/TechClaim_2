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
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_activity extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean conpasswordShowing = false;
    private EditText signup_studID, signup_name, signup_email, signup_password, signup_password_con;
    private ProgressBar progressBar;
    private static final String TAG= "signup_activity";

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

        progressBar = findViewById(R.id.progressBar);
        signup_name = findViewById(R.id.signup_name);
        signup_studID = findViewById(R.id.signup_studID);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_password_con = findViewById(R.id.signup_password_con);

        pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(signup_password, pass_icon);
            }
        });

        pass_iconcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(signup_password_con, pass_iconcon);
            }
        });

        logintxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studID = signup_studID.getText().toString().trim();
                String name = signup_name.getText().toString().trim();
                String email = signup_email.getText().toString().trim();
                String pass = signup_password.getText().toString().trim();
                String pass_con = signup_password_con.getText().toString().trim();

                if (TextUtils.isEmpty(studID)) {
                    Toast.makeText(signup_activity.this, "Please enter your ID", Toast.LENGTH_SHORT).show();
                    signup_studID.setError("Student ID is required");
                    signup_studID.requestFocus();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(signup_activity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    signup_name.setError("Full name is required");
                    signup_name.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup_activity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    signup_email.setError("Email is required");
                    signup_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(signup_activity.this, "Please re-nter your Email", Toast.LENGTH_SHORT).show();
                    signup_email.setError("Valid Email is required");
                    signup_email.requestFocus();
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(signup_activity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    signup_password.setError("Password is required");
                    signup_password.requestFocus();
                }else if (pass.length() < 6) {
                    Toast.makeText(signup_activity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    signup_password.setError("Password too weak");
                    signup_password.requestFocus();
                } else if (TextUtils.isEmpty(pass_con)) {
                    Toast.makeText(signup_activity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    signup_password_con.setError("Password confirmation is required");
                    signup_password_con.requestFocus();
                }else if (!pass.equals(pass_con)) {
                    Toast.makeText(signup_activity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    signup_password_con.setError("Password confirmation is required");
                    signup_password_con.requestFocus();
                    //Clear the entered passwords
                    signup_password.clearComposingText();
                    signup_password_con.clearComposingText();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(studID, name, email, pass);
                }
            }
        });

    }

    private void togglePasswordVisibility(EditText passwordField, ImageView icon) {
        if (passwordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            icon.setImageResource(R.drawable.hide_pass);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            icon.setImageResource(R.drawable.show_pass);
        }
        passwordField.setSelection(passwordField.length());
    }

    //Register User using the credentials given
    private void registerUser(String studID, String name, String email, String pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create user Profile
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(signup_activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Enter User Data Into the Firebase Realtime Database.
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(studID, name);

                    //Extracting user reference from Database
                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceprofile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                //Send Verification Email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(signup_activity.this, "User successfully registered. Please verify your email", Toast.LENGTH_LONG).show();

                                //Open User Profile after successful registration
                                Intent intent = new Intent(signup_activity.this, Profile.class);

                                //Prevent users to return in signup page
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                                // Start the dashboard activity
                                intent = new Intent(signup_activity.this, MainActivity.class);

                                // Prevent users from returning to the signup page
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(signup_activity.this, "Sign up failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                            //Hide ProgressBar whether user successfully registered or not
                            progressBar.setVisibility(View.VISIBLE);

                        }
                    });


                } else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        signup_password.setError("Your password is too weak. Kindly use a combination of characters, numbers and special characters.");
                        signup_password.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        signup_password.setError("Your email is invalid or already in use. Kindly re-enter.");
                        signup_password.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e) {
                        signup_password.setError("Email is already registered. Use another email.");
                        signup_password.requestFocus();
                    }catch (Exception e) {
                        Log log = null;
                        log.e(TAG, e.getMessage());
                        Toast.makeText(signup_activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    //Hide ProgressBar whether user successfully registered or not
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}