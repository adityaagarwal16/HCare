package com.hcare.homeopathy.hcare.Checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.hcare.homeopathy.hcare.DiseaseInfo;
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.Checkout.Constants.CONSULTATION_FEE;
import static com.hcare.homeopathy.hcare.Checkout.Constants.DISCOUNT;
import static com.hcare.homeopathy.hcare.Checkout.Constants.DISEASE_OBJECT;
import static com.hcare.homeopathy.hcare.Checkout.Constants.FIRST_100;
import static com.hcare.homeopathy.hcare.Checkout.Constants.FIRST_100_COUPON;
import static com.hcare.homeopathy.hcare.Checkout.Constants.RS50_COUPON;
import static com.hcare.homeopathy.hcare.Checkout.Constants.TEST_COUNT;

public class CheckoutFragment extends Fragment {

    View root;
    private int totalAmount = 0;
    String patientName, patientIssue;
    DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_checkout, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientName = Objects.requireNonNull(getArguments()).getString("name");
        patientIssue = getArguments().getString("details1");

        userRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(Objects.requireNonNull(
                        FirebaseAuth.getInstance().getCurrentUser()).getUid());

        userRef.child("consultCount").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((int) dataSnapshot.getChildrenCount() == TEST_COUNT) {
                    setFields(FIRST_100, FIRST_100_COUPON);
                    totalAmount = CONSULTATION_FEE - FIRST_100;
                } else {
                    setFields(DISCOUNT, RS50_COUPON);
                    totalAmount = CONSULTATION_FEE - DISCOUNT;
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        root.findViewById(R.id.payNowButton).setOnClickListener(v -> startPayment());

        flipper();
        setHeaders();
    }

    void setHeaders() {
        try {
            DiseaseInfo disease = new DiseaseInfo((Diseases)
                    Objects.requireNonNull(getArguments()).getSerializable(DISEASE_OBJECT));

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
        } catch(Exception ignored) {}
    }

    void setFields(int discount, String coupon) {
        ((TextView) root.findViewById(R.id.subTotal)).setText(String.valueOf(CONSULTATION_FEE));
        ((TextView) root.findViewById(R.id.discount)).setText(String.valueOf(discount));
        ((TextView) root.findViewById(R.id.total)).setText(String.valueOf(CONSULTATION_FEE - discount));
        ((TextView) root.findViewById(R.id.couponHeader)).setText(String.valueOf(coupon));
    }

    void flipper() {
        int[] images ={R.drawable.review1,
                R.drawable.review2,
                R.drawable.review3,
                R.drawable.review4};
        ViewFlipper mFlipper = root.findViewById(R.id.imageFlipper);

        for (int image: images) {
            ImageView imageView = new ImageView(requireContext());
            imageView.setBackgroundResource(image);

            mFlipper.addView(imageView);
            mFlipper.setFlipInterval(4000);
            mFlipper.setAutoStart(true);

            mFlipper.setOutAnimation(requireContext(), android.R.anim.slide_out_right);
            mFlipper.setInAnimation(requireContext(), android.R.anim.slide_in_left);
        }
    }

    public void startPayment() {
        final AppCompatActivity activity = (AppCompatActivity) requireActivity();
        final Checkout co = new Checkout();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    JSONObject options = new JSONObject();
                    options.put("name", "Hcare");
                    options.put("description", "discount applied");

                    //You can omit the image option to fetch the image from dashboard
                    options.put("currency", "INR");

                    int RAZORPAY_MULTIPLIER = 100;
                    options.put("amount", totalAmount * RAZORPAY_MULTIPLIER);

                    JSONObject preFill = new JSONObject();
                    preFill.put("email", (String) dataSnapshot.child("email").getValue());
                    preFill.put("contact", (String) dataSnapshot.child("phone number").getValue());

                    options.put("prefill", preFill);

                    co.open(activity, options);

                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
