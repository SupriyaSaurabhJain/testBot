package com.EY.DBConnection;

import java.sql.*;
import java.util.logging.Logger;

import com.EY.ChatBot.MyWebhookServlet;

public class DbOperation extends ConnectionDetails{
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());

	public void addNewTopicToDB(String topic , String subTopic){
		log.info("inside method addTopic");
		Connection connection = ConnectionDetails.getConnection();
		try {
			Statement statement =  connection.createStatement();
			int response = statement.executeUpdate("insert into sample Values('Benifit', 'Group Insurance')");
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
	}
}
