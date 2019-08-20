package com.iiysoftware.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.academy.Models.Students;

import java.util.ArrayList;
import java.util.List;

public class CollectedFeesActivity extends AppCompatActivity {

    private RecyclerView studentsList;
    private LinearLayoutManager layoutManager;
    private StudentsFeesAdapter adapter;
    private FirebaseFirestore db;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_fees);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Collected Fees");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.all_list_spinner);
        db = FirebaseFirestore.getInstance();

        spinner.setSelection(0, false);

        db.collection("Parent").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    cate.add(0, "Select Class");

                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getData().get("student_class").toString();
                        cate.add(name);
                    }
                    ArrayAdapter<String> madapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, cate);

                    madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(madapter);
                }
            }
        });


        studentsList = findViewById(R.id.all_collected_fees_list);
        layoutManager = new LinearLayoutManager(this);
        studentsList.setHasFixedSize(true);
        studentsList.setLayoutManager(layoutManager);

        Query query = db.collection("Parent");
        final FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>()
                .setQuery(query, Students.class)
                .build();

        adapter = new StudentsFeesAdapter(getApplicationContext(), options);
        studentsList.setAdapter(adapter);


        //-----spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                db.collection("Parent").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                final String value = doc.getData().get("student_class").toString();
                                String filter = parent.getSelectedItem().toString();
                                if (value.equals(filter)){
                                    Toast.makeText(CollectedFeesActivity.this, filter, Toast.LENGTH_SHORT).show();

                                    Query query = db.collection("Parent").whereEqualTo("student_class", filter);
                                    final FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>()
                                            .setQuery(query, Students.class)
                                            .build();

                                    adapter = new StudentsFeesAdapter(getApplicationContext(), options);
                                    studentsList.setAdapter(adapter);

                                    adapter.startListening();

                                }else if (filter.equals("Select Class")){
                                    Query query = db.collection("Parent");
                                    final FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>()
                                            .setQuery(query, Students.class)
                                            .build();

                                    adapter = new StudentsFeesAdapter(getApplicationContext(), options);
                                    studentsList.setAdapter(adapter);

                                    adapter.startListening();
                                }
                            }
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
