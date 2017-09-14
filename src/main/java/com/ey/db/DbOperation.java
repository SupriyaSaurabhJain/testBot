package com.ey.db;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ey.util.Queries;
public class DbOperation extends ConnectionService {
	private static final Logger log = Logger.getLogger(DbOperation.class.getName());
	//Method to get list of topics along with their Id 
	//Returns hashMap <topic, topicId>
	public static HashMap<String, Integer> getTopics() {
		log.info("inside method getTopic");
		HashMap<String, Integer> topics = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetTopicDetails"));
			ResultSet result = statement.executeQuery();
			log.info("Query executed response : " + result);
			while (result.next()) {
				topics.put(result.getString("topic_name"), result.getInt("topic_id"));
			}
			result.close();
			statement.close();

		} 
		catch (SQLException e) {
			log.severe("exception while fetching list of Topics from table :" + e);
		}
		return topics;
	}
	//Method to get list of sub-topics along with their Id 
	//Returns hashMap <sub-topic, sub-topicId>
	public static HashMap<String, Integer> getSubTopics() {
		log.info("inside method getTopic");
		HashMap<String, Integer> subTopics = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetSubTopicDetails"));
			ResultSet result = statement.executeQuery();
			log.info("Query executed response : " + result);
			while (result.next()) {
				subTopics.put(result.getString("sub_topic_name"), result.getInt("sub_topic_id"));
			}
			result.close();
			statement.close();

		} catch (SQLException e) {
			log.severe("exception while fetching list of subTopics from table :" + e);
		}
		return subTopics;
	}
	//Method to add new topic & subTopic to database
	//returns 1 if success
	public static int addNewTopicSubTopicToDB(String topic, String subTopic) {
		log.info("inside method addTopic");
		HashMap<String, Integer> topicSet = getTopics();
		int topicId = -1;
		int check = 0;
		int response = -1;
		for (String topicVal : topicSet.keySet()) { //Check if topic already exists or not
			check++;
			if (topicVal.equalsIgnoreCase(topic)) {
				topicId = topicSet.get(topicVal);
				break;
			}
		}
		if (check == topicSet.keySet().size()) {
			insertTopic(topic);					// if topic not present add it to database
			topicId = getTopicId(topic); 	//get topic Id for the topic
		}
		response = insertSubTopic(subTopic, topicId) ; //Method call to add subtopic to database
		return response;
	}
//Method to add sub-topic for given topic-id to database
	 private static int insertSubTopic(String subTopic ,int topicId) {
		Connection connection = ConnectionService.getConnection();
		int response = -1;
		
		//String query = "INSERT INTO SubTopics(sub_topic_name ,topic_id) VALUES('" + subTopic + "','" + topicId + "')" ;
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("InsertSubTopic"));
			statement.setString(1,subTopic );
			statement.setInt(2, topicId);
			response = statement.executeUpdate();	
			log.info("Subtopic added to table SubTopics : " + response);
			statement.close();

		} catch (SQLException e) {
			log.severe("Exception adding sub topic to table : " + e);

			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
	 //Method to get topic-id for given topic
	public static int getTopicId(String topic) {
		Connection connection = ConnectionService.getConnection();
		int topic_id = -1;
		topic = topic.trim().toUpperCase();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetTopicId"));
			statement.setString(1, topic);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				topic_id = rs.getInt("topic_id");
			}
			rs.close();

		} catch (SQLException e) {
			log.severe("exception fetching topic id " + e);
			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		log.info("topic id fetched :  " + topic_id);
		return topic_id;

	}
