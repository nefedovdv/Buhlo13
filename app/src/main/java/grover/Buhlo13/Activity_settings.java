package grover.Buhlo13;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ndv on 29.09.2016.
 */
public class Activity_settings extends Activity {

    static final String APP_PREFERENCES_NAME_BUTTON0X = "name";
    static final String APP_PREFERENCES_PRICE_BUTTON0X = "price";
    static final String APP_PREFERENCES = "buhlo.settings";
    //static final String STRING_DELIMITER = "\"";
    //private SharedPreferences mSettings;
    private int mCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        MainActivity.mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


    }
    public void clickSaveSettings(View view){
                setSettings(view);
                if (mCount==5) finish();
    }

    private void setSettings(View view) {
        int price =0;
        String name;

        TextView textName = (EditText) findViewById(R.id.editTextName);
        TextView textPrice = (EditText) findViewById(R.id.editTextPrice);
        try {
            price = Integer.parseInt(textPrice.getText().toString());
        } catch (NumberFormatException nf) {
            Snackbar.make(view, "Цена должна быть цифрами", Snackbar.LENGTH_SHORT).show();
        }
        name = textName.getText().toString();
        if ((!name.equals("")) && (price != 0)) {
            SharedPreferences.Editor editor = MainActivity.mSettings.edit();
            editor.putString(APP_PREFERENCES_NAME_BUTTON0X + mCount, name);
            editor.putInt(APP_PREFERENCES_PRICE_BUTTON0X + mCount, price);
            editor.apply();

            mCount++;
            textName.setText("");
            textPrice.setText("");
            textName.requestFocus();
            Snackbar.make(view, "add", Snackbar.LENGTH_SHORT).show();
        } else Snackbar.make(view, "Цена и наименование должны быть заполнены", Snackbar.LENGTH_SHORT).show();
    }


    public void buttonFinishClick(View view) {
        setSettings(view);
        finish();


    }
}