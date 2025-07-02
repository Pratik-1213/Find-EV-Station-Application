package com.example.evstations.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.evstations.DBClass;
import com.example.evstations.LoginActivity;
import com.example.evstations.R;
import com.example.evstations.adapters.User;
import com.example.evstations.admin.AdminHomeActivity;
import com.example.evstations.admin.EVStationsActivity;
import com.example.evstations.admin.UsersActivity;
import com.example.evstations.admin.ViewAllBookingActivity;

public class UserHomeActivity extends AppCompatActivity {

    CardView cardView1, cardView2, cardView3, cardView4, cardView5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        cardView1 = findViewById(R.id.card_view1);
        cardView2 = findViewById(R.id.card_view2);
        cardView3 = findViewById(R.id.card_view3);
        cardView4 = findViewById(R.id.card_view4);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, VehiclesActivity.class);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, StationFilterActivity.class);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, MyBookingsActivity.class);
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}