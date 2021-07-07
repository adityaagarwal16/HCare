package com.hcare.homeopathy.hcare.NewConsultation.Checkout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseInfo;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.consultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.pricing;
import static com.hcare.homeopathy.hcare.NewConsultation.Checkout.CheckoutActivity.email;
import static com.hcare.homeopathy.hcare.NewConsultation.Checkout.CheckoutActivity.patientName;
import static com.hcare.homeopathy.hcare.NewConsultation.Checkout.CheckoutActivity.phoneNumber;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.DISCOUNT;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.DISEASE_OBJECT;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.FIRST_100;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.FIRST_100_COUPON;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.RS50_COUPON;

public class CheckoutFragment extends Fragment {

    View root;
    private int totalAmount = 0;
    String patientIssue;


    int CONSULTATION_FEE = 199;

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
        patientIssue = requireArguments().getString("details1");

        String userID = Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference rootReference = FirebaseDatabase.getInstance()
                .getReference();

        rootReference.child(pricing)
                .child(consultations).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            int val = Objects.requireNonNull(
                                    dataSnapshot.getValue(Integer.class));
                            if(val > 100 && val < 500)
                                CONSULTATION_FEE = val;
                        } catch(NullPointerException ignored) {}
                        rootReference.child(consultations).child(userID)
                                .addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if ((int) dataSnapshot.getChildrenCount() < 1) {
                                            setFields(FIRST_100, FIRST_100_COUPON);
                                            totalAmount = CONSULTATION_FEE - FIRST_100;
                                        } else {
                                            setFields(DISCOUNT, RS50_COUPON);
                                            totalAmount = CONSULTATION_FEE - DISCOUNT;
                                        }
                                    }

                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });

                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

        root.findViewById(R.id.payNowButton).setOnClickListener(v -> startPayment());

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
        setHeaders();
    }

    void setHeaders() {
        try {
            DiseaseInfo disease = new DiseaseInfo((Diseases)
                    requireArguments().getSerializable(DISEASE_OBJECT));

            ((TextView) root.findViewById(R.id.header)).setText
                    (MessageFormat.format("{0} {1}", "Consultation for",
                            patientName.substring(0, 1).toUpperCase() + patientName.substring(1)));

            ((TextView) root.findViewById(R.id.diseaseName))
                    .setText(disease.getDiseaseName());
            ((ImageView) root.findViewById(R.id.diseaseImage))
                    .setImageResource(disease.getDrawable());

            if(!patientIssue.equals(""))
                ((TextView) root.findViewById(R.id.patientIssue))
                        .setText(patientIssue);
        } catch(Exception ignored) { }
    }

    void setFields(int discount, String coupon) {
        ((TextView) root.findViewById(R.id.subTotal)).setText(String.valueOf(CONSULTATION_FEE));
        ((TextView) root.findViewById(R.id.discount)).setText(String.valueOf(discount));
        ((TextView) root.findViewById(R.id.savings)).setText(
                MessageFormat.format("{0} {1}",
                        "Your Savings : ₹",
                        discount)
        );
        ((TextView) root.findViewById(R.id.total)).setText(String.valueOf(CONSULTATION_FEE - discount));
        ((TextView) root.findViewById(R.id.total1)).setText(
                MessageFormat.format("{0} {1}",
                        "₹", String.valueOf(CONSULTATION_FEE - discount)));
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

    public void startPayment() {
        final AppCompatActivity activity = (AppCompatActivity) requireContext();
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "HCare");
            options.put("description", "Discount applied");
            options.put("currency", "INR");

            int RAZORPAY_MULTIPLIER = 100;
            options.put("amount", totalAmount * RAZORPAY_MULTIPLIER);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phoneNumber);

            options.put("prefill", preFill);

            co.setImage(R.drawable.logo_green);
            co.open(activity, options);
        } catch (Exception e) { e.printStackTrace(); }
    }

}
