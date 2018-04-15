package com.ec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.apis.Services;
import com.ec.helper.AdvancedSpannableString;
import com.ec.helper.FunctionHelper;
import com.ec.helper.GPSTracker;
import com.ec.helper.PrefUtils;
import com.ec.model.LoginResponse;
import com.ec.model.RegisterReq;
import com.ec.model.UserLatLong;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private android.widget.TextView txtSinUpLabel;
    private Context context;
    private android.widget.ImageView imgCloseButton;
    private android.widget.Button btnSignUp;
    private android.widget.EditText edtName;
    private android.widget.EditText edtEmail;
    private android.widget.EditText edtMobile;
    private android.widget.EditText edtPassword;
    private android.widget.EditText edtRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initViews();
        initListeners();
        initLogic();
        GPSTracker gpsTracker = new GPSTracker(this);
        PrefUtils.setUserLatLong(context, new UserLatLong(gpsTracker.getLatitude(), gpsTracker.getLongitude()));

    }

    private void initLogic() {
        AdvancedSpannableString ass = new AdvancedSpannableString("SIGN UP");
        ass.setColor(context.getResources().getColor(R.color.colorAccent), "UP");
        txtSinUpLabel.setText(ass);
    }

    private void initListeners() {
        imgCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndRegister();

            }
        });

    }

    private void validateAndRegister() {
        if (!FunctionHelper.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = edtEmail.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Please enter Full Sname", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(context, "Please enter Mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterReq registerReq = new RegisterReq();
        registerReq.setEmailId(email);
        registerReq.setMobile(mobile);
        registerReq.setName(name);
        registerReq.setPassword(password);


        AppApplication.getRetrofit().create(Services.class).doRegister(registerReq).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body().getStatus() == 1) {
                    PrefUtils.setUser(context, response.body().getLoginData());
                    PrefUtils.setLoggedIn(context, true);
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Failure", t.getMessage());
                Toast.makeText(context, "Error while processing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        setContentView(R.layout.activity_sign_up);
        this.edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        this.edtPassword = (EditText) findViewById(R.id.edtPassword);
        this.edtMobile = (EditText) findViewById(R.id.edtMobile);
        this.edtEmail = (EditText) findViewById(R.id.edtEmail);
        this.edtName = (EditText) findViewById(R.id.edtName);
        this.btnSignUp = (Button) findViewById(R.id.btnSignUp);
        this.imgCloseButton = (ImageView) findViewById(R.id.imgCloseButton);
        this.txtSinUpLabel = (TextView) findViewById(R.id.txtSinUpLabel);
    }

}

/*Teri bahoon meh jo sukun tha mila ...
mene dhundha bohot par fir na mila....*/