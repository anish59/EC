package com.ec.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ec.AppApplication;
import com.ec.R;
import com.ec.adapters.NotificationAdapter;
import com.ec.apis.Services;
import com.ec.helper.PrefUtils;
import com.ec.model.NotificationData;
import com.ec.model.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anish on 22-02-2018.
 */

public class NotificationFragment extends Fragment {
    private NotificationAdapter notificationAdapter;
    private List<NotificationData> notificationDataList;
    private android.support.v7.widget.RecyclerView rvNotificationItems;
    private ProgressDialog progressDialog;
    private android.widget.TextView txtEmptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification, container, false);
        this.txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
        this.rvNotificationItems = (RecyclerView) view.findViewById(R.id.rvNotificationItems);
        notificationDataList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getActivity(), notificationDataList);
        rvNotificationItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotificationItems.setAdapter(notificationAdapter);
        Intent intent = new Intent();
        intent.setAction("now");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        callApi();
        return view;
    }

    private void callApi() {
        progressDialog.show();
        AppApplication.getRetrofit().create(Services.class)
                .getNotificationData(PrefUtils.getUser(getActivity()).getUserId(), "1").enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 1) {
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        notificationDataList = response.body().getData();
                        notificationAdapter.setNotificationData(notificationDataList);
                        setEmptyView(false);
                    } else {
                        setEmptyView(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error:", t.toString());
                setEmptyView(true);
            }
        });

    }

    private void setEmptyView(boolean isNoData) {
        if (isNoData) {
            txtEmptyView.setVisibility(View.VISIBLE);
            rvNotificationItems.setVisibility(View.GONE);
        } else {
            txtEmptyView.setVisibility(View.GONE);
            rvNotificationItems.setVisibility(View.VISIBLE);
        }
    }

}
