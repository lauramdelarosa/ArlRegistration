package com.delarosa.arlregistration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class WelcomeActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private boolean enableButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

    }

    private void requestPermissions() {
        if (checkPermission()) {
            enableButton = true;
        } else {
            enableButton = false;
            requestPermission();

        }
    }

    public void nextButton(View view) {
        if (enableButton)
            startActivity(new Intent(this, FormActivity.class));
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //permission to save the file in the device
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    //result of the answer of the user
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableButton = true;
                } else {
                    Snackbar.make(findViewById(R.id.MainLayout), getResources().getString(R.string.permission_denied), Snackbar.LENGTH_LONG).show();
                    enableButton = false;
                }
                break;
        }
    }


    //todo cuando se deja presionado saca configuraciones , boton enviar email.

}
