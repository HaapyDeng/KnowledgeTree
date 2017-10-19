package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
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

import static com.max_plus.knowledgetree.tools.AllToast.doToast;

public class FixNickNameActivity extends Activity implements View.OnClickListener, TextWatcher {

    private ImageView back;
    private EditText inputNickName;
    private TextView warningText;
    private Button fixBtn;
    private String nickName, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_nick_name);
        SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        nickName = sp.getString("nickName", "");
        token = sp.getString("token", "");
        if (!nickName.equals("null")) {
            inputNickName = findViewById(R.id.et_inputNickName);
            inputNickName.setText(nickName);
        }
        initView();
    }

    private void initView() {
        back = findViewById(R.id.iv_back);
        back.setOnClickListener(this);

        inputNickName = findViewById(R.id.et_inputNickName);
        nickName = inputNickName.getText().toString().trim();
        inputNickName.addTextChangedListener(this);

        warningText = findViewById(R.id.tv_warning);

        fixBtn = findViewById(R.id.btn_fix);
        fixBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_fix:

                doFixNickName();
                break;
        }
    }

    private void doFixNickName() {
        inputNickName = findViewById(R.id.et_inputNickName);
        nickName = inputNickName.getText().toString().trim();
        String url = NetworkUtils.returnUrl() + NetworkUtils.returnUploadActorApi() + "?token=" + token;
        Log.d("url==>>>", url);
        //判断网络是否正常
        if (NetworkUtils.checkNetWork(this) == false) {
            AllToast.doToast(this, getResources().getString(R.string.isNotNetWork));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("username", nickName);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                int code;
                Log.d("reponse..>>>", response.toString());
                try {
                    code = response.getInt("code");
                    if (code == 0) {
                        AllToast.doToast(FixNickNameActivity.this, getString(R.string.fix_success));
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("nikeName", nickName);//添加要返回给页面的数据
                        intent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, intent);//返回页面
                        finish();
                    } else {
                        AllToast.doToast(FixNickNameActivity.this, getString(R.string.fix_fail));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AllToast.doToast(FixNickNameActivity.this, getString(R.string.fix_fail));
                return;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputNickName = findViewById(R.id.et_inputNickName);
        nickName = inputNickName.getText().toString().trim();
        if (nickName.isEmpty()) {
            warningText.setVisibility(View.INVISIBLE);
            fixBtn.setBackgroundResource(R.drawable.shape_corner_grey);
            fixBtn.setClickable(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputNickName = findViewById(R.id.et_inputNickName);
        nickName = inputNickName.getText().toString().trim();
        if (!nickName.isEmpty()) {
            fixBtn.setBackgroundResource(R.drawable.shape_corner_blue);
            fixBtn.setClickable(true);
        }
        if (nickName.length() > 10) {
            warningText.setVisibility(View.VISIBLE);
            fixBtn.setClickable(false);
            fixBtn.setBackgroundResource(R.drawable.shape_corner_grey);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        inputNickName = findViewById(R.id.et_inputNickName);
        nickName = inputNickName.getText().toString().trim();
        if (nickName.isEmpty()) {
            warningText.setVisibility(View.INVISIBLE);
            fixBtn.setBackgroundResource(R.drawable.shape_corner_grey);
            fixBtn.setClickable(false);
        }
    }
}
