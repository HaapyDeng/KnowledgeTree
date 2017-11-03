package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.AllToast;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar mBottomNavigationBar;

    private LinearLayout mFrameLayout;
    private TreeFragment mTreeFragment;
    private SelfTestStartFragment mSelfTestFragment;
    private SelfTestInfoFragment mselfTestInfoFragment;
    private MyFragment mMyFragment;
    private FindFragment mFindFragment;
    private String userName, password, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                .setInActiveColor("#FF000000")
                .initialise();
        setDefaultFragment();
//        getFragments();
    }

//    private ArrayList<Fragment> getFragments() {
//        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(TreeFragment.newInstance("", ""));
//        fragments.add(SelfTestStartFragment.newInstance());
//        fragments.add(FindFragment.newInstance("", ""));
//        fragments.add(MyFragment.newInstance());
//        return fragments;
//    }


    /**
     * set the default fragment
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mTreeFragment = TreeFragment.newInstance("1");
        transaction.replace(R.id.fragment_container, mTreeFragment).commit();
    }


    @Override
    public void onTabSelected(int position) {

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (mTreeFragment == null) {
                    mTreeFragment = TreeFragment.newInstance("1");
                }
                transaction.replace(R.id.fragment_container, mTreeFragment).commit();
                break;
            case 1:
                //判断网络是否正常
                if (NetworkUtils.checkNetWork(this) == false) {
                    AllToast.doToast(HomeActivity.this, getString(R.string.isNotNetWork));
                    return;
                }
                SharedPreferences sp = this.getSharedPreferences("user", Activity.MODE_PRIVATE);
                userName = sp.getString("username", "");
                password = sp.getString("password", "");
                token = sp.getString("token", "");
                String url = NetworkUtils.returnUrl() + NetworkUtils.returnSelfTestHistory() + "?token=" + token;
                Log.d("url ==>>", url);
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("response==>>", response.toString());
                        try {
                            Log.d("code==>>", "" + response.getInt("code"));
                            if (response.getInt("code") == 0) {
                                JSONObject dateObject = (JSONObject) response.get("data");
                                JSONArray jsonArray = dateObject.getJSONArray("list");
                                Log.d("list===>>>", jsonArray.toString());
                                if (jsonArray.length() == 0) {

                                    if (mSelfTestFragment == null) {
                                        mSelfTestFragment = SelfTestStartFragment.newInstance();
                                    }
                                    transaction.replace(R.id.fragment_container, mSelfTestFragment).commit();
                                } else {
                                    if (mselfTestInfoFragment == null) {
                                        mselfTestInfoFragment = SelfTestInfoFragment.newInstance();
                                    }
                                    transaction.replace(R.id.fragment_container, mselfTestInfoFragment).commit();

                                }
                            } else {
                                AllToast.doToast(HomeActivity.this, getString(R.string.sever_busy));
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        AllToast.doToast(HomeActivity.this, getString(R.string.sever_busy));
                        return;
                    }
                });

                break;
            case 2:
                if (mFindFragment == null) {
                    mFindFragment = FindFragment.newInstance("", "");
                }
                transaction.replace(R.id.fragment_container, mFindFragment).commit();
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mMyFragment).commit();
                break;
        }

    }


    @Override
    public void onTabUnselected(int position) {
        Log.d("onTabUnselected....", "onTabUnselected() called with: " + "position = [" + position + "]");
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause...", "start!!!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
//如果是自测完了过后再跳入Home，则i=1
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int i = bundle.getInt("start");
        Log.d("i===>>>>", "" + i);
        if (i == 1) {
            InitNavigationBar();
            mBottomNavigationBar.selectTab(1);
        } else {
            InitNavigationBar();
            mBottomNavigationBar.selectTab(0);
        }

        Log.d("onResume...", "start!!!!");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("onRestart...", "start!!!!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart...", "start!!!!");
    }
}
