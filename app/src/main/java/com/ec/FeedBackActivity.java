package com.ec;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.helper.FunctionHelper;

public class FeedBackActivity extends AppCompatActivity {
    private Context context;
    private android.widget.TextView txtLoginLabel;
    private android.widget.EditText edtTitle;
    private android.widget.EditText edtContent;
    private android.widget.Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        listeners();
    }

    private void listeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtTitle.getText().toString().trim())) {
                    Toast.makeText(context, "Please enter title.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtContent.getText().toString().trim())) {
                    Toast.makeText(context, "Please fill up the Description.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (FunctionHelper.isNetworkAvailable(context)) {
                    sendFeedBackMail();
//                    sendEmail();
                } else {
                    Toast.makeText(context, "Please check your internet!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void sendFeedBackMail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");

        i.setData(Uri.parse("mailto:ec.complain@gmail.com"));
//        i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"ec.complain@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, edtTitle.getText().toString().trim());
        i.putExtra(Intent.EXTRA_TEXT, edtContent.getText().toString().trim());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        setContentView(R.layout.activity_feed_back);
        this.btnSubmit = (Button) findViewById(R.id.btnSubmit);
        this.edtContent = (EditText) findViewById(R.id.edtContent);
        this.edtTitle = (EditText) findViewById(R.id.edtTitle);
        this.txtLoginLabel = (TextView) findViewById(R.id.txtLoginLabel);
    }

    public void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"care@xyz.com"});

        intent.putExtra(Intent.EXTRA_SUBJECT, "care Feedback");

        intent.putExtra(Intent.EXTRA_TEXT, "");

        startActivity(Intent.createChooser(intent, "Send Email"));

    }
}
