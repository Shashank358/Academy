package com.iiysoftware.academy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.academy.Models.Date;
import com.iiysoftware.academy.Models.Drivers;
import com.iiysoftware.academy.R;
import com.squareup.picasso.Picasso;

import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class PresentsAdapter extends FirestoreRecyclerAdapter<Date, PresentsAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private PresentListAdapter adapter;

    OnItemClick onItemClick;

    public PresentsAdapter(Context mContext, FirestoreRecyclerOptions<Date> options) {
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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Date model) {

        holder.date.setText(model.getDate());

        db = FirebaseFirestore.getInstance();
        java.util.Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c);

        Query query = db.collection("Attendance").document(formattedDate)
                .collection("Data");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new PresentListAdapter(mContext, options);
        holder.recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_present_driver_att, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView date;
        private RecyclerView recyclerView;
        private LinearLayoutManager layoutManager;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.single_date_text);
            recyclerView = itemView.findViewById(R.id.present_single_driver_list);
            layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);


        }

    }
}
