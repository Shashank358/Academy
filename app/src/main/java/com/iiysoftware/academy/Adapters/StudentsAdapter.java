package com.iiysoftware.academy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.academy.Models.Drivers;
import com.iiysoftware.academy.Models.Students;
import com.iiysoftware.academy.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentsAdapter extends FirestoreRecyclerAdapter<Students, StudentsAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public StudentsAdapter(Context mContext, FirestoreRecyclerOptions<Students> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Students model) {

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        holder.name.setText(model.getStudent_name());
        holder.email.setText(model.getStudent_email());

        db.collection("Students").document(model.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        boolean check = task.getResult().getBoolean("isAssigned");
                        if (check == true){
                            holder.assigned.setVisibility(View.VISIBLE);
                            holder.add.setVisibility(View.GONE);
                        }else {
                            holder.assigned.setVisibility(View.GONE);
                            holder.add.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isAssigned", true);
                db.collection("Students").document(model.getUid())
                        .update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            holder.assigned.setVisibility(View.VISIBLE);
                            holder.add.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        holder.assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isAssigned", false);
                db.collection("Students").document(model.getUid())
                        .update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            holder.assigned.setVisibility(View.GONE);
                            holder.add.setVisibility(View.VISIBLE);
                            Toast.makeText(mContext, "Unselected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_students_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email;
        private CardView cardView;
        private ImageView add, assigned;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_student_name);
            cardView = itemView.findViewById(R.id.student_card);
            profile = itemView.findViewById(R.id.single_student_image);
            email = itemView.findViewById(R.id.single_student_email);
            add = itemView.findViewById(R.id.add_student_view);
            assigned = itemView.findViewById(R.id.asigned_student_view);

        }

    }
}
