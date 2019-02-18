package com.delarosa.arlregistration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

  /*      new CountDownTimer(30 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                finishAffinity();
                System.exit(0);

            }
        }.start();*/
    }
}
