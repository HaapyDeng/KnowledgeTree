package com.max_plus.knowledgetree.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.max_plus.knowledgetree.R;

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
        et_user = findViewById(R.id.et_userName);
        userName = et_user.toString().trim();

        et_password = findViewById(R.id.et_password);
        password = et_password.toString().trim();

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

    private void getShared(String username, String password) {
        et_user = findViewById(R.id.et_userName);
        et_user.setText(username);
        et_password = findViewById(R.id.et_password);
        et_password.setText(password);
    }
}
