package com.ec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ec.helper.AdvancedSpannableString;

public class LoginActivity extends AppCompatActivity {

    private android.widget.TextView txtLoginLabel;
    private android.widget.TextView txtSinUpLabel;
    private Context context;
    private android.widget.Button btnLogin;
    private TextView btnForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initViews();
        initListeners();
        init();
    }

    private void init() {

        AdvancedSpannableString ass = new AdvancedSpannableString("LOG IN");
        ass.setColor(context.getResources().getColor(R.color.colorAccent), "IN");
        txtLoginLabel.setText(ass);

        AdvancedSpannableString ass2 = new AdvancedSpannableString(context.getString(R.string.don_t_have_an_account_sign_up));
        ass2.setColor(context.getResources().getColor(R.color.colorWhite), "Sign up");
        txtSinUpLabel.setText(ass2);
    }

    private void initListeners() {
        txtSinUpLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        btnForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void initViews() {
        setContentView(R.layout.activity_login);
        this.btnForgotPwd = (TextView) findViewById(R.id.btnForgotPwd);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.txtSinUpLabel = (TextView) findViewById(R.id.txtSinUpLabel);
        this.txtLoginLabel = (TextView) findViewById(R.id.txtLoginLabel);
    }
}
