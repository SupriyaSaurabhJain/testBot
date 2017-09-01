package com.EY.DB;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.EY.ChatBot.MyWebhookServlet;

public class DbOperation extends ConnectionDetails{
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());
	public static HashMap<String,Integer> getTopics(){
		log.info("inside method getTopic");
		HashMap<String,Integer> topics = new HashMap<String,Integer>();
		Connection connection = ConnectionDetails.getConnection();
		try {
			Statement statement =  connection.createStatement();
			 ResultSet result = statement.executeQuery("SELECT topic_id , topic_name FROM Topics");
			log.info("Query executed response : "+ result);
			 while (result.next()){
				 topics.put(result.getString("topic_name"),result.getInt("topic_id") );
			 }
			 }
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting from table :" + e);
			e.printStackTrace();
		}
		return topics;
	}
	public static int addNewTopicToDB(String topic , String subTopic){
		log.info("inside method addTopic");
		Connection connection = ConnectionDetails.getConnection();
		HashMap<String, Integer> topicSet = getTopics();
		int topic_id = -1;
		int check = 0 ;
		int response = -1;
		for (String topicVal : topicSet.keySet()) {
			check ++;
			if (topicVal.equalsIgnoreCase(topic)) {
				topic_id = topicSet.get(topicVal);
				break;
			}
		}
		if (check  == topicSet.keySet().size()) {
			insertTopic(topic);
			topic_id = getTopicId(topic);
		}
		try {
			Statement statement = connection.createStatement();
			response = statement.executeUpdate("INSERT INTO SubTopics(sub_topic_name ,topic_id) VALUES('"+subTopic+"','"+topic_id+"')");
			 }
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting from table :" + e);
			e.printStackTrace();
		}
		return response;
	}
	public static  int getTopicId(String topic) {
		Connection connection = ConnectionDetails.getConnection();
		int topic_id = -1;
		topic = topic.trim().toUpperCase();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select topic_id from Topics where topic_name='"+topic+"';");
			while(rs.next()){
				topic_id  = rs.getInt("topic_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception fetching topic id");
			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}
		log.info("topic id fetched :  " + topic_id);
		return topic_id;

	}
	static int insertTopic(String topic)  {
		Connection connection = ConnectionDetails.getConnection();
		Statement statement;
		int response = -1;
		try {
			statement = connection.createStatement();
				
				response = statement.executeUpdate("insert into Topics(topic_name) Values('"+topic+"')");
				log.info("added to table Topics : "+ response);
					} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("Exception adding topic to table : " + e);

			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}
		return response ;
	}
	public int deleteFromDb(String topic , String subTopic){
		log.info("inside method deleteTopic");
		int response = 0;
		Connection connection = ConnectionDetails.getConnection();
		try {
			Statement statement =  connection.createStatement();
			 response = statement.executeUpdate("DELETE FROM sample WHERE subTopic = ' " +subTopic+ "'");
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting from table :" + e);
			e.printStackTrace();
		}
		finally{
			try {
				connection.close();
				log.info("connection closed");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.severe("Error closing connection");
				e.printStackTrace();
			}
		}
		return response;
	}
	public static String getResponse(String subTopic , String state , String country){
		log.info("inside getResponse");
		String response = "";
		String Query = "";
		int subTopic_id = -1; 
		Connection connection = ConnectionDetails.getConnection();
		if (state.toUpperCase().equalsIgnoreCase("FEDERAL")) {
			Query = "SELECT law_description FROM Law_Description WHERE topic_id = '"+subTopic_id+"' AND state_id IS NULL;" ;
		}
		else{
			int state_id = getstateId(state);
			Query = "SELECT law_description FROM Law_Description WHERE topic_id = '"+subTopic_id+"' AND state_id = '" +state_id+"';";
		}
		try {
			Statement statement =  connection.createStatement();
			ResultSet rs = statement.executeQuery(Query);
			while(rs.next()){
				response  = rs.getString("law_description");
			}
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while selecting from table :" + e);
			e.printStackTrace();
		}
		finally{
			try {
				connection.close();
				log.info("connection closed");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.severe("Error closing connection");
				e.printStackTrace();
			}
		return response;
	}
		
}
	
	public static  int getstateId(String state){
		Connection connection = ConnectionDetails.getConnection();
		int state_id = -1;
		state = state.trim().toUpperCase();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select state_id from State where state_name='"+state+"';");
			while(rs.next()){
				//Retrieve by column name
				state_id  = rs.getInt("state_id");


			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception fetching state id");
			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}
		log.info("state id fetched : "+ state_id);
		return state_id;
	}
}
