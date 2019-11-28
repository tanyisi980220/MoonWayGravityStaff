package com.example.moonwaygravitystaff;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarFlowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarFlowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarFlowFragment extends Fragment {
    TextView text;


    private OnFragmentInteractionListener mListener;

    public CarFlowFragment() {
        // Required empty public constructor
    }



    public static CarFlowFragment newInstance(String param1, String param2) {
        CarFlowFragment fragment = new CarFlowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_car_flow, container, false);
        init(view);
        return view;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void init(View view){
        text = (TextView)view.findViewById(R.id.text);
        text.setText("Hello,Car flow");
    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
