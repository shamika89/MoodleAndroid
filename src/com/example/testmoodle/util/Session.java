package com.example.testmoodle.util;

import android.content.Context;

public class Session {
	private static User user;
	private static Context context;
	
	public static User getUser() {
		if(user==null){
			return null;
		}
		return user;
	}
	public static void setUser(User user) {
		Session.user = user;
	}
	public static Context getContext() {
		return context;
	}
	public static void setContext(Context context) {
		Session.context = context;
	}
	
	
	
}
