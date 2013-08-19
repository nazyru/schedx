
package com.nazir.schedx.model;

import com.nazir.schedx.types.AssessmentTriggerMode;
import com.nazir.schedx.types.AssessmentType;

public class Assessment
{
    private AssessmentType assessmentType;
    private Course course;
    private long date;
    private int id;
    private String location;
    private String memo;
    private AssessmentTriggerMode triggerMode;

    public Assessment()
    {
    }

    public Assessment(Course course, long date, AssessmentType assessmentType)
    {
        this.course = course;
        this.date = date;
        this.assessmentType = assessmentType;
    }

    public boolean equals(Object obj)
    {
        if(this != obj)
        {
            if(obj == null || getClass() != obj.getClass())
                return false;
            Assessment assessment = (Assessment)obj;
            if(id != assessment.id)
                return false;
        }
        return true;
    }

    public AssessmentType getAssessmentType()
    {
        return assessmentType;
    }

    public Course getCourse()
    {
        return course;
    }

    public long getDate()
    {
        return date;
    }

    public int getId()
    {
        return id;
    }

    public String getLocation()
    {
        return location;
    }

    public String getMemo()
    {
        return memo;
    }

    public AssessmentTriggerMode getTriggerMode()
    {
        return triggerMode;
    }

    public int hashCode()
    {
        int i = 31 * id;
        int j;
        if(course.getID() != 0)
            j = Integer.valueOf(course.getID()).hashCode();
        else
            j = 0;
        return i + j;
    }

    public void setAssessmentType(AssessmentType assessmenttype)
    {
        assessmentType = assessmenttype;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }

    public void setDate(long l)
    {
        date = l;
    }

    public void setId(int i)
    {
        id = i;
    }

    public void setLocation(String s)
    {
        location = s;
    }

    public void setMemo(String s)
    {
        memo = s;
    }

    public void setTriggerMode(AssessmentTriggerMode assessmenttriggermode)
    {
        triggerMode = assessmenttriggermode;
    }

    
}
