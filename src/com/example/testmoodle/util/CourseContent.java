package com.example.testmoodle.util;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

public class CourseContent implements Parcelable {

	public CourseContent() {
	}

	private int id;
	private String name;
	private String summery;
	private int visible;
	private int summeryFormat;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummery() {
		return summery;
	}

	public void setSummery(String summery) {
		this.summery = summery;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getSummeryFormat() {
		return summeryFormat;
	}

	public void setSummeryFormat(int summeryFormat) {
		this.summeryFormat = summeryFormat;
	}

	private ArrayList<Module> modules = new ArrayList<Module>();

	public ArrayList<Module> getModules() {
		return modules;
	}

	public void setModules(ArrayList<Module> modules) {
		this.modules = modules;
	}

	public void populateCourseContent(JSONObject jsonObject) {

		try {
			if (jsonObject != null) {

				String id = jsonObject.getString("id");
				this.setId(Integer.valueOf(id));
				String name = jsonObject.optString("name");
				if (name != null && name.trim().length() > 0)
					this.setName(name);
				String visible = jsonObject.optString("visible");
				if (visible != null && visible.trim().length() > 0)
					this.setVisible(Integer.valueOf(visible));
				String summary = jsonObject.optString("summary");
				if (summary != null && summary.trim().length() > 0)
					this.setSummery(summary);

				JSONArray modules = jsonObject.getJSONArray("modules");
				ArrayList<Module> modulesArray = new ArrayList<Module>();
				// looping through all Modules
				for (int i = 0; i < modules.length(); i++) {
					JSONObject c = modules.getJSONObject(i);
					Module module = new Module();
					module.populateModule(c);
					modulesArray.add(module);
				}

				if (modulesArray.size() > 0) {
					this.setModules(modulesArray);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int describeContents() {
		return 0;
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<CourseContent> CREATOR = new Parcelable.Creator<CourseContent>() {
		public CourseContent createFromParcel(Parcel in) {
			return new CourseContent(in);
		}

		public CourseContent[] newArray(int size) {
			return new CourseContent[size];
		}
	};

	// write your object's data to the passed-in Parcel
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeInt(visible);
		dest.writeString(summery);
		dest.writeTypedList(modules);
	}

	private CourseContent(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.visible = in.readInt();
		this.summery = in.readString();
		in.readTypedList(this.modules, Module.CREATOR);
	}
}
