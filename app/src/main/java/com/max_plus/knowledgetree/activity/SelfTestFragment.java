package com.max_plus.knowledgetree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.AllToast;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;


public class SelfTestFragment extends Fragment {
    private String userName, password, token;
    private Boolean tested = true;
    private LayoutInflater view;


    public static SelfTestFragment newInstance() {
        SelfTestFragment fragment = new SelfTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //判断是否进行过自测
//        checkTested();
//        Log.d("tested==>>>", tested.toString());

        return inflater.inflate(R.layout.fragment_self_test, container, false);
        // Inflate the layout for this fragment

    }


    private void checkTested() {

        //判断网络是否正常
        if (NetworkUtils.checkNetWork(getActivity()) == false) {
            return;
        }
        SharedPreferences sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
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
                            tested = false;
                            Log.d("tested==>>>111111", tested.toString());
                        } else {
                            tested = true;
                            Log.d("tested==>>>22222", tested.toString());
                        }
                    } else {
                        AllToast.doToast(getActivity(), getString(R.string.sever_busy));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
