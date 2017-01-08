package com.udacity.stockhawk.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;

/**
 * Created by Justin on 1/3/17.
 */

public class StockWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private int mCount;
    private Cursor data;
    private Context mContext;
    private int mAppWidgetId;

    @Override
    public void onDataSectChanged() {
        if (data != null) {
            data.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        Uri quoteUri = Contract.Quote.URI;

        data = new StockProvider().query(
                quoteUri,
                null,
                null,
                null,
                null
        );
        Log.d("StockWidgetService", "The built query cursor is " + data);
    }

}
