package com.example.techclaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu_button_drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView welcomeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu_button_drawer = findViewById(R.id.menu_button_drawer);
        navigationView = findViewById(R.id.nav_view);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize TextView
        welcomeTV = findViewById(R.id.welcomeTV);

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch user's name from database
            mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        welcomeTV.setText("Welcome, " + name + "!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }

        menu_button_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        View headerView = navigationView.getHeaderView(0);
        ImageView userProf = headerView.findViewById(R.id.user_profile);
        TextView userName = headerView.findViewById(R.id.user_name);

        userProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, userName.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Intent intent = null;

                if (itemId == R.id.home){
                    Toast.makeText(Home.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Home.this, Home.class);
                }

                if (itemId == R.id.activities){
                    Toast.makeText(Home.this, "Activities Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Home.this, Activities.class);
                }

                if (itemId == R.id.invoice){
                    Toast.makeText(Home.this, "Invoice Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Home.this, Invoice.class);
                }

                if (itemId == R.id.payment){
                    Toast.makeText(Home.this, "Payment Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Home.this, Payment.class);
                }

                if (itemId == R.id.acct_mngmnt){
                    Toast.makeText(Home.this, "Account Management Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Home.this, Acc_Management.class);
                }

                if (itemId == R.id.logout){
                    Toast.makeText(Home.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    finishAffinity();  // Close all activities and quit the application
                    System.exit(0);
                }

                if (intent != null) {
                    startActivity(intent);
                }

                drawerLayout.close();

                return false;
            }
        });

        CardView intrams = findViewById(R.id.intramsCV);
        intrams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Activities.class);
                startActivity(intent);
            }
        });

        CardView fest = findViewById(R.id.fest_CV);
        fest.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Activities.class);
                startActivity(intent);
            }
        }));

        CardView wika = findViewById(R.id.wika_CV);
        wika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Activities.class);
                startActivity(intent);
            }
        });

    }

    private void clearSessionData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}