package com.nazir.schedx.types;


public enum AlarmRepeatMode
{
    ONCE("Once"),
    ONCE_A_DAY("Once Every Day"),
    ONCE_A_MONTH("Once Every Week"),
    ONCE_A_WEEK("Once Every Month");
    
   private String description;

   private AlarmRepeatMode(String desc){
	   this.description = desc;
   }
   
   public String toString(){
	   return description;
   }
}
