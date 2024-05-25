package com.example.techclaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

public class Payment extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu_button_drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu_button_drawer = findViewById(R.id.menu_button_drawer);
        navigationView = findViewById(R.id.nav_view);

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
                Toast.makeText(Payment.this, userName.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Intent intent = null;

                if (itemId == R.id.home){
                    Toast.makeText(Payment.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Payment.this, Home.class);
                }

                if (itemId == R.id.activities){
                    Toast.makeText(Payment.this, "Activities Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Payment.this, Activities.class);
                }

                if (itemId == R.id.invoice){
                    Toast.makeText(Payment.this, "Invoice Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Payment.this, Invoice.class);
                }

                if (itemId == R.id.payment){
                    Toast.makeText(Payment.this, "Payment Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Payment.this, Payment.class);
                }

                if (itemId == R.id.acct_mngmnt){
                    Toast.makeText(Payment.this, "Account Management Clicked", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Payment.this, Acc_Management.class);
                }

                if (itemId == R.id.logout){
                    Toast.makeText(Payment.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
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

    }

    private void clearSessionData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}