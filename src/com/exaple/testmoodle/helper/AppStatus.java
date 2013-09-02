package com.exaple.testmoodle.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppStatus {
	 private static AppStatus instance = new AppStatus();
	    static Context context;
	    ConnectivityManager connectivityManager;
	    NetworkInfo wifiInfo, mobileInfo;
	    boolean connected = false;

	    public static AppStatus getInstance(Context ctx) {
	        context = ctx;
	        return instance;
	    }

	    public boolean isOnline(Context con) {
	        try {
	            connectivityManager = (ConnectivityManager) con
	                        .getSystemService(Context.CONNECTIVITY_SERVICE);

	        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	        connected = networkInfo != null && networkInfo.isAvailable() &&
	                networkInfo.isConnected();
	        return connected;


	        } catch (Exception e) {
	            System.out.println("CheckConnectivity Exception: " + e.getMessage());
	            Log.v("connectivity", e.toString());
	        }
	        return connected;
	    }
	}

