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

import android.view.KeyEvent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.RoutesAdapter;
import com.iiysoftware.academy.DashActivity;
import com.iiysoftware.academy.Models.Routes;
import com.iiysoftware.academy.R;
import com.iiysoftware.academy.RouteAssignActivity;

import java.util.HashMap;

import okhttp3.Route;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment {

    private RecyclerView routeList;
    private LinearLayoutManager layoutManager;
    private RoutesAdapter adapter;
    private FirebaseFirestore db;
    private Toolbar toolbar;
    String currentUser, driver_id;

    public RouteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        driver_id = getArguments().getString("uid");

        routeList = view.findViewById(R.id.all_route_list);
        layoutManager = new LinearLayoutManager(getActivity());
        routeList.setHasFixedSize(true);
        routeList.setLayoutManager(layoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        toolbar = view.findViewById(R.id.route_fragment_toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("All Routes");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RouteAssignActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Routes");
        final FirestoreRecyclerOptions<Routes> options = new FirestoreRecyclerOptions.Builder<Routes>()
                .setQuery(query, Routes.class)
                .build();

        adapter = new RoutesAdapter(getContext(), options);
        routeList.setAdapter(adapter);

        adapter.setOnItemClick(new RoutesAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId, String from, String dist, String stops, String to, boolean isAssigned) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("uid    ", userId);
                hashMap.put("from", from);
                hashMap.put("to", to);
                hashMap.put("isAssigned", isAssigned);
                hashMap.put("stops", stops);
                hashMap.put("dist", dist);
                hashMap.put("driver", driver_id);

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("isAssigned", true);

                db.collection("Routes").document(userId).update(hashMap1);
                db.collection("AssignedRoutes").document(driver_id)
                        .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity() , "Route assigned to driver", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getActivity(), RouteAssignActivity.class));
                    }
                });
            }
        });

        return view;
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

}
