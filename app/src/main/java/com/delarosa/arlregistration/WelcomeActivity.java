package com.delarosa.arlregistration;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WelcomeActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 2;

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
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        return (result == PackageManager.PERMISSION_GRANTED && resultCamera == PackageManager.PERMISSION_GRANTED);

    }

    //permission to save the file in the device
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
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
            case PERMISSION_REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableButton = true;
                } else {
                    Snackbar.make(findViewById(R.id.MainLayout), getResources().getString(R.string.permission_denied), Snackbar.LENGTH_LONG).show();
                    enableButton = false;
                }
                break;
        }
    }

    public void config(View _view) {
        final TextView passwordTextView;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_password, null);

        passwordTextView = view.findViewById(R.id.dialog_edittext_password);
        builder.setView(view)
                .setTitle("Ingresa a Configuraciones")
                .setCancelable(false)
                .setPositiveButton("Desbloquear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        String password = passwordTextView.getText().toString().trim();

                        if (password.equals("987654")) {
                            startActivity(new Intent(WelcomeActivity.this, SettingsActivity.class));
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }


    //todo cuando se deja presionado saca configuraciones , boton enviar email.

}
