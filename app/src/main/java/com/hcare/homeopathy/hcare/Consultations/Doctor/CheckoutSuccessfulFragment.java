package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcare.homeopathy.hcare.R;

public class CheckoutSuccessfulFragment extends Fragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_checkout_successful, container, false);
        return root;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) root.findViewById(R.id.text)).setText("Continue chatting");
        ((TextView) root.findViewById(R.id.subHeader))
                .setText("Transaction successful! You can now consult your doctor again for follow-ups.");
        root.findViewById(R.id.closeActivity).setOnClickListener(v -> {
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        });
    }

}
