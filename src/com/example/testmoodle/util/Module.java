package com.example.testmoodle.util;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Module implements Parcelable {

	public Module() {

	}

	private int id;
	private String url;
	private String name;
	private String description;
	private int visible;
	private String modicon;
	private String modname;
	private String modplural;
	private int availableFrom;
	private int availableUntil;
	private int indent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getModicon() {
		return modicon;
	}

	public void setModicon(String modicon) {
		this.modicon = modicon;
	}

	public String getModname() {
		return modname;
	}

	public void setModname(String modname) {
		this.modname = modname;
	}

	public String getModplural() {
		return modplural;
	}

	public void setModplural(String modplural) {
		this.modplural = modplural;
	}

	public int getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(int availableFrom) {
		this.availableFrom = availableFrom;
	}

	public int getAvailableUntil() {
		return availableUntil;
	}

	public void setAvailableUntil(int availableUntil) {
		this.availableUntil = availableUntil;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	private ArrayList<Document> files = new ArrayList<Document>();

	public ArrayList<Document> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<Document> files) {
		this.files = files;
	}

	public void populateModule(JSONObject jsonObject) {

		try {
			if (jsonObject != null) {

				String id = jsonObject.getString("id");
				this.setId(Integer.valueOf(id));
				String url = jsonObject.optString("url");
				if (url != null && url.trim().length() > 0)
					this.setUrl(url);
				Log.d("URL", url);
				String name = jsonObject.optString("name");
				if (name != null && name.trim().length() > 0)
					this.setName(name);
				String description = jsonObject.optString("description");
				if (description != null && description.trim().length() > 0)
					this.setDescription(description);
				String visible = jsonObject.optString("visible");
				if (visible != null && visible.trim().length() > 0)
					this.setVisible(Integer.valueOf(visible));
				String modicon = jsonObject.optString("modicon");
				if (modicon != null && modicon.trim().length() > 0)
					this.setModicon(modicon);
				String modname = jsonObject.optString("modname");
				if (modname != null && modname.trim().length() > 0)
					this.setModname(modname);
				String modplural = jsonObject.optString("modplural");
				if (modplural != null && modplural.trim().length() > 0)
					this.setModplural(modplural);
				String availablefrom = jsonObject.optString("availablefrom");
				if (availablefrom != null && availablefrom.trim().length() > 0)
					this.setAvailableFrom(Integer.valueOf(availablefrom));
				String availableuntil = jsonObject.optString("availableuntil");
				if (availableuntil != null
						&& availableuntil.trim().length() > 0)
					this.setAvailableUntil(Integer.valueOf(availableuntil));
				String indent = jsonObject.optString("indent");
				if (indent != null && indent.trim().length() > 0)
					this.setIndent(Integer.valueOf(indent));

				JSONArray contents = jsonObject.getJSONArray("contents");
				ArrayList<Document> contentsArray = new ArrayList<Document>();
				// looping through all Contents
				for (int i = 0; i < contents.length(); i++) {
					JSONObject c = contents.getJSONObject(i);
					Document content = new Document();
					content.populateFile(c);
					contentsArray.add(content);
				}

				if (contentsArray.size() > 0) {
					this.setFiles(contentsArray);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static final Parcelable.Creator<Module> CREATOR = new Parcelable.Creator<Module>() {
		public Module createFromParcel(Parcel in) {
			return new Module(in);
		}

		public Module[] newArray(int size) {
			return new Module[size];
		}
	};

	// write the object's data to the passed-in Parcel
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(url);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeInt(visible);
		dest.writeString(modicon);
		dest.writeString(modname);
		dest.writeString(modplural);
		dest.writeInt(availableFrom);
		dest.writeInt(availableUntil);
		dest.writeInt(indent);
		dest.writeTypedList(files);
	}

	private Module(Parcel in) {
		this.id = in.readInt();
		this.url = in.readString();
		this.name = in.readString();
		this.description = in.readString();
		this.visible = in.readInt();
		this.modicon = in.readString();
		this.modname = in.readString();
		this.modplural = in.readString();
		this.availableFrom = in.readInt();
		this.availableUntil = in.readInt();
		this.indent = in.readInt();
		in.readTypedList(this.files, Document.CREATOR);
	}

	@Override
	public int describeContents() {

		return 0;
	}

}
