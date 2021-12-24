package com.lazackna.juicefinder.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lazackna.juicefinder.databinding.FragmentPopupBinding;
import com.lazackna.juicefinder.util.juiceroot.Connection;
import com.lazackna.juicefinder.util.juiceroot.Feature;

import org.osmdroid.util.GeoPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopupFragment extends Fragment {

    GotoClicked mCallback;

    public interface GotoClicked {
        public void setRouteTo(GeoPoint geoPoint);
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentPopupBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PopupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopupFragment newInstance(String param1, String param2) {
        PopupFragment fragment = new PopupFragment();
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

        try {
            mCallback = (GotoClicked) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement GotoClicked");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = requireArguments();

        Feature f = (Feature) bundle.getSerializable("feature");

        binding = FragmentPopupBinding.inflate(inflater, container, false);

        binding.detailsChrName.setText(f.properties.name);
        binding.detailsChrPower.setText(getPowerLevelString(f.properties.poi.connections));
        binding.detailsChrPrice.setText(f.properties.connectionType);
        binding.detailsChrType.setText(f.type);

        binding.detailsPopupClose.setOnClickListener(click ->{
            getActivity().onBackPressed();
        });

        binding.detailsRouteStart.setOnClickListener(click ->{
            getActivity().onBackPressed();
            mCallback.setRouteTo(new GeoPoint(f.geometry.coordinates[1], f.geometry.coordinates[0]));
        });

        return binding.getRoot();
    }


    public String getPowerLevelString(Connection[] connections){

        if(connections.length == 0) return "0 Kw";

        double lowest = connections[0].powerKW;
        double highest = connections[0].powerKW;

        for (int i = 1; i < connections.length; i++) {
            double power = connections[i].powerKW;
            if(power > highest) highest = power;
            if(power < lowest) lowest = power;
        }

        if(lowest == highest) return lowest + " Kw";
        else return "(" + lowest + " - " + highest + ") Kw";
    }
}