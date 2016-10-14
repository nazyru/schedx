package com.nazir.schedx.types;


public enum AlarmTrigger
{
	OFF("Off"),
    EXACT("On-Time"),
    FIVE("5 Minutes Before"),
    TEN("10 Minutes Before"),
    THIRTY("30 Minutes Before"),
    HOUR("1 hour Before"),
    DAY("24 hours Before");
    
	private String description;
	
	private AlarmTrigger(String desc){
		this.description = desc;
	}
    @Override
    public String toString()
    {
        return description;
    }
  
}
