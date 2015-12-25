 package com.arad_itc.newsit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class Newsit extends Application {

    public static Context context;
    public static LayoutInflater inflater;
    public static Activity currentActivity;
    public static Activity currentActivity2;
    public static ArrayList<Row_List_View_object> array = new ArrayList<Row_List_View_object>();
    public static ArrayList<Row_List_View_object> array_2 = new ArrayList<Row_List_View_object>();

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public static boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) Newsit.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

}
