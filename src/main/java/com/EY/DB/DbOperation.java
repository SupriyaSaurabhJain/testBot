package com.EY.DB;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.EY.ChatBot.MyWebhookServlet;

public class DbOperation extends ConnectionService{
	private static final Logger log = Logger.getLogger(DbOperation.class.getName());
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
			ConnectionService.closeConnection();
		}
		return response;

	}
	public static String getResponse(int subTopicId , int stateId ){
		log.info("inside getResponse");
		String response = "";
		String Query = "";
		Connection connection = ConnectionService.getConnection();
		Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '"+subTopicId+"' AND state_id = '" +stateId+"';";
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
			ConnectionService.closeConnection();
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
		int countryId  = getCountryIdFromState(state);
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

	private static int getCountryIdFromState(String state) {
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
	
	public static String fetchComplianceDetailsFromDB(int page_number){

		log.info("Inside method fetchComplianceDetailsFromDB");
		
		JSONObject complianceDetails = new JSONObject();
		JSONArray dataArray = new JSONArray();
		
		String queryToFetchTopicSubtopic = "select T.topic_id, T.topic_name, ST.sub_topic_id, ST.sub_topic_name from Topics T, SubTopics ST WHERE T.topic_id = ST.topic_id limit "+(page_number*10)+",10;";
		
		Connection connection = ConnectionService.getConnection();
		
		try {
			
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(queryToFetchTopicSubtopic);
			while(rs.next()){
				
				JSONObject rowData = new JSONObject();
				
				rowData.put("topic_name" , rs.getString("topic_name"));
				rowData.put("sub_topic_name", rs.getString("sub_topic_name"));
				rowData.put("topic_id" , rs.getString("topic_id"));
				rowData.put("sub_topic_id", rs.getString("sub_topic_id"));
				String queryToFetchNumberOfQuestions = "select count(question_id) as number_of_questions from QuestionsManagement group by sub_topic_id having sub_topic_id = "+rs.getInt("sub_topic_id")+";";
				
				//log.info(queryToFetchNumberOfQuestions);
				
				Statement statement2 = connection.createStatement();
				
				ResultSet rs2 = statement2.executeQuery(queryToFetchNumberOfQuestions);
				
				if(rs2.next())
					rowData.put("number_of_questions", rs2.getInt("number_of_questions"));	
				else 
					rowData.put("number_of_questions", 0); 
				
				//log.info(rowData.toJSONString());
				
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
	
public static String fetchQuestionsFromDB(int topic_id, int sub_topic_id){
		
		log.info("Inside method fetchQuestionsFromDB");
		
		JSONObject listOfQuestions = new JSONObject();
		
		JSONArray data = new JSONArray();

		Connection connection = ConnectionService.getConnection();
		
		String queryToFetchQuestions = "select * from QuestionsManagement where topic_id = "+topic_id+" and sub_topic_id = "+sub_topic_id+";";
		
		try {
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(queryToFetchQuestions);
			
			while(rs.next()){
				JSONObject questionData = new JSONObject();
				questionData.put("question_id" , rs.getInt("question_id"));
				questionData.put("question" , rs.getString("possible_questions"));
				questionData.put("question_type" , rs.getString("questions_type"));
				questionData.put("user_id" , rs.getInt("User_ID"));
				
				data.add(questionData);
				
			}
			
			rs.close();
			
			listOfQuestions.put("data", data);
			
		} catch (Exception e) {
			log.info("Error in fetchQuestionsFromDB : "+ e);
			
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return listOfQuestions.toJSONString();
	}
	public static HashMap<String,Integer> getCountryList(){
		HashMap<String,Integer> countryList = new HashMap<String,Integer>();
		Connection connection = ConnectionService.getConnection();
		String query = "SELECT country_id , country_name FROM Country ;";
		try {
			Statement statement =  connection.createStatement();
			ResultSet resultset = statement.executeQuery(query);
			log.info("Query executed resultSet : "+ resultset);
			while (resultset.next()) {
				countryList.put(resultset.getString("country_name"), resultset.getInt("country_id"));
			}
			resultset.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while fetching Countries from table :" + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();

		}
		
		return countryList;
	}
	public static HashMap<String,Integer> getStateList(int countryId){
		HashMap<String,Integer> stateList = new HashMap<String,Integer>();
		Connection connection = ConnectionService.getConnection();
		String query = "SELECT * FROM State WHERE country_id = '"+ countryId+"' ;";
		try {
			Statement statement =  connection.createStatement();
			ResultSet resultset = statement.executeQuery(query);
			log.info("Query executed resultSet : "+ resultset);
			while (resultset.next()) {
				stateList.put(resultset.getString("state_name"), resultset.getInt("state_id"));
			}
			resultset.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while fetching States from table :" + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();

		}
		
		return stateList;
	}
	
	public static int addSubscriber(String userName, String emailID, String password, boolean isadmin, String status){
		log.info("inside add subscriber");
		int response = -1;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date date = new Date(System.currentTimeMillis());
		log.info(dateFormat.format(date));
		String query;
		if(isadmin)
		{
			query = "INSERT INTO User(Username, Email , Password , Status , IsAdmin, account_creation_date) VALUES" +
				" ('"+userName+"' , '"+emailID+"' , '"+ password  +"' , ' "+ status+"' , '"+ isadmin+"' , '"+ dateFormat.format(date).toString()+"') ;" ;
		}
		else
		{
			query = "INSERT INTO User(Username, Email , Status , IsAdmin, account_creation_date) VALUES" +
					" ('"+userName+"' , '"+emailID+"' , '"+  status +"' , '"+ isadmin+"' , '"+ dateFormat.format(date).toString()+"') ;" ;
		}
		log.info(query);
		Connection connection=null;
		Statement stmt = null;
		try {
			
			connection = ConnectionService.getConnection();
			stmt = connection.createStatement();
			response = stmt.executeUpdate(query);
			log.info("subscriber added successfully");
			
		} 
		catch(SQLIntegrityConstraintViolationException e){
			log.info("email id repeated");
			response = -2;
		}
		catch (Exception e) {
			// TODO: handle exception
			log.severe("exception inserting the subscriber");
		}
		finally{
			ConnectionService.closeConnection();
		}
		return response;
	}
	
	public static int deleteSubscriberFromDb(int userID) {
		// TODO Auto-generated method stub
		log.info("inside method deleteSubscriber");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		String query = "DELETE FROM User WHERE User_ID  = ' " +userID+ " ';";
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
	
	public static int ModifySubscriberFromDb(int userID, String Username, String email, String Password, String Status, boolean isadmin, boolean isRoleChange) {
		// TODO Auto-generated method stub
		log.info("inside method ModifySubscriber");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		String query = "";
		if(isRoleChange)
		{
			query="UPDATE User SET Username  = ' " +Username+" ' , Email = ' "+email+
					" ' , Password = ' "+Password+" ' , Status = ' "+Status+" ' , IsAdmin = ' "+isadmin+" ' WHERE User_ID = ' "+userID+" ' ;";
		}
		else{
			query="UPDATE User SET Username  = ' " +Username+" ' , Email = ' "+email+
					" ' , Status = ' "+Status+" ' , IsAdmin = ' "+isadmin+" ' WHERE User_ID = ' "+userID+" ' ;";
		}
		log.info(query);
		try {
			Statement statement =  connection.createStatement();
			response = statement.executeUpdate(query);
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while modifying in table :" + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();

		}
		return response;	
	}
	
  public static String fetchSubscribersFromDB(){
		
		log.info("Inside method fetchSubscriberFromDB");
		
		JSONObject listOfSubscribers = new JSONObject();
		
		JSONArray data = new JSONArray();

		Connection connection = ConnectionService.getConnection();
		
		String query = "select * from User;";
		
		try {
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()){
				JSONObject SubscriberData = new JSONObject();
				SubscriberData.put("User_ID" , rs.getInt("User_ID"));
				SubscriberData.put("Username" , rs.getString("Username"));
				SubscriberData.put("Email" , rs.getString("Email"));
				SubscriberData.put("Status" , rs.getString("Status"));
				SubscriberData.put("IsAdmin" , rs.getString("IsAdmin"));
				
				data.add(SubscriberData);
				
			}
			
			rs.close();
			
			listOfSubscribers.put("data", data);
			
		} catch (Exception e) {
			log.info("Error in fetchSubscribersFromDB : "+ e);
			
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return listOfSubscribers.toJSONString();
	}
  
   public static boolean IsUserAdmin(int User_ID)
   {
	   log.info("checking if user is admin");
	   boolean isadmin = false;
	   String query = "select IsAdmin from User where User_ID = ' "+User_ID+" ';";
	   log.info(query);
	   Connection connection = ConnectionService.getConnection();
	   try{
		   Statement stmt = connection.createStatement();
		   ResultSet rs = stmt.executeQuery(query);
		   if(rs.next())
		   {
			   isadmin = rs.getBoolean("IsAdmin");
			   log.info("Collected user's role : "+isadmin);
		   }
		   rs.close();
		   stmt.close();
		   
	   }catch(Exception e){
		   log.info("Exception while cheking user role");
		   e.printStackTrace();
	   }
	   finally{
		   ConnectionService.closeConnection();
	   }
	   return isadmin;
   }
}
