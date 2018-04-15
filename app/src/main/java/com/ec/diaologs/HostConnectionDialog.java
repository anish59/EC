package com.ec.diaologs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ec.AppApplication;
import com.ec.R;
import com.ec.apis.Services;
import com.ec.model.LoginRequest;
import com.ec.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anish on 4/14/2018.
 */

public class HostConnectionDialog extends Dialog {
    Context context;
    Services service = AppApplication.getRetrofit().create(Services.class);
    private android.widget.EditText edtUrl;
    private android.widget.Button btnConnect;
    private OnConnectListener onConnectListener;


    public HostConnectionDialog(@NonNull Context context, OnConnectListener onConnectListener) {
        super(context);
        this.context = context;
        this.onConnectListener = onConnectListener;
        init();
        initListeners();
    }

    private void initListeners() {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callService();
            }
        });
    }

    private void callService() {

        service.doLogin(new LoginRequest("", "")).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (!TextUtils.isEmpty(edtUrl.getText().toString().trim())) {
                        onConnectListener.onConnect(edtUrl.getText().toString().trim());
                        dismiss();
                    } else {
                        Toast.makeText(context, "Enter URL", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                onFailure();
                edtUrl.setText(t.getMessage());
            }
        });
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.diaolog_host_connection, null, false);
        this.btnConnect = (Button) view.findViewById(R.id.btnConnect);
        this.edtUrl = (EditText) view.findViewById(R.id.edtUrl);
        setContentView(view);
    }

    private void setDialogProps(View view) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
//        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        this.setCanceledOnTouchOutside(false);
        show();
    }

    public interface OnConnectListener {
        void onConnect(String url);

    }


}
