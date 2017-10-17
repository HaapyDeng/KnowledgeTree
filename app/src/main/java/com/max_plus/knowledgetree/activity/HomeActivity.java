package com.max_plus.knowledgetree.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.max_plus.knowledgetree.R;

public class HomeActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar mBottomNavigationBar;

    private FrameLayout mFrameLayout;
    private TreeFragment mTreeFragment;
    private SelfTestFragment mSelfTestFragment;
    private MyFragment mMyFragment;
    private FindFragment mFindFragment;

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
                .addItem(new BottomNavigationItem(R.drawable.tabbar_ic_tree_default, getResources().getString(R.string.knowledge_tree)).setActiveColor("#FF37C9CE"))
                .addItem(new BottomNavigationItem(R.drawable.tabbar_icon_test_default, getResources().getString(R.string.test_self)).setActiveColor("#FF37C9CE"))
                .addItem(new BottomNavigationItem(R.drawable.tabbar_ic_fund_efault, getResources().getString(R.string.find)).setActiveColor("#FF37C9CE"))
                .addItem(new BottomNavigationItem(R.drawable.tabbar_ic_my_default, getResources().getString(R.string.my)).setActiveColor("#FF37C9CE"))
                .setFirstSelectedPosition(0)
                .setInActiveColor("#FF000000")
                .initialise();
//        setDefaultFragment();
    }

    /**
     * set the default fragment
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mTreeFragment = TreeFragment.newInstance();
        transaction.replace(R.id.fragment_container, mTreeFragment).commit();
    }


    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (mTreeFragment == null) {
                    mTreeFragment = mTreeFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mTreeFragment);
                break;
            case 1:
                if (mSelfTestFragment == null) {
                    mSelfTestFragment = SelfTestFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mSelfTestFragment);
                break;
            case 2:
                if (mFindFragment == null) {
                    mFindFragment = FindFragment.newInstance("", "");
                }
                transaction.replace(R.id.fragment_container, mFindFragment);
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mMyFragment);
                break;
            default:
                if (mTreeFragment == null) {
                    mTreeFragment = mTreeFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mTreeFragment);
                break;
        }
        transaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