//Method to insert topic in database
	 private static int insertTopic(String topic) {
		Connection connection = ConnectionService.getConnection();
		int response = -1;
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("InsertTopic"));
			statement.setString(1, topic);
			response = statement.executeUpdate();	
		//	response = statement.executeUpdate("insert into Topics(topic_name) Values('" + topic + "')");
			log.info("added to table Topics : " + response);
			statement.close();

		} catch (SQLException e) {
			log.severe("Exception adding topic to table : " + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
//Method to delete topic from database
	public int deleteTopicFromDb(String topic) {
		log.info("inside method deleteTopic");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("DeleteTopic"));
			statement.setString(1, topic);
			response = statement.executeUpdate();
		//	response = statement.executeUpdate("DELETE FROM Topics WHERE topic_name = ' " + topic + "'");
			log.info("Query executed response : " + response);
			statement.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while deleting topic from table :" + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
//Method to delete sub topic from databases
	public static int deleteSubTopicFromDb(String subTopic) {
		log.info("inside method deleteSubTopic");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("DeleteSubTopic"));
			statement.setString(1, subTopic);
			response = statement.executeUpdate();
			//response = statement.executeUpdate("DELETE FROM SubTopics WHERE sub_topic_name = ' " + subTopic + "'");
			log.info("Query executed response : " + response);
			statement.close();

		} catch (SQLException e) {
			log.severe("exception while deleting subTopic from table :" + e);
			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
//Method to get subTopicId for given sub topic
	public static int getSubTopicId(String subtopic) {
		log.info("inside get subtopicId");
		Connection connection = ConnectionService.getConnection();
		int subTopic_id = -1;
		subtopic = subtopic.trim().toUpperCase();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetSubTopicId"));
			statement.setString(1, subtopic);
			ResultSet rs = statement.executeQuery();
			//ResultSet rs = statement.executeQuery("SELECT sub_topic_id FROM SubTopics WHERE sub_topic_name='" + subtopic + "';");
			while (rs.next()) {
				subTopic_id = rs.getInt("sub_topic_id");
			}
			rs.close();
			statement.close();
			log.info("SubTopicId : "+subTopic_id);
		} catch (SQLException e) {
			log.severe("Exception fetching sub topic id "+ e);
		} finally {
			ConnectionService.closeConnection();
		}
		return subTopic_id;
	}
//Method to get lawDescription(Response for Query) for provided subtopic state & country returning law description string 
	public static String getResponse(String subTopic, String state, String country) {
		log.info("inside getResponse(String subTopic, String state, String country)");
		String response = "";
		String query = "";
		int subTopic_id = getSubTopicId(subTopic.toUpperCase());
		Connection connection = ConnectionService.getConnection();
		PreparedStatement statement ;	
		try {
		if (state.toUpperCase().equalsIgnoreCase("FEDERAL")) { // check if its for federal or some state
			query = "SELECT LD.law_description , LD.law_desc_id,S.state_id,ST.sub_topic_id FROM Law_Description AS LD RIGHT OUTER JOIN SubTopics AS ST ON LD.sub_topic_id = ST.sub_topic_id RIGHT OUTER JOIN State AS S  ON S.state_id = LD.state_id WHERE UPPER(S.state_name) = 'WEST VIRGINIA' and  UPPER(ST.sub_topic_name) = 'NON-COMPETE AGREEMENTS' ;";
			 statement = connection.prepareStatement(query);
			/*query = Queries.getQuery("GetLawDescriptionForFederal");
			log.info("Query : "+query);
			statement = connection.prepareStatement(query);
			statement.setInt(1, subTopic_id);*/
		} else {
			int state_id = getstateId(state.toUpperCase());
		/*	Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '" + subTopic_id
					+ "' AND state_id = '" + state_id + "';";*/
			query = Queries.getQuery("GetLawDescriptionForState");
			statement = connection.prepareStatement(query);
			log.info("Query : "+query);
			statement.setInt(1,subTopic_id );
			statement.setInt(2,state_id);
		}
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				response = rs.getString("law_description");
			}
			rs.close();
			statement.close();
			log.info("Query executed response : " + response);
		} catch (SQLException e) {
			log.severe("exception while fetching response / lawDescription from table :" + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return response;

	}
// overloaded getResponse method to fetch law description for provide subtopicId & stateId
	@SuppressWarnings("unchecked")
	public static JSONObject getResponse(int subTopicId, int stateId) {
		log.info("inside JSONObject getResponse(int subTopicId, int stateId) ");
		String law_desription = "";
		JSONObject response = new JSONObject();
		Connection connection = ConnectionService.getConnection();
		/*Query = "SELECT law_description , law_desc_id FROM Law_Description WHERE sub_topic_id = '" + subTopicId
				+ "' AND state_id = '" + stateId + "';";*/
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetLawDescriptionForState"));
			statement.setInt(1,subTopicId );
			statement.setInt(2,stateId );
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				law_desription = rs.getString("law_description");
				response.put("law_description_id", rs.getInt("law_desc_id"));
				response.put("law_description", law_desription);
			}
			rs.close();
			statement.close();
			log.info("Query executed response : " + response);
		} catch (SQLException e) {
			log.severe("exception while fetching response / lawDescription from table :" + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
//Method to get state id for given state
	public static int getstateId(String state) {
		Connection connection = ConnectionService.getConnection();
		int state_id = -1;
		state = state.trim().toUpperCase();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(Queries.getQuery("GetStateId"));
			statement.setString(1, state);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				// Retrieve by column name
				state_id = rs.getInt("state_id");
			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception fetching state id");
			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		log.info("state id fetched : " + state_id);
		return state_id;
	}
//Method to add new question to database
	public static int addNewQuestionToDB(String topic, String subTopic, String question, int userId) {
		log.info("inside addNewQuestionToDB(String topic, String subTopic, String question, int userId)");
		int response = -1;
		int topicId = getTopicId(topic);
		int subTopicId = getSubTopicId(subTopic);
		String type = "USER";
		//check if userId = -1, it is custom/system generated question
		if (userId == -1) { 
			type = "CUSTOM" ;
		}
		/*String query = "INSERT INTO QuestionsManagement(possible_questions , questions_type , User_ID , sub_topic_id ,topic_id) VALUES"
				+ " ('" + question + "' , 'USER' , ' " + userId + "' , '" + subTopicId + "' , '" + topicId + "') ;";*/
		Connection connection = ConnectionService.getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(Queries.getQuery("InsertQuestion"));
			statement.setString(1, question);
			statement.setString(2, type);
			statement.setInt(3,userId );
			statement.setInt(4,subTopicId );
			statement.setInt(5,topicId );
			response = statement.executeUpdate();
			log.info("question added sucessfully");
			statement.close();
		} catch (SQLException e) {
			log.severe("Exception adding question : " + e);
		} finally {
			ConnectionService.closeConnection();

		}
		return response;
	}
//Method to delete question from db
	public static int deleteQuestionFromDb(int questionId) {
		log.info("inside method deleteQuestion");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
/*		String query = "DELETE FROM QuestionsManagement WHERE question_id  = ' " + questionId + "'";
*/		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("DeleteQuestion"));
			statement.setInt(1,questionId );
			response = statement.executeUpdate();
			log.info("Query executed response : " + response);
			statement.close();
		} catch (SQLException e) {
			log.severe("exception while deleting from table :" + e);
		} finally {
			ConnectionService.closeConnection();

		}
		return response;
	}
