package com.example.testmoodle.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
		Integer[] imageitems= {R.drawable.document, R.drawable.assignment, R.drawable.forums};
		 
		MyAdapter adapter1= new MyAdapter(this, items, imageitems);
		contentlist.setItemsCanFocus(false);
		contentlist.setOnItemClickListener(this);
		contentlist.setAdapter(adapter1);
		
		
		
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
			final int pose=position;
			
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
		  tv.setClickable(true);
			
			tv.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					switch (pose) {
					case 0: // Documents
					Intent i = new Intent(context, CourseDocumentView.class); //transferring to CourseDocumentView activity 
					i.putExtra("userObject", user); 
					startActivity(i);
						break;
					case 1: // Assignment
						Intent intent = new Intent(context, CourseAssignnmentView.class); //transferring to CourseDocumentView activity 
						intent.putExtra("userObject", user); 
						startActivity(intent);
							break;
					case 2: // Forums
						Intent intent2 = new Intent(context, CourseForumView.class); //transferring to CourseDocumentView activity 
						intent2.putExtra("userObject", user); 
						startActivity(intent2);
							break;
					default:
						break;
					}
					

				}
			});
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
