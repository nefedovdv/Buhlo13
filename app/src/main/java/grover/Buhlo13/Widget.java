package grover.Buhlo13;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

/**
 * Created by ndv on 28.11.2016.
 */
public class Widget extends AppWidgetProvider {
    private final String LOG_TAG = "my log";
    @Override
    public void onEnabled(Context context) {
        Log.d(LOG_TAG, "enabled");
        super.onEnabled(context);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "update");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


}
