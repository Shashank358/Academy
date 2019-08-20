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
import com.iiysoftware.academy.Models.Cities;

public class CityActivity extends AppCompatActivity {

    private RecyclerView citiesList;
    private LinearLayoutManager layoutManager;
    private CitiesAdapter adapter;
    private FirebaseFirestore db;
    String state_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        state_id = getIntent().getStringExtra("state_id");

        Toolbar toolbar = findViewById(R.id.city_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cities");

        citiesList = findViewById(R.id.cities_list);
        layoutManager = new LinearLayoutManager(this);
        citiesList.setHasFixedSize(true);
        citiesList.setLayoutManager(layoutManager);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Cities").document("Punjab")
                .collection("CityData");
        final FirestoreRecyclerOptions<Cities> options = new FirestoreRecyclerOptions.Builder<Cities>()
                .setQuery(query, Cities.class)
                .build();

        adapter = new CitiesAdapter(getApplicationContext(), options);
        citiesList.setAdapter(adapter);

        adapter.setOnItemClick(new CitiesAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                String city_name = userId;
                Intent intent = new Intent(CityActivity.this, LocalAreaActivity.class);
                intent.putExtra("city_name", city_name);
                intent.putExtra("state_id", state_id);
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
                .orderBy("city_name").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<Cities> options = new FirestoreRecyclerOptions.Builder<Cities>()
                .setQuery(query, Cities.class)
                .build();

        adapter = new CitiesAdapter(getApplicationContext(), options);
        citiesList.setAdapter(adapter);
        adapter.setOnItemClick(new CitiesAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                String city_name = userId;
                Intent intent = new Intent(CityActivity.this, LocalAreaActivity.class);
                intent.putExtra("city_name", city_name);
                intent.putExtra("state_id", state_id);
                startActivity(intent);
            }
        });
        adapter.startListening();

    }

    private void searchData(String s) {
        Query query = db.collection("Cities").document(state_id).collection("CityData")
                .orderBy("city_name").startAt(s).endAt(s+"\uf8ff");
        final FirestoreRecyclerOptions<Cities> options = new FirestoreRecyclerOptions.Builder<Cities>()
                .setQuery(query, Cities.class)
                .build();

        adapter = new CitiesAdapter(getApplicationContext(), options);
        citiesList.setAdapter(adapter);
        adapter.setOnItemClick(new CitiesAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                String city_name = userId;
                Intent intent = new Intent(CityActivity.this, LocalAreaActivity.class);
                intent.putExtra("city_name", city_name);
                intent.putExtra("state_id", state_id);
                startActivity(intent);
            }
        });
        adapter.startListening();
    }
}
