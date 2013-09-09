package com.example.testmoodle.activity;



import java.util.ArrayList;
import java.util.List;

import com.example.testmoodle.R;
import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;

public class CourseDetailsActivity extends Activity {
	private User user;
	private ListView courselist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courselist_activity);
		
		courselist= (ListView) findViewById(R.id.myCourses);
		Intent i = getIntent();
        user = (User) i.getParcelableExtra("userObject"); 
        
        listCourses();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void listCourses(){
		  List<Course> values = user.getCourses();
		  
		  Log.d("LoggingTracker", "ID=" +values.get(0).getIdNumber());
		 
		  MyAdapter adapter1= new MyAdapter(this, (ArrayList<Course>) values);
		  courselist.setAdapter(adapter1);
		
	}
	
	private class MyAdapter extends BaseAdapter {
	      private final Context context;
	      private final ArrayList<Course> array;

	      public MyAdapter(Context context, ArrayList<Course> array) {
	        this.context = context;
	        this.array = array;
	      }

	    public int getCount() {
	        return array.size();
	    }

	    public Object getItem(int position) {
	        return array.get(position);
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	     

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Course values = array.get(position);
			final int id=values.getId();
			LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView;

	        if (convertView == null) { 
	            rowView = new View(context);
	            rowView = inflater.inflate(R.layout.courselistview_layout, parent, false);
	            
	        } else {
	            rowView = (View) convertView;
	        }
	    
	       TextView myCourse = (TextView)rowView.findViewById(R.id.myCoursesName);
	       myCourse.setText(values.getShortName()+" "+values.getFulltName());
	      Log.d("LoggingTracker", values.getShortName()+" "+values.getFulltName());
	       if (position % 2 != 0)
				myCourse.setBackgroundResource(R.drawable.listview_item_differentiate_color);

			rowView.setClickable(true);
			
			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					final int REQUEST_CODE = id;
					Intent i = new Intent(context, ContentSelectorActivity.class);
					user.setSelectedCourseID(id);
					i.putExtra("userObject", user); 
					startActivity(i);

				}
			});
	        return rowView;
		}
}
	
}
