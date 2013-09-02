package com.exaple.testmoodle.helper;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.testmoodle.MainActivity;
import com.example.testmoole.util.User;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TokenHttpRequest extends AsyncTask<Object, Object, JSONObject> {
	/*
	 * Retrieve the token based on the username, password and url
	 *
	 */
	private static Context context;
	private MainActivity activity;
	private User user;


	public TokenHttpRequest(MainActivity activity) {
		this.activity = activity;
     }
	
	@Override
	protected JSONObject doInBackground(Object... params) {
		Log.d("LoggingTracker", "Processing login in background");
		String tokenUrl = (String) params[0];
		user=(User) params[1];
		
		String responseBody = "";

		DefaultHttpClient httpClient = new DefaultHttpClient();

		// Creating HTTP Post
		HttpGet httpPost = new HttpGet(tokenUrl);
		Log.d("LoggingTracker", tokenUrl);
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpClient.execute(httpPost, responseHandler);

			JSONObject json = new JSONObject(responseBody);
			
			return json;
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostExecute(JSONObject jsonObject) {
		try {
			Log.d("Logging Tracker", "Got a new token : " + jsonObject.getString("token"));
			//Toast.makeText(context, "token received", Toast.LENGTH_SHORT).show();
			user.setToken(jsonObject.getString("token"));
			activity.getSiteInfo();
		} catch (JSONException e) {
			Log.d("Logging Tracker", "JSON Exception, setting token to null");
			
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("Logging Tracker", "Something else went wrong while authenticating");
			Toast.makeText(context, "Logging Incorrect", Toast.LENGTH_SHORT).show();
			
			e.printStackTrace();
		}
	}
}
