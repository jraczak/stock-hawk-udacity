package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockHistoryAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StockDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = StockDetailActivity.class.getSimpleName();

    private String mProvidedSymbol;
    private String mStockHistory;
    private LineChart mLineChart;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        //TODO: Figure out why this is broken
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProvidedSymbol = getIntent().getStringExtra("symbol");
        mStockHistory = getIntent().getStringExtra("history");



        mLineChart = (LineChart) findViewById(R.id.graph_stock_detail_history);

        TextView symbolTextView = (TextView) findViewById(R.id.label_stock_symbol);
        symbolTextView.setText(mProvidedSymbol);

        ArrayList<String> quoteList = new ArrayList<>(Arrays.asList(mStockHistory.split("\n")));

        Log.d(LOG_TAG, quoteList.getClass().getSimpleName());

        Map<String, String> mappedHistory = new HashMap<>();

        for (String entry : quoteList) {
            String[] pair = entry.split(", ");
            Log.d(LOG_TAG, "Key is " + pair[0] + ", value is " + pair[1]);
            //TODO: Convert types here instead of converting them everywhere these values are used
            // Format the price to resemble proper currency when inserting it as value in the map
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

        TreeMap<String, String> treeMappedHistory = new TreeMap<>(mappedHistory);

        StockHistoryAdapter historyAdapter = new StockHistoryAdapter(treeMappedHistory);
        ListView listView = (ListView) findViewById(R.id.stock_history_listview);
        listView.setAdapter(historyAdapter);
        // Make sure the list view is tall enough to hold all the records
        setListViewHeightBasedOnItems(listView);


        List<Entry> graphEntries = new ArrayList<>();

        DecimalFormat currencyFormat = new DecimalFormat("###,##0.00");
        String formattedCurrency;
        for (Map.Entry<String, String> historyEntry : treeMappedHistory.entrySet()) {
            graphEntries.add(new Entry(Float.parseFloat(historyEntry.getKey()), Float.parseFloat(historyEntry.getValue())));
        }

        LineDataSet lineDataSet = new LineDataSet(graphEntries, null);
        lineDataSet.setColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setLineWidth(2);


        LineData lineData = new LineData(lineDataSet);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        mLineChart.setTouchEnabled(false);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
        mLineChart.requestFocus();


    }

    public static String getFriendlyDate(long milliseconds) {

        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;

            for (int itemPosition = 0; itemPosition < numberOfItems; itemPosition++) {
                View item = listAdapter.getView(itemPosition, null, listView);
                item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                //totalItemsHeight += item.getMeasuredHeight();
                totalItemsHeight += item.getMeasuredHeight();
                Log.d(LOG_TAG, "Measured height of list item is " + item.getMeasuredHeight());
                Log.d(LOG_TAG, "Total height of list view is now " + totalItemsHeight);
            }

            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems -1 );

            ViewGroup.LayoutParams params =
                    listView.getLayoutParams();
            params.height = ((totalItemsHeight + totalDividersHeight)/2 + 700);
            // TODO: Figure out why this doesn't measure the correct 51dp size
            //params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;
        }
        else {
            return false;
        }
    }

}
