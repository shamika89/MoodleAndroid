package com.example.testmoodle.util;

import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Document implements Parcelable {
	private String type;
	private String fileName;
	private String filePath;
	private int fileSize;
	private String fileURL;
	private String content;
	private Long timeCreated;
	private Long timeModified;
	private int sortedOrder;
	private int userID;
	private String author;
	private String license;
	private String timeCreatedmodified;
	private String fileSizewithUnits;

	public String getFileSizewithUnits() {
		return fileSizewithUnits;
	}

	public void setFileSizewithUnits(String fileSizewithUnits) {
		this.fileSizewithUnits = fileSizewithUnits;
	}

	public String getTimeCreatedmodified() {
		return timeCreatedmodified;
	}

	public void setTimeCreatedmodified(String timeCreateodified) {
		this.timeCreatedmodified = timeCreateodified;
	}

	public void setTimeCreated(Long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public void setTimeModified(Long timeModified) {
		this.timeModified = timeModified;
	}

	public Document() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public long getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(long timeModified) {
		this.timeModified = timeModified;
	}

	public int getSortedOrder() {
		return sortedOrder;
	}

	public void setSortedOrder(int sortedOrder) {
		this.sortedOrder = sortedOrder;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public void populateFile(JSONObject jsonObject) {

		if (jsonObject != null) {
			String type = jsonObject.optString("type");
			if (type != null && type.trim().length() > 0)
				this.setType(type);
			String filename = jsonObject.optString("filename");
			Log.d("Module", filename);
			if (filename != null && filename.trim().length() > 0)
				this.setFileName(filename);
			String filepath = jsonObject.optString("filepath");
			Log.d("Module", filepath);
			if (filepath != null && filepath.trim().length() > 0)
				this.setFilePath(filepath);
			String filesize = jsonObject.optString("filesize");
			if (filesize != null && filesize.trim().length() > 0)
				this.setFileSize(Integer.valueOf(filesize));
			String fileurl = jsonObject.optString("fileurl");
			if (fileurl != null && fileurl.trim().length() > 0)
				this.setFileURL(fileurl);
			String content = jsonObject.optString("content");
			if (content != null && content.trim().length() > 0)
				this.setContent(content);
			String timecreated = jsonObject.optString("timecreated");
			if (timecreated != null && timecreated.trim().length() > 0)
				this.setTimeCreated(Integer.valueOf(timecreated));
			String timemodified = jsonObject.optString("timemodified");
			if (timemodified != null && timemodified.trim().length() > 0)
				this.setTimeModified(Integer.valueOf(timemodified));
			String sortorder = jsonObject.optString("sortorder");
			if (sortorder != null && sortorder.trim().length() > 0)
				this.setSortedOrder(Integer.valueOf(sortorder));
			String userid = jsonObject.optString("userid");
			if (userid != null && userid.trim().length() > 0)
				this.setUserID(Integer.valueOf(userid));
			String author = jsonObject.optString("author");
			if (author != null && author.trim().length() > 0)
				this.setAuthor(author);
			String license = jsonObject.optString("license");
			if (license != null && license.trim().length() > 0)
				this.setLicense(license);
		}
	}

	public int describeContents() {
		return 0;
	}

	// This is used to regenerate the object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {
		public Document createFromParcel(Parcel in) {
			return new Document(in);
		}

		public Document[] newArray(int size) {
			return new Document[size];
		}
	};

	// write the object's data to the passed-in Parcel
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
		dest.writeString(fileName);
		dest.writeString(filePath);
		dest.writeInt(fileSize);
		dest.writeString(fileURL);
		dest.writeString(content);
		dest.writeLong(timeCreated);
		dest.writeLong(timeModified);
		dest.writeInt(sortedOrder);
		dest.writeInt(userID);
		dest.writeString(author);
		dest.writeString(license);
	}

	private Document(Parcel in) {
		this.type = in.readString();
		this.fileName = in.readString();
		this.filePath = in.readString();
		this.fileSize = in.readInt();
		this.fileURL = in.readString();
		this.content = in.readString();
		this.timeCreated = in.readLong();
		this.timeModified = in.readLong();
		this.sortedOrder = in.readInt();
		this.userID = in.readInt();
		this.author = in.readString();
		this.license = in.readString();
	}

}
