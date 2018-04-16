package com.ec.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ec.AppApplication;
import com.ec.R;
import com.ec.adapters.LocationComplainAdapter;
import com.ec.apis.Services;
import com.ec.model.GetPostRes;
import com.ec.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anish on 06-02-2018.
 */

public class NearByFragment extends Fragment {
    private RecyclerView rvLocationPosts;
    private LocationComplainAdapter locationComplainAdapter;
    private List<Post> list;
    private ProgressDialog progressDialog;
    private android.widget.TextView txtEmptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_location, container, false);
        this.txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
        this.rvLocationPosts = (RecyclerView) view.findViewById(R.id.rvLocationPosts);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            callApi();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        locationComplainAdapter = new LocationComplainAdapter(getActivity(), list);
        rvLocationPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvLocationPosts.setAdapter(locationComplainAdapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait");

    }

    private void callApi() {
        progressDialog.show();
        Services services = AppApplication.getRetrofit().create(Services.class);
        services.getPosts(0).enqueue(new Callback<GetPostRes>() {
            @Override
            public void onResponse(Call<GetPostRes> call, Response<GetPostRes> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getData() != null) {
                    if (response.body().getData().size() > 0) {
                        list = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (distance(Double.parseDouble(response.body().getData().get(i).getLatitude()), Double.parseDouble(response.body().getData().get(i).getLongitude()), 22.3220425, 73.0329986) <= 10) {
                                list.add(response.body().getData().get(i));
                            }
                        }
                        list = response.body().getData();
                        locationComplainAdapter.setData(list);
                        setEmptyView(false);
                    } else {
                        setEmptyView(true);
                    }
                } else {
                    setEmptyView(true);
                }
            }

            @Override
            public void onFailure(Call<GetPostRes> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error:2", t.toString());
                setEmptyView(true);
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private void setEmptyView(boolean isNoData) {
        if (isNoData) {
            txtEmptyView.setVisibility(View.VISIBLE);
            rvLocationPosts.setVisibility(View.GONE);
        } else {
            txtEmptyView.setVisibility(View.GONE);
            rvLocationPosts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }
}
