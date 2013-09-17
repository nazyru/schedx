package com.nazir.schedx.util;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class MyDatePickerDialog extends DialogFragment implements OnDateSetListener{
	private OnDateChangeCallback callback;
	private Calendar initDate = Calendar.getInstance();
	public static String INIT_DATE_BUNDLE = "com.nazir.schedx.util.INT_DATE_BUNDLE";
	
	public interface OnDateChangeCallback{
		void onDateChanged(Calendar newDate);
	}
	public MyDatePickerDialog(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		long dateMillis = getArguments().getLong(INIT_DATE_BUNDLE);
		initDate.setTimeInMillis(dateMillis);
	}

	@Override
	public void onAttach(Activity activity) {
		//check to make sure the parent activity has implemented our callback
		try{
			callback = (OnDateChangeCallback) activity;
		}catch(ClassCastException exception){
			throw new ClassCastException("Activity: "+ getActivity().toString() +
					"Does not implement OnDateChangedCallback");
		}
		super.onAttach(activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		
		int year = initDate.get(Calendar.YEAR);
		int month = initDate.get(Calendar.MONTH);
		int day = initDate.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, 
				year, month, day);
		
		return dialog;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		initDate.set(Calendar.YEAR, year);
		initDate.set(Calendar.MONTH, monthOfYear);
		initDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		callback.onDateChanged(initDate);
	}

}
