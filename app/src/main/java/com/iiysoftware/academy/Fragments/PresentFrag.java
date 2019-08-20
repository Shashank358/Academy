package com.iiysoftware.academy.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Adapters.LocalAreaAdapter;
import com.iiysoftware.academy.Adapters.PresentsAdapter;
import com.iiysoftware.academy.Models.Date;
import com.iiysoftware.academy.Models.LocalAreas;
import com.iiysoftware.academy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresentFrag extends Fragment {

    private RecyclerView attList;
    private LinearLayoutManager layoutManager;
    private PresentsAdapter adapter;
    private FirebaseFirestore db;

    public PresentFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_present, container, false);

        attList = view.findViewById(R.id.present_list);
        layoutManager = new LinearLayoutManager(getActivity());
        attList.setHasFixedSize(true);
        attList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Attendance");
        final FirestoreRecyclerOptions<Date> options = new FirestoreRecyclerOptions.Builder<Date>()
                .setQuery(query, Date.class)
                .build();

        adapter = new PresentsAdapter(getContext(), options);
        attList.setAdapter(adapter);


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
