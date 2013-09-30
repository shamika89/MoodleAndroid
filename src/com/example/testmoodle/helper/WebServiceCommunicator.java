package com.example.testmoodle.helper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.testmoodle.activity.CourseDetailsActivity;
import com.example.testmoodle.activity.MainActivity;
import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.CourseContent;
import com.example.testmoodle.util.User;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class WebServiceCommunicator extends AsyncTask<Object, Object, JSONObject>{
	private String function;
	private int extra; 
	private String url;
	private Context context;
	private User user;
	private MainActivity activity;
	private CourseDetailsActivity detailActivity;
	public WebServiceCommunicator(Context context, MainActivity activity){
		this.context=context;
		this.activity=activity;
	}

	
	public WebServiceCommunicator(Context context,
			CourseDetailsActivity courseDetailsActivity) {
		this.context=context;
		this.detailActivity=courseDetailsActivity;
	}


	@Override
	protected JSONObject doInBackground(Object... params) {
		url=(String) params[0];
		function = (String) params[1];
		String urlParameters = (String) params[2];
		user=(User) params[3];
		int xslRawId = (Integer) params[4];
		if(params.length > 5) {
			extra = (Integer)params[5];
		}
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) new URL(url + function).openConnection();
		
			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Language", "en-US");
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDoInput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream is = con.getInputStream();                          // Get Response
			Log.d("XML Response", is.toString());
			Source xmlSource = new StreamSource(is);
			Log.d("XML Response", xmlSource.toString());
			Source xsltSource = new StreamSource(context.getResources().openRawResource(xslRawId));
			TransformerFactory transFact = TransformerFactory.newInstance();        //transform XML to JSon.
			Transformer trans = transFact.newTransformer(xsltSource);
			StringWriter writer = new StringWriter();
			trans.transform(xmlSource, new StreamResult(writer));

			String jsonstr = writer.toString();
			jsonstr = jsonstr.replace("<div class=\"no-overflow\"><p>", "");
			jsonstr = jsonstr.replace("</p></div>", "");
			jsonstr = jsonstr.replace("<p>", "");
			jsonstr = jsonstr.replace("</p>", "");
			Log.d("LoggingTracker", jsonstr);
			return new JSONObject(jsonstr);
			
		} catch (MalformedURLException e) {
			//Toast.makeText(context, "Error web service request", Toast.LENGTH_SHORT).show();
			Log.d("WebServiceCommunicator", "MalFunc exception"+e.toString());              // writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("WebServiceCommunicator", "IO exception"+e.toString());
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			Log.d("WebServiceCommunicator", "TransformConfig exception"+e.toString());
			e.printStackTrace();
		} catch (TransformerException e) {
			Log.d("WebServiceCommunicatorr", "Transformer exception"+e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d("WebServiceCommunicator", "Json exception"+e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("WebServiceCommunicator", "Exception "+e.toString());
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@Override
	protected void onPostExecute(JSONObject jsonObject) {
		if(jsonObject == null)
			return;
		
		if(function.equals(WebserviceFunction.moodle_webservice_get_siteinfo)){
			user.getSiteInfo().populateSiteInfo(jsonObject);
			try {
				activity.getCourseInfo();
			} catch (UnsupportedEncodingException e) {
			
				e.printStackTrace();
			}
		}
		else if(function.equals(WebserviceFunction.moodle_enrol_get_users_courses)){
			try {
				JSONArray courses=jsonObject.getJSONArray("courses");
				for(int i=0; i<courses.length();i++){
					Course c=new Course();
					JSONObject obj=courses.getJSONObject(i);
					c.populateCourse(obj);
					user.getCourses().add(c);
				}
				//activity.getCourseContents();
				activity.viewCourse();
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		
		}
		else if(function.equals(WebserviceFunction.core_course_get_contents)){
			try {
				JSONArray coursecontents = jsonObject.getJSONArray("coursecontents");
				  for(int i = 0; i < coursecontents.length(); i++){ 
		    	        JSONObject c = coursecontents.getJSONObject(i); 
		    	        CourseContent coursecontent = new CourseContent();
		    	        coursecontent.populateCourseContent(c);
		    	        user.getCourse(user.getSelectedCourseID()).getCourseContents().add(coursecontent);
		    	    } 			
				
					detailActivity.viewContents();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}


