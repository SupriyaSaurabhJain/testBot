package com.ey.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;


public class readFromExcel {
	private static final Logger log = Logger.getLogger(testing.class.getName());

	public static void toDb(){
	}
	static void insertTopic(Set<String> topics)  {
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			for (String topic : topics) {		
				int response = statement.executeUpdate("insert into Topics(topic_name) Values('"+topic+"')");
				log.info("added to table Topics : "+ response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("Exception adding topic to table : " + e);

			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
	}

	
	public static void insertSubTopic(HashMap<String , ArrayList<String>> topicsSubtopic){
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();

			for (String topic : topicsSubtopic.keySet()) {
				log.info("insertin sub topic : topic id : "+ topic);
				int topic_id = getTopicId(topic);
				for (String subTopic : topicsSubtopic.get(topic)) {
					int t = statement.executeUpdate("insert into SubTopics(sub_topic_name,topic_id) Values('"+subTopic+"','"+topic_id+"')");
					log.info("sub topic added " + t);
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding sub topic : " + e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
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

	public static  void insertState(String[] headers, String country){
		HashSet<String> state = new HashSet<String>();
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			for (int i = 5; i < headers.length; i++) {

				state.add(headers[i].trim().toUpperCase());

			}
			log.info("Total states : " + state.size());
			for (String stringState : state) {
				int t1 = 1;
				int response = statement.executeUpdate("insert into State(state_name,country_id) Values('"+stringState+"','"+t1+"')");
				log.info("state added" + stringState + "response "+ response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding state : "+ e);
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}



	}

	public  static void insertLawDesc(TreeMap<String, HashMap<String, String>> descriptionLib){ // descriptionLib ::  map of subTopic --> <state,law>
		log.info("inside method law desc insert");
		//		Connection connection = ConnectionService.getConnection();
		//Statement statement;
		try {
			//			statement = connection.createStatement();
			for (String  subTopic : descriptionLib.keySet()) {
				log.info("subTopic : " + subTopic);
				int subTopicId = getSubTopicId(subTopic);
				HashMap<String , String> stateLawMap = descriptionLib.get(subTopic.toUpperCase());

				for (String state : stateLawMap.keySet()) {

					String escapeSequence = "\\\\" ;
					log.info("state : " + state);
					String lawDescription = stateLawMap.get(state).replaceAll("\'", escapeSequence+"\'").replaceAll("\"", escapeSequence+"\""); //.replaceAll("\n", "/\n").replaceAll("\t", "/\t");
					if (state.equalsIgnoreCase("FEDERAL")) {
						log.info("insert into Law_Description(law_description1,country_id,state_id,sub_topic_id) Values('"+lawDescription+"','"+"1"+"','"+"NULL"+"','"+subTopicId+"')");
						insertDescIntoDb("insert into Law_Description1(law_description,country_id,sub_topic_id) Values('"+lawDescription+"','"+"1"+"','"+subTopicId+"')");
					}
					else{
						int state_id = getstateId(state); 
						log.info("insert into Law_Description(law_description1,state_id,sub_topic_id) Values('"+lawDescription+"','"+state_id+"','"+subTopicId+"')");
						insertDescIntoDb("insert into Law_Description1(law_description,country_id,state_id,sub_topic_id) Values('"+lawDescription+"','"+"1"+"','"+state_id+"','"+subTopicId+"')");

					}
				}

			}
			log.info("Law description added ");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding law desc : "+ e);
			e.printStackTrace();
		}
		finally{
			//ConnectionService.closeConnection();
		}
	}
	private static int  insertDescIntoDb(String Query) throws SQLException{

		Connection connection = ConnectionService.getConnection();
		Statement statement = connection.createStatement();
		int response = statement.executeUpdate(Query);
		ConnectionService.closeConnection();
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

	public static  int getSubTopicId(String subtopic){
		Connection connection = ConnectionService.getConnection();
		int subTopic_id = -1;
		subtopic = subtopic.trim().toUpperCase();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select sub_topic_id from SubTopics where sub_topic_name='"+subtopic+"';");
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

	public  static void insertQuestion(String question, String topic, String subtopic)  {
		question = question.replaceAll("\'", "");
		Connection connection = ConnectionService.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();	
			int topic_id = getTopicId(topic);
			int sub_topic_id = getSubTopicId(subtopic);
			int uid = 1;
			int t = statement.executeUpdate("insert into QuestionsMgnt(possible_questions,questions_type,User_id,topic_id,sub_topic_id) Values('"+question+"','SYSTEM','"+uid+"','"+topic_id+"','"+sub_topic_id+"')");	
			log.info("sucessfully addded question" + t);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding question");
			e.printStackTrace();
		}
		finally{
			ConnectionService.closeConnection();
		}
	}

}
