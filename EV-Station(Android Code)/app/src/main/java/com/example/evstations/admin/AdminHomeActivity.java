package com.example.evstations.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.window.SplashScreen;

import com.example.evstations.DBClass;
import com.example.evstations.LoginActivity;
import com.example.evstations.R;
import com.example.evstations.SplashScreenActivity;

public class AdminHomeActivity extends AppCompatActivity {

    CardView cardView1, cardView2, cardView3, cardView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        cardView1 = findViewById(R.id.card_view1);
        cardView2 = findViewById(R.id.card_view2);
        cardView3 = findViewById(R.id.card_view3);
        cardView4 = findViewById(R.id.card_view4);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, EVStationsActivity.class);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ViewAllBookingActivity.class);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "DELETE FROM Configuration";
                DBClass.execNonQuery(query);
                Intent intent = new Intent(AdminHomeActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}