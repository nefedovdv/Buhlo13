package grover.Buhlo13;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private CrutDB mCrutDB;
    public static final String TAG = "myLogs";
    //if backup - true/ restore - false
    boolean isBackup = false;


    //private static final String APP_PREFERENCES_NAME_BUTTON0X="buttonName0";
   // private static final String APP_PREFERENCES_PRICE_BUTTON0X="buttonPrice0";

    public static SharedPreferences mSettings;

    @Override
    public void onResume() {
        super.onResume();
       // initButton();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();

        } else {
            getFragmentManager().popBackStack();
            Log.v("onBackPressed", "true");
        }
        // super.onBackPressed();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
                      // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isBackup){
                        mCrutDB.backupDb(findViewById(android.R.id.content), this);
                    } else {
                        mCrutDB.restoreDb(findViewById(android.R.id.content), this);
                    }

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "backup/restore disabled", Snackbar.LENGTH_SHORT);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }


    }

    boolean checkPermissions(Activity activity){

        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogWindow();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        mCrutDB = new CrutDB(this);
        mSettings = getSharedPreferences(Activity_settings.APP_PREFERENCES, Context.MODE_PRIVATE);

           //start activity_settings if First;
        firstRun();


       // initButton();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCrutDB.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                //to Do
                isBackup = true;
                if (checkPermissions(this)) {
                    mCrutDB.backupDb(findViewById(android.R.id.content), this);
                }
//                mCrutDB.backupDbTest(getApplicationContext());
//                Snackbar.make(findViewById(R.id.fab), Environment.getExternalStorageDirectory() + " "+ Environment.getDataDirectory(), Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.action_clearDB:
                mCrutDB.dropDb();
                return true;
            case R.id.action_clearSettings:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.clear();
                editor.apply();
                firstRun();
                return true;
            case R.id.action_restoreDb:
                isBackup = false;
                if (checkPermissions(this)) {
                    mCrutDB.close();
                    mCrutDB.restoreDb(findViewById(android.R.id.content), this);
                }
        }
        return super.onOptionsItemSelected(item);
    }


    private void dialogWindow(){

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
//        edittext.setFocusableInTouchMode(true);
//        edittext.requestFocus();
        //show keyBoard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        alert.setMessage("Доп. расходы:");
        //alert.setTitle("Enter Your Title");

        alert.setView(edittext);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Log.d(TAG, "************ key code-"+whichButton);
                try {
                    String text = edittext.getText().toString();
                    //Log.d(TAG, "----------- "+text);
                    if (!text.equals("")) {
                        int sum = Integer.parseInt(text);
                        mCrutDB.insertDb("Разное", sum, convertDate());
                    }

                }catch (NumberFormatException nfe){
                    Snackbar.make(findViewById(R.id.fab), "Слишком большая сумма", Snackbar.LENGTH_LONG).show();
                }
                //hide keyBoard
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
            }
        });

//        alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // what ever you want to do with No option.
//            }
//        });

        alert.show();
    }

    //split data (get month)
    String convertDate(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String getDate = sd.format(new Date());
         return getDate;
    }




//
    private void firstRun(){
        Boolean firstRun;
        String runKey= "firstRun";
        firstRun = mSettings.getBoolean(runKey, false);
        if (!firstRun){
            Intent intent = new Intent(MainActivity.this, Activity_settings.class);
            startActivity(intent);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(runKey, true);
            editor.apply();
        }

    }

    private void initButton(){

        LinearLayout layout = (LinearLayout) findViewById(R.id.buttonLayout);


        int count = 0;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, 1);
        //while found key
        while (count < 5) {

                final Button button = new Button(this);
                button.setLayoutParams(param);
                button.setText(mSettings.getString(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X + count, ""));
                button.setId(count);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = button.getId();
                        String name = mSettings.getString(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X+id, "");
                        int price = mSettings.getInt(Activity_settings.APP_PREFERENCES_PRICE_BUTTON0X+id, 0);
                        Snackbar.make(findViewById(R.id.buttonLayout), name + " "+ price, Snackbar.LENGTH_SHORT).show();
                    }
                });
                layout.addView(button);

                count++;

            //} else trigger = false;
        }
    }


}
