package com.delarosa.arlregistration;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WelcomeActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private boolean enableButton = false;
    String[] appPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkAndPermission()) {
            enableButton();
        }

    }

    //habilita el boton siguiente, para pasar a llenar el formualrio
    public void enableButton() {
        enableButton = true;
    }

    //checkea si los permisos están autorizados y si no los muestra
    private boolean checkAndPermission() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }


    //result of the answer of the user
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            // Gather permission grant results.
            for (int i = 0; i < grantResults.length; i++) {
                // Add only permissions which are denied.
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            // Check if all permissions are granted.
            if (deniedCount == 0) {
                enableButton();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {

                        showDialogs("esta app necesita el permiso de alamcenar en el dispositivo para funcionar sin probelmas",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkAndPermission();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });


                    } else {
                        showDialogs("has denegado algunos permisos. Autorice los permisos manualmente",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                    }
                }
            }
        }
    }


    public AlertDialog showDialogs(String msg, DialogInterface.OnClickListener positiveOnClick, DialogInterface.OnClickListener negativeOnClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Permisos");
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton("Si autorizo", positiveOnClick);
        builder.setNegativeButton("No, Salir de la App", negativeOnClick);
        AlertDialog alert = builder.create();
        alert.show();

        return alert;
    }

//BOTONES LINKEADOS A LA VISTA

    public void nextButton(View view) {
        if (enableButton)
            startActivity(new Intent(this, FormActivity.class));
        else
            Snackbar.make(findViewById(R.id.MainLayout), getResources().getString(R.string.permission_denied), Snackbar.LENGTH_LONG).show();
    }


    public void configButton(View _view) {
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


}
