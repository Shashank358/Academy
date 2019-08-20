package com.iiysoftware.academy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.LocationDriversAdapter;
import com.iiysoftware.academy.Adapters.StudentsDriversAdapter;
import com.iiysoftware.academy.Models.Drivers;

import javax.annotation.Nullable;

public class LocationActivity extends AppCompatActivity {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private LocationDriversAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Toolbar toolbar = findViewById(R.id.location_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Track Drivers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        driverList = findViewById(R.id.all_tracker_list);
        layoutManager = new LinearLayoutManager(this);
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new LocationDriversAdapter(getApplicationContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnItemClick(new LocationDriversAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                Intent intent = new Intent(LocationActivity.this, MapsActivity.class);
                intent.putExtra("uid", userId);
                startActivity(intent);
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
