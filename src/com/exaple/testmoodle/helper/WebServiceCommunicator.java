package com.exaple.testmoodle.helper;

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

import com.example.testmoodle.MainActivity;
import com.example.testmoole.util.Course;
import com.example.testmoole.util.SiteInfo;
import com.example.testmoole.util.User;

import android.R.xml;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.webkit.WebBackForwardList;
import android.widget.Toast;

public class WebServiceCommunicator extends AsyncTask<Object, Object, JSONObject>{
	private String function;
	private Object extra; 
	private String url;
	private Context context;
	private User user;
	private MainActivity activity;
	public WebServiceCommunicator(Context context, MainActivity activity){
		this.context=context;
		this.activity=activity;
	}
	

	/*public WebServiceCommunicator(MainActivity activity) {
		this.activity = activity;
     }*/
	
	@Override
	protected JSONObject doInBackground(Object... params) {
		url=(String) params[0];
		function = (String) params[1];
		String urlParameters = (String) params[2];
		user=(User) params[3];
		int xslRawId = (Integer) params[4];
		if(params.length > 3) {
			extra = params[3];
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

			// Get Response
			InputStream is = con.getInputStream();
			
			Source xmlSource = new StreamSource(is);
			Source xsltSource = new StreamSource(context.getResources().openRawResource(xslRawId));

			TransformerFactory transFact = TransformerFactory.newInstance();
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
			Log.d("LoggingTracker", "MalFunc exception"+e.toString());
		} catch (IOException e) {
			Log.d("LoggingTracker", "Io exception"+e.toString());
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			Log.d("LoggingTracker", "TransformConfig exception"+e.toString());
			e.printStackTrace();
		} catch (TransformerException e) {
			Log.d("LoggingTracker", "Transformer exception"+e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d("LoggingTracker", "json exception"+e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("LoggingTracker", " exception"+e.toString());
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
				activity.viewCourse();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		}
		/*else if(fn.equals(WebServiceFunction.core_course_get_contents))
			Session.getCourseManager().setCourseDetails(jsonObject, (Integer)extra);
		else
			Log.d(Constants.LOG_WSR, "Some unknown request was executed, please specify where to send the result to");*/
	}
}


