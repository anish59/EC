package com.ec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.ec.helper.FunctionHelper;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anish on 21-02-2018.
 */

public class AddComplainActivity extends AppCompatActivity {
    private android.widget.EditText txtTitle;
    private android.widget.EditText txtTemplateMsg;
    private GPSTracker gpsTracker;
    String path = "";
    private FloatingActionButton imagePicker;

    private ProgressDialog progressDialog;
    private long lastClickTime = 0;
    private ImageView img;
    private Toolbar toolbar;
    private android.support.design.widget.CollapsingToolbarLayout collapsingtoolbar;
    private android.support.design.widget.AppBarLayout appbarlayout;
    private android.support.v4.widget.NestedScrollView scroll;
    private android.widget.Button btnSubmit;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_complain_activity_revised);
        this.btnSubmit = (Button) findViewById(R.id.btnSubmit);
        this.imagePicker = (FloatingActionButton) findViewById(R.id.imagePicker);
        this.scroll = (NestedScrollView) findViewById(R.id.scroll);
        this.txtTemplateMsg = (EditText) findViewById(R.id.txtTemplateMsg);
        this.txtTitle = (EditText) findViewById(R.id.txtTitle);
        this.appbarlayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        this.collapsingtoolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.img = (ImageView) findViewById(R.id.img);

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

        UiHelper.initToolbar(AddComplainActivity.this, toolbar, "Add Complain");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // preventing double, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();

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
                if (FunctionHelper.getFileSizeinMb(path) > 2) {
                    Toast.makeText(AddComplainActivity.this, "Please select file less than 2 Mb", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.show();


                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
                Log.e("BitmapSize", "" + BitmapCompat.getAllocationByteCount(bitmap));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); //compress to which format you want.

                Log.e("CompBitmap", "" + BitmapCompat.getAllocationByteCount(bitmap));
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

                callservice(addComplainReq);
                Log.e("Counter", ">" + count++);
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

    private void callservice(AddComplainReq addComplainReq) {
        Services services = AppApplication.getRetrofit().create(Services.class);


        services.addComplain(addComplainReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body().getStatus() == 1) {
                    Toast.makeText(AddComplainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (response.body() != null && response.body().getMessage() != null) {
                        Toast.makeText(AddComplainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddComplainActivity.this, "Oops there seems to be some issue, please try again later!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddComplainActivity.this, "Error while processing", Toast.LENGTH_SHORT).show();
                Log.e("UploadError", t.toString());

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_complain_activity, menu);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            path = image.getPath();


            long fileSize = FunctionHelper.getFileSizeinMb(path);
            Log.e("FileSize", "" + fileSize + " Mb");
            if (fileSize > 2) {
                Toast.makeText(AddComplainActivity.this, "Please select file less than 2 Mb", Toast.LENGTH_SHORT).show();
            } else {
                FunctionHelper functionHelper = new FunctionHelper();
                path = functionHelper.compressImage(path);
                Glide.with(AddComplainActivity.this).load(new File(path)).into(img);
            }
        }

    }
}
