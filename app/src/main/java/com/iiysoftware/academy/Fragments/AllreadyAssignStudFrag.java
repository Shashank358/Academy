package com.iiysoftware.academy.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.AssignedStudentsAdapter;
import com.iiysoftware.academy.Adapters.StudentsAdapter;
import com.iiysoftware.academy.Models.AssignedStudents;
import com.iiysoftware.academy.Models.Students;
import com.iiysoftware.academy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllreadyAssignStudFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private AssignedStudentsAdapter adapter;
    private FirebaseFirestore db;
    private FloatingActionButton fab;

    public AllreadyAssignStudFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_allready_assign_stud, container, false);

        driverList = view.findViewById(R.id.assigned_student_lists);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);
        fab = view.findViewById(R.id.assign_student_fab);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("AssignedStudents");
        final FirestoreRecyclerOptions<AssignedStudents> options = new FirestoreRecyclerOptions.Builder<AssignedStudents>()
                .setQuery(query, AssignedStudents.class)
                .build();

        adapter = new AssignedStudentsAdapter(getContext(), options);
        driverList.setAdapter(adapter);

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
