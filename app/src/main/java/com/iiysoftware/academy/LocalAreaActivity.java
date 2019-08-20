package com.iiysoftware.academy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.CitiesAdapter;
import com.iiysoftware.academy.Adapters.LocalAreaAdapter;
import com.iiysoftware.academy.Models.Cities;
import com.iiysoftware.academy.Models.LocalAreas;

public class LocalAreaActivity extends AppCompatActivity {

    private RecyclerView areaList;
    private LinearLayoutManager layoutManager;
    private LocalAreaAdapter adapter;
    private FirebaseFirestore db;
    private String local, state_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_area);

        local = getIntent().getStringExtra("city_name");
        state_id = getIntent().getStringExtra("state_id");

        Toolbar toolbar = findViewById(R.id.area_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Local Areas");

        areaList = findViewById(R.id.area_list);
        layoutManager = new LinearLayoutManager(this);
        areaList.setHasFixedSize(true);
        areaList.setLayoutManager(layoutManager);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Cities").document("Punjab")
                .collection("CityData").document(local).collection("AreaData");
        final FirestoreRecyclerOptions<LocalAreas> options = new FirestoreRecyclerOptions.Builder<LocalAreas>()
                .setQuery(query, LocalAreas.class)
                .build();

        adapter = new LocalAreaAdapter(getApplicationContext(), options);
        areaList.setAdapter(adapter);

        adapter.setOnItemClick(new LocalAreaAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                String area_name = userId;
                Intent intent = new Intent(LocalAreaActivity.this, DashActivity.class);
                intent.putExtra("area_name", area_name);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchLiveData(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    private void searchLiveData(String s) {

        Query query = db.collection("Cities").document(state_id).collection("CityData")
                .document(local).collection("AreaData")
                .orderBy("area_name").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<LocalAreas> options = new FirestoreRecyclerOptions.Builder<LocalAreas>()
                .setQuery(query, LocalAreas.class)
                .build();

        adapter = new LocalAreaAdapter(getApplicationContext(), options);
        areaList.setAdapter(adapter);
        adapter.setOnItemClick(new LocalAreaAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                String area_name = userId;
                Intent intent = new Intent(LocalAreaActivity.this, DashActivity.class);
                intent.putExtra("area_name", area_name);
                startActivity(intent);
            }
        });
        adapter.startListening();

    }

    private void searchData(String s) {
        Query query = db.collection("Cities").document(state_id).collection("CityData")
                .document(local).collection("AreaData")
                .orderBy("area_name").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<LocalAreas> options = new FirestoreRecyclerOptions.Builder<LocalAreas>()
                .setQuery(query, LocalAreas.class)
                .build();

        adapter = new LocalAreaAdapter(getApplicationContext(), options);
        areaList.setAdapter(adapter);
        adapter.setOnItemClick(new LocalAreaAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                String area_name = userId;
                Intent intent = new Intent(LocalAreaActivity.this, DashActivity.class);
                intent.putExtra("area_name", area_name);
                startActivity(intent);
            }
        });
        adapter.startListening();
    }
}
