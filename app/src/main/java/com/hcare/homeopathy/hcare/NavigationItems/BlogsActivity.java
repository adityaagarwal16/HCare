package com.hcare.homeopathy.hcare.NavigationItems;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.hcare.homeopathy.hcare.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class BlogsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseUser mCurrentUser;
    private RecyclerView mDoctorList;
    private DatabaseReference mDoctorsDatabase,userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);
     /* setTitle("Blogs");
        setDisplayHomeAsUpEnabled(true);

        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("Blogs");
        mDoctorsDatabase.keepSynced(true);

        mDoctorList = (RecyclerView) findViewById(R.id.doctor_list);
        mDoctorList.setHasFixedSize(true);
        mDoctorList.setLayoutManager(new LinearLayoutManager(this));
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
    }
    @Override
    protected void onStart() {
        super.onStart();

        userRef.child("status").setValue("online");
        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(mDoctorsDatabase, Doctors.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Doctors,DoctorsVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctors, DoctorsVeiwHolder>(
                options
        ) {
            @NonNull
            @Override
            public DoctorsVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return  new DoctorsVeiwHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctors_single_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorsVeiwHolder viewHolder, int position, @NonNull Doctors model) {


                viewHolder.Name(model.getName());
                //viewHolder.Degree(model.getEmail());
               // viewHolder.experience(model.getAge());
                viewHolder.setDoctorImage(model.getThumb_image(),getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent startIntent = new Intent(  BlogsActivity.this, DocprofileActivity.class);
                        startIntent.putExtra("user_id" ,user_id);
                        //       startIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(startIntent);
                        finish();
                    }
                });

            }
        };

        mDoctorList.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(  BlogsActivity.this, MainActivity.class);
        startIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startIntent);
        finish();
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
        public void Degree(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.doctor_degree);
            doctorsNameView.setText(name);
        }

        public void experience(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.experience);
            doctorsNameView.setText(name);
        }

        public void Name(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.doctor_single_name);
            doctorsNameView.setText(name);
        }

        public void setDoctorImage(final String thumb_image, final Context ctx){
            final ImageView userImageView =(ImageView) mView.findViewById(R.id.orderBtn);

            Picasso.with(ctx).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.doctor).into(userImageView ,new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(thumb_image).into(userImageView);
                }
            });
        }
        */
    }
}
