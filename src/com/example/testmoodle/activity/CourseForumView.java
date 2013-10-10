package com.example.testmoodle.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.testmoodle.R;
import com.example.testmoodle.util.CourseContent;
import com.example.testmoodle.util.Module;
import com.example.testmoodle.util.User;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseForumView extends Activity implements OnClickListener {
	private User user;
	private ListView forumlist;
	private Button overviewButton, courseButton, logoutButton, backButton;
	private Intent nextPage;
	private LinearLayout emptyLayout, contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum_listing);

		forumlist = (ListView) findViewById(R.id.myForums);
		overviewButton = (Button) findViewById(R.id.coursework_overview);
		courseButton = (Button) findViewById(R.id.select_course);
		logoutButton = (Button) findViewById(R.id.logout);
		backButton = (Button) findViewById(R.id.back);
		emptyLayout = (LinearLayout) findViewById(R.id.course_forumitem_empty);
		contentLayout = (LinearLayout) findViewById(R.id.course_forum_listview);
		emptyLayout.setVisibility(View.INVISIBLE);
		contentLayout.setVisibility(View.VISIBLE);

		Intent i = getIntent();
		user = (User) i.getParcelableExtra("userObject"); // get the parceable
															// user object
		getCourseForums();
		listForums();

		courseButton.setOnClickListener(this);
		overviewButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		backButton.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void getCourseForums() {
		ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
		if (user != null && user.getCourse(user.getSelectedCourseID()) != null) {
			coursecontent = user.getCourse(user.getSelectedCourseID())
					.getCourseContents();

			for (CourseContent content : coursecontent) {
				if (content.getVisible() == 1) {
					for (Module module : content.getModules()) {
						if (module.getModname().equalsIgnoreCase("forum")
								&& module.getVisible() == 1) {
							Module forum = new Module();
							forum.setName(module.getName());
							forum.setUrl(module.getUrl());
							user.getCourse(user.getSelectedCourseID())
									.getForum().add(forum);
						}
					}
				}
			}
		}

	}

	public void listForums() {
		List<Module> values = user.getCourse(user.getSelectedCourseID())
				.getForum();

		if (values.size() > 0) {
			MyAdapter adapter1 = new MyAdapter(this, (ArrayList<Module>) values); // setting
																					// an
																					// adapter
																					// for
																					// listview
			forumlist.setAdapter(adapter1);
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.INVISIBLE);
		}

	}

	private class MyAdapter extends BaseAdapter { // create new adapter class
		private final Context context;
		private final ArrayList<Module> array;

		public MyAdapter(Context context, ArrayList<Module> array) {
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
			Module values = array.get(position);
			final int pose = position;

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView;

			if (convertView == null) {
				rowView = new View(context);
				rowView = inflater.inflate(R.layout.forumlistview_layout,
						parent, false);

			} else {
				rowView = (View) convertView;
			}

			TextView forum = (TextView) rowView.findViewById(R.id.forumName);
			forum.setText(values.getName());

			if (position % 2 != 0)
				rowView.setBackgroundResource(R.drawable.listview_item_differentiate_color); // for
																								// differentiate
																								// listview
																								// items

			rowView.setClickable(true);

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String url = user.getCourse(user.getSelectedCourseID())
							.getForum().get(pose).getUrl();

					if (!url.startsWith("http://")
							&& !url.startsWith("https://"))
						url = "http://" + url;

					Log.d("URL", url);

					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));

					try {
						// Start the activity
						startActivity(browserIntent);
					} catch (ActivityNotFoundException e) {
						// Raise on activity not found
						Toast.makeText(getApplicationContext(),
								"Browser not found.", Toast.LENGTH_SHORT)
								.show();
					}

				}
			});
			return rowView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.coursework_overview:
			nextPage = new Intent(this, ContentSelector.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
		case R.id.select_course:
			nextPage = new Intent(this, CourseDetailsActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		case R.id.back:
			nextPage = new Intent(this, ContentSelector.class);
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

}
