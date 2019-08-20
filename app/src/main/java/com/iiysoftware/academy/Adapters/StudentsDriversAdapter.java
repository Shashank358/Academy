package com.iiysoftware.academy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.academy.AllDriversActivity;
import com.iiysoftware.academy.Models.Drivers;
import com.iiysoftware.academy.R;
import com.iiysoftware.academy.StudentAssignActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentsDriversAdapter extends FirestoreRecyclerAdapter<Drivers, StudentsDriversAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public StudentsDriversAdapter(Context mContext, FirestoreRecyclerOptions<Drivers> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId, String name, String image);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Drivers model) {

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.name.setText(model.getUser_name());
        holder.email.setText(model.getUser_email());

        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onItemClick.getPosition(model.getPush_id(), model.getUser_name(), model.getImage());

                Query query1 = db.collection("Students").whereEqualTo("isAssigned", true);
                query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            String name = doc.get("name").toString();
                            String profile = doc.get("profile").toString();
                            String email = doc.get("email").toString();
                            final String uid = doc.get("uid").toString();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", name);
                            hashMap.put("profile", profile);
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("isAssigned", true);
                            hashMap.put("driver_id", model.getPush_id());
                            hashMap.put("driver_name", model.getUser_name());
                            hashMap.put("driver_image", model.getImage());

                            db.collection("AssignedStudents").document(uid).set(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(mContext, "Students assigned successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            db.collection("Students").document(uid).delete();
                        }
                        holder.assign.setText("Assigned");
                        holder.assign.setEnabled(false);
                    }
                });
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_driver_students_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email;
        private CardView cardView;
        private Button assign;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.driver_name);
            cardView = itemView.findViewById(R.id.student_card);
            profile = itemView.findViewById(R.id.driver_image);
            email = itemView.findViewById(R.id.driver_email);
            assign = itemView.findViewById(R.id.assign_route_btn);

        }

    }
}
