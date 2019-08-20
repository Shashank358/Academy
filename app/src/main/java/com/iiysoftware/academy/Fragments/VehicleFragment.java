package com.iiysoftware.academy.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.VehicleAdapter;
import com.iiysoftware.academy.Models.Vehicles;
import com.iiysoftware.academy.R;
import com.iiysoftware.academy.VehicleAssignActivity;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleFragment extends Fragment {

    private RecyclerView vehicleList;
    private LinearLayoutManager layoutManager;
    private VehicleAdapter adapter;
    private FirebaseFirestore db;
    private Toolbar toolbar;

    public VehicleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_vehicle, container, false);

        final String uid = getArguments().getString("uid");

        vehicleList = view.findViewById(R.id.all_vehicles_list);
        layoutManager = new LinearLayoutManager(getActivity());
        vehicleList.setHasFixedSize(true);
        vehicleList.setLayoutManager(layoutManager);

        toolbar = view.findViewById(R.id.vehicles_fragment_toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("All Vehicles");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VehicleAssignActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                
            }
        });

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Vehicles").whereEqualTo("isAssigned", false);
        final FirestoreRecyclerOptions<Vehicles> options = new FirestoreRecyclerOptions.Builder<Vehicles>()
                .setQuery(query, Vehicles.class)
                .build();

        adapter = new VehicleAdapter(getContext(), options);
        vehicleList.setAdapter(adapter);

        adapter.setOnItemClick(new VehicleAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId, String image, String name, boolean isAssigned) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("uid", userId);
                hashMap.put("driver_id", uid);
                hashMap.put("image", image);
                hashMap.put("name_plate", name);
                hashMap.put("isAssigned", isAssigned);

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("isAssigned", true);
                hashMap1.put("driver_id", uid);

                db.collection("Vehicles").document(userId).update(hashMap1);
                db.collection("AssignedVehicles").document(uid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity() , "Vehicle assigned", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), VehicleAssignActivity.class));
                        }
                    }
                });
            }
        });



        return  view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.city_menu, menu);

        MenuItem item = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                searchData(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                searchLiveData(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
