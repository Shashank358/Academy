package com.iiysoftware.academy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iiysoftware.academy.Models.Students;


public class StudentsFeesAdapter extends FirestoreRecyclerAdapter<Students, StudentsFeesAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public StudentsFeesAdapter(Context mContext, FirestoreRecyclerOptions<Students> options) {
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

//        Picasso picasso = Picasso.get();
//        picasso.setIndicatorsEnabled(false);
//        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.profile);
          holder.name.setText("Name: " + model.getStudent_name());
          holder.total.setText(model.getFair());
          holder.distance.setText("Total Distance: " + model.getTravel() + " Km");
          holder.pending.setText("Pending Amount: " + model.getPending_amount());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_collected_fees, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private TextView name, distance, total, pending;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.fees_student_name);
            distance = itemView.findViewById(R.id.fees_distance);
            total = itemView.findViewById(R.id.fees_total_fees);
            pending = itemView.findViewById(R.id.fees_pending_fees);
        }

    }
}
