package com.nazir.schedx.types;


public enum AlarmRepeatMode
{
    ONCE("Once"),
    ONCE_A_DAY("Once A Day"),
    ONCE_A_MONTH("Once A Week"),
    ONCE_A_WEEK("Once A Month");
    
   private String description;

   private AlarmRepeatMode(String desc){
	   this.description = desc;
   }
   
   public String toString(){
	   return description;
   }
}
