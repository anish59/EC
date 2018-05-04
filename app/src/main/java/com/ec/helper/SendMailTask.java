package com.ec.helper;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class SendMailTask extends AsyncTask {
    private MailSentListener mailSentListener;

    public SendMailTask(MailSentListener mailSentListener) {
        this.mailSentListener = mailSentListener;
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            GMail androidEmail = new GMail(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString());
            publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            publishProgress("Sending email....");
            androidEmail.sendEmail();
            publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    mailSentListener.onSuccessful();
                }
            });
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    mailSentListener.onMailFailure();
                }
            });
        }
        return null;
    }

    public interface MailSentListener {
        void onSuccessful();

        void onMailFailure();
    }

}