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

public class CheckoutDisableFragment extends Fragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_checkout_disable, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
