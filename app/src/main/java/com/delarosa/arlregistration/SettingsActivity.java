package com.delarosa.arlregistration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.delarosa.arlregistration.model.Entitys.DTOArl;
import com.delarosa.arlregistration.model.GMailSender;
import com.delarosa.arlregistration.model.database.DataBaseArl;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SettingsActivity extends AppCompatActivity {
    DataBaseArl dataBaseArl;
    Button EnviarExcelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EnviarExcelButton = findViewById(R.id.EnviarExcelButton);
        dataBaseArl = new DataBaseArl(this);
        EnviarExcelButton.setEnabled(true);
    }

    public void enviarExcel(View view) {
        ArrayList<DTOArl> recordsList = dataBaseArl.getRecords();
        buildExcel(recordsList);
        EnviarExcelButton.setEnabled(false);
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
                String emergencyCall = recordsList.get(i).getEmergencyCall();
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
                sheet.addCell(new Label(4, index, emergencyCall));
                sheet.addCell(new Label(5, index, cellphone));
                sheet.addCell(new Label(6, index, eps));
                sheet.addCell(new Label(7, index, arl));
                sheet.addCell(new Label(8, index, time));
                sheet.addCell(new Label(9, index, date));

            }

            workbook.write();
            workbook.close();
            sendExcelToEmail();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendExcelToEmail() {
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
}
