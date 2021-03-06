package widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockWidgetService;
import com.udacity.stockhawk.ui.MainActivity;

/**
 * Created by Justin on 1/2/17.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_my_stocks;
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            Intent serviceIntent = new Intent(context, StockWidgetService.class);

            views.setRemoteAdapter(appWidgetId, R.id.widget_listview, serviceIntent);

            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);

            views.setInt(R.id.widget_frame_layout, "setBackgroundColor", Color.BLACK);

            views.setOnClickPendingIntent(R.id.widget_frame_layout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
