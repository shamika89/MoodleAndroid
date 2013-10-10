package com.example.testmoodle.activity;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.example.testmoodle.R;

import com.example.testmoodle.helper.FileDownloadTask;
import com.example.testmoodle.util.CourseContent;
import com.example.testmoodle.util.Document;
import com.example.testmoodle.util.Module;
import com.example.testmoodle.util.User;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CourseDocumentView extends Activity implements OnClickListener {
	private User user;
	private ListView courselist;
	private Button overviewButton, courseButton, logoutButton, backButton;
	private Intent nextPage;
	private String courseName;
	private ProgressBar loadingImage;
	private TextView loadingText;
	private LinearLayout downloadStatusLayout;
	private LinearLayout emptyLayout, contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelisting);

		courselist = (ListView) findViewById(R.id.myFiles);
		overviewButton = (Button) findViewById(R.id.coursework_overview);
		courseButton = (Button) findViewById(R.id.select_course);
		logoutButton = (Button) findViewById(R.id.logout);
		backButton = (Button) findViewById(R.id.back);
		loadingImage = (ProgressBar) findViewById(R.id.LoadingProgressImage);
		loadingText = (TextView) findViewById(R.id.LoadingProgressText);
		downloadStatusLayout = (LinearLayout) findViewById(R.id.linearLayoutLoadingResourses);
		downloadStatusLayout.setVisibility(View.INVISIBLE);

		Intent i = getIntent();
		user = (User) i.getParcelableExtra("userObject"); // get the parceable
															// user object
		courseName = user.getCourse(user.getSelectedCourseID()).getFulltName();
		getCourseDocuments();
		listDocuments();

		courseButton.setOnClickListener(this);
		overviewButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		backButton.setOnClickListener(this);

	}

	public void getCourseDocuments() {
		ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
		if (user != null && user.getCourse(user.getSelectedCourseID()) != null) {
			coursecontent = user.getCourse(user.getSelectedCourseID())
					.getCourseContents();
		}

		for (int i = 0; i < coursecontent.size(); i++) {
			// String sectionName = content.getName();
			if (coursecontent.get(i).getVisible() == 1) {
				for (Module module : coursecontent.get(i).getModules()) {
					if (module.getModname().equalsIgnoreCase("resource")
							&& module.getVisible() == 1) {
						for (Document item : module.getFiles()) {
							if (item.getType().equalsIgnoreCase("file")) {
								Document doc = new Document();

								String fileextension = item.getFileName()
										.substring(
												item.getFileName().lastIndexOf(
														'.') + 1);
								String fullextension = "." + fileextension;
								String filename = item.getFileName().replace(
										fullextension, "");

								File file = new File(Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/Moodle/"
										+ courseName
										+ "/Documents/" + item.getFileName());
								file.exists();

								long timeCreate = item.getTimeCreated() * 1000;
								Date createDate = new Date(timeCreate);

								long time = item.getTimeModified() * 1000;
								Date modifyDate = new Date(time);

								SimpleDateFormat dateformat = new SimpleDateFormat(
										"dd\\MM\\yyyy");

								String eol = System
										.getProperty("line.separator");
								String timeCreatedModified = "Created On: "
										+ dateformat.format(createDate) + eol
										+ "Modified Date: "
										+ dateformat.format(modifyDate);

								int currentfilesize = item.getFileSize();

								float theSize = 0;
								String filesize;
								if (currentfilesize / 1024 > 1024) {
									theSize = (currentfilesize / 1024) / 1024;
									filesize = String.valueOf(theSize) + "Mb";
								} else {
									theSize = currentfilesize / 1024;
									filesize = String.valueOf(theSize) + "kb";
								}
								doc.setFileURL(item.getFileURL());
								doc.setFileName(item.getFileName());
								doc.setTimeCreatedmodified(timeCreatedModified);
								doc.setFileSizewithUnits(filesize);
								doc.setFileSize(currentfilesize);
								user.getCourse(user.getSelectedCourseID())
										.getDocument().add(doc);

							}
						}
					}
				}
			}
		}

	}

	public void listDocuments() {
		List<Document> values = user.getCourse(user.getSelectedCourseID())
				.getDocument();

		emptyLayout = (LinearLayout) findViewById(R.id.course_document_empty);
		contentLayout = (LinearLayout) findViewById(R.id.course_document_listview);
		emptyLayout.setVisibility(View.INVISIBLE);
		contentLayout.setVisibility(View.VISIBLE);

		if (values != null && values.size() > 0) {
			MyAdapter adapter1 = new MyAdapter(this,
					(ArrayList<Document>) values); // setting an adapter for
													// listview
			courselist.setAdapter(adapter1);
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.INVISIBLE);
		}

	}

	private class MyAdapter extends BaseAdapter { // create new adapter class
		private final Context context;
		private final ArrayList<Document> array;

		public MyAdapter(Context context, ArrayList<Document> array) {
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
			final Document values = array.get(position);
			final int pos = position;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView;

			if (convertView == null) {
				rowView = new View(context);
				rowView = inflater.inflate(R.layout.filelistview_layout,
						parent, false);

			} else {
				rowView = (View) convertView;
			}

			TextView myDocument = (TextView) rowView
					.findViewById(R.id.myFileName);
			TextView downloadState = (TextView) rowView
					.findViewById(R.id.myFileDownloadStatus);
			final Button downloadButton = (Button) rowView
					.findViewById(R.id.myFileButton);
			downloadButton.setId(position + 10001000);

			// check whether the file is already existing
			String courseDir = android.os.Environment
					.getExternalStorageDirectory().getPath()
					+ "/Moodle/"
					+ courseName + "/";
			File dir = new File(courseDir);
			File[] files = dir.listFiles();

			if (files != null) {

				for (int i = 0; i < user.getCourse(user.getSelectedCourseID())
						.getDocument().size(); i++) {
					for (File f : files) {
						if (f.getName().equals(
								user.getCourse(user.getSelectedCourseID())
										.getDocument().get(position)
										.getFileName())) {
							downloadButton.setText("Open");
							downloadButton
									.setBackgroundResource(R.drawable.bluebtn);
							break;
						} else {
							downloadButton.setText("Dowload");
							downloadButton
									.setBackgroundResource(R.drawable.orangebtn);
						}
					}
				}
			}

			myDocument.setText(values.getFileName());
			downloadState.setText(values.getFileSizewithUnits() + " "
					+ values.getTimeCreatedmodified());

			if (position % 2 != 0)
				rowView.setBackgroundResource(R.drawable.listview_item_differentiate_color); // for
																								// differentiate
																								// listview
																								// items

			downloadButton.setClickable(true);

			downloadButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					String mimeType = null;
					if (downloadButton.getText().equals("Download")) {
						downloadStatusLayout.setVisibility(View.VISIBLE);
						downloadButton.setEnabled(false);
						downloadButton
								.setBackgroundResource(R.drawable.greenbtn);
						new FileDownloadTask(CourseDocumentView.this).execute(
								values.getFileURL(), user.getToken(),
								values.getFileName(), courseName,
								downloadButton.getId(), loadingText.getId(),
								values.getFileSize(), this);
					} else {
						int i = values.getFileName().lastIndexOf('.');
						if (i > 0) {
							mimeType = values.getFileName().substring(i + 1);
						}
						String fileUrl = android.os.Environment
								.getExternalStorageDirectory().getPath()
								+ "/Moodle/"
								+ courseName
								+ "/"
								+ values.getFileName();
						File fileToBeOpened = new File(fileUrl);
						nextPage = new Intent(
								android.content.Intent.ACTION_VIEW);
						nextPage.setDataAndType(Uri.fromFile(fileToBeOpened),
								"application/" + mimeType);
						nextPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						try {
							startActivity(nextPage);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(
									getBaseContext(),
									"No application found to open file type "
											+ mimeType, Toast.LENGTH_LONG)
									.show();
						}
					}

				}
			});
			return rowView;
		}
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
