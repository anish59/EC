package com.ec.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ec.R;
import com.ec.model.NotificationData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anish on 22-02-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<NotificationData> notificationDataList;

    public NotificationAdapter(Context context, List<NotificationData> notificationDataList) {
        this.context = context;
        this.notificationDataList = notificationDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtTitle.setText(notificationDataList.get(position).getTitle());
    }

    public void setNotificationData(List<NotificationData> notificationDataList) {
        this.notificationDataList = new ArrayList<>();
        this.notificationDataList = notificationDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (notificationDataList != null && notificationDataList.size() > 0 ? notificationDataList.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtLabelTitle, txtTitle, txtLabelStatus, txtStatus;


        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        }
    }
}
