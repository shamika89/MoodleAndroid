package com.example.testmoole.util;

import java.sql.Date;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private String usename;
	private String password;
	private String token;
	//private Date tokenCreatedDate;
	private int selectedCourseID;
	private SiteInfo siteInfo;
	
	private ArrayList<Course> courses=new ArrayList<Course>();
	
	/*public Date getTokenCreatedDate() {
		return tokenCreatedDate;
	}
	public void setTokenCreatedDate(Date tokenCreatedDate) {
		this.tokenCreatedDate = tokenCreatedDate;
	}*/
	public int getSelectedCourseID() {
		return selectedCourseID;
	}
	public void setSelectedCourseID(int selectedCourseID) {
		this.selectedCourseID = selectedCourseID;
	}
	public SiteInfo getSiteInfo() {
		return siteInfo;
	}
	public void setSiteInfo(SiteInfo siteInfo) {
		this.siteInfo = siteInfo;
	}
	public ArrayList<Course> getCourses() {
		return courses;
	}
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	public String getUsename() {
		return usename;
	}
	public String getPassword() {
		return password;
	}
	public String getToken() {
		return token;
	}
	public void setUsename(String usename) {
		this.usename = usename;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public Course getCourse(int id){
		for(Course c: courses){
			if(c.getId()==id)
				return c;
		}
		return null;
	}
	
	 public int describeContents() { 
	        return 0; 
	    } 
	    
	 // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods 
	    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() { 
	        public User createFromParcel(Parcel in) { 
	            return new User(in); 
	        } 
	 
	        public User[] newArray(int size) { 
	            return new User[size]; 
	        } 
	    }; 
	 
	    // write your object's data to the passed-in Parcel 
	    public void writeToParcel(Parcel dest, int flags) { 
	    	dest.writeString(usename); 
	    	dest.writeString(password);
	    	
	    	dest.writeString(token);
	    	//dest.writeLong(tokencreatedate.getTime());
	    	dest.writeInt(selectedCourseID);
	    	dest.writeParcelable(siteInfo, flags);
	    	dest.writeTypedList(courses); 
	    }
	    
	    private User(Parcel in) { 
	        this.usename = in.readString(); 
	        this.password = in.readString();
	        this.token = in.readString();
	      //  this.tokenCreatedDate = new Date(in.readLong());
	        this.selectedCourseID = in.readInt();
	        this.siteInfo = in.readParcelable(SiteInfo.class.getClassLoader());
	        in.readTypedList(this.courses, Course.CREATOR); 
	    }
		public User() {
		
		}
	}


