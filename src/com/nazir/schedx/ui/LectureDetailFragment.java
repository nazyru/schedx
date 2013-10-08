package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.nazir.schedx.R;
import com.nazir.schedx.model.*;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.PreferenceHelper;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;

public class LectureDetailFragment extends MyCustomFragment
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
        final Lecture lecture = lectureshelper.getLectureSchedlule(getArguments().getInt(_ID));
        
        boolean flag = getArguments().getBoolean(LectureDetailActivity.LECTURE_NOTIF_FLAG);
        StringBuilder extraMessage = new StringBuilder();
        
        
        if(flag){
        	switch(lecture.getAlarmTrigger()){
			case EXACT:
				extraMessage.append(getResources().getQuantityString(R.plurals.lecture_extra_message, 1));
				break;
			case FIFTEEN:
				extraMessage.append(getResources().getQuantityString(R.plurals.lecture_extra_message, 2, 15, "Minutes"));
				break;
			case FIVE:
				extraMessage.append(getResources().getQuantityString(R.plurals.lecture_extra_message, 2, 5, "Minutes"));
				break;
			case TEN:
				extraMessage.append(getResources().getQuantityString(R.plurals.lecture_extra_message, 2, 10, "Minutes"));
				break;
			case THIRTY:
				extraMessage.append(getResources().getQuantityString(R.plurals.lecture_extra_message, 2, 30, "Minutes"));
				break;
			case TWENTY:
				extraMessage.append(getResources().getQuantityString(R.plurals.lecture_extra_message, 2, 20, "Minutes"));
				break;
			default:
				break;
        	}
        }
        
        courseCodeView.setText(lecture.getCourse().getCourseCode());
        startTimeView.setText(DateTimeHelper.getTimeToString(lecture.getStartTime())  + " " + extraMessage.toString());
        endTimeView.setText(DateTimeHelper.getTimeToString(lecture.getEndTime()));
        venueView.setText(lecture.getVenue());
        dayView.setText(lecture.getDay());
        
        if(lecture.getStatus() != null)
            statusView.setText(lecture.getStatus().toString());
        
        classRepView.setText(lecture.getClassRep() != null ? lecture.getClassRep().getName(): "Not Assigned");
        classRepView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(lecture.getClassRep() == null){
					Intent intent = new Intent(getSherlockActivity(), ClassRepActivity.class);
					intent.putExtra(_ID, lecture.getId());
					startActivity(intent);
					return;
				}
				showPopup(lecture);
				
			}
		});
        lectureshelper.disconnect();
    }

	protected void showPopup(final Lecture lecture) {
		View view = getSherlockActivity().getLayoutInflater().inflate(R.layout.class_rep_contextual_popup_layout, null, false);
		ImageView callView = (ImageView) view.findViewById(R.id.call_id);
		ImageView emailView = (ImageView) view.findViewById(R.id.email_class_rep);
		
		final AlertDialog dialog = new AlertDialog.Builder(getSherlockActivity())
		.setView(view).show();
		
		callView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+ lecture.getClassRep().getPhoneNumber()));
				startActivity(intent);
				dialog.dismiss();
			}
		});
		emailView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("mailto:" + lecture.getClassRep().getEmailAddress()));
				boolean isStudentMode = new PreferenceHelper(getSherlockActivity()).isStudentMode();
				intent.putExtra("subject", isStudentMode ? "Lecture Status":"Lecture would Hold");
				intent.putExtra("body", isStudentMode? "Hey! there, Is the Lecturer coming?":"I am on my way please");
				startActivity(intent);
				dialog.dismiss();
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
		super.onCreateOptionsMenu(menu, menuinflater);
		
		menu.removeItem(R.id.action_item_new);
	}
	  
}
