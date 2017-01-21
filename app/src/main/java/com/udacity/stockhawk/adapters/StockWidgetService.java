package com.udacity.stockhawk.adapters;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.Locale;

/**
 * Created by Justin on 1/3/17.
 */

public class StockWidgetService extends RemoteViewsService {



    private final static String LOG_TAG = StockWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = Contract.Quote.URI;
                data = getContentResolver().query(
                        uri,
                        null,
                        null,
                        null,
                        null
                );
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_stock_list_item);

                Log.d(LOG_TAG, "Attempting to pull data from cursor");
                String stockName = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
                Log.d(LOG_TAG, "Stock symbol is " + stockName);
                String stockPrice = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_PRICE));
                Log.d(LOG_TAG, "Stock price is " + stockPrice);

                views.setTextViewText(R.id.widget_stock_name_textview, stockName);
                views.setTextColor(R.id.widget_stock_name_textview, Color.WHITE);
                views.setTextViewText(R.id.widget_stock_price_textview, String.format(Locale.US, "$%.2f", Double.valueOf(stockPrice)));
                views.setTextColor(R.id.widget_stock_price_textview, Color.WHITE);

                final Intent intent = new Intent();

                Uri uri = Contract.Quote.URI;
                intent.setData(uri);
                views.setOnClickFillInIntent(R.id.widget_list_item_linear_layout, intent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_stock_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(data.getColumnIndex("symbol"));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}











//class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
//
//    private int mCount;
//    private Cursor data;
//    private Context mContext;
//    private int mAppWidgetId;
//
//    public void onCreate() {
//        // Nothing to do?
//    }
//
//    @Override
//    public void onDataSectChanged() {
//        if (data != null) {
//            data.close();
//        }
//
//        final long identityToken = Binder.clearCallingIdentity();
//        Uri quoteUri = Contract.Quote.URI;
//
//        data = new StockProvider().query(
//                quoteUri,
//                null,
//                null,
//                null,
//                null
//        );
//        Log.d("StockWidgetService", "The built query cursor is " + data);
//    }

//}
