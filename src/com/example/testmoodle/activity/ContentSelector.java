package com.example.testmoodle.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import android.R.string;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.testmoodle.R;

import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.User;

public class ContentSelector extends Activity implements OnClickListener, OnItemClickListener{
	private User user;
	private ListView contentlist;
	private Button overviewButton, courseButton, logoutButton, backButton;
	private Intent nextPage;
	private EditText selectedCorse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_selector);
		
		contentlist= (ListView) findViewById(R.id.myContets);
		selectedCorse=(EditText) findViewById(R.id.selectedCourse);
		overviewButton=(Button) findViewById(R.id.coursework_overview);
		courseButton=(Button) findViewById(R.id.select_course);
		logoutButton=(Button) findViewById(R.id.logout);
		backButton=(Button) findViewById(R.id.back);
		
		Intent i = getIntent();
        user = (User) i.getParcelableExtra("userObject"); 
        
        Course course= user.getCourse(user.getSelectedCourseID());
        selectedCorse.setText(course.getFulltName());
        listContents();
        courseButton.setOnClickListener(this);
        overviewButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void listContents(){
			String[] items=getResources().getStringArray(R.array.contents);
			Integer[] imageitems= {R.drawable.document, R.drawable.assignment, R.drawable.grades};
		 
		 
		  MyAdapter adapter1= new MyAdapter(this, items, imageitems);
		  contentlist.setAdapter(adapter1);
	
		  contentlist.setOnItemClickListener(this);
		 /*contentlist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent i = new Intent(parent.getContext(), CourseDocumentView.class);
					
					i.putExtra("userObject", user); 
					startActivity(i);
								
					/*HashMap<String, String> selectedMap = courseDetailList.get(position);	
					String value = selectedMap.get(LazyAdapter.KEY_ID);
					int selectedId = Integer.valueOf(value);	
					
					switch(selectedId)
					{
						case 0: //DOCUMENTS
							nextPage = new Intent(parent.getContext(), CourseContentView.class);
							nextPage.putExtra("userObject", user);		
							startActivity(nextPage);
							break;
						case 1: //ASSIGNMENTS
							nextPage = new Intent(parent.getContext(), CourseAssignmentView.class);
							nextPage.putExtra("userObject", user);		
							startActivity(nextPage);
							break;
						case 2: //GRADES
							nextPage = new Intent(parent.getContext(), CourseGradeView.class);
							nextPage.putExtra("userObject", user);		
							startActivity(nextPage);
							break;
						case 3: //FORUMS
							nextPage = new Intent(parent.getContext(), CourseForumView.class);
							nextPage.putExtra("userObject", user);		
							startActivity(nextPage);
							break;
						case 4: //OFFLINE FILES
							nextPage = new Intent(parent.getContext(), Database.class);
							nextPage.putExtra("userObject", user);		
							startActivity(nextPage);
							break;
							default:	
					}
				}
			});	*/
		  
		
	}
	
	private class MyAdapter extends BaseAdapter {
	      private final Context context;
	      private final String[] items;
	      private final Integer[] imageItems;

	      public MyAdapter(Context context, String[] items, Integer[] imageitems) {
	        this.context = context;
	        this.items = items;
	        this.imageItems=imageitems;
	      }

	    public int getCount() {
	        return items.length;
	    }

	    public Object getItem(int position) {
	        return items[position];
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	     

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String value = items[position];
			Log.d("LoggingTracker", value);
			LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView;

	        if (convertView == null) { 
	            rowView = new View(context);
	            rowView = inflater.inflate(R.layout.contentslector_listview_layout, parent, false);
	            
	        } else {
	            rowView = (View) convertView;
	        }
	    
	      final TextView tv = (TextView)rowView.findViewById(R.id.cotentTitleTextview);
	      ImageView iv= (ImageView) rowView.findViewById(R.id.contentImageview);
	      tv.setText(items[position]);
		  iv.setImageResource(imageItems[position]);
		  
	     /*  if (position % 2 != 0)
				tv.setBackgroundResource(R.drawable.listview_item_differentiate_color);
	       else
	    	   tv.setBackgroundResource(R.drawable.listviewitem);

			tv.setClickable(true);
			
			
			tv.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if(value.equalsIgnoreCase("Document")){

						Intent i = new Intent(context, CourseDocumentView.class);
						
						i.putExtra("userObject", user); 
						startActivity(i);	
					}
					else if(tv.getText().toString().equalsIgnoreCase("Assignment")){

						Intent i = new Intent(context, CourseDetailsActivity.class);
						
						i.putExtra("userObject", user); 
						startActivity(i);	
					}

				}
			});*/
				
				/*	Intent i = new Intent(context, ContentSelectorActivity.class);
					user.setSelectedCourseID(id);
					i.putExtra("userObject", user); 
					startActivity(i);*/
          
			return rowView;
				}
		
				
}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {		
		case R.id.coursework_overview:
			nextPage = new Intent(this, ContentSelector.class);
			nextPage.putExtra("userObject", user);		
			startActivity(nextPage);
			break;	
		
		case R.id.select_course:
			nextPage = new Intent(this, CourseDetailsActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		case R.id.back:
			nextPage = new Intent(this, CourseDetailsActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		case R.id.logout:
			nextPage = new Intent(this, MainActivity.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
			default:
			
	}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
		Intent i = new Intent(parent.getContext(), CourseDocumentView.class);
		i.putExtra("userObject", user); 
		startActivity(i);
		view.setBackgroundResource(R.drawable.listviewitem);
			
			break;

		default:
			break;
		}
		
		
	}

}
