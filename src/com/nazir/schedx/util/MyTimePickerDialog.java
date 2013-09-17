package com.nazir.schedx.util;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyTimePickerDialog extends DialogFragment implements OnTimeSetListener, OnClickListener{
	private OnTimeChangedCallback callback;
	private Calendar initTime = Calendar.getInstance();
	public static String INIT_TIME_BUNDLE = "com.nazir.schedx.util.INT_TIME_BUNDLE";

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		
		long initTimeMillis = getArguments().getLong(INIT_TIME_BUNDLE);
		initTime.setTimeInMillis(initTimeMillis);
	}


	public interface OnTimeChangedCallback{
		void onTimeChanged(DialogFragment dialog, Calendar date);
	}

	
	@Override
	public void onAttach(Activity activity) {
		try{
			callback = (OnTimeChangedCallback) activity;
			
		}catch(ClassCastException exception){
			throw new ClassCastException("Activity:" + getActivity().toString() + 
					" Does not Implements OnTimeChangeCallback");
		}
		super.onAttach(activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, 
				initTime.get(Calendar.HOUR_OF_DAY), initTime.get(Calendar.MINUTE), false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", this);
		
		return dialog;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		initTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
		initTime.set(Calendar.MINUTE, minute);
		callback.onTimeChanged(this, initTime);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == Dialog.BUTTON_NEGATIVE)
			Toast.makeText(getActivity(), "Dismiss", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
	}
	
}
