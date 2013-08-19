package com.nazir.schedx.util;

import com.nazir.schedx.model.Course;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.persist.MySqliteOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.*;
import android.widget.TextView;

public class MyCustomCursorAdapter extends SimpleCursorAdapter
{
    private Context context;
    private TextView courseCode;
    private Cursor cursor;
    private TextView endTime;
    private String from[];
    private TextView startingTime;
    private int to[];
    private TextView venue;
    
    public MyCustomCursorAdapter(Context context, Cursor cursor, String from[], int to[])
    {
        super(context, 0x7f03001c, cursor, from, to);
        this.context = context;
        this.cursor = cursor;
        this.from = from;
        this.to = to;
    }

    public void bindView(View view, Context context1, Cursor cursor1)
    {
        super.bindView(view, context1, cursor1);
        if(view != null)
        {
        	int courseId = cursor1.getInt(cursor1.getColumnIndex(MySqliteOpenHelper.Lectures.COURSE_ID));
        	Course course = new CoursesHelper(context1).getCourse(courseId);
            courseCode = (TextView)view.findViewById(0x7f050041);
            startingTime = (TextView)view.findViewById(0x7f050042);
            endTime = (TextView)view.findViewById(0x7f050044);
            venue = (TextView)view.findViewById(0x7f050045);
            
            courseCode.setText(course.getCourseCode());
            startingTime.setText(DateTimeHelper.getTimeToString(cursor1.getLong(cursor1.getColumnIndex("start_time"))));
            endTime.setText(DateTimeHelper.getTimeToString(cursor1.getLong(cursor1.getColumnIndex("end_time"))));
            venue.setText(cursor1.getString(cursor1.getColumnIndex("venue")));
        }
    }

    public View newView(Context context1, Cursor cursor1, ViewGroup viewgroup)
    {
        super.newView(context1, cursor1, viewgroup);
        return LayoutInflater.from(context1).inflate(0x7f03001c, null, false);
    }

    
}
