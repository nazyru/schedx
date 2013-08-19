package com.nazir.schedx.types;

public enum LectureAlarmTrigger {
	EXACT("Remind Me On-Time"),
	FIVE("5 Minutes Before"),
	TEN("10 Minutes Before"),
	FIFTEEN("15 Minutes Before"),
	TWENTY("20 Minutes Before"),
	THIRTY("30 Minutes Before"),
	OFF("Dont Remind Me");
	
	private String description;
	
	private LectureAlarmTrigger(String desc){
		this.description = desc;
	}
	
	@Override
	public String toString(){
		return description;
	}

}
