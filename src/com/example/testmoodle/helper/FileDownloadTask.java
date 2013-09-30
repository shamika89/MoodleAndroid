package com.example.testmoodle.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


import org.apache.http.util.ByteArrayBuffer;

import com.example.testmoodle.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class FileDownloadTask extends AsyncTask<Object, Integer, File> {
	private final String PATH = Environment.getExternalStorageDirectory().getPath();  //put the downloaded file here
	
	private int  fileLength;
	private String fileSizeString;
	private Context context;
	private Activity activity;
	private Integer downloadStatusButtonID;
	private Integer downloadStatusTextViewID;
	

	public FileDownloadTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected File doInBackground(Object... params) {
		File file = null;
		
		//TODO make Moodle independent
		String fileURL = (String) params[0] + "&token=" + params[1];
		String fileName = (String) params[2];
		String directory = (String) params[3];
		downloadStatusButtonID= (Integer) params[4];
		downloadStatusTextViewID=(Integer)params[5];
		
		fileLength= (Integer) params[6];
		//context=(Context) params[7];
		//Log.d("Download", String.valueOf(fileLength));
			
		try {
			URL url = new URL(fileURL); 
			
			boolean mExternalStorageAvailable = false;
        	boolean mExternalStorageWriteable = false;

        	if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        	    mExternalStorageAvailable = mExternalStorageWriteable = true;
        	else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
        	    mExternalStorageAvailable = true;
        	    mExternalStorageWriteable = false;
        	} else
        	    mExternalStorageAvailable = mExternalStorageWriteable = false;

        	
        	if (mExternalStorageAvailable || mExternalStorageWriteable) {
			
				// create a File object for the parent directory 
        		//TODO make moodle independent
				File fileDirectory = new File(PATH + "/Moodle/" + directory); 
				// have the object build the directory structure, if needed. 
				fileDirectory.mkdirs(); 
				// create a File object for the output file 
				file = new File(fileDirectory, fileName); 
				
				long startTime = System.currentTimeMillis();			
				Log.d("Download", "download begining");
				Log.d("Download", "download url:" + url);
				Log.d("Download", "downloaded file name:" + fileName);
				
				/* Open a connection to that URL. */
				URLConnection ucon = url.openConnection();
				
				/*
				 * Define InputStreams to read from the URLConnection.
				 */
				InputStream is = ucon.getInputStream();						
				BufferedInputStream bis = new BufferedInputStream(is);
				
				/*
				 * Read bytes to the Buffer until there is nothing more to read(-1).
				 */
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				long total = 0;
				while ((current = bis.read()) != -1) {
						total += current;
						Log.d("Total", String.valueOf(total));
						publishProgress((int) (total * 100 / fileLength),
								fileLength / 1024);
						baf.append((byte) current);
				}
				
				/* Convert the Bytes read to a String. */
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
				
				Log.d("Download", "download ready in"
								+ ((System.currentTimeMillis() - startTime) / 1000)
								+ " sec");	
        	}
			
		} catch (IOException e) {
				e.printStackTrace();
		}
		return file;
	}
	

	

	@Override
	protected void onProgressUpdate( Integer... progress) {
		super.onProgressUpdate(progress);
		if (progress[1] > 1024) {
			fileSizeString = (progress[1] / 1024) + " MB";
		} else {
			fileSizeString = (progress[1]) + " KB";
		}

		TextView downloadStatusTextView = (TextView)activity.findViewById(downloadStatusTextViewID) ;
		Button fileDownloadButton = (Button) activity.findViewById(downloadStatusButtonID);
		if (downloadStatusTextView != null) {
			downloadStatusTextView.setText(fileSizeString + ", "
					+ progress[0] + "% downloaded");
			fileDownloadButton.setText("Downloading..");
			fileDownloadButton.setBackgroundResource(R.drawable.greenbtn);
		}
	}



	
	
	
	@Override
	public void onPostExecute(File file) {
		if(file != null) {
			Log.d("Download", "File downloaded: " + file.getAbsolutePath());
			TextView downloadStatusTextView = (TextView)activity.findViewById(downloadStatusTextViewID) ;
			Button fileDownloadButton = (Button) activity.findViewById(downloadStatusButtonID);
			if (downloadStatusTextView != null) {
				downloadStatusTextView.setText(fileSizeString
						+ ", download complete");
				fileDownloadButton.setText("Open");
				fileDownloadButton.setBackgroundResource(R.drawable.bluebtn);
				
			}
			
		} else
			Log.d("Download", "File not downloaded, set to NULL");
		   // Toast.makeText(context, "There is no SD Card installed to save the file to. Please insert to view the file.", Toast.LENGTH_LONG).show();
	}

}
