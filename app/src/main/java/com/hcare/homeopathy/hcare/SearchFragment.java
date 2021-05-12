package com.hcare.homeopathy.hcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcare.homeopathy.hcare.Disease.DiseaseActivity;

import java.util.ArrayList;
import java.util.EnumSet;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment {

    View root;

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
        ListView listView = root.findViewById(R.id.list);

        final InputMethodManager manager = (InputMethodManager)
                requireContext().getSystemService(INPUT_METHOD_SERVICE);
        search.requestFocus();
        search.setText("");
        manager.showSoftInput(search, 1);

        root.findViewById(R.id.back).setOnClickListener(v -> {
            search.clearFocus();
            manager.hideSoftInputFromWindow(search.getRootView().getWindowToken(), 0);
            requireActivity().onBackPressed();
        });

        ArrayList<Diseases> list = new ArrayList<>(EnumSet.allOf(Diseases.class));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                boolean empty = false;
                String text = s.toString();
                ArrayList<String> searchList = new ArrayList<>();
                ArrayList<Diseases> diseaseList = new ArrayList<>();

                if(text.length() > 0) {
                    for(int i =0; i < list.size(); i++) {
                        DiseaseInfo disease = new DiseaseInfo(list.get(i));
                        if(disease.getDiseaseName().contains(text)) {
                            diseaseList.add(list.get(i));
                            searchList.add(disease.getDiseaseName());
                        }
                    }

                    if(searchList.isEmpty()) {
                        empty = true;
                        searchList.add("Couldn't find the disease you're looking for, tap here.");
                    }

                    boolean finalEmpty = empty;
                    listView.setOnItemClickListener((parent, view1, position, id) -> {
                        Intent intent = new Intent(requireContext(), DiseaseActivity.class);
                        if(finalEmpty)
                            intent.putExtra("request_type1", Diseases.others);
                        else
                            intent.putExtra("request_type1", diseaseList.get(position));
                        requireActivity().startActivity(intent);
                    });
                }

                listView.setAdapter(new ArrayAdapter<>(requireContext(),
                        R.layout.view_search_text, searchList));
            }
        });


    }


}
