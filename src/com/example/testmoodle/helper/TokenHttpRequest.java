package com.example.testmoodle.helper;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.testmoodle.activity.MainActivity;
import com.example.testmoodle.util.User;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TokenHttpRequest extends AsyncTask<Object, Object, JSONObject> {
	/*
	 * Retrieve the token based on the username, password and url
	 */
	private static Context context;
	private MainActivity activity;
	private User user;

	public TokenHttpRequest(MainActivity activity, Context context) {
		this.activity = activity;
		this.context=context;
	}

	@Override
	protected JSONObject doInBackground(Object... params) {
		Log.d("LoggingTracker", "Processing login in background");
		String tokenUrl = (String) params[0];
		user = (User) params[1];

		String responseBody = "";

		DefaultHttpClient httpClient = new DefaultHttpClient(); // creating HTTP
																// client
		HttpGet httpPost = new HttpGet(tokenUrl); // Creating HTTP Post
		Log.d("LoggingTracker", tokenUrl);
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpClient.execute(httpPost, responseHandler);
			JSONObject json = new JSONObject(responseBody);
			return json;
		} catch (ClientProtocolException e) {
			Log.d("Exception", "ClientProtocolException " + e.toString());// writing
																			// exception
																			// to
																			// log
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("Exception", "IOException " + e.toString());// writing
																// exception to
																// log
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("Exception", "Exception " + e.toString());// writing exception
															// to log
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onPostExecute(JSONObject jsonObject) {
		if(jsonObject!=null){
		try {
			Log.d("TokenHttpRequest",
					"Got a new token : " + jsonObject.getString("token"));
			user.setToken(jsonObject.getString("token"));
			if(activity!=null){
			activity.getSiteInfo();
			}
		} catch (JSONException e) {
			Toast.makeText(context,
					"Username or Password may incorrect. Please Recheck",
					Toast.LENGTH_SHORT).show();
			user.setToken(null);
			Log.d("TokenHttpRequest", "JSON Exception " + e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("TokenHttpRequest",
					"Something went wrong while authenticating  " +e.toString() );
			Toast.makeText(context, "Logging Incorrect", Toast.LENGTH_SHORT)
					.show();
			user.setToken(null);
			e.printStackTrace();
		}
	}
	}
}
