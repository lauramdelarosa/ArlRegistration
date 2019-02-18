package com.delarosa.arlregistration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.delarosa.arlregistration.model.Entitys.DTOArl;
import com.delarosa.arlregistration.model.database.DataBaseArl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class FormActivity extends AppCompatActivity {
    EditText name, nit, company, visitor, emergencycall, cellphone, eps, arl;
    DataBaseArl dataBaseArl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        name = findViewById(R.id.name);
        nit = findViewById(R.id.cc);
        company = findViewById(R.id.company);
        visitor = findViewById(R.id.visitor);
        emergencycall = findViewById(R.id.emergency_call);
        cellphone = findViewById(R.id.cel);
        eps = findViewById(R.id.eps);
        arl = findViewById(R.id.arl);

        dataBaseArl = new DataBaseArl(this);

    }

    public void finishButton(View view) {
        saveRecord();
        validateDataToSend();
        startActivity(new Intent(this, FinishActivity.class));

    }

    private void validateDataToSend() {
        ArrayList<DTOArl> recordsList = dataBaseArl.getRecords();
        //parametrizable
        if (recordsList.size() > 2) {
            buildExcel(recordsList);

        }
    }

    private void buildExcel(ArrayList<DTOArl> recordsList) {

        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "ArlRecord.xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("ArlRecord", 0);

            sheet.addCell(new Label(0, 0, "Name")); // column and row
            sheet.addCell(new Label(1, 0, "Nit"));
            sheet.addCell(new Label(2, 0, "Company"));
            sheet.addCell(new Label(3, 0, "Visitor"));
            sheet.addCell(new Label(4, 0, "Emergency Call"));
            sheet.addCell(new Label(5, 0, "Cellphone"));
            sheet.addCell(new Label(6, 0, "Eps"));
            sheet.addCell(new Label(7, 0, "Arl"));
            sheet.addCell(new Label(8, 0, "Time"));
            sheet.addCell(new Label(9, 0, "Date"));

            for (int i = 0; i < recordsList.size(); i++) {

                String name = recordsList.get(i).getName();
                String nit = recordsList.get(i).getNit();
                String company = recordsList.get(i).getCompany();
                String visitor = recordsList.get(i).getVisitor();
                String emergencycall = recordsList.get(i).getEmergencyCall();
                String cellphone = recordsList.get(i).getCellphone();
                String eps = recordsList.get(i).getEps();
                String arl = recordsList.get(i).getArl();
                String time = recordsList.get(i).getTime();
                String date = recordsList.get(i).getDate();
                int index = i + 1;
                sheet.addCell(new Label(0, index, name));
                sheet.addCell(new Label(1, index, nit));
                sheet.addCell(new Label(2, index, company));
                sheet.addCell(new Label(3, index, visitor));
                sheet.addCell(new Label(4, index, emergencycall));
                sheet.addCell(new Label(5, index, cellphone));
                sheet.addCell(new Label(6, index, eps));
                sheet.addCell(new Label(7, index, arl));
                sheet.addCell(new Label(8, index, time));
                sheet.addCell(new Label(9, index, date));

            }

            workbook.write();
            workbook.close();
            sendExceltoEmail();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendExceltoEmail() {
        new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {

                try {
                    GMailSender sender = new GMailSender("clientes.arkbox@gmail.com", "tekuzer0s");
                    sender.sendMail("Envio de registro de arl",
                            "Adjunto archivo excel con los registros",
                            "clientes.arkbox@gmail.com",
                            "laura.dlr.cardona@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                return null;

            }

        }.execute();
    }


    private void saveRecord() {
        DTOArl dtoArl = new DTOArl();
        dtoArl.setName(name.getText().toString());
        dtoArl.setNit(nit.getText().toString());
        dtoArl.setCompany(company.getText().toString());
        dtoArl.setVisitor(visitor.getText().toString());
        dtoArl.setEmergencyCall(emergencycall.getText().toString());
        dtoArl.setCellphone(cellphone.getText().toString());
        dtoArl.setEps(eps.getText().toString());
        dtoArl.setArl(arl.getText().toString());

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        dtoArl.setTime(time.format(new Date()));
        dtoArl.setDate(date.format(new Date()));
        dataBaseArl.saveRecord(dtoArl);
    }


}
