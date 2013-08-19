
package com.nazir.schedx.types;


public enum Status
{
	ONGOING("Ongoing"),
    FINISHED("Finished");
	
	private String description;
	
    private Status(String desc)
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
