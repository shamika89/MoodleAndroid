package com.example.testmoodle.util;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

public class SiteInfo implements Parcelable {
	private String siteName;
	private String username;
	private String fullName;
	private String firstName;
	private String lastName;
	private String lang;
	private int userID;
	private String siteURL;
	private String userPictureURL;
	private boolean downloadFiles;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userame) {
		this.username = userame;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getSiteURL() {
		return siteURL;
	}

	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	public String getUserPictureURL() {
		return userPictureURL;
	}

	public void setUserPictureURL(String userPictureURL) {
		this.userPictureURL = userPictureURL;
	}

	public boolean isDownloadFiles() {
		return downloadFiles;
	}

	public void setDownloadFiles(boolean downloadFiles) {
		this.downloadFiles = downloadFiles;
	}

	public void populateSiteInfo(JSONObject jsonObject) {

		try {

			if (jsonObject != null) {
				// extract details from JSon object and setting values of
				// attributes
				String sitename = jsonObject.getString("sitename");
				this.setSiteName(sitename);
				String username = jsonObject.getString("username");
				this.setUsername(username);
				String firstname = jsonObject.getString("firstname");
				this.setFirstName(firstname);
				String lastname = jsonObject.getString("lastname");
				this.setLastName(lastname);
				String fullname = jsonObject.getString("fullname");
				this.setFullName(fullname);
				String userid = jsonObject.getString("userid");
				this.setUserID(Integer.valueOf(userid));
				String siteurl = jsonObject.getString("siteurl");
				this.setSiteURL(siteurl);
				String userpictureurl = jsonObject.getString("userpictureurl");
				this.setUserPictureURL(userpictureurl);
				String downloadfiles = jsonObject.getString("downloadfiles");
				this.setDownloadFiles((downloadfiles.equals("1")) ? Boolean.TRUE
						: Boolean.FALSE);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int describeContents() {
		return 0;
	}

	// This is used to regenerate the object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<SiteInfo> CREATOR = new Parcelable.Creator<SiteInfo>() {
		public SiteInfo createFromParcel(Parcel in) {
			return new SiteInfo(in);
		}

		public SiteInfo[] newArray(int size) {
			return new SiteInfo[size];
		}
	};

	// write the object's data to the passed-in Parcel
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(siteName);
		dest.writeString(username);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(fullName);
		dest.writeInt(userID);
		dest.writeString(siteURL);
		dest.writeString(userPictureURL);
		dest.writeByte((byte) (downloadFiles ? 1 : 0));

	}

	private SiteInfo(Parcel in) {
		this.siteName = in.readString();
		this.username = in.readString();
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.fullName = in.readString();
		this.userID = in.readInt();
		this.siteURL = in.readString();
		this.userPictureURL = in.readString();
		this.downloadFiles = in.readByte() == 1;

	}

	public SiteInfo() {
		// TODO Auto-generated constructor stub
	}
}