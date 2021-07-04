package com.hcare.homeopathy.hcare.Start.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Slide;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hcare.homeopathy.hcare.R;
import com.hcare.homeopathy.hcare.Start.PhoneNumberFragment;

public class LoginHomeFragment extends Fragment {

    View root;
    public static final int RC_SIGN_IN = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_signup_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = root.findViewById(R.id.viewPagerSignUp);
        TabLayout tabLayout =  root.findViewById(R.id.tabLayoutSignUp);

        FragmentManager manager = getChildFragmentManager();
        ViewPagerTabsAdapter adapter = new ViewPagerTabsAdapter(manager,getLifecycle());
        adapter.addFrag(new SignUp3Fragment());
        adapter.addFrag(new SignUp1Fragment());
        adapter.addFrag(new SignUp2Fragment());
        viewPager.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                true, (tab, position) -> {
        });
        tabLayoutMediator.attach();

        root.findViewById(R.id.openPhoneFragment).setOnClickListener(v->{
            PhoneNumberFragment fragment = new PhoneNumberFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.slide_in, 0, 0, R.anim.slide_out)
                    .addToBackStack(null)
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        });

        root.findViewById(R.id.googleSignIn).setOnClickListener
                (v -> signInWithGoogle());

    }

    protected void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient =
                GoogleSignIn.getClient(requireContext(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        requireActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
        requireActivity().findViewById(R.id.circleLoader).setVisibility(View.VISIBLE);
    }
}
