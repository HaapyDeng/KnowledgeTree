package com.max_plus.knowledgetree.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.view.TakePhotoPopWin;

public class MyFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView takePhoto;
    private Context mContext;
    private WindowManager.LayoutParams params;


    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        mRootView = layoutInflater.inflate(R.layout.fragment_my,
                (ViewGroup) getActivity().findViewById(R.id.fragment_container), false);
        initView();
    }

    private void initView() {
        takePhoto = mRootView.findViewById(R.id.iv_takePhoto);
        takePhoto.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_takePhoto:
                showPopFormBottom(mRootView);
        }
    }

    private void showPopFormBottom(View view) {

        TakePhotoPopWin takePhotoPopWin = new TakePhotoPopWin(getActivity());
        //        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getActivity().getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });

    }
}
