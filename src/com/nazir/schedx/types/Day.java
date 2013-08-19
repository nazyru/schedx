package com.nazir.schedx.types;


public enum Day
{
	CHOOSE("--Choose--"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

	private String description;
	
    private Day(String desc)
    { 
        this.description = desc;
    }

    public String getDescription()
    {
        return description;
    }

    public String toString()
    {
        return description;
    }

}
