package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.utils.IntentKeys;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetailsActivity extends AppCompatActivity {


	@BindView(R.id.stock_name_text)
	TextView stockName;
	@BindView(R.id.stock_chart)
	LineChart stockLineChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_details);
		ButterKnife.bind(this);

		if (getIntent().hasExtra(IntentKeys.EXTRA_STOCK_SYMBOL)) {
			String symbol = getIntent().getStringExtra(IntentKeys.EXTRA_STOCK_SYMBOL);
			stockName.setText(symbol);
			renderGraph(symbol);
		} else {
			Timber.e("Error getting symbol extra");
			finish();
		}
	}


	private void renderGraph(String symbol){
		String stockHistoryData = getStockData(symbol);

		List<String[]> lines = new ArrayList<>();
		CSVReader reader = new CSVReader(new StringReader(stockHistoryData));
		try {
			lines.addAll(reader.readAll());
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Entry> entries = new ArrayList<>();
		final List<Long> xAxisValues = new ArrayList<>();

		for (int i = 0; i < lines.size(); i++){
			xAxisValues.add(Long.valueOf(lines.get(i)[0]));
			entries.add(new Entry(i, Float.valueOf(lines.get(i)[1])));

		}

		LineData lineData = new LineData(new LineDataSet(entries, symbol));

		stockLineChart.setData(lineData);

		XAxis xAxis =  stockLineChart.getXAxis();
		xAxis.setValueFormatter(new IAxisValueFormatter() {
			@Override
			public String getFormattedValue(float value, AxisBase axis) {
				Date date = new Date(xAxisValues.get(xAxisValues.size()- (int)value - 1));
				return new SimpleDateFormat( "yyyy-MM-dd", Locale.ENGLISH).format(date);
			}
		});

		stockLineChart.getDescription().setEnabled(false);
		stockLineChart.getAxisLeft().setTextColor(Color.WHITE);
		stockLineChart.getAxisRight().setTextColor(Color.WHITE);
		stockLineChart.getXAxis().setTextColor(Color.WHITE);
		stockLineChart.getLegend().setTextColor(Color.WHITE);

	}





	private String getStockData (String symbol) {
		Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(symbol), null, null, null, null);
		String history = null;
		if(cursor != null){
			cursor.moveToFirst();
			history = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
			cursor.close();
		}
		return history;
	}


}
