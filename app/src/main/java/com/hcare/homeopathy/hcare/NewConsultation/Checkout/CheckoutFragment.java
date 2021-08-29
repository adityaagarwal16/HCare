package com.hcare.homeopathy.hcare.NewConsultation.Checkout;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.pricing;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.userConsultations;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.DISCOUNT;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.DISEASE_OBJECT;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.FIRST_100;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.FIRST_100_COUPON;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.RS50_COUPON;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.issue;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.Main.Doctors.LimitedDoctorsAdapter;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseInfo;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.PaymentsReferrals.RazorPay;
import com.hcare.homeopathy.hcare.PaymentsReferrals.WalletStatic;
import com.hcare.homeopathy.hcare.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class CheckoutFragment extends Fragment {

    View root;
    private int subTotal = 0, discount = 0;
    String patientIssue;
    int CONSULTATION_FEE = 199, totalMoneyInWallet = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_checkout,
                container, false);
        flipper();
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WalletStatic.walletMoneyUsed = 0;
        patientIssue = requireArguments().getString(issue);

        String userID = Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference rootReference = FirebaseDatabase.getInstance()
                .getReference();

        setHeaders();

        rootReference.child(pricing)
                .child(userConsultations).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            int val = Objects.requireNonNull(
                                    dataSnapshot.getValue(Integer.class));
                            if(val > 100 && val < 500)
                                CONSULTATION_FEE = val;
                        } catch(Exception ignored) {}
                        rootReference.child(userConsultations).child(userID)
                                .addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        subTotal = CONSULTATION_FEE;
                                        if ((int) dataSnapshot.getChildrenCount() < 1) {
                                            discount = FIRST_100;
                                            setFields(FIRST_100_COUPON);
                                        } else {
                                            discount = DISCOUNT;
                                            setFields( RS50_COUPON);
                                        }
                                    }

                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });

                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

        //flow ->
        //opens razor pay
        //payment successful receiver in Checkout Activity
        //Calls PaymentSuccessful class that handles all successful transactions

        root.findViewById(R.id.payNowButton).setOnClickListener(v -> {
            int total = subTotal - discount -  WalletStatic.walletMoneyUsed;
            new RazorPay(total, (AppCompatActivity) requireActivity());
        });

        setWallet(userID);
        walletCheck();

        View summaryBoxExp = root.findViewById(R.id.summaryBoxExpanded);
        TextView breakup = root.findViewById(R.id.viewBreakupText);

        root.findViewById(R.id.summaryLayout).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    breakup.setTextColor(Color.LTGRAY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    breakup.setTextColor(getResources().getColor(R.color.colorSecondary));
                    break;
                case MotionEvent.ACTION_UP:
                    breakup.setTextColor(getResources().getColor(R.color.colorSecondary));
                    if(summaryBoxExp.getVisibility() == View.GONE)
                        summaryBoxExp.setVisibility(View.VISIBLE);
                    else summaryBoxExp.setVisibility(View.GONE);
                    break;
            }
            return true;
        });

        setDoctorsRecycler();
    }

    void walletCheck() {
        CheckBox walletIsChecked = root.findViewById(R.id.walletCheckBox2);
        walletIsChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                int max = (int) Math.ceil((double) (subTotal - discount) / 2);
                WalletStatic.walletMoneyUsed = Math.min(totalMoneyInWallet, max);
            }
            else
                WalletStatic.walletMoneyUsed = 0;

            setVariableFields();
        });
    }

    void setDoctorsRecycler() {
        //Know your doctors
        try {
            ArrayList<DoctorObject> arrayList = new ArrayList<>();

            DatabaseReference mDoctorsDatabase =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Doctors");

            RecyclerView mDoctorList = root.findViewById(R.id.doctorsRecycler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            mDoctorList.setLayoutManager(linearLayoutManager);
            mDoctorList.hasFixedSize();
            String[] list = {"Y16wj8xBkqNiXCbz1Y6mnYuy1Bm2",
                    "eAOz5y53YHSzX5uRHEyTPUld4fm2",
                    "sCBWYaI75xZmIk8fXIaxFBJ4v2s2",
                    "w7sQhwsRFjN7sXBKt0Fy0p65r4o1",
                    "ZwthiKA5aDaXf6DNYVWVinzm0XP2",
                    "AQtq6nwXN6cjsvm0GqDdB49rH8u2",
                    "viewMore"};

            LimitedDoctorsAdapter adapter =
                    new LimitedDoctorsAdapter(arrayList,
                            requireContext(), list);
            for (String s : list) {
                try {
                    mDoctorsDatabase.child(s)
                            .addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                DoctorObject obj = dataSnapshot.getValue(DoctorObject.class);
                                Objects.requireNonNull(obj).setDoctorID(s);
                                arrayList.add(obj);
                                adapter.notifyDataSetChanged();
                            } catch (Exception ignored) {
                                arrayList.add(new DoctorObject());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } catch (Exception e) {arrayList.add(new DoctorObject());}
            }

            mDoctorList.setAdapter(adapter);

        } catch (Exception ignored) { }
    }

    private void setWallet(String userID) {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    totalMoneyInWallet = Objects.requireNonNull(snapshot
                            .child("Wallet").getValue(Integer.class));
                    ((TextView) root.findViewById(R.id.availableMoney))
                            .setText(MessageFormat.format("₹ {0}",  totalMoneyInWallet));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    void setHeaders() {
        DiseaseInfo disease = new DiseaseInfo((Diseases)
                requireArguments().getSerializable(DISEASE_OBJECT));
        try {
            ((TextView) root.findViewById(R.id.diseaseName))
                    .setText(disease.getDiseaseName());
            ((ImageView) root.findViewById(R.id.diseaseImage))
                    .setImageResource(disease.getDrawable());

            if(!patientIssue.equals(""))
                ((TextView) root.findViewById(R.id.patientIssue))
                        .setText(patientIssue);
        } catch(Exception e) { e.printStackTrace(); }

    }

    void setVariableFields() {
        int total = subTotal - discount -  WalletStatic.walletMoneyUsed,
                totalDiscount = discount + WalletStatic.walletMoneyUsed;
        ((TextView) root.findViewById(R.id.total))
                .setText(String.valueOf(total));
        ((TextView) root.findViewById(R.id.total1)).setText(
                MessageFormat.format("{0} {1}",
                        "₹", String.valueOf(total)));
        ((TextView) root.findViewById(R.id.savings)).setText(
                MessageFormat.format("{0} {1}",
                        "Your Savings : ₹", totalDiscount));
        ((TextView) root.findViewById(R.id.walletInBreakup))
                .setText(String.valueOf(WalletStatic.walletMoneyUsed));

    }

    void setFields(String coupon) {
        int total = subTotal - discount -  WalletStatic.walletMoneyUsed,
                totalDiscount = discount + WalletStatic.walletMoneyUsed;
        ((TextView) root.findViewById(R.id.subTotal)).setText(String.valueOf(CONSULTATION_FEE));
        ((TextView) root.findViewById(R.id.discount)).setText(String.valueOf(discount));
        ((TextView) root.findViewById(R.id.savings)).setText(
                MessageFormat.format("{0} {1}",
                        "Your Savings : ₹",
                        discount +  WalletStatic.walletMoneyUsed)
        );

        ((TextView) root.findViewById(R.id.total))
                .setText(String.valueOf(total));
        ((TextView) root.findViewById(R.id.walletInBreakup))
                .setText(String.valueOf(WalletStatic.walletMoneyUsed));

        ((TextView) root.findViewById(R.id.total1)).setText(
                MessageFormat.format("{0} {1}",
                        "₹", String.valueOf(total)));
        ((TextView) root.findViewById(R.id.savings)).setText(
                MessageFormat.format("{0} {1}",
                        "Your Savings : ₹", totalDiscount)
        );

        ((TextView) root.findViewById(R.id.couponHeader)).setText(coupon);
    }

    void flipper() {
        int[] images ={
               // R.drawable.review1,
                R.drawable.review2,
                R.drawable.review3,
                R.drawable.review4
        };
        ViewFlipper mFlipper = root.findViewById(R.id.imageFlipper);

        for (int image: images) {
            ImageView imageView = new ImageView(requireActivity());
            imageView.setBackgroundResource(image);

            mFlipper.addView(imageView);
            mFlipper.setFlipInterval(4000);
            mFlipper.setAutoStart(true);
            mFlipper.startFlipping();

            mFlipper.setOutAnimation(requireActivity(),android.R.anim.slide_out_right);
            mFlipper.setInAnimation(requireActivity(),android.R.anim.slide_in_left);
        }
    }

}
