package com.delarosa.arlregistration.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.delarosa.arlregistration.model.Entitys.DTOArl;

import java.util.ArrayList;

public class DataBaseArl {

    public static final String DB_NAME = "DataBaseArlRegistration.db";
    public static final int DB_VERSION = 1;

    static final String T_ARL_REGISTRATION = "tbl_arlregistration";
    static final String CREATE_TABLE_ARLREGISTRATION = "create table if not exists "
            + T_ARL_REGISTRATION
            + "("
            + "name                             TEXT,"
            + "nit                              TEXT,"
            + "company                          TEXT,"
            + "visitor                          TEXT,"
            + "emergencycall                    TEXT,"
            + "cellphone                        TEXT,"
            + "eps                              TEXT,"
            + "arl                              TEXT,"
            + "time                              TEXT,"
            + "date                              TEXT);";
    public SQLiteDatabase db;
    Context context;
    private DBHelper dbHelper;

    public DataBaseArl(Context mContext) {

        context = mContext;
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public DataBaseArl open() throws SQLException {
        try {

            db = dbHelper.getWritableDatabase();
            db.execSQL("PRAGMA automatic_index = false;");

        } catch (Exception e) {
            Log.d("Depuracion", "Error al abrir la base de datos " + e);
            dbHelper.close();
        }
        return this;
    }

    //get all the records
    public ArrayList<DTOArl> getRecords() {
        ArrayList<DTOArl> arlArrayList = new ArrayList<>();
        Cursor cursor;
        try {
            open();
            cursor = db.rawQuery("select * from " + T_ARL_REGISTRATION, null);

            if (cursor.moveToFirst()) {
                do {
                    DTOArl dtoArl = new DTOArl();
                    dtoArl.setName(cursor.getString(0));
                    dtoArl.setNit(cursor.getString(1));
                    dtoArl.setCompany(cursor.getString(2));
                    dtoArl.setVisitor(cursor.getString(3));
                    dtoArl.setEmergencyCall(cursor.getString(4));
                    dtoArl.setCellphone(cursor.getString(5));
                    dtoArl.setEps(cursor.getString(6));
                    dtoArl.setArl(cursor.getString(7));
                    dtoArl.setTime(cursor.getString(8));
                    dtoArl.setDate(cursor.getString(9));

                    arlArrayList.add(dtoArl);
                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arlArrayList;
    }

    public void deleteAllRecords() {
        try {
            open();
            db.delete(T_ARL_REGISTRATION, null, null);
        } catch (SQLException ex) {
            Log.i("Base de datos: ", " deleteAllRecords " + ex);
        } finally {
            db.close();
        }

    }

    //save the records into the db
    public void saveRecord(DTOArl dtoArl) {

        ContentValues newValues = new ContentValues();
        newValues.put("name", dtoArl.getName());
        newValues.put("nit", dtoArl.getNit());
        newValues.put("company", dtoArl.getCompany());
        newValues.put("visitor", dtoArl.getVisitor());
        newValues.put("emergencycall", dtoArl.getEmergencyCall());
        newValues.put("cellphone", dtoArl.getCellphone());
        newValues.put("eps", dtoArl.getEps());
        newValues.put("arl", dtoArl.getArl());
        newValues.put("time", dtoArl.getTime());
        newValues.put("date", dtoArl.getDate());
        try {
            open();
            db.insert(T_ARL_REGISTRATION, null, newValues);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }


}
