package com.hcare.homeopathy.hcare.PostConsultation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class TreatmentSelectionActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private int count;
    private Button placeorder;
    private FirebaseUser mCurrentUser;
    private DatabaseReference userRef,mDoctorsDatabase;
    private RecyclerView mDoctorList;
    private TextView mrptext,Discount1,Discount2,Netprice,payamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_selection);

        setTitle("My Cart");


        mDoctorList = (RecyclerView) findViewById(R.id.doctor_lists);
        mDoctorList.setLayoutManager(new LinearLayoutManager(this));
        mDoctorList.setHasFixedSize(true);

        mrptext =(TextView)findViewById(R.id.mrptxt);
        Discount1 =(TextView)findViewById(R.id.discnttxt);
        Discount2 =(TextView)findViewById(R.id.textView72);
        Netprice =(TextView)findViewById(R.id.netmrptxt);
        payamount =(TextView)findViewById(R.id.paytxt);
        final String mChatUser = getIntent().getStringExtra("user_id");
        final String medicine_id = getIntent().getStringExtra("medicine_id");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        placeorder =(Button)findViewById(R.id.Placeorder);



        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("PrescribedMedicine").child(mChatUser).child(current_uid);
        mDoctorsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                //Log.d("test123",""+count);
                int value1 =240;
                int mrp = (value1 * count);
                mrptext.setText(""+mrp);
                int Discount =(mrp / 100)* 40;
                final int netpricecal = mrp-Discount;
                Discount1.setText(""+Discount);
                Discount2.setText("You will save "+"\u20B9"+Discount+" on this order");
                Netprice.setText(""+netpricecal);
                payamount.setText(""+netpricecal);

                placeorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent regIntent = new Intent(TreatmentSelectionActivity.this, AddressActivity.class);
                        regIntent.putExtra("user_id" , mChatUser);
                        Log.d("test123",""+ mChatUser);
                        regIntent.putExtra("medicine_id",medicine_id);
                        regIntent.putExtra("price",netpricecal);
                        startActivity(regIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      //  int value1 = 250;

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
                        .inflate(R.layout.prescriptionview2_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorsVeiwHolder viewHolder, int position, @NonNull Prescriptionjavaclass model) {

                viewHolder.sets(model.getMedicine_name());

            }
        };

        mDoctorList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.child("status").setValue("online");


        //set the value to the textview, to display on screen.
       //
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.child("status").setValue("offline");
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


    }
    private void calculate(){

    }
}



