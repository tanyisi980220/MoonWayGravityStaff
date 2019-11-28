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
 * {@link SuspiciousFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SuspiciousFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuspiciousFragment extends Fragment {

    TextView text;


    private OnFragmentInteractionListener mListener;

    public SuspiciousFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuspiciousFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuspiciousFragment newInstance(String param1, String param2) {
        SuspiciousFragment fragment = new SuspiciousFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suspicious, container, false);
        init(view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        text.setText("Hello, Suspicous");
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
