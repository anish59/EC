package com.ec.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.AppApplication;
import com.ec.R;
import com.ec.apis.Services;
import com.ec.helper.PrefUtils;
import com.ec.model.LoginData;
import com.ec.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anish on 05-02-2018.
 */

public class ProfileFragment extends Fragment {
    private android.widget.TextView idName;
    private android.widget.TextView txtName;
    private android.widget.TextView txtEmailId;
    private android.widget.TextView txtMobile;
    private android.widget.TextView txtTotal;
    private android.widget.TextView txtSolved;
    private android.widget.TextView txtUnsolved;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile2, container, false);
        this.txtUnsolved = (TextView) view.findViewById(R.id.txtUnsolved);
        this.txtSolved = (TextView) view.findViewById(R.id.txtSolved);
        this.txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        this.txtMobile = (TextView) view.findViewById(R.id.txtMobile);
        this.txtEmailId = (TextView) view.findViewById(R.id.txtEmailId);
        this.txtName = (TextView) view.findViewById(R.id.txtName);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait");
        callApi();
        return view;
    }

    private void callApi() {
        progressDialog.show();
        AppApplication.getRetrofit().create(Services.class).getUserProfile(PrefUtils.getUser(getActivity()).getUserId()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getStatus() == 1) {
                    if (response.body().getLoginData() != null) {
                        LoginData loginData = response.body().getLoginData();
                        txtName.setText(loginData.getName().trim());
                        txtEmailId.setText(loginData.getEmailId().trim());
                        txtMobile.setText(loginData.getMobile().trim());
                        if (loginData.getTotal() != null && !loginData.getTotal().isEmpty()) {
                            txtTotal.setText(loginData.getTotal());
                        }
                        if (loginData.getSolved() != null && !loginData.getSolved().isEmpty()) {
                            txtSolved.setText(loginData.getSolved());
                        }
                        if (loginData.getTotal() != null && loginData.getSolved() != null) {
                            txtUnsolved.setText((Integer.parseInt(loginData.getTotal()) - Integer.parseInt(loginData.getSolved()) + ""));
                        }
                    }
                } else {
                    if (response.body() != null && response.body().getMessage() != null) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Oops there seems to be some issue, please try again later!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ErrorProfile:", t.toString());
            }
        });
    }

}
