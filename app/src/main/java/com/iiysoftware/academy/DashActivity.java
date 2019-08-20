package com.iiysoftware.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.iiysoftware.academy.Adapters.AttendanceDriversAdapter;

public class DashActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CardView card1, card2, card3, card4, card5, card6, card7;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        String area_name = getIntent().getStringExtra("area_name");

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu_image);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(GravityCompat.START);

                }else {
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
            }
        });

        Toolbar toolbar = findViewById(R.id.dash_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(toolbar.getTitle());

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, RouteAssignActivity.class);
                startActivity(intent);
            }
        });


        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, VehicleAssignActivity.class);
                startActivity(intent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, StudentAssignActivity.class);
                startActivity(intent);
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, DriverAttendanceActivity.class);
                startActivity(intent);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, CalenderActivity.class);
                startActivity(intent);
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, CollectedFeesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.menu_location){
            Intent intent = new Intent(DashActivity.this, LocationActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_route){
            Intent intent = new Intent(DashActivity.this, RouteAssignActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_vehicles){
            Intent intent = new Intent(DashActivity.this, VehicleAssignActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_students){
            Intent intent = new Intent(DashActivity.this, StudentAssignActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_calendar){
            Intent intent = new Intent(DashActivity.this, CalenderActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_attendance){
            Intent intent = new Intent(DashActivity.this, DriverAttendanceActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_fees){
            Intent intent = new Intent(DashActivity.this, CollectedFeesActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
