package com.udacity.stockhawk.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Justin on 12/27/16.
 */

public class StockHistoryAdapter extends BaseAdapter {

    private final ArrayList mHistoryRecords;

    public StockHistoryAdapter(TreeMap<String, String> historyMap) {
        mHistoryRecords = new ArrayList();
        mHistoryRecords.addAll(historyMap.entrySet());
    }

    @Override
    public int getCount() {
        return mHistoryRecords.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mHistoryRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_history_item, parent, false);
        } else {
            view = convertView;
        }

        TextView dateTextView = (TextView) view.findViewById(R.id.stock_history_date_textview);
        TextView priceTextView = (TextView) view.findViewById(R.id.stock_history_price_textview);
        final Map.Entry<String, String> entry = getItem(position);
        dateTextView.setText(getFriendlyDate(Long.parseLong(entry.getKey())));
        priceTextView.setText(String.format(Locale.US, "$%.2f", Double.valueOf(entry.getValue())));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Closing price was " +
                        String.format(Locale.US, "$%.2f", Double.valueOf(entry.getValue())) +
                        " on " +
                        getFriendlyDate(Long.parseLong(entry.getKey())), Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    public static String getFriendlyDate(long milliseconds) {

        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return simpleDateFormat.format(calendar.getTime());
    }
}
