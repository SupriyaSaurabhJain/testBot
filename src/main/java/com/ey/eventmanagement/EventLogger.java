package com.ey.eventmanagement;

import com.ey.db.DBOperationsForEvent;

public class EventLogger {
	
	public static void logEvent(String eventDescription, EventName eventType, int userID){
		
		//GET EVENT ID FROM EVENTNAME
		int eventID = DBOperationsForEvent.getEventIDByEventName(eventType.toString());
		 
		DBOperationsForEvent.logEvent(eventDescription, eventID, userID);
	}

}