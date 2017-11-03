package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;


public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText et_user, et_password;
    private Button btn_login;
    private TextView tv_fPassword, tv_register;
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //判断是否保存过用户名和密码
        SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        userName = sp.getString("username", "");
        password = sp.getString("password", "");
        if ((userName.length() != 0) && (password.length() != 0)) {
            getShared(userName, password);
//            btn_login = findViewById(R.id.btn_login);
//            btn_login.setBackgroundResource(R.drawable.btn_y_shape);
        }
        initView();
    }

    private void initView() {

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_fPassword = findViewById(R.id.tv_fpassword);
        tv_fPassword.setOnClickListener(this);

        tv_register = findViewById(R.id.tv_regist);
        tv_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                et_user = findViewById(R.id.et_userName);
                userName = et_user.getText().toString().trim();

                et_password = findViewById(R.id.et_password);
                password = et_password.getText().toString().trim();

                doLogin(userName, password);
                break;
            case R.id.tv_fpassword:
                Intent intent2 = new Intent();
                intent2.setClass(this, ResetPasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_regist:
                Intent intent1 = new Intent();
                intent1.setClass(this, RegisterActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void doLogin(final String userName, final String password) {
        Log.d("user==>>>", userName);
        Log.d("password==>>>", password);
        if (userName.length() == 0) {
            doToast(LoginActivity.this, getResources().getString(R.string.userName_not_null));
            return;
        }
        if (password.length() == 0) {
            doToast(LoginActivity.this, getResources().getString(R.string.password_not_null));
            return;
        }
        //判断账号是否合法
        if (NetworkUtils.isMobileNO(userName) == false && NetworkUtils.isEmail(userName) == false) {
            doToast(LoginActivity.this, getResources().getString(R.string.please_input_right_user));
            return;

        }
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(LoginActivity.this, getResources().getString(R.string.isNotNetWork));
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
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
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
                            Bundle bundle = new Bundle();
                            bundle.putInt("start",0);
                            intent.putExtras(bundle);
                            intent.setClass(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            doToast(LoginActivity.this, getResources().getString(R.string.sever_busy));
                            return;
                        }

                    } else {
                        doToast(LoginActivity.this, getResources().getString(R.string.sever_busy));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                doToast(LoginActivity.this, getResources().getString(R.string.sever_busy));
                return;
            }
        });
    }

    private void getShared(String username, String password) {
        et_user = findViewById(R.id.et_userName);
        et_user.setText(username);
        et_password = findViewById(R.id.et_password);
        et_password.setText(password);
    }
}
