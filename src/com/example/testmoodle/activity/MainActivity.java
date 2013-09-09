package com.example.testmoodle.activity;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.testmoodle.R;
import com.example.testmoodle.helper.AppStatus;
import com.example.testmoodle.helper.TokenHttpRequest;
import com.example.testmoodle.helper.WebServiceCommunicator;
import com.example.testmoodle.helper.WebserviceFunction;
import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.SiteInfo;
import com.example.testmoodle.util.User;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText siteURl, username, password;
	private Button login;
	private ProgressDialog dialog;
	private String url,siteURLVal,passwordVal,usernameVal;
	private User user;
	private String token;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		/*setContentView(R.layout.login_activity);
	
		siteURl= (EditText) findViewById(R.id.siteURL);
		username= (EditText) findViewById(R.id.userName);
		password=(EditText) findViewById(R.id.password);
		login=(Button) findViewById(R.id.button1);
		
		siteURl.setHint("Site URL");
		username.setHint("Username");
		password.setHint("Password");*/
		
		siteURl= (EditText) findViewById(R.id.serverurlText);
		username= (EditText) findViewById(R.id.usernameEditText);
		password=(EditText) findViewById(R.id.passwordEditText);
		login=(Button) findViewById(R.id.loginButton);
		
		
		siteURl.setText("http://10.0.2.2/moodle");
		username.setText("shamika");
		password.setText("Shami@123");
		
		login.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		dialog= ProgressDialog.show(this, "", "Please Wait....", true);
		dialog.setCancelable(true);
		
	    siteURLVal=siteURl.getText().toString();
		passwordVal= password.getText().toString();
		usernameVal=username.getText().toString();
		
		
		if (AppStatus.getInstance(this).isOnline(this)) {
			
		    Toast.makeText(this,"You are online!!!!",Toast.LENGTH_LONG).show();
		    messageHandler.sendEmptyMessage(0);
		    
		    try {
		    	 user=new User();
				 user.setUsename(usernameVal);
				 user.setPassword(passwordVal);
				 getToken();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} catch (ExecutionException e) {
				
				e.printStackTrace();
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}else{
			 Toast.makeText(this,"No connections are available. Please make sure your data connection and try again later",Toast.LENGTH_LONG).show();
			 messageHandler.sendEmptyMessage(0);
		}
	}
	
	public Handler messageHandler=new Handler(){
		 public void handleMessage(Message msg) {
		        super.handleMessage(msg);
		        dialog.dismiss();

		    }

	};
	
	private void getToken() throws InterruptedException, ExecutionException, JSONException{
		 url = siteURLVal + "/login/token.php?username=" + usernameVal + "&password=" + passwordVal + "&service=moodle_mobile_app";
		 user=new User();
		 user.setUsename(usernameVal);
		 user.setPassword(passwordVal);
				JSONObject jsonObj=new TokenHttpRequest(this).execute(url,user).get();
				token=jsonObj.getString("token");
				if(token!=null){
					
					user.setUsename(usernameVal);
					user.setPassword(passwordVal);
					user.setToken(token);
				}
	}
	
	public void getSiteInfo() {
		SiteInfo siteInfo=new SiteInfo();
		user.setSiteInfo(siteInfo);
		String serverurl = siteURLVal + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=";
		String urlParameters = "";
		String siteFunction=WebserviceFunction.moodle_webservice_get_siteinfo;
		new WebServiceCommunicator(this, this).execute(serverurl, siteFunction, urlParameters, user, R.raw.siteinfoxsl);
	}
	
	public void getCourseInfo() throws UnsupportedEncodingException{
		ArrayList<Course> courses=new ArrayList<Course>();
		user.setCourses(courses);
		String serverurl = siteURLVal + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=";
		String id= String.valueOf(user.getSiteInfo().getUserID());
		Log.d("LoggingTracker", id+ "k");
		String courseUrlParameters= "userid="+URLEncoder.encode(id, "UTF-8");
		String courseFunction=WebserviceFunction.moodle_enrol_get_users_courses;
		new WebServiceCommunicator(this, this).execute(serverurl,courseFunction, courseUrlParameters, user, R.raw.coursesxsl );
		
	}
	
	public void viewCourse(){
		if(user.getCourses().size()>0){
			Intent intent = new Intent(this, CourseDetailsActivity.class);
			intent.putExtra("userObject", user); 
			startActivity(intent);
		}else{
			messageHandler.sendEmptyMessage(0);
    		Log.e("Logging Tracker", "User is not enrolled in any courses");
        	Toast.makeText(getApplicationContext(), "You are not Enrolled in any Courses, please contact your Lecturer", Toast.LENGTH_LONG).show();
		}
	}
		
	}
	
