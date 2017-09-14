package com.ey.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.ey.util.Queries;


public class DBOperationsForEvent{

	private static final Logger log = Logger.getLogger(DBOperationsForEvent.class.getName());
	
	public static void logEvent(String eventDescription, int eventID, int userID){
		
		log.info("Inside logEventIntoDB");
		
		Connection connection = ConnectionService.getConnection();
		
		try{
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToInsertEventLog"));
			
			//SET PARAMETERS
			statement.setString(1, eventDescription);
			statement.setInt(2, eventID);
			statement.setInt(3, userID);
			
			//EXECUTE QUERY TO INSERT EVENT LOG
			statement.executeUpdate();
			
			//CLOSE DB PARAMETERS 
			statement.close();
			
			log.info("Event added successfully");
		}
		catch(SQLException e){
			log.info("Error in logEventIntoDB : "+e);
		}
		finally{
			ConnectionService.closeConnection();
		}
		
	}

	public static int getEventIDByEventName(String eventName) {
		
		log.info("Inside getEventID");
		
		Connection connection = ConnectionService.getConnection();
		
		try{
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToGetEventID"));
			
			//SET PARAMETERS
			statement.setString(1, eventName);
			
			//EXECUTE QUERY
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
				return resultSet.getInt("Event_ID");
			
			//CLOSE DB PARAMETERS
			resultSet.close();
			statement.close();
		}
		catch(SQLException e){
			log.info("Error in getEventID : "+e);
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return 0;
	}
	
	
	public static int logNewSession(int userID){
		
		log.info("Inside logNewSession");
		
		int chatSessionID = 0; 
		Connection connection = ConnectionService.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToLogNewSession"), Statement.RETURN_GENERATED_KEYS);
			
			//SET PAREMETERS
			statement.setInt(1, userID);
			
			//EXECUTE QUERY
			statement.executeUpdate();
			
			//GET CHAT SESSION ID FOR INSERTED LOG 
			ResultSet result = statement.getGeneratedKeys();
			
			if(result.next())
				chatSessionID = result.getInt(1);
			
			//CLOSE DB PARAMETERS
			result.close();
			statement.close();
			
		}
		catch (SQLException e) {
			log.info("Error in logNewSession : "+e);
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return chatSessionID;
	}

	public static void logEmailSupportRequest(int chatSessionID,String chatEntry) {
		
		log.info("Inside logEmailSupportRequest");
		
		Connection connection = ConnectionService.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToLogEmailSupportRequest"));

			//SET PAREMETERS
			statement.setInt(1, chatSessionID);
			statement.setString(2, chatEntry);

			//EXECUTE QUERY 
			statement.executeUpdate();	
			
			//CLOSE DB PARAMETERS
			statement.close();
		}
		catch (SQLException e) {
			log.info("Error in logNewSession : "+e);
		}
		finally{
			ConnectionService.closeConnection();
		}
		
	}

	public static void logNewMessage(int chatSessionID, String chatEntry, String responseType) {
		
		log.info("Inside logNewMessage");
		
		Connection connection = ConnectionService.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToLogMessage"));
			
			//SET PARAMETERS
			statement.setInt(1, chatSessionID);
			statement.setString(2, chatEntry);
			statement.setInt(3, DbOperation.getSubTopicId(responseType));
			
			//EXECUTE QUERY
			statement.executeUpdate();
			
			//CLOSE DB PARAMETERS
			statement.close();
		}
		catch (SQLException e) {
			log.info("Error in logNewMessage : "+e);
		}
		finally{
			ConnectionService.closeConnection();
		}
		
	}
	
	
}