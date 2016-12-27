package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.udacity.stockhawk.R;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        //TODO: Figure out why this is broken
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView symbolTextView = (TextView) findViewById(R.id.label_stock_symbol);
        symbolTextView.setText(getIntent().getStringExtra("symbol"));
    }

}
