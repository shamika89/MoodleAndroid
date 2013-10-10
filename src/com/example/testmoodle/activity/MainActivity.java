package com.example.testmoodle.activity;

import java.io.File;
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
import com.example.testmoodle.util.CourseContent;
import com.example.testmoodle.util.SiteInfo;
import com.example.testmoodle.util.User;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText siteURl, username, password; // to enter moodle site url,
													// user's username and
													// password
	private Button login, offline;
	private ProgressDialog dialog; // show dialog while application does
									// background work for finding network
									// connections
	private String url, siteURLVal, passwordVal, usernameVal;
	private User user;
	private String token; // token return from web service
	ArrayList<Course> courses;
	private static MainActivity instance;
	private SharedPreferences pref;
	private Intent nextPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		instance = this;
		siteURl = (EditText) findViewById(R.id.serverurlText);
		username = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		login = (Button) findViewById(R.id.loginButton);
		offline = (Button) findViewById(R.id.OfflineButton);

		siteURl.setText("http://10.0.2.2/moodle");
		username.setText("shamika");
		password.setText("Shami@123");

		login.setOnClickListener(this);
		offline.setOnClickListener(this);

	}

	public static MainActivity getInstance() {
		return instance;
	}

	public static void setInstance(MainActivity instance) {
		MainActivity.instance = instance;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginButton:
			login.setEnabled(false);
			siteURLVal = siteURl.getText().toString();
			passwordVal = password.getText().toString();
			usernameVal = username.getText().toString();
			
			dialog = ProgressDialog.show(this, "", "Please Wait....", true);
			dialog.setCancelable(true);

			

			if (AppStatus.getInstance(this).isOnline(this)) { // check whether
																// the
																// user has
																// network
																// connection

				Toast.makeText(this, "You are online!!!!", Toast.LENGTH_LONG)
						.show();
				messageHandler.sendEmptyMessage(0); // cancel the dialog

				try {
					user = new User(); // creating a session for the user and
										// setting username and password
					user.setUsename(usernameVal);
					user.setPassword(passwordVal);
					user.setSiteUrl(siteURLVal);
					receiveToken();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				pref = getSharedPreferences("loginDetails", MODE_PRIVATE);

				SharedPreferences.Editor e = pref.edit();
				e.putString("siteUrlVal", siteURLVal);
				e.putString("username", usernameVal);
				e.putString("pwd", passwordVal);
				e.commit();

			} else {
				Toast.makeText(
						this,
						"No connections are available. Please make sure your data connection and try again later",
						Toast.LENGTH_LONG).show();
				messageHandler.sendEmptyMessage(0);
			}
			
			break;

		case R.id.OfflineButton:
			String file = android.os.Environment.getExternalStorageDirectory()
					.getPath() + "/Moodle";
			File f = new File(file);
			if (f.exists()) {
				nextPage = new Intent(this, OfflieFolderListigActivity.class);
				startActivity(nextPage);
			} else {
				Toast.makeText(getBaseContext(),
						"You must login atleast once to use this feature!",
						Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
		

	}

	public String getToken() {
		return token;
	}

	public Handler messageHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.dismiss();

		}

	};

	private void receiveToken() throws InterruptedException,
			ExecutionException, JSONException { // calling moodle webservice for
												// returning a token for the
												// user
		url = siteURLVal + "/login/token.php?username=" + usernameVal
				+ "&password=" + passwordVal + "&service=moodle_mobile_app";
		JSONObject jsonObj = new TokenHttpRequest(this, this)
				.execute(url, user).get();
		token = jsonObj.getString("token");
		if (token != null) { // check whether the user is a valid user
			user.setUsename(usernameVal);
			user.setPassword(passwordVal);
			user.setToken(token);

		}
	}

	public void getSiteInfo() { // get site information. basically to get the
								// user id.
		SiteInfo siteInfo = new SiteInfo();
		user.setSiteInfo(siteInfo);
		String serverurl = siteURLVal + "/webservice/rest/server.php"
				+ "?wstoken=" + token + "&wsfunction=";
		String urlParameters = "";
		String siteFunction = WebserviceFunction.moodle_webservice_get_siteinfo;
		new WebServiceCommunicator(this, this).execute(serverurl, siteFunction,
				urlParameters, user, R.raw.siteinfoxsl);
		// background work is handled by WebService Communicator asynchronous
		// class
	}

	public void getCourseInfo() throws UnsupportedEncodingException { // get
																		// user's
																		// enrolled
																		// courses
		courses = new ArrayList<Course>();
		user.setCourses(courses);
		String serverurl = siteURLVal + "/webservice/rest/server.php"
				+ "?wstoken=" + token + "&wsfunction=";
		String id = String.valueOf(user.getSiteInfo().getUserID());
		String courseUrlParameters = "userid=" + URLEncoder.encode(id, "UTF-8");
		String courseFunction = WebserviceFunction.moodle_enrol_get_users_courses;
		new WebServiceCommunicator(this, this).execute(serverurl,
				courseFunction, courseUrlParameters, user, R.raw.coursesxsl);

	}

	public void getCourseContents() throws UnsupportedEncodingException {
		if (user.getCourses().size() > 0) {
			String serverurl = siteURLVal + "/webservice/rest/server.php"
					+ "?wstoken=" + token + "&wsfunction=";
			String contentFunction = WebserviceFunction.core_course_get_contents;

			for (int i = 0; i < user.getCourses().size(); i++) {
				String course = String
						.valueOf(user.getCourses().get(i).getId());
				String contentUrlParameters = "courseid="
						+ URLEncoder.encode(course, "UTF-8");
				Course c = user.getCourses().get(i);
				ArrayList<CourseContent> content = new ArrayList<CourseContent>();
				c.setCourseContents(content);
				new WebServiceCommunicator(this, this).execute(serverurl,
						contentFunction, contentUrlParameters, user,
						R.raw.contentxsl, i);

			}

		}
	}

	
	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void viewCourse() { // Transferring to CourseDetails activity
		if (user.getCourses().size() > 0) {
			nextPage = new Intent(this, CourseDetailsActivity.class);
			nextPage.putExtra("userObject", user);
			nextPage.putExtra("siteUrl", siteURLVal);
			startActivity(nextPage);
		} else {
			Log.e("Logging Tracker", "User is not enrolled in any course");
			Toast.makeText(
					getApplicationContext(),
					"You are not Enrolled in any Courses, please contact your Lecturer",
					Toast.LENGTH_LONG).show();
		}
	}

}
