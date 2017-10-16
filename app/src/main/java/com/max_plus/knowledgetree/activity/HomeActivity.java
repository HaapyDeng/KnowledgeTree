package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.max_plus.knowledgetree.R;

import java.util.ArrayList;

public class HomeActivity extends Activity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar mBottomNavigationBar;

    FrameLayout mFrameLayout;
    private TreeFragment mTreeFragment;
    private SelfTestFragment mSelfTestFragment;
    private MyFragment mMyFragment;
    private FindFragment mFindFragment;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitNavigationBar();
    }

    private void InitNavigationBar() {
        mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        mFrameLayout = findViewById(R.id.fragment_container);
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.tabbar_ic_tree_default, getResources().getString(R.string.knowledge_tree)))
                .addItem(new BottomNavigationItem(R.drawable.tabbar_icon_test_default, getResources().getString(R.string.test_self)))
                .addItem(new BottomNavigationItem(R.drawable.tabbar_ic_fund_efault, getResources().getString(R.string.find)))
                .addItem(new BottomNavigationItem(R.drawable.tabbar_ic_my_default, getResources().getString(R.string.my)))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
