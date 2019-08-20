package com.iiysoftware.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.academy.Adapters.StudentsAdapter;
import com.iiysoftware.academy.Adapters.StudentsDriversAdapter;
import com.iiysoftware.academy.Models.Drivers;
import com.iiysoftware.academy.Models.Students;

import java.util.HashMap;

import javax.annotation.Nullable;

public class AllDriversActivity extends AppCompatActivity {

    private RecyclerView citiesList;
    private LinearLayoutManager layoutManager;
    private StudentsDriversAdapter adapter;
    private FirebaseFirestore db;
    String state_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drivers);

        state_id = getIntent().getStringExtra("uid");

        Toolbar toolbar = findViewById(R.id.driver_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Drivers");

        citiesList = findViewById(R.id.all_drivers_list);
        layoutManager = new LinearLayoutManager(this);
        citiesList.setHasFixedSize(true);
        citiesList.setLayoutManager(layoutManager);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers>options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new StudentsDriversAdapter(getApplicationContext(), options);
        citiesList.setAdapter(adapter);

        adapter.setOnItemClick(new StudentsDriversAdapter.OnItemClick() {
            @Override
            public void getPosition(final String userId, final String driverName, final String driverImage) {

//                Query query1 = db.collection("Students").whereEqualTo("isAssigned", true);
//                query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
//                            String name = doc.get("name").toString();
//                            String profile = doc.get("profile").toString();
//                            String email = doc.get("email").toString();
//                            final String uid = doc.get("uid").toString();
//
//                            HashMap<String, Object> hashMap = new HashMap<>();
//                            hashMap.put("name", name);
//                            hashMap.put("profile", profile);
//                            hashMap.put("email", email);
//                            hashMap.put("uid", uid);
//                            hashMap.put("isAssigned", true);
//                            hashMap.put("driver_id", userId);
//                            hashMap.put("driver_name", driverName);
//                            hashMap.put("driver_image", driverImage);
//
//                            db.collection("AssignedStudents").document(uid).set(hashMap)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()){
//                                                Toast.makeText(AllDriversActivity.this, "Students assigned successfully", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//
//                            db.collection("Students").document(uid).delete();
//                        }
//                        Intent intent = new Intent(AllDriversActivity.this, StudentAssignActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//
//                    }
//                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

}
