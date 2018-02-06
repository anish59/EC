package com.ec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ec.helper.AdvancedSpannableString;

public class SignUpActivity extends AppCompatActivity {

    private android.widget.TextView txtSinUpLabel;
    private Context context;
    private android.widget.ImageView imgCloseButton;
    private android.widget.Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_sign_up);
        this.btnSignUp = (Button) findViewById(R.id.btnSignUp);
        this.imgCloseButton = (ImageView) findViewById(R.id.imgCloseButton);
        this.txtSinUpLabel = (TextView) findViewById(R.id.txtSinUpLabel);

        AdvancedSpannableString ass = new AdvancedSpannableString("SIGN UP");
        ass.setColor(context.getResources().getColor(R.color.colorAccent), "UP");
        txtSinUpLabel.setText(ass);

        imgCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

    }

}
