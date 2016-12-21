package grover.Buhlo13;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ndv on 09.09.2016.
 */
public class CrutDB {


    //private final Context context;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private static final String TAG = "myLogs";
    enum Select {
        month_sum, month_name_sum
    }
    //Context mContext = MainActivity.this;

    public CrutDB(Context context){
        //this. = context;
        mDatabaseHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }



    public void insertDb(String name, int price, String date){
        ContentValues values = new ContentValues();
        // Задайте значения для каждого столбца
        values.put(DatabaseHelper.NAME_COLUMN, name);
        values.put(DatabaseHelper.PRICE_COLUMN, price);
        values.put(DatabaseHelper.DATE_COLUMN, date);

        // Вставляем данные в таблицу
        mSqLiteDatabase.insert(DatabaseHelper.getDatabaseTable(), null, values);

    }

    public Cursor selectVerboseCurrentMonthGroupByName(String name){
        String where = "strftime('%Y',date)='"+getDate(2)+"' and strftime('%m',date)='"+getDate(1)+"' and name='"+name+"'";

        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {"_id", DatabaseHelper.NAME_COLUMN,
                DatabaseHelper.PRICE_COLUMN, DatabaseHelper.DATE_COLUMN}, where, null, null, null, null);
    }

    public Cursor selectVerboseYearGroupByMonth(String year){

        String priceSum = "sum(price) as price";
        String groupBy = "strftime('%m',"+DatabaseHelper.DATE_COLUMN+")";
        String orderBy = groupBy + " desc";
        String where = "strftime('%Y',date)='"+year+"'";
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {"_id",
                priceSum, "strftime('%m',"+DatabaseHelper.DATE_COLUMN+")"}, where, null, groupBy, null, orderBy);
    }

    //dd-MM-yyyy
    public String getDate (int date){
        Date mDate = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String month = sf.format(mDate);
        switch (date){
            case 0:
                return month.split("-")[0];
            case 1:
                return month.split("-")[1];
            case 2:
                return month.split("-")[2];
        }
        return null;
    }


    public Cursor selectByMonth(){
        String priceSum = "sum(price) as price";
        String groupBy = DatabaseHelper.NAME_COLUMN+", strftime('%m',"+DatabaseHelper.DATE_COLUMN+")";
//        Date mDate = new Date();
//        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        String month = sf.format(mDate);
//        String year = month.split("-")[2];
//        month = month.split("-")[1];


        //Log.v(TAG, DatabaseHelper.NAME_COLUMN+" "+ priceSum+ " strftime('%m',"+DatabaseHelper.DATE_COLUMN+")"+ groupBy);
        String selectWhere = "strftime('%m',"+DatabaseHelper.DATE_COLUMN+") = '"+getDate(1)+"'";
        //String selectWhere = DatabaseHelper.DATE_COLUMN+" like '%"+getMonthFromDate()+"%'";
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(),
                new String[] {"_id", DatabaseHelper.NAME_COLUMN,
                        priceSum, "strftime('%m',"+DatabaseHelper.DATE_COLUMN+")"},
                selectWhere , null, groupBy, null, null);

    }

    public Cursor selectGroupByYear(){
        String priceSum = "sum(price) as price";
        String groupBy = "strftime('%Y',"+DatabaseHelper.DATE_COLUMN+")";
        String orderBy = groupBy + " desc";
//        Log.d("--------------", "select Year");
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {"_id",
                priceSum, "strftime('%Y',"+DatabaseHelper.DATE_COLUMN+")"}, null, null, groupBy, null, orderBy);

    }


//нужно заменить на поиск по текущему месяцу
    public Cursor selectByName(String name){
        String selectWhere = DatabaseHelper.NAME_COLUMN+" = '"+name+"'";
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[]
                        {"_id", DatabaseHelper.NAME_COLUMN, DatabaseHelper.PRICE_COLUMN, DatabaseHelper.DATE_COLUMN},
                                selectWhere, null, null, null, null);
    }

//dropDB
    public void dropDb(){
        mSqLiteDatabase.execSQL("DELETE FROM " + DatabaseHelper.getDatabaseTable());
        mSqLiteDatabase.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '"+DatabaseHelper.getDatabaseTable()+"'");
        mSqLiteDatabase.execSQL("VACUUM");

    }

    void deleteRow(int id){
        mSqLiteDatabase.execSQL("DELETE FROM " + DatabaseHelper.getDatabaseTable() + " where _id="+id);
        //update _ID & count _ID
        mSqLiteDatabase.execSQL("UPDATE "+DatabaseHelper.getDatabaseTable()+" set _ID = (_ID - 1) WHERE _ID > "+id);
        mSqLiteDatabase.execSQL("UPDATE SQLITE_SEQUENCE SET seq = seq - 1 " +
                "WHERE name = '"+DatabaseHelper.getDatabaseTable()+"'");
    }


