package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.utils.IntentKeys;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	private Uri stockUri;

	@BindView(R.id.stock_name_text)
	TextView stockName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_details);
		ButterKnife.bind(this);

		if (getIntent().hasExtra(IntentKeys.EXTRA_STOCK_NAME)){
			String symbol = getIntent().getStringExtra(IntentKeys.EXTRA_STOCK_NAME);
			stockUri = Contract.Quote.makeUriForStock(symbol);
			stockName.setText(symbol);
		}
		else {
			Timber.e("Error getting symbol extra");
			finish();
		}


	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(
				this,
				stockUri,
				null,
				null,
				null,
				null
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data != null && data.moveToFirst()){
			// TODO: set graph data
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
