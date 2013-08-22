package com.nazir.schedx.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import com.nazir.schedx.R;
import com.nazir.schedx.model.*;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.util.DateTimeHelper;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;

public class LectureDetailFragment extends MyCustomListFragment
{
    private TextView classRepView;
    private TextView courseCodeView;
    private TextView dayView;
    private TextView endTimeView;
    private TextView startTimeView;
    private TextView statusView;
    private TextView venueView;
    
    public LectureDetailFragment()
    {
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.lecture_detail_layout, null, false);
    }

    public void onPause()
    {
        super.onPause();
        
    }

    public void onStart()
    {
        super.onStart();
        courseCodeView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_course_code);
        startTimeView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_start_time);
        endTimeView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_end_time);
        venueView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_venue);
        statusView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_status);
        classRepView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_class_rep);
        dayView = (TextView)getSherlockActivity().findViewById(R.id.lecture_detail_day);
        
        LecturesHelper lectureshelper = new LecturesHelper(getSherlockActivity());
        Lecture lecture = lectureshelper.getLectureSchedlule(getArguments().getInt(_ID));
        
        courseCodeView.setText(lecture.getCourse().getCourseCode());
        startTimeView.setText(DateTimeHelper.getTimeToString(lecture.getStartTime()));
        endTimeView.setText(DateTimeHelper.getTimeToString(lecture.getEndTime()));
        venueView.setText(lecture.getVenue());
        dayView.setText(lecture.getDay());
        
        if(lecture.getStatus() != null)
            statusView.setText(lecture.getStatus().getDescription());
        
        classRepView.setText(lecture.getClassRep() != null ? lecture.getClassRep().getName(): "Not Assigned");
        
        lectureshelper.disconnect();
    }

    
}
