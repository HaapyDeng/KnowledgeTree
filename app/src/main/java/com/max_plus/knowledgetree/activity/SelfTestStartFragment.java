package com.max_plus.knowledgetree.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;


public class SelfTestStartFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private TextView tv_goodTest;
    private Button start_test;
    private Context mContext = this.getActivity();


    public static SelfTestStartFragment newInstance() {
        SelfTestStartFragment fragment = new SelfTestStartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_self_test, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        tv_goodTest = mRootView.findViewById(R.id.tv_6);
        tv_goodTest.setOnClickListener(this);

        start_test = mRootView.findViewById(R.id.start_test);
        start_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_6:
                Intent intent = new Intent();
                intent.setClass(getActivity(), GoodTestActivity.class);
                startActivity(intent);
                break;
            case R.id.start_test:
                Intent testIntent = new Intent();
                testIntent.setClass(getActivity(), TestSettingActivity.class);
                startActivity(testIntent);
                break;
        }
    }
}
