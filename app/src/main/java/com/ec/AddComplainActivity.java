package com.ec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.ec.apis.Services;
import com.ec.helper.GPSTracker;
import com.ec.helper.PrefUtils;
import com.ec.helper.UiHelper;
import com.ec.model.AddComplainReq;
import com.ec.model.BaseResponse;
import com.ec.model.UserLatLong;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anish on 21-02-2018.
 */

public class AddComplainActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private android.widget.EditText txtTitle;
    private android.widget.EditText txtTemplateMsg;
    private android.widget.Button btnLogin;
    private GPSTracker gpsTracker;
    String path = "";
    private FloatingActionButton imagePicker;
    private ImageView img;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complain);
        imagePicker = (FloatingActionButton) findViewById(R.id.imagePicker);
        img = (ImageView) findViewById(R.id.img);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.txtTemplateMsg = (EditText) findViewById(R.id.txtTemplateMsg);
        this.txtTitle = (EditText) findViewById(R.id.txtTitle);
        this.toolBar = findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(AddComplainActivity.this);
        progressDialog.setMessage("Processing..");

        init();


        gpsTracker = new GPSTracker(this);
        PrefUtils.setUserLatLong(this, new UserLatLong(gpsTracker.getLatitude(), gpsTracker.getLongitude()));

    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // for showing same    bg in notification bar
        }

        UiHelper.initToolbar(AddComplainActivity.this, toolBar, "Add Complain");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddComplainActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txtTemplateMsg.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddComplainActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (path != null && path.trim().length() == 0) {
                    Toast.makeText(AddComplainActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                    return;
                }


                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
                byte[] byte_arr = stream.toByteArray();
                String encoded = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                AddComplainReq addComplainReq = new AddComplainReq();
                addComplainReq.setTitle(txtTitle.getText().toString().trim());
                addComplainReq.setDescription(txtTemplateMsg.getText().toString().trim());
                addComplainReq.setCity(gpsTracker.getLocality(AddComplainActivity.this, PrefUtils.getUserLatLong(AddComplainActivity.this).getLat(), PrefUtils.getUserLatLong(AddComplainActivity.this).getLon()));
                addComplainReq.setUserId(PrefUtils.getUser(AddComplainActivity.this).getUserId());
                addComplainReq.setImage(new File(path).getName());
                addComplainReq.setImageSrc(encoded);
                addComplainReq.setLatitude(PrefUtils.getUserLatLong(AddComplainActivity.this).getLat() + "");
                addComplainReq.setLongitude(PrefUtils.getUserLatLong(AddComplainActivity.this).getLon() + "");

                Services services = AppApplication.getRetrofit().create(Services.class);

                progressDialog.show();
                services.addComplain(addComplainReq).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful() && response.body().getStatus() == 1) {
                            Toast.makeText(gpsTracker, "Post Successfully Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        progressDialog.dismiss();

                    }
                });
            }
        });

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(AddComplainActivity.this)
                        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                        .single() // single mode
                        .limit(1) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .enableLog(false) // disabling log
                        .start(); // start image picker activity with request code
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            path = image.getPath();

            Glide.with(AddComplainActivity.this).load(new File(path)).into(img);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
