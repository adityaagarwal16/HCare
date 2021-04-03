package com.hcare.homeopathy.hcare.PostConsultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PrescriptionActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseUser mCurrent_patient;
    private String mChatUser,names,medicine_id;
    private RecyclerView mDoctorList;
    private DatabaseReference mDoctorsDatabase,p;
    private Button orderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        mToolbar = (Toolbar) findViewById(R.id.doctors_appBar);

        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Treatment");
        mDoctorList = (RecyclerView) findViewById(R.id.doctor_lists);
        mDoctorList.setLayoutManager(new LinearLayoutManager(this));
        mDoctorList.setHasFixedSize(true);
        mChatUser = getIntent().getStringExtra("user_id");
       medicine_id = getIntent().getStringExtra("medicine_id");
        mCurrent_patient = FirebaseAuth.getInstance().getCurrentUser();
        names = mCurrent_patient.getUid();
        orderBtn =(Button)findViewById(R.id.ordrbtn);

        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("PrescribedMedicine").child(mChatUser).child(names);
        p = FirebaseDatabase.getInstance().getReference().child("Doctors").child(mChatUser);
        p.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String chat_user_name = dataSnapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle("Dr "+chat_user_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(  PrescriptionActivity.this, TreatmentSelectionActivity.class);
              //  startIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startIntent.putExtra("medicine_id",medicine_id);
                startIntent.putExtra("user_id" , mChatUser);
                startActivity(startIntent);
                finish();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Prescriptionjavaclass> options =
                new FirebaseRecyclerOptions.Builder<Prescriptionjavaclass>()
                        .setQuery(mDoctorsDatabase, Prescriptionjavaclass.class)
                        .setLifecycleOwner(this)
                        .build();
        final FirebaseRecyclerAdapter<Prescriptionjavaclass,DoctorsVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Prescriptionjavaclass, DoctorsVeiwHolder>(
                options
        ) {
            @NonNull
            @Override
            public DoctorsVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new DoctorsVeiwHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.prescriptionview_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorsVeiwHolder viewHolder, int position, @NonNull Prescriptionjavaclass model) {

                viewHolder.sets(model.getMedicine_name());
                viewHolder.setB(model.getMedicine_days());
                viewHolder.setC(model.getMedicine_time());
                viewHolder.setTime(model.getMedicine_Instruction());
                viewHolder.setSos(model.getInstructions());

            }
        };

        mDoctorList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class DoctorsVeiwHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsVeiwHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void sets(String medicine_A){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medA);
            doctorsNameView.setText(medicine_A);
        }
        public void setB(String medicine_B){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medB);
            doctorsNameView.setText(medicine_B);
        }
        public void setC(String medicine_C){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medC);
            doctorsNameView.setText(medicine_C);
        }

        public void setSos(String sos){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medE);
            doctorsNameView.setText(sos);
        }
        public void setTime(String time){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medD);
            doctorsNameView.setText(time);
        }

    }
}