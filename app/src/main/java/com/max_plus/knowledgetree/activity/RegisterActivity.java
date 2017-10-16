package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.MyWarningDailog;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

public class RegisterActivity extends Activity implements View.OnClickListener, TextWatcher {
    private ImageView back;
    private EditText et_user, et_code, et_setpassword;
    private Button btn_register, tv_getcode;
    private String userName, password, code;
    private Dialog waringDialog;

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
                et_setpassword = findViewById(R.id.et_setpassword);
                password = et_setpassword.getText().toString().trim();
                doRegister(userName, code, password);
                break;
        }

    }

    private void doRegister(final String userName, String code, final String password) {
        Log.d("user==>>>", userName);
        Log.d("password==>>>", password);
        if (userName.length() == 0) {
            doToast(RegisterActivity.this, getResources().getString(R.string.userName_not_null));
            return;
        }
        et_code = findViewById(R.id.et_code);
        code = et_code.getText().toString().trim();
        if (code.length() == 0) {
            doToast(RegisterActivity.this, getResources().getString(R.string.verify_code_not_null));
            return;
        }
        if (password.length() == 0) {
            doToast(RegisterActivity.this, getResources().getString(R.string.password_not_null));
            return;
        }
        //判断账号是否合法
        if (NetworkUtils.isMobileNO(userName) == false && NetworkUtils.isEmail(userName) == false) {
            doToast(RegisterActivity.this, getResources().getString(R.string.please_input_right_user));
            return;
        }
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(RegisterActivity.this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnRegistApi();
        Log.d("url==>>", url);
        AsyncHttpClient client3 = new AsyncHttpClient();
        RequestParams params3 = new RequestParams();
        if (NetworkUtils.isMobileNO(userName)) {
            params3.put("mobile", userName);
        } else {
            params3.put("email", userName);
        }
        params3.put("code", code);
        params3.put("password", password);
        client3.post(url, params3, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                int repCode;
                String rspMmessage;
                try {
                    repCode = response.getInt("code");
                    Log.d("repCode==>>>", String.valueOf(repCode));
                    switch (repCode) {
                        case 0://注册成功，跳转进入注册成功页面
                            rspMmessage = getResources().getString(R.string.reg_success);
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("userName", userName);
                            bundle.putString("password", password);
                            intent.putExtras(bundle);
                            intent.setClass(RegisterActivity.this, RegistSuccessActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            rspMmessage = getResources().getString(R.string.verfy_error_or_outtime);
                            doToast(RegisterActivity.this, rspMmessage);
                            break;
                        case 2:
                            rspMmessage = getResources().getString(R.string.reg_fail);
                            doToast(RegisterActivity.this, rspMmessage);
                            break;
                        case 3:
                            rspMmessage = getResources().getString(R.string.user_form_error);
                            doToast(RegisterActivity.this, rspMmessage);
                            break;
                        case 4:
                            rspMmessage = getResources().getString(R.string.user_exist);
                            doToast(RegisterActivity.this, rspMmessage);
                            break;
                        case 5:
                            rspMmessage = getResources().getString(R.string.parm_lost);
                            doToast(RegisterActivity.this, rspMmessage);
                            break;
                        default:
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("response==>>", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void doVerifyUser(final String userName) {
        Log.d("user==>>>", userName);
        if (userName.length() == 0) {
            doToast(RegisterActivity.this, getResources().getString(R.string.userName_not_null));
            return;
        }
        //判断账号是否合法
        if (NetworkUtils.isMobileNO(userName) == false && NetworkUtils.isEmail(userName) == false) {
            doToast(RegisterActivity.this, getResources().getString(R.string.please_input_right_user));
            return;

        }
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(RegisterActivity.this, getResources().getString(R.string.isNotNetWork));
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
                            doToast(RegisterActivity.this, message);
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


    private void doGetCode(String userName) {
        final String url;
        AsyncHttpClient client2 = new AsyncHttpClient();
        final RequestParams params2 = new RequestParams();
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            doToast(RegisterActivity.this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        //判断注册账号是手机还是邮箱
        if (NetworkUtils.isMobileNO(userName)) {
            url = NetworkUtils.returnUrl() + NetworkUtils.returnPhoneCodeApi();
            params2.put("mobile", userName);
        } else {
            url = NetworkUtils.returnUrl() + NetworkUtils.returnEmailCodeApi();
            params2.put("email", userName);
        }
        Log.d("url==>>>", url);
        client2.post(url, params2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                int code;
                String message;
                try {
                    code = response.getInt("code");
                    if (code == 0) {
                        waringDialog = new MyWarningDailog(RegisterActivity.this, R.layout.warning_dialog_layout, null);
                        waringDialog.show();
                        timer.start();
                    } else {
                        message = response.getString("message");
                        doToast(RegisterActivity.this, message);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    /**
     * 倒计时功能
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            if ((millisUntilFinished / 1000) <= 60) {
                tv_getcode.setBackground(getResources().getDrawable(R.drawable.shape_corner_grey));
                tv_getcode.setEnabled(false);
            }
            tv_getcode.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            tv_getcode.setBackground(getResources().getDrawable(R.drawable.shape_corner_blue));
            tv_getcode.setEnabled(true);
            tv_getcode.setText("获取验证码");
        }
    };

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
