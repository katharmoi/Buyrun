package com.kadirkertis.orfo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.ui.placeprofile.PlaceProfileActivity;
import com.kadirkertis.domain.utils.Constants;

/**
 * Created by Kadir Kertis on 30.3.2017.
 */

public class OrfoWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            //Intent for the service
            Intent intent = new Intent(context, OrfoRemoteViewsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            //Main layout
            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.appwidget);


            rv.setRemoteAdapter(R.id.widgetCollectionList, intent);
            rv.setEmptyView(R.id.widgetCollectionList,R.id.widgetEmptyView);

            //Open PlaceProfile activity when item click
            //Create pending intent template
            Intent onItemClick = new Intent(context, PlaceProfileActivity.class);
            PendingIntent onClickPendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(onItemClick)
                    .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.widgetCollectionList,onClickPendingIntent);
            appWidgetManager.updateAppWidget(widgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.ACTION_WIDGET_DATA_FETCHED)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widgetCollectionList);
        }
        super.onReceive(context, intent);
    }
}
