package grover.Buhlo13;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.nfc.Tag;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormatSymbols;

/**
 * Created by ndv on 12.09.2016.
 */
public class DbToList extends Activity {
    CrutDB mCrutDB;
    private int mSum = 0;


/*

    public DbToList(Context context) {
        mCrutDB = new CrutDB(context);
    }


    public void getDBDataToListView (CrutDB mCrutDB, ListView mListView, Context context, CrutDB.Select select){
        Cursor mCursor = mCrutDB.selectData(select);

        //mCrutDB.selectByMonth();
        ArrayAdapter<String> mArrayAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cursorToString(mCursor));
        mListView.setAdapter(mArrayAdapter);
        //cursorToString(mCursor);
        mCursor.close();

    }
/*
    public void getDBDataToListView (CrutDB mCrutDB, ListView mListView, Context context, String nameForSelect){

        Cursor mCursor = mCrutDB.selectByName(nameForSelect);
        //mCrutDB.selectByMonth();
        ArrayAdapter<String> mArrayAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cursorToString(mCursor));
        mListView.setAdapter(mArrayAdapter);
        //cursorToString(mCursor);
        mCursor.close();

    }
/*
    public String getMonth(int monthNumber) {
        return new DateFormatSymbols().getShortMonths()[monthNumber-1];
    }

    public String [] cursorToString (Cursor mCursor){
        String [] stringMass = new String[mCursor.getCount()];
        String [] columnMass;
        int i=0;
        columnMass = mCursor.getColumnNames();
        for (String text : columnMass){
            Log.d("column --", text);
        }

        while (mCursor.moveToNext()){
            //
            stringMass[i]="";  //remove null;
            for(int j = 0; j<columnMass.length; j++) {
                if (mCursor.getColumnIndex(columnMass[j]) != -1) {
                    String temp  = mCursor.getString(mCursor.getColumnIndex(columnMass[j]));
                    temp = parceStringToList(columnMass[j],temp);
                    stringMass[i] = stringMass[i].concat(temp);
                }
            }
            i++;
        }
        return stringMass;
    }

    /*
    //my column: date, name, _id, price, date;
    String parceStringToList(String nameColumn, String stringToParce){
        switch (nameColumn){
            case "_id":
                return stringToParce;
            case DatabaseHelper.NAME_COLUMN:
                return Activity_settings.STRING_DELIMITER+stringToParce+Activity_settings.STRING_DELIMITER+" - ";
            case DatabaseHelper.PRICE_COLUMN:
                return stringToParce+"руб. ";    //getString return java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.res.Resources android.content.Context.getResources()'
            case DatabaseHelper.DATE_COLUMN:  //return full date(YYYY-MM-DD) in verbose mode
                return stringToParce;
            case "strftime('%m',date)":     //get month number from sqlite date format
                try {
                    return getMonth(Integer.parseInt(stringToParce));  //number to month

                } catch (NumberFormatException ne) {
                    Log.d("NumberFormatException", " parceStringToList(String nameColumn, String stringToParce)");
                }
                //return nameColumn;
        }
        return "colimn name "+nameColumn+"  notFound";
    }*/
}

