package com.EY.DB;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	public int deleteTopicFromDb(String topic){
		log.info("inside method deleteTopic");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		try {
			Statement statement =  connection.createStatement();
			response = statement.executeUpdate("DELETE FROM Topics WHERE topic_name = ' " +topic+ "'");
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting topic from table :" + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		return response;
	}
	public int deleteSubTopicFromDb(String subTopic){
		log.info("inside method deleteSubTopic");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		try {
			Statement statement =  connection.createStatement();
			response = statement.executeUpdate("DELETE FROM SubTopics WHERE sub_topic_name = ' " +subTopic+ "'");
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting subTopic from table :" + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
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

	public static int deleteQuestionFromDb(int questionId) {
		// TODO Auto-generated method stub
		log.info("inside method deleteTopic");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		String query = "DELETE FROM QuestionsManagement WHERE question_id  = ' " +questionId+ "'";
		try {
			Statement statement =  connection.createStatement();
			response = statement.executeUpdate(query);
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting from table :" + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();

		}
		return response;	
	}

	public static int addLawDescriptionToDB(String topic, String subTopic, String country, String state,String description , int descriptionId) {
		int response = -1;
		int subTopicId = getSubTopicId(subTopic);
		int countryId  = getCountryId(state);
		int stateId = getstateId(state);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        log.info("Timestamp " + timestamp);
		String query ;
		if (descriptionId == -1) {
			query = "INSERT INTO Law_Description(law_description , state_id , country_id , sub_topic_id, CreateTimeStamp, ModifiedTimestamp) VALUES" +
					" ('" +description+"' , '"+ stateId  +"' , ' "+ countryId+"' , '"+ subTopicId+"' , '"+ timestamp+"' , '" +timestamp+"') ;" ;
		}
		else{
			query = "UPDATE  Law_Description SET law_description = '"+description+"', state_id = '"+ stateId +"', country_id = '"+countryId +"', sub_topic_id = '"+subTopicId +"' , ModifiedTimestamp = '"+timestamp +"'" +
						"		WHERE law_desc_id = '" +descriptionId+ "' ; " ;
		}
		
		log.info(query);
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			response = statement.executeUpdate(query);
			log.info("description added sucessfully");
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

	private static int getCountryId(String state) {
		Connection connection = ConnectionService.getConnection();
		int countryId = -1;
		state = state.trim().toUpperCase();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select country_id from State where UPPER(state_name) = '"+state+"';");
			while(rs.next()){
				countryId  = rs.getInt("country_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception fetching country_id");
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		log.info("country_id fetched :  " + countryId);
		return countryId;	
		}
	public static String fetchComplianceDetailsFromDB(){

		log.info("Inside method fetchComplianceDetailsFromDB");
		
		JSONObject complianceDetails = new JSONObject();
		JSONArray dataArray = new JSONArray();
		
		String queryToFetchTopicSubtopic = "select T.topic_id, T.topic_name, ST.sub_topic_id, ST.sub_topic_name from Topics T, SubTopics ST WHERE T.topic_id = ST.topic_id;";
		
		Connection connection = ConnectionService.getConnection();
		
		try {
			
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(queryToFetchTopicSubtopic);
			while(rs.next()){
				
				JSONObject rowData = new JSONObject();
				
				rowData.put("topic_name" , rs.getString("topic_name"));
				rowData.put("sub_topic_name", rs.getString("sub_topic_name"));
				
				String queryToFetchNumberOfQuestions = "select count(question_id) as number_of_questions from QuestionsManagement group by sub_topic_id having sub_topic_id = "+rs.getInt("sub_topic_id")+";";
				
				//log.info(queryToFetchNumberOfQuestions);
				
				Statement statement2 = connection.createStatement();
				
				ResultSet rs2 = statement2.executeQuery(queryToFetchNumberOfQuestions);
				
				if(rs2.next())
					rowData.put("number_of_questions", rs2.getInt("number_of_questions"));	
				else 
					rowData.put("number_of_questions", 0); 
				
				log.info(rowData.toJSONString());
				
				rs2.close();
				
				dataArray.add(rowData);
			
			}
			
			complianceDetails.put("data", dataArray);
			
		} catch (Exception e) {
			
			log.info("Error"+ e);
			
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return complianceDetails.toJSONString();
	}
}
