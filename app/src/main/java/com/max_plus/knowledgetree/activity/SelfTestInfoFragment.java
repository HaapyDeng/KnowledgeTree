package com.max_plus.knowledgetree.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.AllToast;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfTestInfoFragment extends Fragment {

    private View mRootView;
    private String token;

    public SelfTestInfoFragment() {
        // Required empty public constructor
    }

    public static SelfTestInfoFragment newInstance() {

        Bundle args = new Bundle();

        SelfTestInfoFragment fragment = new SelfTestInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_self_tested, container, false);
        initDate();
        return mRootView;
    }

    private void initDate() {
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(getActivity()) == false) {
            doToast(getActivity(), getResources().getString(R.string.isNotNetWork));
            return;
        }
        SharedPreferences sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnReportInfo() + "?token=" + token;
        Log.d("url ==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userid", token);
        params.put("batchid", HomeActivity.batchid);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                return;
            }
        });
    }

}
