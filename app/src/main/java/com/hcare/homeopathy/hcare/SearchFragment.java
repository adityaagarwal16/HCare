package com.hcare.homeopathy.hcare;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.EnumSet;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment {

    View root;
    ArrayList<Diseases> diseaseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search,
                container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        EditText search = root.findViewById(R.id.search);
        RecyclerView recyclerView = root.findViewById(R.id.list);
        diseaseList = new ArrayList<>();

        final InputMethodManager manager = (InputMethodManager)
                requireContext().getSystemService(INPUT_METHOD_SERVICE);
        search.requestFocus();
        search.setText("");
        manager.showSoftInput(search, 1);

        root.findViewById(R.id.cross).setOnClickListener(v -> {
            search.setText("");
            search.requestFocus();
            manager.showSoftInput(search, 1);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SearchAdapter adapter = new SearchAdapter(diseaseList, requireContext());
        recyclerView.setAdapter(adapter);
        ArrayList<Diseases> list = new ArrayList<>(EnumSet.allOf(Diseases.class));

        root.findViewById(R.id.back).setOnClickListener(v -> {
            search.clearFocus();
            manager.hideSoftInputFromWindow(search.getRootView().getWindowToken(), 0);
            requireActivity().onBackPressed();
        });

        try {
            search.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    if(text.isEmpty())
                        root.findViewById(R.id.cross).setVisibility(View.INVISIBLE);
                    else
                        root.findViewById(R.id.cross).setVisibility(View.VISIBLE);
                    diseaseList.clear();

                    if (text.length() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (new DiseaseInfo(list.get(i)).getDiseaseName()
                                    .contains(text))
                                diseaseList.add(list.get(i));
                        }

                        if (diseaseList.isEmpty()) {
                            diseaseList.add(Diseases.others);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (Exception ignored) {}
    }

}
