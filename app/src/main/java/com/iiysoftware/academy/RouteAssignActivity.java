package com.iiysoftware.academy;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.DriversAdapter;
import com.iiysoftware.academy.Fragments.RouteFragment;
import com.iiysoftware.academy.Fragments.SeeAssignedRouteFrag;
import com.iiysoftware.academy.Models.Drivers;

import okhttp3.Route;

public class RouteAssignActivity extends AppCompatActivity {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private DriversAdapter adapter;
    private FirebaseFirestore db;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_assign);

        Toolbar toolbar = findViewById(R.id.route_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assign Route");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        driverList = findViewById(R.id.all_students_lis);
        layoutManager = new LinearLayoutManager(this);
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        frameLayout = findViewById(R.id.vehicle_container);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new DriversAdapter(getApplicationContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnShowClick(new DriversAdapter.OnShowclick() {
            @Override
            public void getPosition(String userId) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);

                frameLayout.setVisibility(View.VISIBLE);
                SeeAssignedRouteFrag assignedRouteFrag = new SeeAssignedRouteFrag();
                assignedRouteFrag.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, assignedRouteFrag)
                        .commit();
            }
        });
        adapter.setOnItemClick(new DriversAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {

                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);

                frameLayout.setVisibility(View.VISIBLE);
                RouteFragment profileFragment = new RouteFragment();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
                        .commit();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
