package com.nazir.schedx.types;


public enum AssessmentTriggerMode
{
    EXACT("Remind Me On-Time"),
    TWO("2 days before"),
    FIVE("5 days before"),
    TEN("10 days before"),
    FIFTEEN("15 days before"),
    TWENTY("20 days before"),
    OFF("Dont Remind Me");
	
	private String description;
	
	private AssessmentTriggerMode(String desc){
		this.description = desc;
	}
    
    public String toString()
    {
        return description;
    }

}
