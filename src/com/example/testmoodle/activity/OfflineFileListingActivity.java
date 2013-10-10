package com.example.testmoodle.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.testmoodle.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OfflineFileListingActivity extends Activity implements
		OnClickListener {

	String courseFolderName;
	private ArrayList<String> offlineFileName = new ArrayList<String>();
	private ArrayList<String> offlineFileDateModified = new ArrayList<String>();
	private ArrayList<String> offlineFileSize = new ArrayList<String>();
	private LinearLayout emptyLayout, contentLayout;
	private Button backButton;
	private Intent nextPage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_file_listing);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		courseFolderName = extras.getString("courseFolderName");

		File root = new File(android.os.Environment
				.getExternalStorageDirectory().getPath()
				+ "/Moodle/"
				+ courseFolderName + "/");
		ListFiles(root);
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(this);
	}

	public void ListFiles(File f) {

		File[] files = f.listFiles();
		offlineFileName.clear();
		offlineFileDateModified.clear();
		offlineFileSize.clear();
		if (files != null) {
			for (File file : files) {
				if (!(file.isDirectory())) {
					int startIndex;
					int endIndex;
					String fileName = file.toString();
					startIndex = fileName.lastIndexOf("/") + 1;
					endIndex = fileName.length();
					fileName = fileName.substring(startIndex, endIndex);
					File fileLocationPath = new File(f + "/" + fileName);
					if (fileName != ".android_secure" && fileName != "LOST.DIR") {
						// Getting the size of file
						long fileSize = fileLocationPath.length();
						String fileSizeInfo;
						if (fileSize > (1024 * 1024))
							fileSizeInfo = fileSize / (1024 * 1024) + "MB";
						else if (fileSize > 1024)
							fileSizeInfo = fileSize / 1024 + "KB";
						else
							fileSizeInfo = fileSize + "Bytes";

						// Last modified date of the folder...
						Date lastModDate = new Date(
								fileLocationPath.lastModified());
						SimpleDateFormat format = new SimpleDateFormat(
								"MM/dd/yyyy hh:mm a");
						String lastModDateformatted = format
								.format(lastModDate);

						offlineFileName.add(fileName);
						offlineFileDateModified.add(lastModDateformatted);
						offlineFileSize.add(fileSizeInfo);

					}
				}
				listFilesInListView(offlineFileName, offlineFileDateModified,
						offlineFileSize);
			}
		}
	}

	public void listFilesInListView(ArrayList<String> offlineFileName,
			ArrayList<String> offlineFileDateModified,
			ArrayList<String> offlineFileSize) {
		emptyLayout = (LinearLayout) findViewById(R.id.offlinne_files_empty);
		contentLayout = (LinearLayout) findViewById(R.id.offline_file_listview);
		emptyLayout.setVisibility(View.INVISIBLE);
		contentLayout.setVisibility(View.VISIBLE);

		ListView listView = (ListView) findViewById(R.id.myOfflineFiles);
		if (offlineFileName != null && offlineFileName.size() > 0) {
			MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
					offlineFileName, offlineFileDateModified, offlineFileSize);
			// Assign adapter to ListView
			listView.setAdapter(adapter);
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.INVISIBLE);
		}
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> offlineFileName;
		private final ArrayList<String> offlineFileDateModified;
		private final ArrayList<String> offlineFileSize;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> offlineFileName,
				ArrayList<String> offlineFileDateModified,
				ArrayList<String> offlineFileSize) {
			super(context, R.layout.offline_file_listing_listview,
					offlineFileName);
			this.context = context;
			this.offlineFileName = offlineFileName;
			this.offlineFileDateModified = offlineFileDateModified;
			this.offlineFileSize = offlineFileSize;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.offline_file_listing_listview, parent, false);

			if (position % 2 == 0)
				rowView.setBackgroundResource(R.drawable.listview_item_differentiate_color);

			final TextView textViewFileName = (TextView) rowView
					.findViewById(R.id.myOfflineFileName);
			textViewFileName.setText(offlineFileName.get(position));

			final TextView textViewFileDetails = (TextView) rowView
					.findViewById(R.id.myOfflineFileDetails);
			textViewFileDetails.setText(offlineFileSize.get(position) + ", "
					+ offlineFileDateModified.get(position));

			final Button fileOpenButton = (Button) rowView
					.findViewById(R.id.myOfflineFileOpenButton);

			final Button fileDeleteButton = (Button) rowView
					.findViewById(R.id.myOfflineFileDeleteButton);

			fileOpenButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fileUrl = android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/Moodle/"
							+ courseFolderName
							+ "/"
							+ textViewFileName.getText().toString();
					int startIndex = fileUrl.lastIndexOf(".") + 1;
					int endIndex = fileUrl.length();
					String fileExtension = fileUrl.substring(startIndex,
							endIndex);
					File fileToBeOpened = new File(fileUrl);
					nextPage = new Intent();
					nextPage.setAction(android.content.Intent.ACTION_VIEW);
					nextPage.setDataAndType(Uri.fromFile(fileToBeOpened),
							"application/" + fileExtension);
					try {
						startActivity(nextPage);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(
								getBaseContext(),
								"No application found to open file type\n"
										+ fileExtension, Toast.LENGTH_LONG)
								.show();
					}

				}
			});

			fileDeleteButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fileUrl = android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/Moodle/"
							+ courseFolderName
							+ "/"
							+ textViewFileName.getText().toString();
					File fileToBeDeleted = new File(fileUrl);
					boolean deleted = fileToBeDeleted.delete();
					if (deleted)
						Toast.makeText(
								getBaseContext(),
								"File " + textViewFileName.getText().toString()
										+ " deleted!", Toast.LENGTH_SHORT)
								.show();
					offlineFileName.remove(position);
					offlineFileDateModified.remove(position);
					offlineFileSize.remove(position);
					listFilesInListView(offlineFileName,
							offlineFileDateModified, offlineFileSize);

				}
			});

			return rowView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back:
			nextPage = new Intent(this, OfflieFolderListigActivity.class);
			startActivity(nextPage);
			break;

		default:

		}

	}

}
