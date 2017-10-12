package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends Activity implements View.OnClickListener, TextWatcher {
    private ImageView back;
    private EditText et_user, et_code, et_setpassword;
    private Button btn_register, tv_getcode;
    private String userName, password, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {
        back = findViewById(R.id.iv_back);
        back.setOnClickListener(this);

        et_user = findViewById(R.id.et_user);
        userName = et_user.getText().toString().trim();
        et_user.addTextChangedListener(this);

        et_code = findViewById(R.id.et_code);
        code = et_code.getText().toString().trim();

        tv_getcode = findViewById(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);

        et_setpassword = findViewById(R.id.et_setpassword);
        password = et_setpassword.getText().toString().trim();

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_getcode:
                doVerifyUser(userName);
                break;

            case R.id.btn_register:
                break;
        }

    }

    private void doVerifyUser(final String userName) {
        Log.d("user==>>>", userName);
        if (userName.length() == 0) {
            Toast.makeText(this, R.string.userName_not_null, Toast.LENGTH_SHORT).show();
            return;
        }
        //判断账号是否合法
        if (NetworkUtils.isMobileNO(userName) == false && NetworkUtils.isEmail(userName) == false) {

            Toast.makeText(this, R.string.please_input_right_user, Toast.LENGTH_SHORT).show();
            return;

        }
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.isNotNetWork, Toast.LENGTH_SHORT).show();
            return;
        }
        final String url = NetworkUtils.returnUrl() + NetworkUtils.returnVerifyUser();
        Log.d("url==>>>>>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        //判断注册账号是手机还是邮箱
        if (NetworkUtils.isMobileNO(userName)) {
            params.put("mobile", userName);
        } else {
            params.put("email", userName);
        }
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("statusCode==>>>", String.valueOf(statusCode));
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    String message;
                    if (statusCode == 200) {
                        if (code == 0) {
                            doGetCode(userName);
                            return;
                        } else if (code == 2) {
                            message = response.getString("message");
                            doToast(message);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private void doToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void doGetCode(String userName) {
        final String url;
        AsyncHttpClient client2 = new AsyncHttpClient();
        final RequestParams params2 = new RequestParams();
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.isNotNetWork, Toast.LENGTH_SHORT).show();
            return;
        }
        //判断注册账号是手机还是邮箱
        if (NetworkUtils.isMobileNO(userName)) {
            url = NetworkUtils.returnUrl() + NetworkUtils.returnPhoneCodeApi();
            params2.put("mobile", userName);
        } else {
            url = NetworkUtils.returnEmailCodeApi();
            params2.put("mail", userName);
        }
        client2.post(url, params2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    //实现监听输入框内容变化
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        userName = et_user.getText().toString().trim();
        if (!userName.isEmpty()) {
            tv_getcode.setBackgroundResource(R.drawable.shape_corner_blue);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        userName = et_user.getText().toString().trim();
        if (userName.isEmpty()) {
            tv_getcode.setBackgroundResource(R.drawable.shape_corner_grey);
            tv_getcode.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        userName = et_user.getText().toString().trim();
        if (!userName.isEmpty()) {
            tv_getcode.setBackgroundResource(R.drawable.shape_corner_blue);
            tv_getcode.setClickable(true);
        }
    }
}
