package com.iiysoftware.academy.AssignStudPackage;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssToDriverFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private StudentsAdapter1 adapter;
    private FirebaseFirestore db;

    public AssToDriverFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ass_to_driver, container, false);

        final String uid = getArguments().getString("uid");

        driverList = view.findViewById(R.id.all_students_list1);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("AssignedStudents").whereEqualTo("driver", uid);
        final FirestoreRecyclerOptions<AssignedStudents> options = new FirestoreRecyclerOptions.Builder<AssignedStudents>()
                .setQuery(query, AssignedStudents.class)
                .build();

        adapter = new StudentsAdapter1(getContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnItemClick(new StudentsAdapter1.OnItemClick() {
            @Override
            public void getPosition(final String userId) {
                db.collection("AssignedStudents").document(userId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("isAssigned", false);
                            hashMap1.put("driver_id", "default");

                            db.collection("Parent").document(userId).update(hashMap1);
                        }
                    }
                });
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AssignedStudFrag profileFragment = new AssignedStudFrag();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.stud_container, profileFragment)
                                .commit();
                        return true;
                    }
                }
                return false;
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
