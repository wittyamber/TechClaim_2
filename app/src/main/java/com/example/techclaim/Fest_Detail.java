package com.example.techclaim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class Fest_Detail extends AppCompatActivity {

    ImageView back_img;
    Button pay_now, partial_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intrams_details);

        back_img = findViewById(R.id.back_img);
        pay_now = findViewById(R.id.pay_now);
        partial_pay = findViewById(R.id.partial_payment);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fest_Detail.this, Invoice.class);
                startActivity(intent);
            }
        });

        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fest_Detail.this, Payment.class);
                startActivity(intent);
            }
        });

        partial_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fest_Detail.this, Payment.class);
                startActivity(intent);
            }
        });
    }
}