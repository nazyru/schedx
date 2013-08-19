package com.nazir.schedx.types;


public enum AssessmentType
{
	TYPE("--Choose Type--"),
    TEST("Test"),
    ASSIGNMENT("Assignment"),
    EXAM("Examination");
    
    private String description;

    private AssessmentType(String desc){
    	this.description = desc;
    }
    
    @Override
    public String toString()
    {
        return description;
    }

    
}
