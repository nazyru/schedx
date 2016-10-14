package com.nazir.schedx.remainder;

import android.content.*;
import com.nazir.schedx.ui.*;

public class ScheduleReceiver extends BroadcastReceiver
{
	
    public void onReceive(Context context, Intent intent)
    {
        Intent intent1 = new Intent(context, ScheduleService.class);
        if(intent.getAction().contains(LectureActivity.LECTURE_ACTION))
        {
            intent1.setAction(LectureActivity.LECTURE_ACTION);
            intent1.putExtra(LectureActivity.LECTURE_ID, intent.getIntExtra(LectureActivity.LECTURE_ID, -1));
            context.startService(intent1);
        } else
        {
            if(intent.getAction().contains(TodoActivity.TODO_ACTION))
            {
                intent1.setAction(TodoActivity.TODO_ACTION);
                intent1.putExtra(TodoActivity.TODO_ID, intent.getIntExtra(TodoActivity.TODO_ID, -1));
                context.startService(intent1);
                return;
            }
            if(intent.getAction().contains(AssessmentActivity.ASSESSMENT_ACTION))
            {
                intent1.setAction(AssessmentActivity.ASSESSMENT_ACTION);
                intent1.putExtra(AssessmentActivity.ASSESSMENT_ID, 
                		intent.getIntExtra(AssessmentActivity.ASSESSMENT_ID, -1));
                context.startService(intent1);
                return;
            }
        }
    }

    

}
