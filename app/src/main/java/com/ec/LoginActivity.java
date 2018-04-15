package com.ec;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.apis.Services;
import com.ec.helper.AdvancedSpannableString;
import com.ec.helper.FunctionHelper;
import com.ec.helper.GPSTracker;
import com.ec.helper.PrefUtils;
import com.ec.model.LoginRequest;
import com.ec.model.LoginResponse;
import com.ec.model.UserLatLong;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

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
    private Services services;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        TedPermission.with(this).setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        services = AppApplication.getRetrofit().create(Services.class);
                        initViews();
                        initListeners();
                        init();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                }).check();
    }

    private void init() {

        AdvancedSpannableString ass = new AdvancedSpannableString("LOG IN");
        ass.setColor(context.getResources().getColor(R.color.colorAccent), "IN");
        txtLoginLabel.setText(ass);

        AdvancedSpannableString ass2 = new AdvancedSpannableString(context.getString(R.string.don_t_have_an_account_sign_up));
        ass2.setColor(context.getResources().getColor(R.color.colorWhite), "Sign up");
        txtSinUpLabel.setText(ass2);

        progressDialog = new ProgressDialog(context);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is not Enabled in your device", Toast.LENGTH_SHORT).show();
        }
        GPSTracker gpsTracker = new GPSTracker(this);
        PrefUtils.setUserLatLong(context, new UserLatLong(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
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
        if (!FunctionHelper.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet not connected", Toast.LENGTH_SHORT).show();
            return;
        }

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
                if (response.isSuccessful() && response.body().getStatus() == 1) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    PrefUtils.setUser(context, response.body().getLoginData());

                    PrefUtils.setLoggedIn(context, true);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

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