//    void backupDb(){
//        mSqLiteDatabase.execSQL(".backup grover.bk");
//    }

    void close(){
        mSqLiteDatabase.close();
        mDatabaseHelper.close();
    }

//    public Cursor selectByMonth(){
//        String priceSum = "sum(price) as price";
//        String groupBy = DatabaseHelper.DATE_COLUMN+", "+DatabaseHelper.NAME_COLUMN;
//        Date mDate = new Date();
//        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        sf.format(mDate);
//        String[] month = mDate.toString().split(" ");
//        String selectWhere = DatabaseHelper.DATE_COLUMN+" = '"+month[1]+"'";
//        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {DatabaseHelper.NAME_COLUMN, priceSum, DatabaseHelper.DATE_COLUMN}, selectWhere , null, groupBy, null, null);
//    }

    String getMonthFromDate(){
        Date mDate = new Date();
        String month = mDate.toString();
        return month.split(" ")[1];
    }

//    public Cursor selectData (Select select) {
//        Cursor cursor = null;
//        switch (select) {
//            case month_name_sum:
//
//                cursor = selectByMonth();
//                break;
//            case month_sum:
//
//                cursor = selectGroupByDate();
//                break;
//        }
//        return cursor;
//    }
    //how to clear cursor??
    public Cursor selectFromDb(){
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {DatabaseHelper.NAME_COLUMN,
                DatabaseHelper.PRICE_COLUMN, DatabaseHelper.DATE_COLUMN}, null, null, null, null, null);


    }
    //return sum price, group by name_beer
    public Cursor selectGroupByName(){
        String priceSum = "sum(price) as price";
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {DatabaseHelper.NAME_COLUMN,
                priceSum}, null, null, DatabaseHelper.NAME_COLUMN, null, null);


    }

    public Cursor selectSumPrice(){
        String priceSum = "sum(price) as price";
        return mSqLiteDatabase.query(DatabaseHelper.getDatabaseTable(), new String[] {priceSum},
                null, null, null, null, null);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void backupDbTest (Context context){
        ContextWrapper cw = new ContextWrapper(context);
        File file = Environment.getExternalStorageDirectory();
//        File[] file = cw.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
//        for (File i : file){
        file = cw.getFilesDir();
           Log.d(TAG, file.getPath());
//        }
        //Log.d(TAG, file.getPath());
//        File directory = cw.getDir("backupTest2", Context.MODE_);
    }

/*
    void checkPermissionsRW(Activity activity){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {


                // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                    } else {

                // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }*/



    public void backupDb(View view, Activity activity) {
                File backupDir = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
//            Log.v(TAG, "test!!!!!!!!!");

                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    Log.d("Test", "sdcard mounted and writable  ******");
                    String currentDBPath = "//data/grover.Buhlo13/databases/costsDB.db";
                    String backupDBPath = "grover/costsDB.bk";
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(backupDir, backupDBPath);
//

                    try {
                        backupDB.getParentFile().mkdirs();
                        backupDB.createNewFile();
                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            Log.d(TAG, backupDB.exists() + "");
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();

                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            Snackbar.make(view, "backup complite", Snackbar.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException Io) {
                        Logger.getLogger(grover.Buhlo13.MainActivity.class.getName()).log(Level.SEVERE, null, Io);
                        Snackbar.make(view, "backup failed (IO Exception)", Snackbar.LENGTH_SHORT).show();
                    }

                } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    Log.d("Test", "sdcard mounted readonly");
                } else {
                    Log.d("Test", "sdcard state: " + state);
                }
//            if (sd.canWrite()) {
//
//            }

    }

    public void getFromBd (){
        String tables [] = {DatabaseHelper.NAME_COLUMN, DatabaseHelper.PRICE_COLUMN, DatabaseHelper.DATE_COLUMN};


    }


    public void restoreDb (View view, Activity activity){

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data/grover.Buhlo13/databases/costsDB.db";
                String backupDBPath = "grover/costsDB.bk";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Snackbar.make(view, "restore complite", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }  catch (IOException Io) {
            Logger.getLogger(grover.Buhlo13.MainActivity.class.getName()).log(Level.SEVERE, null, Io);
        }
    }

}
