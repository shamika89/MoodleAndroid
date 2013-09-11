package com.example.testmoodle.activity;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testmoodle.R;
import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.User;

public class ContentSelectorActivity extends Activity {
	private User user;
	private EditText editCourseName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_selector);
		//setListAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1,R.id.cotentTitleTextview, getResources().getStringArray(R.array.contents)));

		
		editCourseName= (EditText) findViewById(R.id.selectedCourse);
		Intent i = getIntent();
        user = (User) i.getParcelableExtra("userObject");
        
        Course course= user.getCourse(user.getSelectedCourseID());
        editCourseName.setText(course.getFulltName());
		
	}
	
	

	private class MyAdapter extends ArrayAdapter<String>{

		public MyAdapter(Context context, int resource, int textViewResourceId,
				String[] strings) {
			super(context, resource, textViewResourceId, strings);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row=inflater.inflate(R.layout.contentslector_listview_layout, parent, false);
			String[] items=getResources().getStringArray(R.array.contents);
			Integer[] imageitems= {R.drawable.document, R.drawable.assignment, R.drawable.grades};
			
			ImageView iv=(ImageView) row.findViewById(R.id.contentImageview);
			TextView tv=(TextView) row.findViewById(R.id.cotentTitleTextview);
			tv.setText(items[position]);
			iv.setImageResource(imageitems[position]);
			
			return row;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
