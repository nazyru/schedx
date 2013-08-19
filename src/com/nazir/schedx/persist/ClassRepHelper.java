package com.nazir.schedx.persist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nazir.schedx.model.ClassRep;
import com.nazir.schedx.util.Mapper;
import java.util.ArrayList;
import java.util.List;
import static com.nazir.schedx.persist.MySqliteOpenHelper.ClassRep.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Tables.*;

public class ClassRepHelper
{
    ClassRep classRep;
    List<ClassRep> classReps;
    Context context;
    SQLiteDatabase db;
    int emailAddrIndx;
    int nameIndx;
    int phoneNumberIndx;
    int regNoIndx;
    private static String[] cols = {_ID, REG_NO, NAME, PHONE_NUMBER, EMAIL_ADDRESS};

    public ClassRepHelper(Context context)
    {
        classReps = new ArrayList<ClassRep>();
        classRep = new ClassRep();
        this.context = context;
        db = MySqliteOpenHelper.getWritableDb(context);
    }

    public long addClassRep(ClassRep classrep)
    {
        return db.insertOrThrow("class_rep", null, Mapper.mapToClassRep(classrep));
    }

    public void delete(int i)
    {
        String as[] = new String[1];
        as[0] = Integer.toString(i);
        db.delete("class_rep", "_id = ?", as);
    }

    public void disconnect()
    {
        db.close();
    }

    public List getAllClassReps()
    {
        
        Cursor cursor = db.query("class_rep", cols, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            regNoIndx = cursor.getColumnIndex("reg_number");
            nameIndx = cursor.getColumnIndex("name");
            phoneNumberIndx = cursor.getColumnIndex("phone_number");
            emailAddrIndx = cursor.getColumnIndex("email_address");
            do
            {
                classRep.setRegNumber(cursor.getString(regNoIndx));
                classRep.setName(cursor.getString(nameIndx));
                classRep.setPhoneNumber(cursor.getString(phoneNumberIndx));
                classRep.setEmailAddress(cursor.getString(emailAddrIndx));
                classReps.add(classRep);
            } while(cursor.moveToNext());
        }
        return classReps;
    }

    public ClassRep getClassRep(int classRepId)
    {
            ClassRep classrep = new ClassRep();
            String selectionArgs[] = { Integer.toString(classRepId) };
            String selection = _ID + " = ? "; 
            
            Cursor cursor = db.query(CLASS_REP, cols, selection, selectionArgs, null, null, null);
            
            if(cursor.moveToFirst())
            {
            	classrep.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                classrep.setRegNumber(cursor.getString(cursor.getColumnIndex(REG_NO)));
                classrep.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                classrep.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
                classrep.setEmailAddress(cursor.getString(cursor.getColumnIndex(EMAIL_ADDRESS)));
                
                return classrep;
            }
            return null;  
    }

    public Cursor getClassReps()
    {
        return db.query("class_rep", new String[] {
            "_id", "name", "reg_number", "phone_number", "email_address"
        }, null, null, null, null, null);
    }

    public void insertMock(int i)
    {
        db.delete("class_rep", null, null);
        ClassRep classrep = new ClassRep();
        int j = 0;
        do
        {
            if(j >= i)
                return;
            classrep.setName((new StringBuilder("Abubakar Saidu ")).append(j).toString());
            classrep.setPhoneNumber((new StringBuilder("0803333333")).append(j).toString());
            classrep.setRegNumber((new StringBuilder("07/20202U/")).append(j).toString());
            classrep.setEmailAddress("classRep@skool.com");
            addClassRep(classrep);
            j++;
        } while(true);
    }

    public void update(ClassRep classrep)
    {
        String as[] = new String[1];
        as[0] = Integer.toString(classrep.getId());
        db.update("class_rep", Mapper.mapToClassRep(classrep), "_id = ?", as);
    }

    
}
