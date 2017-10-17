package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

public class RegisterSuccessActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private Button btn_start;
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_success);
        initView();
        initDate();
    }


    private void initDate() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName = bundle.getString("userName");
        password = bundle.getString("password");
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
    }

    private void doBackgroudLogin(final String userName, final String password) {
        Log.d("userName==>>", userName);
        Log.d("password==>>", password);
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(this, getResources().getString(R.string.isNotNetWork));
            return;
        }

        final String url = NetworkUtils.returnUrl() + NetworkUtils.returnLoginApi();
        Log.d("url==>>>>>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        if (NetworkUtils.isMobileNO(userName)) {
            params.put("mobile", userName);
        } else {
            params.put("email", userName);
        }
        params.put("password", password);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d("code==>>", String.valueOf(statusCode));
                    Log.d("response==>>", response.toString());
                    int code;
                    String token, message;
                    code = response.getInt("code");
                    if (statusCode == 200) {
                        if (code == 1) {
                            message = response.getString("message");
                            doToast(RegisterSuccessActivity.this, message);
                            return;
                        } else if (code == 0) {
                            message = response.getString("message");
                            token = response.getJSONObject("data").getString("token");
                            Log.d("token==>>>>", token);
                            //保存用户信息到SharedPreferences
                            SharedPreferences mySharedPreferences = getSharedPreferences("user",
                                    Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit = mySharedPreferences.edit();
                            edit.putString("token", token);
                            edit.putString("username", userName);
                            edit.putString("password", password);
                            edit.commit();
                            Intent intent = new Intent();
                            intent.setClass(RegisterSuccessActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            doToast(RegisterSuccessActivity.this, getResources().getString(R.string.sever_busy));
                            return;
                        }

                    } else {
                        doToast(RegisterSuccessActivity.this, getResources().getString(R.string.sever_busy));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("code==>>", String.valueOf(statusCode));
                Log.d("response==>>", errorResponse.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_start:
                doBackgroudLogin(userName, password);
                break;
        }
    }
}