//Method to get law description Id
	public static int getLawDescriptionId(int subTopicId, int countryId, int stateId) {
		int descriptionId = -1;
		/*String query = "SELECT law_desc_id FROM Law_Description WHERE  subTopicId = '" + subTopicId + "' country_id = '"
				+ countryId + "' state_id = '" + stateId + "' ;";*/
		Connection connection = ConnectionService.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetLawDescriptionId"));
			statement.setInt(1, subTopicId);			
			statement.setInt(2, countryId);
			statement.setInt(3, stateId);

			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				descriptionId = resultSet.getInt("law_desc_id");
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			log.severe("exception geting law desc id" + e);
			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		return descriptionId;
	}

	public static int addLawDescriptionToDB(int topicId, int subTopicId, int countryId, int stateId,
			String description) {
		int response = -1;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		log.info("Timestamp " + timestamp);
		int descriptionId = getLawDescriptionId(subTopicId, countryId, stateId);
		String query = "";
		if (descriptionId == -1) {
			/*query = "INSERT INTO Law_Description(law_description , state_id , country_id , sub_topic_id, CreateTimeStamp, ModifiedTimestamp) VALUES"
					+ " ('" + description + "' , '" + stateId + "' , ' " + countryId + "' , '" + subTopicId + "' , '"
					+ timestamp + "' , '" + timestamp + "') ;";*/
			Connection connection = ConnectionService.getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement(Queries.getQuery("InsertLawDescription"));
				statement.setString(1, description);			
				statement.setInt(2, stateId);
				statement.setInt(3, countryId);
				statement.setTimestamp(4, timestamp);
				statement.setTimestamp(5, timestamp);
				response = statement.executeUpdate();
				log.info("description added sucessfully");
				statement.close();
			} catch (SQLException e) {
				log.severe("Exception Adding Law Description : " + e);
			} finally {

				ConnectionService.closeConnection();

			}
		} else {
			response = updateLawDescription(descriptionId, description);
			/*
			 * query = "UPDATE  Law_Description SET law_description = '"
			 * +description+"', state_id = '"+ stateId
			 * +"', country_id = '"+countryId +"', sub_topic_id = '"+subTopicId
			 * +"' , ModifiedTimestamp = '"+timestamp +"'" +
			 * "		WHERE law_desc_id = '" +descriptionId+ "' ; " ;
			 */
		}

		log.info(query);

		return response;
	}

	private static int getCountryIdFromState(String state) {
		Connection connection = ConnectionService.getConnection();
		int countryId = -1;
		state = state.trim().toUpperCase();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetCountryId"));
			statement.setString(1, state.toUpperCase());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				countryId = rs.getInt("country_id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception fetching country_id");
		} finally {
			ConnectionService.closeConnection();
		}
		log.info("country_id fetched :  " + countryId);
		return countryId;
	}

	public static String fetchComplianceDetails(){

		log.info("Inside method fetchComplianceDetailsFromDB");
		
		JSONObject complianceDetails = new JSONObject();
		JSONArray dataArray = new JSONArray();
				
		Connection connection = ConnectionService.getConnection();
		
		try {
			
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToFetchTopicSubtopic"));
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				
				JSONObject rowData = new JSONObject();
				
				rowData.put("topic_id",  resultSet.getString("topic_id"));
				rowData.put("topic_name" , resultSet.getString("topic_name"));

				rowData.put("sub_topic_id",  resultSet.getString("sub_topic_id"));
				rowData.put("sub_topic_name", resultSet.getString("sub_topic_name"));
				
				String queryToFetchNumberOfQuestions = Queries.getQuery("queryToFetchNumberOfQuestions");
				
				log.info(queryToFetchNumberOfQuestions);
				
				PreparedStatement statement2 = connection.prepareStatement(queryToFetchNumberOfQuestions);
				
				//SET PARAMETERS 
				statement2.setInt(1, resultSet.getInt("sub_topic_id"));
				
				ResultSet numberOfQuestions = statement2.executeQuery();
				
				if(numberOfQuestions.next())
					rowData.put("number_of_questions", numberOfQuestions.getInt("number_of_questions"));	
				else 
					rowData.put("number_of_questions", 0); 
				
				log.info(rowData.toJSONString());

				numberOfQuestions.close();
				statement2.close();
				
				dataArray.add(rowData);
			
			}
			
			//CLOSE DB CONNECTIONS
			resultSet.close();
			statement.close();
			
			//ADD DATA TO JSON
			complianceDetails.put("data", dataArray);
			
		} catch (SQLException e) {
			
			log.info("Error in fetchComplianceDetails : "+ e);
			
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return complianceDetails.toJSONString();
	}
	
	public static String fetchQuestions(int topic_id, int sub_topic_id){
		
		log.info("Inside method fetchQuestions");
		
		JSONObject listOfQuestions = new JSONObject();
		
		JSONArray data = new JSONArray();

		Connection connection = ConnectionService.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("queryToFetchQuestions"));

			//SET PARAMETERS
			statement.setInt(1, topic_id);
			statement.setInt(2, sub_topic_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				JSONObject questionData = new JSONObject();
				
				questionData.put("question_id" , resultSet.getInt("question_id"));
				questionData.put("question" , resultSet.getString("possible_questions"));
				questionData.put("question_type" , resultSet.getString("questions_type"));
				questionData.put("user_id" , resultSet.getInt("User_ID"));
				
				data.add(questionData);
			}
			
			resultSet.close();
			statement.close();
			
			listOfQuestions.put("data", data);
			
		} catch (SQLException e) {
			log.info("Error in fetchQuestions : "+ e);
			
		}
		finally{
			ConnectionService.closeConnection();
		}
		
		return listOfQuestions.toJSONString();
	}

