package com.nazir.schedx.remainder;

import android.app.IntentService;
import android.content.Intent;
import com.nazir.schedx.model.Assessment;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.persist.*;
import java.util.List;

public class RebootService extends IntentService
{
    public RebootService()
    {
        super("Reboot Service");
    }

    protected void onHandleIntent(Intent intent)
    {
        TodosHelper todoshelper;
        List<Todo> todos;
        
        AssessmentHelper assessmenthelper;
        List<Assessment> assessments;
        
        LecturesHelper lectureshelper;
        List<Lecture> lectures;
        
        todoshelper = new TodosHelper(this);
        todos = todoshelper.getPendingTodos();
        
        assessmenthelper = new AssessmentHelper(this);
        assessments = assessmenthelper.getAllAssessments();
        
        lectureshelper = new LecturesHelper(this);
        lectures = lectureshelper.getPendingLectures();
        
        todoshelper.disconnect();
        assessmenthelper.disconnect();
        lectureshelper.disconnect();
          
        for(Todo todo: todos)
        	AlarmHelper.updateTodoRemainder(todo, this);
        
        for(Assessment assessment: assessments)
        	AlarmHelper.updateAssessmentRemainder(assessment, this);
        
         for(Lecture lecture: lectures)
        	 AlarmHelper.updateLectureRemainder(lecture, this);
    }
}
