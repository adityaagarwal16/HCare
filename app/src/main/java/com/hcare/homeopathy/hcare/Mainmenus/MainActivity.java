package com.hcare.homeopathy.hcare.Mainmenus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.PostConsultation.OrderActivity;
import com.hcare.homeopathy.hcare.PreConsultation.ChatwithdoctorsActivity;
import com.hcare.homeopathy.hcare.PreConsultation.ConsultbtnActivity;
import com.hcare.homeopathy.hcare.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity  {
    ViewFlipper mflipper;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private ImageView mpiles;
    private ImageView mmens;
    private ImageView mhair;
    private ImageView mkidneystones;
    private ImageView macne;
    private ImageView ordersbtn;
    private ImageView cdiabetes,cthyroid,cpcos,cpiles,cmens,chair,ckidneystones,cacne,cdigestive,cweight,cheadache,cRespiratory,cjoints;
    private ImageView cDepression,cGrowth,cEye,cHeart,cEnt,cMouth,cChild,cNutrition,cMeternal,cOthers;
    private TextView consultreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUserRef =
                FirebaseDatabase.getInstance().getReference().child("Users").
                        child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        DatabaseReference publicconsult = FirebaseDatabase.getInstance().
                getReference().child("public_Consulting").child(mAuth.getCurrentUser().getUid());


        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        consultreq = findViewById(R.id.reqtxt);
        consultreq.setVisibility(View.GONE);
        mUserRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String hname = dataSnapshot.child("name").getValue().toString();

              // getSupportActionBar().setIcon(R.drawable.logohorizontal);
              getSupportActionBar().setTitle("Hi, "+hname);

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
        publicconsult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("name")) {
                    consultreq.setVisibility(View.VISIBLE);

                }else {

                    consultreq.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, AccountmainmenuActivity.class);
                docprofileIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(docprofileIntent);
                finish();
            }
        });
      //flipper images
        int[] images ={R.drawable.ban1, R.drawable.ban2,R.drawable.ban3,R.drawable.ban4,R.drawable.ban5};
        mflipper = findViewById(R.id.imageView9);

        for (int image: images){
            flipperimages(image);
        }
        //flipper images

            mUserRef = FirebaseDatabase.getInstance().getReference().
                    child("Users").child(mAuth.getCurrentUser().getUid());

        FloatingActionButton consultBtn = (FloatingActionButton) findViewById(R.id.consultBtn);
            consultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent docprofileIntent = new Intent(MainActivity.this, ConsultbtnActivity.class);
                        startActivity(docprofileIntent);

                }
            });

            ordersbtn = findViewById(R.id.imageVi);
            ordersbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent docprofileIntent = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(docprofileIntent);
                    finish();
                }
            });

        ImageView mainimageview = (ImageView) findViewById(R.id.mainimage);
            mainimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent docprofileIntent = new Intent(MainActivity.this, ConsultbtnActivity.class);
                    startActivity(docprofileIntent);
                    finish();
                }
            });

            //bottombar
        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottomNavigationItemView);

            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent docprofileIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(docprofileIntent);
                  //  Toast.makeText(MainActivity.this, "Main menu", Toast.LENGTH_LONG).show();
                }
            });
            bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.Recentconsultbtn) {
                        Intent docprofileIntent = new Intent(MainActivity.this, RecentmainmenuActivity.class);
                        startActivity(docprofileIntent);
                        //  Toast.makeText(MainActivity.this, " Recent consultation", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    return false;

                }
            });

            //top catogeries
        ImageView mdiabetes = (ImageView) findViewById(R.id.imagenoa1);
        mdiabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Diabetes");
                startActivity(docprofileIntent);
            }
        });

        ImageView mthyroid = (ImageView) findViewById(R.id.imagenoa2);
        mthyroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Thyroid");
                startActivity(docprofileIntent);
            }
        });

        ImageView mpcos = (ImageView) findViewById(R.id.imagenoa3);
        mpcos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Female");
                startActivity(docprofileIntent);
            }
        });

        mpiles =(ImageView)findViewById(R.id.imagenoa4);
        mpiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Piles");
                startActivity(docprofileIntent);
            }
        });

        mmens =(ImageView)findViewById(R.id.imagenoa5);
        mmens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Mens");
                startActivity(docprofileIntent);
            }
        });

        mhair =(ImageView)findViewById(R.id.imagenoa6);
        mhair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Hair");
                startActivity(docprofileIntent);
            }
        });

        mkidneystones = findViewById(R.id.imagenoa7);
        mkidneystones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Renal stones");
                startActivity(docprofileIntent);
            }
        });

        macne = findViewById(R.id.imagenoa8);
        macne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Skin");
                startActivity(docprofileIntent);
            }
        });


        //main catogeries
        cacne =(ImageView)findViewById(R.id.imageno1);
        cacne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Skin");
                startActivity(docprofileIntent);
            }
        });

        ckidneystones =(ImageView)findViewById(R.id.imageno2);
        ckidneystones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Renal Problems");
                startActivity(docprofileIntent);
            }
        });

        cdigestive =(ImageView)findViewById(R.id.imageno3);
        cdigestive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Digestive Problems");
                startActivity(docprofileIntent);
            }
        });

        cweight =(ImageView)findViewById(R.id.imageno4);
        cweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Weight Loss & gain");
                startActivity(docprofileIntent);
            }
        });

        cheadache =(ImageView)findViewById(R.id.imageno5);
        cheadache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Headache");
                startActivity(docprofileIntent);
            }
        });

        chair =(ImageView)findViewById(R.id.imageno6);
        chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Hair ");
                startActivity(docprofileIntent);
            }
        });

        cmens =(ImageView)findViewById(R.id.imageno7);
        cmens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "mens");
                startActivity(docprofileIntent);
            }
        });

        cdiabetes =(ImageView)findViewById(R.id.imageno8);
        cdiabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Diabetes");
                startActivity(docprofileIntent);
            }
        });

        cpiles =(ImageView)findViewById(R.id.imageno9);
        cpiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Piles");
                startActivity(docprofileIntent);
            }
        });

        cthyroid =(ImageView)findViewById(R.id.imageno10);
        cthyroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Thyroid");
                startActivity(docprofileIntent);
            }
        });

        cRespiratory =(ImageView)findViewById(R.id.imageno11);
        cRespiratory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Respiratory problems");
                startActivity(docprofileIntent);
            }
        });

        cjoints =(ImageView)findViewById(R.id.imageno12);
        cjoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Bones & joints");
                startActivity(docprofileIntent);
            }
        });

        cDepression =(ImageView)findViewById(R.id.imageno13);
        cDepression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Depression");
                startActivity(docprofileIntent);
            }
        });

        cGrowth =(ImageView)findViewById(R.id.imageno14);
        cGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Growth");
                startActivity(docprofileIntent);
            }
        });

        cEye =(ImageView)findViewById(R.id.imageno15);
        cEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Eye");
                startActivity(docprofileIntent);
            }
        });

        cHeart =(ImageView)findViewById(R.id.imageno16);
        cHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Heart Problems");
                startActivity(docprofileIntent);
            }
        });

        cEnt =(ImageView)findViewById(R.id.imageno17);
        cEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "ENT");
                startActivity(docprofileIntent);
            }
        });

        cMouth =(ImageView)findViewById(R.id.imageno18);
        cMouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Mouth & Teeth");
                startActivity(docprofileIntent);
            }
        });

        cChild =(ImageView)findViewById(R.id.imageno19);
        cChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Children");
                startActivity(docprofileIntent);
            }
        });

        cNutrition =(ImageView)findViewById(R.id.imageno20);
        cNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Nutrition & health");
                startActivity(docprofileIntent);
            }
        });

        cMeternal =(ImageView)findViewById(R.id.imageno21);
        cMeternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Meternal");
                startActivity(docprofileIntent);
            }
        });

        cOthers =(ImageView)findViewById(R.id.imageno22);
        cOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, ChatwithdoctorsActivity.class);
                docprofileIntent.putExtra("request_type1", "Others");
                startActivity(docprofileIntent);
            }
        });


    }

    @Override
    protected void onResume () {
        //check for changes here
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        ///phone authentication

        mUserRef.child("status").setValue("online");




    }
    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("status").setValue("offline");

        }

    }
    public void flipperimages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        mflipper.addView(imageView);
        mflipper.setFlipInterval(4000);
        mflipper.setAutoStart(true);


        mflipper.setOutAnimation(this,android.R.anim.slide_out_right);
        mflipper.setInAnimation(this,android.R.anim.slide_in_left);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.main_settings_btn) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        }
        if(item.getItemId()==R.id.Orders_btn) {
            Intent profileIntent = new Intent(MainActivity.this, OrderActivity.class);
            startActivity(profileIntent);
        }
        if(item.getItemId()==R.id.customercare_btn) {
            Intent profileIntent = new Intent(MainActivity.this, FaqActivity.class);
            startActivity(profileIntent);
        }
        if(item.getItemId()==R.id.Terms_btn) {
            Intent profileIntent = new Intent(MainActivity.this, ConsultbtnActivity.class);
            startActivity(profileIntent);
        }

        return  true;
    }
}


