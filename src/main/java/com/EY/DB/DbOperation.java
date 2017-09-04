package com.EY.DB;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;

import com.EY.ChatBot.MyWebhookServlet;

public class DbOperation extends ConnectionService{
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());
	public static HashMap<String,Integer> getTopics(){
		log.info("inside method getTopic");
		HashMap<String,Integer> topics = new HashMap<String,Integer>();
		Connection connection = ConnectionService.getConnection();
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
		Connection connection = ConnectionService.getConnection();
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
		Connection connection = ConnectionService.getConnection();
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
			ConnectionService.closeConnection();
		}
		log.info("topic id fetched :  " + topic_id);
		return topic_id;

	}
	static int insertTopic(String topic)  {
		Connection connection = ConnectionService.getConnection();
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
			ConnectionService.closeConnection();
		}
		return response ;
	}
	public int deleteFromDb(String topic , String subTopic){
		log.info("inside method deleteTopic");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
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
	public static  int getSubTopicId(String subtopic){
		Connection connection = ConnectionService.getConnection();
		int subTopic_id = -1;
		subtopic = subtopic.trim().toUpperCase();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT sub_topic_id FROM SubTopics WHERE sub_topic_name='"+subtopic+"';");
			while(rs.next()){
				subTopic_id  = rs.getInt("sub_topic_id");

			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception fetching sub topic id");
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		log.info("subTopic id added : "+ subTopic_id);
		return subTopic_id;
	} 

	public static String getResponse(String subTopic , String state , String country){
		log.info("inside getResponse");
		String response = "";
		String Query = "";
		int subTopic_id = getSubTopicId(subTopic.toUpperCase()) ; 
		Connection connection = ConnectionService.getConnection();
		if (state.toUpperCase().equalsIgnoreCase("FEDERAL")) {
			Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '"+subTopic_id+"' AND state_id IS NULL;" ;
		}
		else{
			int state_id = getstateId(state.toUpperCase());
			Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '"+subTopic_id+"' AND state_id = '" +state_id+"';";
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
	}
		return response;
	
}
	
	public static  int getstateId(String state){
		Connection connection = ConnectionService.getConnection();
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
			ConnectionService.closeConnection();
		}
		log.info("state id fetched : "+ state_id);
		return state_id;
	}
	public static int addNewQuestionToDB(String topic , String subTopic , String question, int userId){
		int response = -1;
		int topicId = getTopicId(topic);
		int subTopicId = getSubTopicId(subTopic);
		String query = "INSERT INTO QuestionsManagement(possible_questions , questions_type , User_ID , sub_topic_id ,topic_id) VALUES" +
		                " ('" +question+"' , 'USER' , ' "+ userId+"' , '"+ subTopicId+"' , '" + topicId +"') ;" ;
		log.info(query);
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			response = statement.executeUpdate(query);
			log.info("question added sucessfully");
			ConnectionService.closeConnection();

		} catch (SQLException e) {
			// TODO: handle exception
			log.severe("exception adding question : "+ e);
		}
		finally {
			ConnectionService.closeConnection();

		}
		return response ;
	}
}
