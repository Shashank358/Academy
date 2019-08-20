package com.iiysoftware.academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.DriversAdapter;
import com.iiysoftware.academy.Adapters.StudentAssignPager;
import com.iiysoftware.academy.Adapters.StudentsDriversAdapter;
import com.iiysoftware.academy.AssignStudPackage.StudentPager;
import com.iiysoftware.academy.Models.Drivers;

public class StudentAssignActivity extends AppCompatActivity {


    private StudentPager adapter;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assign);

        Toolbar toolbar = findViewById(R.id.route_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assign Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPager = findViewById(R.id.student_viewpager);
        tabs = findViewById(R.id.student_tabs);
        adapter = new StudentPager(getSupportFragmentManager());
        tabs.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

    }

}
