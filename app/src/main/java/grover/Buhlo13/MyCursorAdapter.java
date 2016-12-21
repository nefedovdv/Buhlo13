package grover.Buhlo13;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.zip.Inflater;

/**
 * Created by ndv on 11.10.2016.
 */
public class MyCursorAdapter extends CursorAdapter {
//    LinearLayout linearLayout;
    private TextView nameText;
    private TextView priceText;
    private TextView dateText;


    public MyCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name, date;
        int price, id, month;



        nameText = (TextView)view.findViewById(R.id.textViewName);
        priceText = (TextView)view.findViewById(R.id.textViewPrice);
        dateText = (TextView)view.findViewById(R.id.textViewDate);
//        cursor.moveToFirst();
//        Log.d("***********", "cursor");
        if (cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN)!=-1) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            nameText.setText(name);
        } else nameText.setText("Всего: ");

        if (cursor.getColumnIndex(DatabaseHelper.PRICE_COLUMN)!=-1) {
            price = cursor.getInt(cursor.getColumnIndex("price"));
            priceText.setText(price+"");
        }
        if (cursor.getColumnIndex(DatabaseHelper.DATE_COLUMN)!=-1) {
            date = cursor.getString(cursor.getColumnIndex("date"));
            int monthNumber = 0;
            try {
                monthNumber = Integer.parseInt(date.split("-")[1]);
            } catch (NumberFormatException Nfe){
                Log.d("NumberFormatException", " public String getMonth(String monthNumberString)");
            }
            // get date "MM dd"  from yyyy-mm-dd
            date = date.split("-")[2]+" " + getMonth(monthNumber);
            dateText.setText(date);
        }
//        if (cursor.getColumnIndex("_id"+"")!=-1) {
//            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
//            TextView idTextView = new TextView(context);
//            idTextView.setText(id+"");
//            linearLayout.addView(idTextView);
//        }
        if (cursor.getColumnIndex("strftime('%m',"+DatabaseHelper.DATE_COLUMN+")")!=-1) {
            month = cursor.getInt(cursor.getColumnIndex("strftime('%m',date)"));
            dateText.setText(getMonth(month));
//            Log.v("Cursor", month+"");
        }

        if (cursor.getColumnIndex("strftime('%Y',date)")!=-1) {
            month = cursor.getInt(cursor.getColumnIndex("strftime('%Y',date)"));
            dateText.setText(month+"");
//            Log.v("CursorYear", month+"");
        }

        //changeCursor(cursor);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        //clearTextView();
    }

//    public String getMonth(String monthNumberString) {
//        int monthNumber = 0;
//        try {
//            monthNumber = Integer.parseInt(monthNumberString);
//        } catch (NumberFormatException Nfe){
//            Log.d("NumberFormatException", " public String getMonth(String monthNumberString)");
//        }
//        if (monthNumber=0)
//        monthNumber = Math.abs(monthNumber - 1);
//        return new DateFormatSymbols().getShortMonths()[monthNumber];
//    }


    public String getMonth(int monthNumber) {
        monthNumber = Math.abs(monthNumber - 1);
        return new DateFormatSymbols().getShortMonths()[monthNumber];
    }

    //my column: date, name, _id, price, date;
//    String parceStringToList(String nameColumn, String stringToParce){
//        switch (nameColumn){
//            case "_id":
//                return stringToParce;
//            case DatabaseHelper.NAME_COLUMN:
//                return Activity_settings.STRING_DELIMITER+stringToParce+Activity_settings.STRING_DELIMITER+" - ";
//            case DatabaseHelper.PRICE_COLUMN:
//                return stringToParce+"руб. ";    //getString return java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.res.Resources android.content.Context.getResources()'
//            case DatabaseHelper.DATE_COLUMN:  //return full date(YYYY-MM-DD) in verbose mode
//                return stringToParce;
//            case "strftime('%m',date)":     //get month number from sqlite date format
//                try {
//                    return getMonth(Integer.parseInt(stringToParce));  //number to month
//
//                } catch (NumberFormatException ne) {
//                    Log.d("NumberFormatException", " parceStringToList(String nameColumn, String stringToParce)");
//                }
//                //return nameColumn;
//        }
//        return "colimn name "+nameColumn+"  notFound";
//    }

    void clearTextView (){
        nameText.setText("");
        priceText.setText("");
        dateText.setText("");
    }

}
