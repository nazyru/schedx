package com.nazir.schedx.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Assessment;
import com.nazir.schedx.persist.AssessmentHelper;
import com.nazir.schedx.util.DateTimeHelper;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;

public class AssessmentDetailFragment extends MyCustomFragment{
	private TextView typeView;
	private TextView courseCodeView;
	private TextView timeView;
	private TextView locationView;
	private TextView memoView;

	@Override
	public void onStart() {
		super.onStart();
		
		int id = getArguments().getInt(ID);
		AssessmentHelper helper = new AssessmentHelper(getSherlockActivity());
		Assessment assessment = helper.getAssessment(id);
		helper.disconnect();
		
		typeView = (TextView) getSherlockActivity().findViewById(R.id.assessment_detail_type_view);
		courseCodeView = (TextView) getSherlockActivity().findViewById(R.id.assessment_detail_course_code_view);
		timeView = (TextView) getSherlockActivity().findViewById(R.id.assessment_detail_time_view);
		locationView = (TextView) getSherlockActivity().findViewById(R.id.assessment_detail_location);
		memoView = (TextView) getSherlockActivity().findViewById(R.id.assessment_detail_memo);
		
		boolean flag = getArguments().getBoolean(AssessmentDetailActivity.ASSESSMENT_NOTIF_FLAG);
		StringBuilder extraMessage = new StringBuilder();
		
		if(flag){
			
			switch(assessment.getTriggerMode()){
			case EXACT:
				extraMessage.append(getResources().getQuantityString(R.plurals.assessment_extra_message, 1));
				break;
			case FIFTEEN:
				extraMessage.append(getResources().getQuantityString(R.plurals.assessment_extra_message, 2, 15 ));
				break;
			case FIVE:
				extraMessage.append(getResources().getQuantityString(R.plurals.assessment_extra_message, 2, 5));
				break;
			case TEN:
				extraMessage.append(getResources().getQuantityString(R.plurals.assessment_extra_message, 2, 10));
				break;
			case TWENTY:
				extraMessage.append(getResources().getQuantityString(R.plurals.assessment_extra_message, 2, 20));
				break;
			case TWO:
				extraMessage.append(getResources().getQuantityString(R.plurals.assessment_extra_message, 2, 2));
				break;
			default:
				break;
			}
		}
		
		typeView.setText(assessment.getAssessmentType().toString());
		courseCodeView.setText(assessment.getCourse().getCourseCode());
		timeView.setText(DateTimeHelper.getDisplayableDate(assessment.getDate()) + extraMessage.toString());
		locationView.setText(assessment.getLocation());
		memoView.setText(assessment.getMemo());
		
	}

	@Override
	public View onCreateView(LayoutInflater layoutinflater,
			ViewGroup viewgroup, Bundle bundle) {
		 return layoutinflater.inflate(R.layout.assessment_detail_layout, null, false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
		super.onCreateOptionsMenu(menu, menuinflater);
		
			menu.removeItem(R.id.action_item_new);
	}
}
