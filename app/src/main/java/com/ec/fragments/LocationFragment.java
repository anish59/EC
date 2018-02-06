package com.ec.fragments;

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

import com.ec.R;
import com.ec.adapters.LocationComplainAdapter;

/**
 * Created by anish on 06-02-2018.
 */

public class LocationFragment extends Fragment {
    private android.support.v7.widget.RecyclerView rvLocationPosts;
    private LocationComplainAdapter locationComplainAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_location, container, false);
        this.rvLocationPosts = (RecyclerView) view.findViewById(R.id.rvLocationPosts);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && getActivity()!=null){
            locationComplainAdapter = new LocationComplainAdapter(getActivity());
            rvLocationPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLocationPosts.setAdapter(locationComplainAdapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationComplainAdapter = new LocationComplainAdapter(getActivity());
        rvLocationPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvLocationPosts.setAdapter(locationComplainAdapter);
    }
}
