package com.nazir.schedx.util;

import com.nazir.schedx.R;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.persist.MySqliteOpenHelper;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.*;
import android.widget.TextView;

public class MyCustomCursorAdapter extends SimpleCursorAdapter
{
    private TextView courseCode;
    private TextView endTime;
    private TextView startingTime;
    private TextView venue;
    
    @SuppressWarnings("deprecation")
	public MyCustomCursorAdapter(Context context, Cursor cursor)
    {
        super(context, R.layout.custom_adapter_layout, cursor, 
        		new String[]{COURSE_ID, START_TIME, END_TIME, VENUE}, new int[]{
        				R.id.custom_adapter_course_code_view, R.id.custom_adapter_starting_time_view,
        	            R.id.custom_adapter_end_time_view, R.id.custom_adapter_venue_view });
    }

    public void bindView(View view, Context context, Cursor cursor1)
    {
        super.bindView(view, context, cursor1);
        if(view != null)
        {
        	int courseId = cursor1.getInt(cursor1.getColumnIndex(MySqliteOpenHelper.Lectures.COURSE_ID));
        	CoursesHelper helper = new CoursesHelper(context);
        	Course course = helper.getCourse(courseId);
        	helper.disconnect();
        	
            courseCode = (TextView)view.findViewById(R.id.custom_adapter_course_code_view);
            startingTime = (TextView)view.findViewById(R.id.custom_adapter_starting_time_view);
            endTime = (TextView)view.findViewById(R.id.custom_adapter_end_time_view);
            venue = (TextView)view.findViewById(R.id.custom_adapter_venue_view);
            
            courseCode.setText(course.getCourseCode());
            startingTime.setText(DateTimeHelper.getTimeToString(cursor1.getLong(cursor1.getColumnIndex(START_TIME))));
            endTime.setText(DateTimeHelper.getTimeToString(cursor1.getLong(cursor1.getColumnIndex(END_TIME))));
            venue.setText(cursor1.getString(cursor1.getColumnIndex(VENUE)));
            
            
        }
    }

    public View newView(Context context1, Cursor cursor1, ViewGroup viewgroup)
    {
        super.newView(context1, cursor1, viewgroup);
        return LayoutInflater.from(context1).inflate(R.layout.custom_adapter_layout, null, false);
    }
    
}
