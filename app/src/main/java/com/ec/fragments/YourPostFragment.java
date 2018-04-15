package com.ec.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ec.AppApplication;
import com.ec.R;
import com.ec.adapters.LocationComplainAdapter;
import com.ec.apis.Services;
import com.ec.helper.PrefUtils;
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

public class YourPostFragment extends Fragment {
    private RecyclerView rvLocationPosts;
    private LocationComplainAdapter locationComplainAdapter;
    private List<Post> list;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_location, container, false);
        this.rvLocationPosts = (RecyclerView) view.findViewById(R.id.rvLocationPosts);
        list = new ArrayList<>();
        locationComplainAdapter = new LocationComplainAdapter(getActivity(), list);
        rvLocationPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvLocationPosts.setAdapter(locationComplainAdapter);
        progressDialog = new ProgressDialog(getActivity());
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

    }

    private void callApi() {
        progressDialog.show();
        Services services = AppApplication.getRetrofit().create(Services.class);
        services.getYourPosts(0, PrefUtils.getUser(getContext()).getUserId()).enqueue(new Callback<GetPostRes>() {
            @Override
            public void onResponse(Call<GetPostRes> call, Response<GetPostRes> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getData() != null) {
                    if (response.body().getData().size() > 0) {
                        list = response.body().getData();
                        locationComplainAdapter.setData(list);
                    } else {
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GetPostRes> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
