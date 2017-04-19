package com.kadirkertis.orfo.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.PlaceInfo;
import com.kadirkertis.orfo.utilities.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kadir Kertis on 30.3.2017.
 */

public class OrfoRemoteViewsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new OrfoRemoteViewsFactory(getApplicationContext(),intent);
    }

    class OrfoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private FirebaseDatabase mDb;
        private FirebaseAuth mAuth;
        private DatabaseReference mPlaceListReference;
        private ChildEventListener mPlacesChildEventListener;

        private Context mContext;
        private List<PlaceInfo> mPlaces;
        private int mAppWidgetId;

        public OrfoRemoteViewsFactory(Context context,Intent intent){
            mContext = context;
            mPlaces = new ArrayList<>();
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        @Override
        public void onCreate() {

            mDb = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mPlaceListReference = mDb.getReference()
                    .child(Constants.DB_PLACE_LIST);
           attachListeners();
            if(mAuth.getCurrentUser() == null){

            }
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            detachListeners();
        }

        @Override
        public int getCount() {
            return mPlaces.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
           final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
            PlaceInfo info = mPlaces.get(i);
            try {
                Bitmap b = Picasso.with(mContext)
                        .load(info.getImageUrl())
                        .get();
                if(b!= null){
                    rv.setImageViewBitmap(R.id.widgetItemImage, b);
                }else{
                    Bitmap noImgIcon = BitmapFactory.decodeResource(getResources(), R.drawable.no_img_placeholder);
                    rv.setImageViewBitmap(R.id.widgetItemImage, noImgIcon);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            rv.setTextViewText(R.id.widgetItemName,info.getPlaceName());
            rv.setTextViewText(R.id.widgetItemType, info.getPlaceType());

            Intent fillInintent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLACE_ID_EXTRA,
                    info.getId());
            fillInintent.putExtras(bundle);
            rv.setOnClickFillInIntent(R.id.widgetSingleItem,fillInintent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private void attachListeners(){
            if(mPlacesChildEventListener == null){
                mPlacesChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        mPlaces.add(dataSnapshot.getValue(PlaceInfo.class));
                        Intent intent = new Intent(Constants.ACTION_WIDGET_DATA_FETCHED);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        mPlaces.remove(dataSnapshot.getValue(PlaceInfo.class));
                        Intent intent = new Intent(Constants.ACTION_WIDGET_DATA_FETCHED);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mPlaceListReference.addChildEventListener(mPlacesChildEventListener);
            }
        }

        private void detachListeners(){
            if(mPlacesChildEventListener != null){
                mPlaceListReference.removeEventListener(mPlacesChildEventListener);
                mPlacesChildEventListener = null;
            }
        }
    }
}
