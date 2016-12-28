package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StockDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = StockDetailActivity.class.getSimpleName();

    private String mProvidedSymbol;
    private String mStockHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        //TODO: Figure out why this is broken
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProvidedSymbol = getIntent().getStringExtra("symbol");
        mStockHistory = getIntent().getStringExtra("history");

        TextView symbolTextView = (TextView) findViewById(R.id.label_stock_symbol);
        symbolTextView.setText(mProvidedSymbol);

        ArrayList<String> quoteList = new ArrayList<>(Arrays.asList(mStockHistory.split("\n")));

        Log.d(LOG_TAG, quoteList.getClass().getSimpleName());

        Map<String, String> mappedHistory = new HashMap<>();

        for (String entry : quoteList) {
            String[] pair = entry.split(", ");
            Log.d(LOG_TAG, "Key is " + pair[0] + ", value is " + pair[1]);
            mappedHistory.put(pair[0], pair[1]);
        }

        for (Map.Entry<String, String> entry : mappedHistory.entrySet()) {
            Log.d(LOG_TAG, "Entry key is " + entry.getKey());
            Log.d(LOG_TAG, "Entry value value is " + entry.getValue());
            String date = getFriendlyDate(Long.parseLong(entry.getKey()));
            float price = Float.valueOf(entry.getValue());

            Log.d(LOG_TAG, "The price of " + mProvidedSymbol + " on " + date + " was " + price);
        }

        Log.d(LOG_TAG, "Converted history: " + mappedHistory);


    }

    public static String getFriendlyDate(long milliseconds) {

        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return simpleDateFormat.format(calendar.getTime());
    }

}