//Method to fetch country list 
	public static HashMap<String, Integer> getCountryList() {
		log.info("inside method getCountryList");
		HashMap<String, Integer> countryList = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
	
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetCountryDetails"));
			ResultSet resultset = statement.executeQuery();
			log.info("Query executed resultSet : " + resultset);
			while (resultset.next()) {
				countryList.put(resultset.getString("country_name"), resultset.getInt("country_id"));
			}
			resultset.close();
		} catch (SQLException e) {
			log.severe("exception while fetching Countries from table :" + e);
		} finally {
			ConnectionService.closeConnection();

		}

		return countryList;
	}
//Method to get list of state for given ountry Id
	public static HashMap<String, Integer> getStateList(int countryId) {
		log.info("inside method getStateList");
		HashMap<String, Integer> stateList = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("GetStateList"));
			statement.setInt(1, countryId);
			ResultSet resultset = statement.executeQuery();
			log.info("Query executed resultSet : " + resultset);
			while (resultset.next()) {
				stateList.put(resultset.getString("state_name"), resultset.getInt("state_id"));
			}
			resultset.close();

		} catch (SQLException e) {
			log.severe("exception while fetching States from table :" + e);
		} finally {
			ConnectionService.closeConnection();

		}

		return stateList;
	}
//Method to update law desc
	public static int updateLawDescription(int law_description_id, String law_description) {
		log.info("inside method updateLawDescription");
		/*String query = "UPDATE  Law_Description SET law_description = '" + law_description + "' WHERE law_desc_id = '"
				+ law_description_id + "' ; ";*/
		Connection connection = ConnectionService.getConnection();
		int response = -1;
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("UpdateLawDescription"));
			statement.setString(1, law_description);
			statement.setInt(2, law_description_id);
			response = statement.executeUpdate();
		} catch (SQLException e) {
			log.severe("exception updating Law desc :" + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}

	public static int ModifyQuestion(int question_id, String question) {
		log.info("inside method ModifyQuestion");
/*		String query = "UPDATE  QuestionsManagement SET possible_questions = '" + question + "' WHERE question_id = '"
				+ question_id + "' ; ";*/
		Connection connection = ConnectionService.getConnection();
		int response = -1;
		try {
			PreparedStatement statement = connection.prepareStatement(Queries.getQuery("UpdateQuestion"));
			statement.setString(1, question);
			statement.setInt(2, question_id);
			response = statement.executeUpdate();
		} catch (SQLException e) {
			log.severe("exception updating question management :"+ e);
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}

	public static int modifyTopic(int topicId, String topic, int subTopicId, String subTopic) {
		log.info("inside method modifyTopic");
		HashMap<String, Integer> topicList = getTopics();
		int checkCount = 0;
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		PreparedStatement statement = null;
		try {
		for (String topicInDb : topicList.keySet()) {
			if (topicInDb.equalsIgnoreCase(topic)) {
				if(topicList.get(topicInDb) == getTopicId(topic)){ //check if it is the same one by comparing their topic-id
					log.info("Topics up to date - no change required");
					break;									   	  //if same no operation required break
				}
				else{					
						statement = connection.prepareStatement(Queries.getQuery("UpdteTopicIdinSubTopics")); //update TopicId in subTopics
						statement.setInt(1,topicList.get(topicInDb) );
						statement.setInt(2, subTopicId);
						log.info("query formed for UpdteTopicIdinSubTopics");
						response = statement.executeUpdate();
						log.info("Topic changes updated");
						statement.close();
					/*query = "UPDATE  SubTopics SET topic_id = '" + topicList.get(topicInDb) + "' WHERE sub_topic_id = '"
							+ subTopicId + "' ; ";//update TopicId in subTopics
*/					break;
				}
			}
			checkCount++;
		}
		if (checkCount == topicList.size()) {
				log.info("Updating spelling mistake in topic");
				statement = connection.prepareStatement(Queries.getQuery("UpdateTopicName")); //modify topic name in table 
				statement.setString(1,topic );
				statement.setInt(2, topicId);
				log.info("query formed for spelling change");
				response = statement.executeUpdate();
				log.info("Topic changes updated");
				statement.close();
				/*query = "UPDATE  Topics SET topic_name = '" + topic + "' WHERE topic_id = '"
					+ topicId + "' ; ";*/
		}			
		} catch (SQLException e) {
			log.severe("exception updating question management ");
			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		checkCount = 0;
		log.info("checking sub topic for updates");
		HashMap<String, Integer> subTopicList = getSubTopics();
		try{
		for (String subTopicInDb : subTopicList.keySet()) {
			if (subTopicInDb.equalsIgnoreCase(subTopic)) {
				if(subTopicList.get(subTopicInDb) == getSubTopicId(subTopic)){//check if it is the same one by comparing the  sub - topic ids
					log.info("no update required in sub topic");
					break;
					//no operation required break
				}
				else{
						statement = connection.prepareStatement(Queries.getQuery("UpdteTopicIdinSubTopics")); //update the topic-id of, given sub-topic 
						statement.setInt(1,topicId );
						statement.setInt(2, getSubTopicId(subTopic));
						log.info("query formed for UpdteTopicIdinSubTopics");
						response = statement.executeUpdate();
						log.info("Sub Topic changes updated");
						statement.close();
					 /*query = "UPDATE  SubTopics SET topic_id = '" + topicId + "' WHERE UPPER(sub_topic) = '"
							+ subTopic.toUpperCase() + "' ; ";*/
					break; //update the topic-id of, given sub-topic
				}
			}
			checkCount++;
		}
		if (checkCount == subTopicList.size()) {
				statement = connection.prepareStatement(Queries.getQuery("UpdateSubTopicName")); //update the topic-id of, given sub-topic 
				statement.setString(1,subTopic );
				statement.setInt(2, subTopicId);
				log.info("query formed for spelling correction in SubTopic");
				response = statement.executeUpdate();
				log.info("Sub Topic changes updated");
				statement.close();
								/*query = "UPDATE  SubTopics SET sub_topic_name = '" + subTopic + "' WHERE sub_topic_id = '"
					+ subTopicId + "' ; ";//modify topic name in table 
*/					
		}
		}catch (SQLException e) {
			log.severe("exception updating question management ");
			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
	
	public static int addSubscriber(String username, String emailId,
			String password, boolean isadmin, String status) {
		log.info("inside add subscriber");
		int response = -1;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		log.info(timestamp.toString());
		Connection connection = null;
		PreparedStatement statement = null;
		try {

			connection = ConnectionService.getConnection();
			//String addSubscriberAsAdmin, addSubscriberAsUser;
			if (isadmin) {
				//addSubscriberAsAdmin = "INSERT INTO User(Username, Email , Password , Status , IsAdmin, account_creation_date) VALUES (?,?,?,?,?,?);";
				//log.info(addSubscriberAsAdmin);	
				statement = connection.prepareStatement(Queries.getQuery("addSubscriberAsAdmin"));
				statement.setString(1, username);
				statement.setString(2, emailId);
				statement.setString(3, password);
				statement.setString(4, status);
				statement.setString(5, String.valueOf(isadmin));
				statement.setString(6, String.valueOf(timestamp));
			} else {
				//addSubscriberAsUser = "INSERT INTO User(Username, Email , Status , IsAdmin, account_creation_date) VALUES (?,?,?,?,?);";
				//log.info(addSubscriberAsUser);	
				statement = connection.prepareStatement(Queries.getQuery("addSubscriberAsUser"));
				statement.setString(1, username);
				statement.setString(2, emailId);
				statement.setString(3, status);
				statement.setString(4, String.valueOf(isadmin));
				statement.setString(5, String.valueOf(timestamp));
			}
			response = statement.executeUpdate();
			log.info("subscriber added successfully");
		} catch (SQLIntegrityConstraintViolationException e) {
			log.info("email id repeated");
			response = -2;
		} catch (Exception e) {
			// TODO: handle exception
			log.severe("exception inserting the subscriber");
		} finally {
			try {
				statement.close();
				connection.close();
				ConnectionService.closeConnection();
			} catch (SQLException e) {
				log.severe("exception closing the db resources");
			}
		}
		return response;
	}

	public static int deleteSubscriber(int userId) {
		// TODO Auto-generated method stub
		log.info("inside method deleteSubscriber");
		int response = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			//String deleteSubscriberQuery = "DELETE FROM User WHERE User_ID  = ?;";
			connection = ConnectionService.getConnection();
			statement = connection.prepareStatement(Queries.getQuery("deleteSubscriberQuery"));
			statement.setInt(1, userId);
			response = statement.executeUpdate();
			log.info("Query executed response : " + response);

		}catch (SQLException e) {
			log.severe("SQLexception deleting the subscriber");
		}catch (Exception e) {
			log.severe("exception deleting the subscriber");
		}
		finally {
			try {
				statement.close();
				connection.close();
				ConnectionService.closeConnection();
			} catch (SQLException e) {
				log.severe("exception closing the db resources");
			}
		}
		return response;
	}

	public static int modifySubscriber(int userId, String username,
			String email, String password, String status, boolean isadmin,
			boolean isRoleChange) {
		// TODO Auto-generated method stub
		log.info("inside method ModifySubscriber");
		int response = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionService.getConnection();
			//String modifySubscriberAsAdmin, modifySubscriberAsUser;
			if (isRoleChange) {
				//modifySubscriberAsAdmin = "UPDATE User SET Username  = ? , Email = ? , Password = ? , Status = ? , IsAdmin = ? WHERE User_ID = ?;";
				statement= connection.prepareStatement(Queries.getQuery("modifySubscriberAsAdmin"));
				statement.setString(1, username);
				statement.setString(2, email);
				statement.setString(3, password);
				statement.setString(4, status);
				statement.setString(5, String.valueOf(isadmin));
				statement.setInt(6, userId);
			} else {
				//modifySubscriberAsUser = "UPDATE User SET Username  = ? , Email = ?, Status = ? , IsAdmin = ? WHERE User_ID = ?;";
				statement= connection.prepareStatement(Queries.getQuery("modifySubscriberAsUser"));
				statement.setString(1, username);
				statement.setString(2, email);
				statement.setString(3, status);
				statement.setString(4, String.valueOf(isadmin));
				statement.setInt(5, userId);
			}
			
			response = statement.executeUpdate();
			log.info("Query executed response : " + response);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("SQLException in modifying the subscriber");

		} finally {
			try {
				statement.close();
				connection.close();
				ConnectionService.closeConnection();
			} catch (SQLException e) {
				log.severe("exception closing the db resources");
			}
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public static String fetchSubscribers() {

		log.info("Inside method fetchSubscriberFromDB");

		JSONObject listOfSubscribers = new JSONObject();

		JSONArray data = new JSONArray();

		
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			//String fetchSubscriberQuery = "select * from User;";
			
			connection = ConnectionService.getConnection();
			statement = connection.prepareStatement(Queries.getQuery("fetchSubscriberQuery"));
			
			ResultSet subscribersResultSet = statement.executeQuery();

			while (subscribersResultSet.next()) {
				JSONObject SubscriberData = new JSONObject();
				SubscriberData.put("User_ID", subscribersResultSet.getInt("User_ID"));
				SubscriberData.put("Username", subscribersResultSet.getString("Username"));
				SubscriberData.put("Email", subscribersResultSet.getString("Email"));
				SubscriberData.put("Status", subscribersResultSet.getString("Status"));
				SubscriberData.put("IsAdmin", subscribersResultSet.getString("IsAdmin"));

				data.add(SubscriberData);

			}
			listOfSubscribers.put("data", data);

		}catch(SQLException e){
			log.severe("SqlException while fetching subscribers");
		}catch (Exception e) {
			log.severe("Exception while fetching subscribers");
		} finally {
			try {
				statement.close();
				connection.close();
				ConnectionService.closeConnection();
			} catch (SQLException e) {
				log.severe("exception closing the db resources");
			}
		}

		return listOfSubscribers.toJSONString();
	}

	public static boolean isUserAdmin(int userId) {
		log.info("checking if user is admin");
		boolean isadmin = false;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			//String isUserAdminQuery = "SELECT IsAdmin FROM User WHERE User_ID = ?;";
			//log.info(isUserAdminQuery);
			connection = ConnectionService.getConnection();
			statement = connection.prepareStatement(Queries.getQuery("isUserAdminQuery"));
			statement.setInt(1, userId);
			ResultSet rolesResultSet = statement.executeQuery();
			while (rolesResultSet.next()) {
				String role = rolesResultSet.getString("IsAdmin");
				isadmin = Boolean.parseBoolean(role.trim());
				log.info("Is user admin :" + isadmin);
			}
			rolesResultSet.close();
		}catch(SQLException e){
			log.severe("SQLException while cheking user role");
		}
		catch (Exception e) {
			log.severe("Exception while cheking user role");
		} finally {
			try {
				statement.close();
				connection.close();
				ConnectionService.closeConnection();
			} catch (SQLException e) {
				log.severe("exception closing the db resources");
			}
		}
		return isadmin;
	}
	
}
