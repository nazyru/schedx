package com.nazir.schedx.util;

import com.nazir.schedx.R;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.persist.CoursesHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;

public class AssessmentCursorAdapter extends SimpleCursorAdapter{
private TextView courseCodeView;
private TextView assessmentTypeView;

	public AssessmentCursorAdapter(Context context, Cursor c) {
		super(context, R.layout.lecture_list_layout, c, new String[] {TYPE}, 
				new int[] {R.id.venue_item_text_view});
		
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		if(view != null){
			
			int courseId = cursor.getInt(cursor.getColumnIndex(COURSE_ID));
			Course course = new CoursesHelper(context).getCourse(courseId);
			
			courseCodeView = (TextView) view.findViewById(R.id.course_item_text_view);
			assessmentTypeView = (TextView) view.findViewById(R.id.venue_item_text_view);
			
			courseCodeView.setText(course.getCourseCode());
			assessmentTypeView.setText(cursor.getString(cursor.getColumnIndex(TYPE)));
			
		}
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		 super.newView(context, cursor, parent);
		 return LayoutInflater.from(context).inflate(R.layout.lecture_list_layout, null, false);
	}

}
