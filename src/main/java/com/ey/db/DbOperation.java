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

		} catch (SQLException e) {
			log.severe("exception fetching sub topic id "+ e);
			e.printStackTrace();
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
			/*Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '" + subTopic_id
					+ "' AND state_id IS NULL;";*/
			query = Queries.getQuery("GetLawDescriptionForFederal");
			statement = connection.prepareStatement(query);
			statement.setInt(1, subTopic_id);
		} else {
			int state_id = getstateId(state.toUpperCase());
		/*	Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '" + subTopic_id
					+ "' AND state_id = '" + state_id + "';";*/
			query = Queries.getQuery("GetLawDescriptionForState");
			statement = connection.prepareStatement(query);
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
		/*String query = "INSERT INTO QuestionsManagement(possible_questions , questions_type , User_ID , sub_topic_id ,topic_id) VALUES"
				+ " ('" + question + "' , 'USER' , ' " + userId + "' , '" + subTopicId + "' , '" + topicId + "') ;";*/
		Connection connection = ConnectionService.getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(Queries.getQuery("InsertQuestion"));
			statement.setString(1, question);
			statement.setString(2, "USER");
			statement.setInt(3,userId );
			statement.setInt(4,subTopicId );
			statement.setInt(5,topicId );
			response = statement.executeUpdate();
			log.info("question added sucessfully");
			statement.close();
		} catch (SQLException e) {
			log.severe("exception adding question : " + e);
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

	public static String fetchComplianceDetailsFromDB(int page_number) {

		log.info("Inside method fetchComplianceDetailsFromDB");

		JSONObject complianceDetails = new JSONObject();
		JSONArray dataArray = new JSONArray();

		String queryToFetchTopicSubtopic = "select T.topic_id, T.topic_name, ST.sub_topic_id, ST.sub_topic_name from Topics T, SubTopics ST WHERE T.topic_id = ST.topic_id limit "
				+ (page_number * 10) + ",10;";

		Connection connection = ConnectionService.getConnection();

		try {

			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(queryToFetchTopicSubtopic);
			while (rs.next()) {

				JSONObject rowData = new JSONObject();

				rowData.put("topic_name", rs.getString("topic_name"));
				rowData.put("sub_topic_name", rs.getString("sub_topic_name"));
				rowData.put("topic_id", rs.getString("topic_id"));
				rowData.put("sub_topic_id", rs.getString("sub_topic_id"));
				String queryToFetchNumberOfQuestions = "select count(question_id) as number_of_questions from QuestionsManagement group by sub_topic_id having sub_topic_id = "
						+ rs.getInt("sub_topic_id") + ";";

				// log.info(queryToFetchNumberOfQuestions);

				Statement statement2 = connection.createStatement();

				ResultSet rs2 = statement2.executeQuery(queryToFetchNumberOfQuestions);

				if (rs2.next())
					rowData.put("number_of_questions", rs2.getInt("number_of_questions"));
				else
					rowData.put("number_of_questions", 0);

				// log.info(rowData.toJSONString());

				rs2.close();

				dataArray.add(rowData);

			}

			complianceDetails.put("data", dataArray);

		} catch (Exception e) {

			log.info("Error" + e);

			e.printStackTrace();
		} finally {
			ConnectionService.closeConnection();
		}

		return complianceDetails.toJSONString();
	}

	public static String fetchQuestionsFromDB(int topic_id, int sub_topic_id) {

		log.info("Inside method fetchQuestionsFromDB");

		JSONObject listOfQuestions = new JSONObject();

		JSONArray data = new JSONArray();

		Connection connection = ConnectionService.getConnection();

		String queryToFetchQuestions = "select * from QuestionsManagement where topic_id = " + topic_id
				+ " and sub_topic_id = " + sub_topic_id + ";";

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(queryToFetchQuestions);

			while (rs.next()) {
				JSONObject questionData = new JSONObject();
				questionData.put("question_id", rs.getInt("question_id"));
				questionData.put("question", rs.getString("possible_questions"));
				questionData.put("question_type", rs.getString("questions_type"));
				questionData.put("user_id", rs.getInt("User_ID"));

				data.add(questionData);

			}

			rs.close();

			listOfQuestions.put("data", data);

		} catch (Exception e) {
			log.info("Error in fetchQuestionsFromDB : " + e);

			e.printStackTrace();
		} finally {
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
		int response = -1;
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
}
