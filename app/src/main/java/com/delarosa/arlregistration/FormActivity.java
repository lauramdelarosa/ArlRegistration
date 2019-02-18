package com.delarosa.arlregistration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.delarosa.arlregistration.model.Entitys.DTOArl;
import com.delarosa.arlregistration.model.database.DataBaseArl;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;


public class FormActivity extends AppCompatActivity {
    private final static String[] names = {"Tekus", "Sara", "Laura", "Pablo", "Luis",
            "Carlos", "Wilder", "Jesica", "Lorena", "Sebastian", "William", "Kevin", "Roger", "Victoria", "Jaime", "Diego Pino", "Leonardo"};
    EditText name, nit, company, emergencyCall, cellphone, eps, arl;
    Spinner visitor;
    DataBaseArl dataBaseArl;

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
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_name), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(nit.getText().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_nit), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(company.getText().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_company), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(visitor.getSelectedItem().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_visitor), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(emergencyCall.getText().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_emergencyCall), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(cellphone.getText().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_cellphone), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(eps.getText().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_eps), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(arl.getText().toString())) {
            Snackbar.make(findViewById(R.id.FormLayout), getResources().getString(R.string.null_arl), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
