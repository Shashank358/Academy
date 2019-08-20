package com.iiysoftware.academy.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.iiysoftware.academy.Models.AssignedStudents;
import com.iiysoftware.academy.Models.Students;
import com.iiysoftware.academy.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class AssignedStudentsAdapter extends FirestoreRecyclerAdapter<AssignedStudents, AssignedStudentsAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;
    private ProgressDialog dialog;

    public AssignedStudentsAdapter(Context mContext, FirestoreRecyclerOptions<AssignedStudents> options) {
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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final AssignedStudents model) {

        dialog = new ProgressDialog(mContext);
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getProfile()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.driverName.setText("Assigned to driver "+ model.getDriver_name());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("please wait...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", model.getName());
                hashMap.put("profile", model.getProfile());
                hashMap.put("email", model.getEmail());
                hashMap.put("uid", model.getUid());
                hashMap.put("isAssigned", false);
                db.collection("Students").document(model.getUid())
                        .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("AssignedStudents").document(model.getUid())
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "Unassigned", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_assigned_students_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email, driverName;
        private ImageView remove;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_assigned_student_name);
            driverName = itemView.findViewById(R.id.single_assigned_driver_name);
            profile = itemView.findViewById(R.id.single_assigned_student_image);
            email = itemView.findViewById(R.id.single_assigned_student_email);
            remove = itemView.findViewById(R.id.unassigned_student);

        }

    }
}
