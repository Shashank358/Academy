package com.iiysoftware.academy.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.academy.Adapters.StudentsAdapter;
import com.iiysoftware.academy.AllDriversActivity;
import com.iiysoftware.academy.Models.Students;
import com.iiysoftware.academy.R;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignStudFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private StudentsAdapter adapter;
    private FirebaseFirestore db;
    private FloatingActionButton fab;

    public AssignStudFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_stud, container, false);


        driverList = view.findViewById(R.id.all_students_lis);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);
        fab = view.findViewById(R.id.assign_student_fab);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Students");
        final FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>()
                .setQuery(query, Students.class)
                .build();

        adapter = new StudentsAdapter(getContext(), options);
        driverList.setAdapter(adapter);

        Query query1 = db.collection("Students").whereEqualTo("isAssigned", true);
        query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    boolean check = doc.getBoolean("isAssigned");
                    if (check == true){
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllDriversActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
