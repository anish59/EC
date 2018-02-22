package com.ec.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ec.R;
import com.ec.adapters.NotificationAdapter;

/**
 * Created by anish on 22-02-2018.
 */

public class NotificationFragment extends Fragment {
    private NotificationAdapter notificationAdapter;
    private android.support.v7.widget.RecyclerView rvNotificationItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification, container, false);
        this.rvNotificationItems = (RecyclerView) view.findViewById(R.id.rvNotificationItems);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationAdapter = new NotificationAdapter(getActivity());
        rvNotificationItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotificationItems.setAdapter(notificationAdapter);
    }
}
