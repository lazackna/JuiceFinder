package com.lazackna.juicefinder.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.lazackna.juicefinder.MainActivity;
import com.lazackna.juicefinder.R;

import com.lazackna.juicefinder.databinding.FragmentFilterBinding;
import com.lazackna.juicefinder.util.FilterSettings;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentFilterBinding binding;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment filter.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentFilterBinding.inflate(inflater, container, false);

        this.binding.filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxResults = Integer.parseInt(binding.filterResults.getText().toString());
                int distance = Integer.parseInt(binding.filterDistance.getText().toString());
                FilterSettings.Unit unit = FilterSettings.Unit.MILES;
                if (binding.filterUnit.isChecked()) unit = FilterSettings.Unit.KM;

                if (maxResults < 0) maxResults = 0;
                if (maxResults > 400) maxResults = 400;
                if (distance < 0) distance = 0;
                if (distance > 100) distance = 100;

                FilterSettings settings = new FilterSettings();
                settings.setDistance(distance);
                settings.setMaxResults(maxResults);
                settings.setUnit(unit);

                MainActivity.viewModel.setSettings(settings);

            }
        });

        return this.binding.getRoot();
    }
}