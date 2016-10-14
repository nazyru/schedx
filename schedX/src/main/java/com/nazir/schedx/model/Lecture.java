
package com.nazir.schedx.model;

import com.nazir.schedx.types.LectureAlarmTrigger;
import com.nazir.schedx.types.Status;

public class Lecture
{
    private ClassRep classRep;
    private Course course;
    private String day;
    private long endTime;
    private int id;
    private String lecturer;
    private long startTime;
    private Status status;
    private String venue;
    private LectureAlarmTrigger alarmTrigger;

    public Lecture()
    {
    }

    public Lecture(Course course, String day, long startTime)
    {
        this.course = course;
        this.day = day;
        this.startTime = startTime;
    }

    public boolean equals(Object obj)
    {
        if(this != obj)
        {
            if(obj == null)
                return false;
            if(getClass() != obj.getClass())
                return false;
            Lecture lecture = (Lecture)obj;
            if(course.getID() == 0)
            {
                if(lecture.course != null)
                    return false;
            } else
            if(!course.equals(lecture.course))
                return false;
            if(day == null)
            {
                if(lecture.day != null)
                    return false;
            } else
            if(!day.equals(lecture.day))
                return false;
            if(startTime != lecture.startTime)
                return false;
        }
        return true;
    }

    public ClassRep getClassRep()
    {
        return classRep;
    }

    public Course getCourse()
    {
        return course;
    }
    public String getDay()
    {
        return day;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public int getId()
    {
        return id;
    }

    public String getLecturer()
    {
        return lecturer;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public Status getStatus()
    {
        return status;
    }

    public String getVenue()
    {
        return venue;
    }

    public int hashCode()
    {
        int i;
        int j;
        String s;
        int k;
        if(course == null)
            i = 0;
        else
            i = Integer.valueOf(course.getID()).hashCode();
        j = 31 * (i + 31);
        s = day;
        k = 0;
        if(s != null)
            k = day.hashCode();
        return 31 * (j + k) + (int)(startTime ^ startTime >>> 32);
    }

    public void setClassRep(ClassRep classrep)
    {
        classRep = classrep;
    }

    public void setCourse(Course course1)
    {
        course = course1;
    }

    public void setDay(String s)
    {
        day = s;
    }

    public void setEndTime(long l)
    {
        endTime = l;
    }

    public void setId(int i)
    {
        id = i;
    }

    public void setLecturer(String s)
    {
        lecturer = s;
    }

    public void setStartTime(long l)
    {
        startTime = l;
    }

    public void setStatus(Status status1)
    {
        status = status1;
    }

    public void setVenue(String s)
    {
        venue = s;
    }

    public LectureAlarmTrigger getAlarmTrigger() {
		return alarmTrigger;
	}

	public void setAlarmTrigger(LectureAlarmTrigger alarmTrigger) {
		this.alarmTrigger = alarmTrigger;
	}

	public String toString()
    {
        return course.getCourseCode()+ " "+ day+" "+startTime;
    }

}
