package com.udacity.stockhawk.ui.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;

import java.security.Provider;

/**
 * Created by Pablo Criado Carrera on 08/04/2017.
 */

public class WidgetService extends IntentService {


	public WidgetService(String name) {
		super(name);
	}

	public WidgetService() {
		super("WidgetService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

		for (int appWidgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.widget);
			remoteViews.setRemoteAdapter(R.id.list_view_widget, new Intent(this, WidgetRemoteViews.class));

			Intent tapIntent = new Intent(this, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.list_item_quote, pendingIntent);

			
			remoteViews.setEmptyView(R.id.list_view_widget, R.id.empty_text_widget);


			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}
	}
}
