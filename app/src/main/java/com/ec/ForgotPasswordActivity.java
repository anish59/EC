package com.ec;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ec.apis.Services;
import com.ec.helper.FunctionHelper;
import com.ec.helper.SendMailTask;
import com.ec.model.ForgotPasswordResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity {
    private android.widget.ImageView imgCloseButton;
    private android.widget.EditText edtEmail;
    private android.widget.Button btnSignUp;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        initViews();
        initListener();
    }

    private void initListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FunctionHelper.isNetworkAvailable(ForgotPasswordActivity.this)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = edtEmail.getText().toString().trim();
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                AppApplication.getRetrofit().create(Services.class).getPassword(email).enqueue(new Callback<ForgotPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 1) {
                            if (response.body().getData() != null) {
                                String userPassword = response.body().getData();
                                if (!progressDialog.isShowing()) {
                                    progressDialog.show();
                                }
                                List<String> emailidList = new ArrayList<>();
                                emailidList.add(email);
                                new SendMailTask(new SendMailTask.MailSentListener() {
                                    @Override
                                    public void onSuccessful() {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForgotPasswordActivity.this, "Password has been send to your account, please check!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onMailFailure() {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForgotPasswordActivity.this, "Oops! There seems to be some issue, Please try again later!", Toast.LENGTH_SHORT).show();
                                    }
                                }).execute("ec.complain@gmail.com",
                                        "12345@54321", emailidList, "Your account password", "your password: " + userPassword + "<br> please take care.<br>Enjoy using our app, thank you!");


                            }
                        } else {
                            if (response.body() != null && response.body().getMessage() != null) {
                                Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Oops! there seems to be an issue. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Error while processing", Toast.LENGTH_SHORT).show();
                        Log.e("Throwable", t.toString());
                    }
                });


            }
        });

        imgCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        setContentView(R.layout.activity_forgot_password);
        this.btnSignUp = (Button) findViewById(R.id.btnSignUp);
        this.edtEmail = (EditText) findViewById(R.id.edtEmail);
        this.imgCloseButton = (ImageView) findViewById(R.id.imgCloseButton);

    }


}
