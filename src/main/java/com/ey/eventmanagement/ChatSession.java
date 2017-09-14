package com.ey.eventmanagement;

import com.ey.db.DBOperationsForEvent;

public class ChatSession {

	public static int startSession(int userID){
		//LOG SESSEION START FOR THE USERID
		return DBOperationsForEvent.logNewSession(userID);
	}
	
	public static void LogEmailSupport(int chatSessionID, String chatEntry){
		//LOG ENTRY FOR EMAIL SUPPORT 
		
		DBOperationsForEvent.logEmailSupportRequest(chatSessionID, chatEntry);
	}
	
	public static void logMessage(int chatSessionID, String chatEntry, String responseType){
		//LOG MESSAGE FROM USER
		
		DBOperationsForEvent.logNewMessage(chatSessionID, chatEntry, responseType);
		
	}
}