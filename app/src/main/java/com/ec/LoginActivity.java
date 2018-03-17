package com.ec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ec.apis.Services;
import com.ec.helper.AdvancedSpannableString;
import com.ec.model.LoginRequest;
import com.ec.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private android.widget.TextView txtLoginLabel;
    private android.widget.TextView txtSinUpLabel;
    private Context context;
    private android.widget.Button btnLogin;
    private TextView btnForgotPwd;
    private android.widget.EditText edtEmail;
    private android.widget.EditText edtPassword;
    private Services services = AppApplication.getRetrofit().create(Services.class);
    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(context);
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
                checkValidationAndDoLogin();
            }
        });

        btnForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void checkValidationAndDoLogin() {
        String email = "";
        String pwd = "";
        if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
            return;
        }

        email = edtEmail.getText().toString().trim();
        pwd = edtPassword.getText().toString().trim();
        progressDialog.show();
        services.doLogin(new LoginRequest(email, pwd)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void initViews() {
        setContentView(R.layout.activity_login);
        this.edtPassword = (EditText) findViewById(R.id.edtPassword);
        this.edtEmail = (EditText) findViewById(R.id.edtEmail);
        this.btnForgotPwd = (TextView) findViewById(R.id.btnForgotPwd);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.txtSinUpLabel = (TextView) findViewById(R.id.txtSinUpLabel);
        this.txtLoginLabel = (TextView) findViewById(R.id.txtLoginLabel);
    }
}
