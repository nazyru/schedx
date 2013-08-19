
package com.nazir.schedx.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import com.nazir.schedx.model.*;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.util.DateTimeHelper;

// Referenced classes of package com.nazir.schedx.ui:
//            MyCustomListFragment

public class LectureDetailFragment extends MyCustomListFragment
{
    private TextView classRepView;
    private TextView courseCodeView;
    private TextView courseTitleView;
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
        return layoutinflater.inflate(0x7f03001e, null, false);
    }

    public void onPause()
    {
        super.onPause();
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction().detach(this).commit();
    }

    public void onStart()
    {
        super.onStart();
        courseCodeView = (TextView)getSherlockActivity().findViewById(0x7f050050);
        courseTitleView = (TextView)getSherlockActivity().findViewById(0x7f050051);
        startTimeView = (TextView)getSherlockActivity().findViewById(0x7f050053);
        endTimeView = (TextView)getSherlockActivity().findViewById(0x7f050054);
        venueView = (TextView)getSherlockActivity().findViewById(0x7f050055);
        statusView = (TextView)getSherlockActivity().findViewById(0x7f050056);
        classRepView = (TextView)getSherlockActivity().findViewById(0x7f050057);
        dayView = (TextView)getSherlockActivity().findViewById(0x7f050052);
        LecturesHelper lectureshelper = new LecturesHelper(getSherlockActivity());
        Lecture lecture = lectureshelper.getLectureSchedlule(getArguments().getInt("_id"));
        courseCodeView.setText(lecture.getCourse().getCourseCode());
        courseTitleView.setText(lecture.getCourse().getCourseTitle());
        startTimeView.setText(DateTimeHelper.getTimeToString(lecture.getStartTime()));
        endTimeView.setText(DateTimeHelper.getTimeToString(lecture.getEndTime()));
        venueView.setText(lecture.getVenue());
        
        if(lecture.getStatus() != null)
            statusView.setText(lecture.getStatus().getDescription());
        if(lecture.getClassRep() != null)
            classRepView.setText(lecture.getClassRep().getName());
        
        dayView.setText(lecture.getDay());
        
        lectureshelper.disconnect();
    }

    
}
