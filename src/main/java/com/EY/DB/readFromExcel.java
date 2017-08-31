package com.EY.DB;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class readFromExcel {
	private static final Logger log = Logger.getLogger(testing.class.getName());

	public static void toDb(){
	}
	static void insertTopic(String topic)  {
		Connection connection = ConnectionDetails.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			int response = statement.executeUpdate("insert into Topics(topic_name) Values('"+topic+"')");
			log.info("added to table Topics : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("Exception adding topic to table : " + e);

			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}
	}

	public static void insertSubTopic(String subtopic, String topic){
		int topic_id = getTopicId(topic);
		log.info("insertin sub topic : topic id : "+ topic_id);
		Connection connection = ConnectionDetails.getConnection();
		Statement statement;

		try {
			statement = connection.createStatement();
			int t = statement.executeUpdate("insert into SubTopics(sub_topic_name,topic_id) Values('"+subtopic+"','"+topic_id+"')");
			log.info("sub topic added ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding sub topic : " + e);
			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}
	}

	public static  int getTopicId(String topic) {
		Connection connection = ConnectionDetails.getConnection();
		int topic_id = -1;
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

	public static  void insertState(String[] headers, String country){
		Connection connection = ConnectionDetails.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			for (int i = 5; i < headers.length; i++) {
				int t1 = 1;
				int response = statement.executeUpdate("insert into State(state_name,country_id) Values('"+headers[i]+"','"+t1+"')");
				log.info("state added");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding state : "+ e);
			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}



	}

	public  static void insertLawDesc(String[] headers, String[] curRow){
		int law_id = 0;
		Connection connection = ConnectionDetails.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			for (int i = 3; i < curRow.length; i++) {
				curRow[i] = curRow[i].replaceAll("\'", "").replaceAll("\"", "").replaceAll("\n", "").replaceAll("\t", "");
				law_id++;
				if(i==3)
				{
					int country_id = 1;
					int topic_id= getTopicId(curRow[0]);
					int t = statement.executeUpdate("insert into Law_Description(law_description,country_id,topic_id) Values('"+curRow[i]+"','"+country_id+"','"+topic_id+"')");
				}
				else
				{
					/*int id = 1;
				conn = createDBConnection();
				int id1 = getstateId(conn, headers[i], out);
				int id2 = getTopicId(conn, curRow[0], out);
				stmt = conn.createStatement();
				int t = stmt.executeUpdate("insert into Law_Description(law_description,state_id,country_id,topic_id) Values('"+curRow[i]+"','"+id1+"','"+id+"','"+id2+"')");
				conn.close();*/
				}
			}
			log.info("Law description added ");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception adding state : "+ e);
			e.printStackTrace();
		}
		finally{
			ConnectionDetails.closeConnection();
		}
	}

	public static  int getstateId(String state){
		Connection connection = ConnectionDetails.getConnection();
		int state_id = -1;
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
		log.info("state id added : "+ state_id);
		return state_id;
	}

	public static  int getSubTopicId(String subtopic){
		Connection connection = ConnectionDetails.getConnection();
		int subTopic_id = -1;
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select sub_topic_id from SubTopics where sub_topic_name='"+subtopic+"';");
			int id=-1;
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
			ConnectionDetails.closeConnection();
		}
		log.info("subTopic id added : "+ subTopic_id);
		return subTopic_id;
	} 

	public  static void insertQuestion(String question, String topic,String subtopic)  {
		question = question.replaceAll("\'", "");
		Connection connection = ConnectionDetails.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();	
			int topic_id = getTopicId(topic);
			int sub_topic_id = getSubTopicId(subtopic);
			int uid = 1;
			int t = statement.executeUpdate("insert into QuestionsMgnt(possible_questions,questions_type,User_id,topic_id,sub_topic_id) Values('"+question+"','SYSTEM','"+uid+"','"+topic_id+"','"+sub_topic_id+"')");	
			log.info("sucessfully addded question");
		}catch (SQLException e) {
		// TODO Auto-generated catch block
		log.severe("exception adding question");
		e.printStackTrace();
	}
	finally{
		ConnectionDetails.closeConnection();
	}
	}
	
}
