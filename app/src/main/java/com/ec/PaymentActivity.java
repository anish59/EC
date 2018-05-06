package com.ec;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.helper.FunctionHelper;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private android.widget.TextView txtLoginLabel;
    private android.widget.TextView txtHouseTax;
    private android.widget.Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        this.btnSubmit = (Button) findViewById(R.id.btnSubmit);
        this.txtHouseTax = (TextView) findViewById(R.id.txtHouseTax);
        this.txtLoginLabel = (TextView) findViewById(R.id.txtLoginLabel);

        initListener();
    }

    private void initListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FunctionHelper.isNetworkAvailable(PaymentActivity.this)) {
                    startPayment();
                } else {
                    Toast.makeText(PaymentActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startPayment() {
          /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "House Tax");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "50000");


            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Done", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment error, please try again later", Toast.LENGTH_SHORT).show();
        finish();
    }
}
