
package com.nazir.schedx.model;


public class ClassRep
{
    private String emailAddress;
    private int id;
    private String name;
    private String phoneNumber;
    private String regNumber;

    public ClassRep()
    {
    }

    public ClassRep(String s)
    {
        regNumber = s;
    }

    public ClassRep(String s, String s1)
    {
        regNumber = s;
        name = s1;
    }

    public boolean equals(Object obj)
    {
        if(this != obj)
        {
            if(obj == null || getClass() != obj.getClass())
                return false;
            ClassRep classrep = (ClassRep)obj;
            if(regNumber == null ? classrep.regNumber != null : !regNumber.equals(classrep.regNumber))
                return false;
        }
        return true;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getRegNumber()
    {
        return regNumber;
    }

    public int hashCode()
    {
        if(regNumber != null)
            return regNumber.hashCode();
        else
            return 0;
    }

    public void setEmailAddress(String s)
    {
        emailAddress = s;
    }

    public void setId(int i)
    {
        id = i;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setPhoneNumber(String s)
    {
        phoneNumber = s;
    }

    public void setRegNumber(String s)
    {
        regNumber = s;
    }

    public String toString()
    {
        return name +" "+ regNumber;
    }

   
}
