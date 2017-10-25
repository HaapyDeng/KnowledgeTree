package com.max_plus.knowledgetree.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.max_plus.knowledgetree.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfTestedFragment extends Fragment {


    public SelfTestedFragment() {
        // Required empty public constructor
    }

    public static SelfTestedFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SelfTestedFragment fragment = new SelfTestedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_self_tested, container, false);
    }

}
