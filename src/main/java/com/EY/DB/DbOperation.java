package com.EY.DB;

import java.sql.*;
import java.util.logging.Logger;

import com.EY.ChatBot.MyWebhookServlet;

public class DbOperation extends ConnectionDetails{
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());

	public int addNewTopicToDB(String topic , String subTopic){
		log.info("inside method addTopic");
		int response = 0;
		Connection connection = ConnectionDetails.getConnection();
		try {
			Statement statement =  connection.createStatement();
			 response = statement.executeUpdate("insert into sample Values(' "+topic +" ' , '" + subTopic +"')");
			log.info("Query executed response : "+ response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception while inseting to table :" + e);
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
	public String getResponse(String topic , String state , String country){
		log.info("inside getResponse");
		String response = "";
		Connection connection = ConnectionDetails.getConnection();
		try {
			Statement statement =  connection.createStatement();
			 response = "";
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
		return "";
	}
}}
