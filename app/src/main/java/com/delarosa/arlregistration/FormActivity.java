package com.delarosa.arlregistration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.delarosa.arlregistration.model.Entitys.DTOArl;
import com.delarosa.arlregistration.model.database.DataBaseArl;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;


public class FormActivity extends AppCompatActivity {
    private final static String[] names = {
            "Tekus", "Sara", "Laura", "Pablo", "Luis", "Carlos", "Wilder", "Jesica", "Lorena", "Sebastian", "William", "Kevin", "Roger", "Jaime", "Diego Pino", "Leonardo"};
    EditText name, nit, company, emergencyCall, cellphone, eps, arl;
    Spinner visitor;
    DataBaseArl dataBaseArl;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        name = findViewById(R.id.name);
        nit = findViewById(R.id.cc);
        company = findViewById(R.id.company);
        visitor = findViewById(R.id.visitor);
        emergencyCall = findViewById(R.id.emergency_call);
        cellphone = findViewById(R.id.cel);
        eps = findViewById(R.id.eps);
        arl = findViewById(R.id.arl);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
        visitor.setAdapter(adapter);

        dataBaseArl = new DataBaseArl(this);

    }

    public void finishButton(View view) {
        if (saveRecord()) startActivity(new Intent(this, FinishActivity.class));

    }

    private boolean saveRecord() {
        if (validateNullFields()) {
            DTOArl dtoArl = new DTOArl();
            dtoArl.setName(name.getText().toString());
            dtoArl.setNit(nit.getText().toString());
            dtoArl.setCompany(company.getText().toString());
            dtoArl.setVisitor(visitor.getSelectedItem().toString());
            dtoArl.setEmergencyCall(emergencyCall.getText().toString());
            dtoArl.setCellphone(cellphone.getText().toString());
            dtoArl.setEps(eps.getText().toString());
            dtoArl.setArl(arl.getText().toString());

            SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

            dtoArl.setTime(time.format(new Date()));
            dtoArl.setDate(date.format(new Date()));
            dataBaseArl.saveRecord(dtoArl);
            return true;
        }
        return false;
    }

    private boolean validateNullFields() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            showMessage(getResources().getString(R.string.null_name));
            return false;
        }
        if (TextUtils.isEmpty(nit.getText().toString())) {
            showMessage(getResources().getString(R.string.null_nit));
            return false;
        }
        if (TextUtils.isEmpty(company.getText().toString())) {
            showMessage(getResources().getString(R.string.null_company));
            return false;
        }
        if (TextUtils.isEmpty(visitor.getSelectedItem().toString())) {
            showMessage(getResources().getString(R.string.null_visitor));
            return false;
        }
        if (TextUtils.isEmpty(emergencyCall.getText().toString())) {
            showMessage(getResources().getString(R.string.null_emergencyCall));
            return false;
        }
        if (TextUtils.isEmpty(cellphone.getText().toString())) {
            showMessage(getResources().getString(R.string.null_cellphone));
            return false;
        }
        if (TextUtils.isEmpty(eps.getText().toString())) {
            showMessage(getResources().getString(R.string.null_eps));
            return false;
        }
        if (TextUtils.isEmpty(arl.getText().toString())) {
            showMessage(getResources().getString(R.string.null_arl));
            return false;
        }

        return true;
    }

    /**
     * muestra el mensaje de error por ser alguno de los campos vacios
     *
     * @param string
     */
    private void showMessage(String string) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.FormLayout), string, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.google));
        snackbar.show();
    }


    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile("example", ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
        }
    }

    public void takePhoto(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
