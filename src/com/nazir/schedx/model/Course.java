
package com.nazir.schedx.model;


public class Course
{
    private int ID;
    private String courseCode;
    private String courseTitle;
    private int courseUnit;
    public Course()
    {
    }

    public Course(String s, String s1)
    {
        courseCode = s;
        courseTitle = s1;
    }

    public boolean equals(Object obj)
    {
        if(this != obj)
        {
            if(obj == null || getClass() != obj.getClass())
                return false;
            Course course = (Course)obj;
            if(!courseCode.equals(course.courseCode))
                return false;
        }
        return true;
    }

    public String getCourseCode()
    {
        return courseCode;
    }

    public String getCourseTitle()
    {
        return courseTitle;
    }

    public int getCourseUnit()
    {
        return courseUnit;
    }

    public int getID()
    {
        return ID;
    }

    public int hashCode()
    {
        return courseCode.hashCode();
    }

    public void setCourseCode(String s)
    {
        courseCode = s;
    }

    public void setCourseTitle(String s)
    {
        courseTitle = s;
    }

    public void setCourseUnit(int i)
    {
        courseUnit = i;
    }

    public void setID(int i)
    {
        ID = i;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(courseCode))).append(" ").append(courseTitle).toString();
    }

    
}
