package com.example.testmoodle.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.example.testmoodle.R;
import com.example.testmoodle.helper.WebServiceCommunicator;
import com.example.testmoodle.helper.WebserviceFunction;
import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.CourseContent;
import com.example.testmoodle.util.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseDetailsActivity extends Activity implements OnClickListener {
	private User user;
	private ListView courselist;
	private Button overviewButton, courseButton, logoutButton, backButton;
	private Intent nextPage;
	private SharedPreferences pref;
	private String siteUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courselist_activity);

		courselist = (ListView) findViewById(R.id.myCourses);
		overviewButton = (Button) findViewById(R.id.coursework_overview);
		courseButton = (Button) findViewById(R.id.select_course);
		logoutButton = (Button) findViewById(R.id.logout);
		backButton = (Button) findViewById(R.id.back);

		Intent i = getIntent();
		user = (User) i.getParcelableExtra("userObject"); // get the parceable
															// user object

		pref = getSharedPreferences("loginDetails", MODE_PRIVATE);
		siteUrl = pref.getString("siteUrlVal", null);

		courseButton.setOnClickListener(this);
		overviewButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		listCourses();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void listCourses() {
		List<Course> values = user.getCourses();

		MyAdapter adapter1 = new MyAdapter(this, (ArrayList<Course>) values); // setting
																				// an
																				// adapter
																				// for
																				// listview
		courselist.setAdapter(adapter1);

	}

	private class MyAdapter extends BaseAdapter { // create new adapter class
		private final Context context;
		private final ArrayList<Course> array;

		public MyAdapter(Context context, ArrayList<Course> array) {
			this.context = context;
			this.array = array;
		}

		public int getCount() {
			return array.size();
		}

		public Object getItem(int position) {
			return array.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Course values = array.get(position);
			final int id = values.getId();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView;

			if (convertView == null) {
				rowView = new View(context);
				rowView = inflater.inflate(R.layout.courselistview_layout,
						parent, false);

			} else {
				rowView = (View) convertView;
			}

			TextView myCourse = (TextView) rowView
					.findViewById(R.id.myCoursesName);
			myCourse.setText(values.getFulltName());

			if (position % 2 != 0)
				myCourse.setBackgroundResource(R.drawable.listview_item_differentiate_color); // for
																								// differentiate
																								// listview
																								// items

			rowView.setClickable(true);

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
				
					// //transferring to ContentSelector activity
					user.setSelectedCourseID(id);
					try {
						getCourseContents();
					} catch (UnsupportedEncodingException e) {

						e.printStackTrace();
					}
					

				}
			});
			return rowView;
		}
	}

	@Override
	public void onClick(View v) { // selection of footer view

		switch (v.getId()) {
		case R.id.coursework_overview:
			Toast.makeText(this, "Please Select a Course", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.select_course:
			nextPage = new Intent(this, CourseDetailsActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		case R.id.back:
			nextPage = new Intent(this, MainActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		case R.id.logout:
			nextPage = new Intent(this, MainActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		default:

		}
	}

	public void getCourseContents() throws UnsupportedEncodingException {

		String serverurl = siteUrl + "/webservice/rest/server.php"
				+ "?wstoken=" + user.getToken() + "&wsfunction=";
		String contentFunction = WebserviceFunction.core_course_get_contents;

		String course = String.valueOf(user.getCourse(
				user.getSelectedCourseID()).getId());
		String contentUrlParameters = "courseid="
				+ URLEncoder.encode(course, "UTF-8");

		ArrayList<CourseContent> content = new ArrayList<CourseContent>();
		user.getCourse(user.getSelectedCourseID()).setCourseContents(content);
		new WebServiceCommunicator(this, this).execute(serverurl,
				contentFunction, contentUrlParameters, user, R.raw.contentxsl);

	}

	public void viewContents() {
		nextPage = new Intent(this, ContentSelector.class); // transferring to
															// ContentSelector
															// activity
		nextPage.putExtra("userObject", user);
		startActivity(nextPage);
	}

}
