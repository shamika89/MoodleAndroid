package com.example.testmoodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import com.example.testmoodle.R;
import com.example.testmoodle.util.Course;
import com.example.testmoodle.util.User;

public class ContentSelectorActivity extends Activity {
	private User user;
	private EditText editCourseName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_selector_activity);
		
		editCourseName= (EditText) findViewById(R.id.selectedCourse);
		Intent i = getIntent();
        user = (User) i.getParcelableExtra("userObject"); 
        
        Course course= user.getCourse(user.getSelectedCourseID());
        editCourseName.setText(course.getFulltName());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
