package com.ey.db;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class DbOperation extends ConnectionService {
	private static final Logger log = Logger.getLogger(DbOperation.class.getName());
	//Method to get list of topics along with their Id 
	//Returns hashMap <topic, topicId>
	public static HashMap<String, Integer> getTopics() {
		log.info("inside method getTopic");
		HashMap<String, Integer> topics = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT topic_id , topic_name FROM Topics");
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
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT sub_topic_id , sub_topic_name FROM SubTopics");
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
		Statement statement;
		int response = -1;
		String query = "INSERT INTO SubTopics(sub_topic_name ,topic_id) VALUES('" + subTopic + "','" + topicId + "')" ;
		try {
			statement = connection.createStatement();
			response = statement.executeUpdate(query);	
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
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select topic_id from Topics where topic_name='" + topic + "';");
			while (rs.next()) {
				topic_id = rs.getInt("topic_id");
			}
			rs.close();
			statement.close();
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
		Statement statement;
		int response = -1;
		try {
			statement = connection.createStatement();
			response = statement.executeUpdate("insert into Topics(topic_name) Values('" + topic + "')");
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
			Statement statement = connection.createStatement();
			response = statement.executeUpdate("DELETE FROM Topics WHERE topic_name = ' " + topic + "'");
			log.info("Query executed response : " + response);
			statement.close();
		} catch (SQLException e) {
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
			Statement statement = connection.createStatement();
			response = statement.executeUpdate("DELETE FROM SubTopics WHERE sub_topic_name = ' " + subTopic + "'");
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
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT sub_topic_id FROM SubTopics WHERE sub_topic_name='" + subtopic + "';");
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
		String Query = "";
		int subTopic_id = getSubTopicId(subTopic.toUpperCase());
		Connection connection = ConnectionService.getConnection();
		if (state.toUpperCase().equalsIgnoreCase("FEDERAL")) { // check if its for federal or some state
			Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '" + subTopic_id
					+ "' AND state_id IS NULL;";
		} else {
			int state_id = getstateId(state.toUpperCase());
			Query = "SELECT law_description FROM Law_Description WHERE sub_topic_id = '" + subTopic_id
					+ "' AND state_id = '" + state_id + "';";
		}
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(Query);
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
		String Query = "";
		JSONObject response = new JSONObject();
		Connection connection = ConnectionService.getConnection();
		Query = "SELECT law_description , law_desc_id FROM Law_Description WHERE sub_topic_id = '" + subTopicId	//same querry as above for state
				+ "' AND state_id = '" + stateId + "';";
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(Query);
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
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select state_id from State where state_name='" + state + "';");
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
		String query = "INSERT INTO QuestionsManagement(possible_questions , questions_type , User_ID , sub_topic_id ,topic_id) VALUES"
				+ " ('" + question + "' , 'USER' , ' " + userId + "' , '" + subTopicId + "' , '" + topicId + "') ;";
		log.info(query);
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			response = statement.executeUpdate(query);
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
		// TODO Auto-generated method stub
		log.info("inside method deleteQuestion");
		int response = 0;
		Connection connection = ConnectionService.getConnection();
		String query = "DELETE FROM QuestionsManagement WHERE question_id  = ' " + questionId + "'";
		try {
			Statement statement = connection.createStatement();
			response = statement.executeUpdate(query);
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
		log.info("inside method getLawDescriptionId");
		int descriptionId = -1;
		String query = "SELECT law_desc_id FROM Law_Description WHERE  subTopicId = '" + subTopicId + "' AND country_id = '"
				+ countryId + "' AND state_id = '" + stateId + "' ;";
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				descriptionId = resultSet.getInt("law_desc_id");
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			log.severe("exception geting law desc id" + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return descriptionId;
	}

	//Method to add New law description
	public static int addLawDescriptionToDB(int topicId, int subTopicId, int countryId, int stateId,
			String description) {
		log.info("inside method addLawDescriptionToDB");
		int response = -1;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		log.info("Timestamp " + timestamp);
		int descriptionId = getLawDescriptionId(subTopicId, countryId, stateId);
		String query = "";
		//check if law description for provided state already exists
		if (descriptionId == -1) {
			query = "INSERT INTO Law_Description(law_description , state_id , country_id , sub_topic_id, CreateTimeStamp, ModifiedTimestamp) VALUES"
					+ " ('" + description + "' , '" + stateId + "' , ' " + countryId + "' , '" + subTopicId + "' , '"
					+ timestamp + "' , '" + timestamp + "') ;";
			Connection connection = ConnectionService.getConnection();
			Statement statement;
			try {
				statement = connection.createStatement();
				response = statement.executeUpdate(query);
				log.info("description added sucessfully");
				statement.close();
			} catch (SQLException e) {
				log.severe("Exception Adding Law Description : " + e);
			} finally {
				ConnectionService.closeConnection();

			}
		} 
		//if already present update the same
		else {
			response = updateLawDescription(descriptionId, description);
			/*
			 * query = "UPDATE  Law_Description SET law_description = '"
			 * +description+"', state_id = '"+ stateId
			 * +"', country_id = '"+countryId +"', sub_topic_id = '"+subTopicId
			 * +"' , ModifiedTimestamp = '"+timestamp +"'" +
			 * "		WHERE law_desc_id = '" +descriptionId+ "' ; " ;
			 */
		}
		return response;
	}
//Method to get countryId of state provided
	private static int getCountryIdFromState(String state) {
		log.info("inside method getCountryIdFromState");

		Connection connection = ConnectionService.getConnection();
		int countryId = -1;
		state = state.trim().toUpperCase();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("select country_id from State where UPPER(state_name) = '" + state + "';");
			while (rs.next()) {
				countryId = rs.getInt("country_id");
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
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

//method to get list of countries with their respective ids
	public static HashMap<String, Integer> getCountryList() {
		log.info("inside method getCountryList");
		HashMap<String, Integer> countryList = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
		String query = "SELECT country_id , country_name FROM Country ;";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery(query);
			log.info("Query executed resultSet : " + resultset);
			while (resultset.next()) {
				countryList.put(resultset.getString("country_name"), resultset.getInt("country_id"));
			}
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			log.severe("exception while fetching Countries from table :" + e);
		} finally {
			ConnectionService.closeConnection();

		}
		return countryList;
	}
	//method to get list of states with their respective ids
	public static HashMap<String, Integer> getStateList(int countryId) {
		log.info("inside method getStateList");
		HashMap<String, Integer> stateList = new HashMap<String, Integer>();
		Connection connection = ConnectionService.getConnection();
		String query = "SELECT * FROM State WHERE country_id = '" + countryId + "' ;";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery(query);
			log.info("Query executed resultSet : " + resultset);
			while (resultset.next()) {
				stateList.put(resultset.getString("state_name"), resultset.getInt("state_id"));
			}
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			log.severe("exception while fetching States from table :" + e);
		} finally {
			ConnectionService.closeConnection();
		}
		return stateList;
	}
//Method to update law description in db
	public static int updateLawDescription(int law_description_id, String law_description) {
		log.info("inside method updateLawDescription");
		String query = "UPDATE  Law_Description SET law_description = '" + law_description + "'"
				+ " WHERE law_desc_id = '"
				+ law_description_id + "' ; ";
		Connection connection = ConnectionService.getConnection();
		int response = -1;
		try {
			Statement statement = connection.createStatement();
			response = statement.executeUpdate(query);
		} catch (SQLException e) {
			log.severe("exception updating Law desc ");
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
//Method to modify question in db
	public static int ModifyQuestion(int question_id, String question) {
		log.info("inside method ModifyQuestion");
		String query = "UPDATE  QuestionsManagement SET possible_questions = '" + question + "' WHERE question_id = '"
				+ question_id + "' ; ";
		Connection connection = ConnectionService.getConnection();
		int response = -1;
		try {
			Statement statement = connection.createStatement();
			response = statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			log.severe("exception updating question in ModifyQuestion ");
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
//Method to modify topic/subtopic in db
	public static int modifyTopic(int topicId, String topic, int subTopicId, String subTopic) {
		log.info("inside method modifyTopic");
		HashMap<String, Integer> topicList = getTopics();
		int checkCount = 0;
		String query="";
		int response = -1;
		Connection connection = ConnectionService.getConnection();
		for (String topicInDb : topicList.keySet()) {
			if (topicInDb.equalsIgnoreCase(topic)) {
				if(topicList.get(topicInDb) == getTopicId(topic)){ //check if it is the same one by comparing their topic-id
					break;									   	  //if same no operation required break
				}
				else{
					query = "UPDATE  SubTopics SET topic_id = '" + topicList.get(topicInDb) + "' WHERE sub_topic_id = '"
							+ subTopicId + "' ; ";//update TopicId in subTopics
					break;

				}
			}
			checkCount++;
		}
		if (checkCount == topicList.size()) {
			query = "UPDATE  Topics SET topic_name = '" + topic + "' WHERE topic_id = '"
					+ topicId + "' ; ";//modify topic name in table 
		}
		try {
			Statement statement = connection.createStatement();
			response = statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			log.severe("exception updating topic ");
		} finally {
			ConnectionService.closeConnection();
		}
		checkCount = 0;
		HashMap<String, Integer> subTopicList = getSubTopics();
		for (String subTopicInDb : subTopicList.keySet()) {
			if (subTopicInDb.equalsIgnoreCase(subTopic)) {
				if(subTopicList.get(subTopicInDb) == getSubTopicId(subTopic)){//check if it is the same one by comparing the  sub - topic ids
					break;
					//no operation required break
				}
				else{
					query = "UPDATE  SubTopics SET topic_id = '" + topicId + "' WHERE UPPER(sub_topic) = '" //use above update query fetching subtopic ids
							+ subTopic.toUpperCase() + "' ; ";
					break; //update the topic-id of, given sub-topic
				}
			}
			checkCount++;
		}
		if (checkCount == subTopicList.size()) {
			query = "UPDATE  SubTopics SET sub_topic_name = '" + subTopic + "' WHERE sub_topic_id = '"
					+ subTopicId + "' ; ";//modify topic name in table 
		}
		try {
			Statement statement = connection.createStatement();
			response = statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			log.severe("exception updating question management ");
		} finally {
			ConnectionService.closeConnection();
		}
		return response;
	}
}