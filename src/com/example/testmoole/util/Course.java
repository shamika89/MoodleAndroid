package com.example.testmoole.util;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Course implements Parcelable{
	private int id;
	private String shortName;
	private String fulltName;
	private int enrolledUserCount;
	private String idNumber;
	private int visible;
	private ArrayList<CourseContent> courseContents=new ArrayList<CourseContent>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getFulltName() {
		return fulltName;
	}
	public void setFulltName(String fulltName) {
		this.fulltName = fulltName;
	}
	public int getEnrolledUserCount() {
		return enrolledUserCount;
	}
	public void setEnrolledUserCount(int enrolledUserCount) {
		this.enrolledUserCount = enrolledUserCount;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public ArrayList<CourseContent> getCourseContents() {
		return courseContents;
	}
	public void setCourseContents(ArrayList<CourseContent> courseContents) {
		this.courseContents = courseContents;
	}
	
	  public void populateCourse(JSONObject jsonObject) {    
	    	 
	    	try {  
	    		if (jsonObject != null) {
	    			
		    		String id = jsonObject.getString("id"); 
		    		this.setId(Integer.valueOf(id));
			        String shortname = jsonObject.optString("shortname"); 
			        if (shortname != null && shortname.trim().length() > 0)
			        	this.setShortName(shortname);
			        String fullname = jsonObject.optString("fullname");  
			        if (fullname != null && fullname.trim().length() > 0)
			        	this.setFulltName(fullname);
			        String enrolledusercount = jsonObject.optString("enrolledusercount");  
			        if (enrolledusercount != null && enrolledusercount.trim().length() > 0)
			        	this.setEnrolledUserCount(Integer.valueOf(enrolledusercount));
			        String idnumber = jsonObject.optString("idnumber"); 
			        if (idnumber != null && idnumber.trim().length() > 0) 
			        	this.setIdNumber(idnumber);
			        String visible = jsonObject.optString("visible");  
			        if (visible != null && visible.trim().length() > 0)
			        	this.setVisible(Integer.valueOf(visible));
			        
			        Log.d("LoggingTracker", shortname);
			        
			        Log.d("LoggingTracker", fullname);
			        
	    		}
	    	} catch (JSONException e) { 
	    	    e.printStackTrace(); 
	    	}
	  }
	  
	  public int describeContents() { 
	        return 0; 
	    } 
	    
	 // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods 
	    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() { 
	        public Course createFromParcel(Parcel in) { 
	            return new Course(in); 
	        } 
	 
	        public Course[] newArray(int size) { 
	            return new Course[size]; 
	        } 
	    }; 
	 
	    // write your object's data to the passed-in Parcel 
	    public void writeToParcel(Parcel dest, int flags) { 
	    	dest.writeInt(id);
	    	dest.writeString(shortName);
	    	dest.writeString(fulltName);
	    	dest.writeInt(enrolledUserCount);
	    	dest.writeString(idNumber);
	    	dest.writeInt(visible);
	    	dest.writeTypedList(courseContents); 
	    }
	    
	    private Course(Parcel in) { 
	        this.id = in.readInt();
	        this.shortName = in.readString();
	        this.fulltName = in.readString();
	        this.enrolledUserCount = in.readInt();
	        this.idNumber = in.readString();
	        this.visible = in.readInt();
	        in.readTypedList(this.courseContents, CourseContent.CREATOR); 
	    }
		public Course() {
			// TODO Auto-generated constructor stub
		}
	    
	}


