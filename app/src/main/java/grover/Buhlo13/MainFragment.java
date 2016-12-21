package grover.Buhlo13;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ndv on 29.11.2016.
 */
public class MainFragment extends Fragment {

    //        Boolean onResumeKey = false;
    LinearLayout ll;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */


    private static final String ARG_SECTION_NUMBER = "section_number";

    public MainFragment(){

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
//            Log.v(TAG, fragment.getArguments().toString());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ll = (LinearLayout)rootView.findViewById(R.id.buttonLayout);
        //ll.addView(new Button(getActivity()));
        //initButton(ll);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
//            Log.d(TAG, "onResume-------------------------");
//            if (!onResumeKey){
        initButton(ll);
//                onResumeKey = true;
//            }


    }


    //split data (get month)
    String convertDate(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String getDate = sd.format(new Date());
            /*
Log.d(TAG,"date -- "+mDate);
String splitDate = mDate.toString();
splitDate = splitDate.split(" ")[0]+" "+splitDate.split(" ")[1]+" "
+splitDate.split(" ")[2]+" "+splitDate.split(" ")[5];
//Log.v(TAG, "**********" + splitDate);
*/
        return getDate;
    }


    void initButton(final LinearLayout layout){
        final CrutDB mCrutDB;
        Boolean trigger = true;
        int count = 0;

        layout.removeAllViews();

        mCrutDB= new CrutDB(getActivity());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, 1);
        //while found key
        while (trigger) {
            //Log.d(TAG, "****************>>>>" + mSettings.contains(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X + count));
            if (MainActivity.mSettings.contains(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X + count)) {
                //Log.d(TAG,"************************************ button " + Activity_settings.APP_PREFERENCES_NAME_BUTTON0X + count);
                final Button button = new Button(getActivity());
                button.setLayoutParams(param);
                button.setText(MainActivity.mSettings.getString(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X + count, ""));
                button.setId(count);
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = button.getId();
                        String name = MainActivity.mSettings.getString(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X+ id, "");
                        dialogWindowEditButton(getActivity(), name, id);
                        Snackbar.make(v, "longClick", Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = button.getId();
                        String name = MainActivity.mSettings.getString(Activity_settings.APP_PREFERENCES_NAME_BUTTON0X+id, "");
                        int price = MainActivity.mSettings.getInt(Activity_settings.APP_PREFERENCES_PRICE_BUTTON0X+id, 0);
                        mCrutDB.insertDb(name, price, convertDate());
                        //Snackbar.make(layout,getString(R.string.addToast)+80, Snackbar.LENGTH_SHORT).show();
                        Snackbar.make(layout, name + " "+ price, Snackbar.LENGTH_SHORT).show();
                    }
                });
                layout.addView(button);

                count++;

            } else trigger = false;
        }
    }

    private void dialogWindowEditButton (Activity activity, String editTextHint, final int idButton){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText edittext = new EditText(activity);
        edittext.setHint(editTextHint);
        alert.setMessage("Введите новое название");
        alert.setView(edittext);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = MainActivity.mSettings.edit();
                editor.putString("name" + idButton , edittext.getText().toString());
                editor.apply();
                initButton(ll);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();

    }


}
