package com.iiysoftware.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.DriversAdapter;
import com.iiysoftware.academy.Adapters.VehicleDriverAdapter;
import com.iiysoftware.academy.Fragments.RouteFragment;
import com.iiysoftware.academy.Fragments.SeeAssignedVehicleFrag;
import com.iiysoftware.academy.Fragments.VehicleFragment;
import com.iiysoftware.academy.Models.Drivers;

import java.util.HashMap;

public class VehicleAssignActivity extends AppCompatActivity {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private VehicleDriverAdapter adapter;
    private FirebaseFirestore db;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_assign);

        Toolbar toolbar = findViewById(R.id.vehicle_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assign Route");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        driverList = findViewById(R.id.vehicle_driver_list);
        layoutManager = new LinearLayoutManager(this);
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        frameLayout = findViewById(R.id.vehicle_container);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new VehicleDriverAdapter(getApplicationContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnAssignItemClick(new VehicleDriverAdapter.OnAssignItemClick() {
            @Override
            public void getPosition(String userId, Button assign) {
                frameLayout.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                VehicleFragment profileFragment = new VehicleFragment();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
                        .commit();
            }
        });

        adapter.setOnUnAssignItemClick(new VehicleDriverAdapter.OnUnassignItemClick() {
            @Override
            public void getPosition(String userId) {
                frameLayout.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                SeeAssignedVehicleFrag profileFragment = new SeeAssignedVehicleFrag();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
                        .commit();
            }
        });

        adapter.setOnItemClick(new VehicleDriverAdapter.OnItemClick() {
            @Override
            public void getPosition(final String userId) {

                db.collection("AssignedVehicles").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String uid = task.getResult().get("uid").toString();
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("isAssigned", false);

                            db.collection("Vehicles").document(uid).update(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }
                                }
                            });
                            db.collection("AssignedVehicles").document(userId)
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(VehicleAssignActivity.this, "unassigned", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });
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
