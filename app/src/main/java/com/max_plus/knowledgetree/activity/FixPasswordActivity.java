package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.knowledgetree.R;
import com.max_plus.knowledgetree.tools.AllToast;
import com.max_plus.knowledgetree.tools.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FixPasswordActivity extends Activity implements View.OnClickListener, TextWatcher {
    private ImageView back;
    private TextView warning_text;
    private EditText et_old_psd, et_new_psd, et_re_new_psd;
    private Button fix_psd;
    private String oldPassword, newPassword, rePassword, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_password);
        back = findViewById(R.id.iv_back);
        back.setOnClickListener(this);

        warning_text = findViewById(R.id.tv_warning);
        warning_text.setOnClickListener(this);

        et_old_psd = findViewById(R.id.et_inputOld);
        et_old_psd.addTextChangedListener(this);
        oldPassword = et_old_psd.getText().toString().trim();

        et_new_psd = findViewById(R.id.et_inputNew);
        et_new_psd.addTextChangedListener(this);
        newPassword = et_new_psd.getText().toString().trim();

        et_re_new_psd = findViewById(R.id.et_reInput);
        et_re_new_psd.addTextChangedListener(this);
        rePassword = et_re_new_psd.getText().toString().trim();

        fix_psd = findViewById(R.id.btn_fixPassword);
        fix_psd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_fixPassword:
                doFixPassword();
                break;
        }
    }

    private void doFixPassword() {
        Log.d("bijiao.....", "" + newPassword.equals(rePassword));
        if (!newPassword.equals(rePassword)) {
            AllToast.doToast(this, getString(R.string.old_and_repsd_not_equal));
            return;
        }
        if (NetworkUtils.checkNetWork(this) == false) {
            AllToast.doToast(this, getString(R.string.isNotNetWork));
            return;
        }
        SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnFixPassword() + "?token=" + token;
        Log.d("url==>>>", url);
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("password", oldPassword);
        params.put("newPassword", newPassword);
        params.put("repeatPassword", rePassword);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response...>", response.toString());
                int code;
                try {
                    code = response.getInt("code");
                    switch (code) {
                        case 0:
                            AllToast.doToast(FixPasswordActivity.this, getString(R.string.fix_psd_success));
                            SharedPreferences s = getSharedPreferences("user", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit = s.edit();
                            edit.putString("password", newPassword);
                            edit.commit();
                            finish();
                            break;
                        case 1:
                            AllToast.doToast(FixPasswordActivity.this, getString(R.string.psd_not_equal));
                            break;
                        case 2:
                            AllToast.doToast(FixPasswordActivity.this, getString(R.string.old_psd_error));
                            break;
                        default:
                            AllToast.doToast(FixPasswordActivity.this, getString(R.string.sever_busy));
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                AllToast.doToast(FixPasswordActivity.this, getString(R.string.sever_busy));
                return;
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        oldPassword = et_old_psd.getText().toString().trim();
        newPassword = et_new_psd.getText().toString().trim();
        rePassword = et_re_new_psd.getText().toString().trim();
        if (oldPassword.length() >= 6 && newPassword.length() >= 6 && rePassword.length() >= 6) {
            fix_psd.setBackgroundResource(R.drawable.shape_corner_blue);
            fix_psd.setClickable(true);
            warning_text.setVisibility(View.INVISIBLE);
        } else {
            fix_psd.setBackgroundResource(R.drawable.shape_corner_grey);
            fix_psd.setClickable(false);
            warning_text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
