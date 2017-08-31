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

import com.EY.ChatBot.MyWebhookServlet;
public class readFromExcel {
	private static final Logger log = Logger.getLogger(readFromExcel.class.getName());

	public static void toDb(){
		try {

			String path = readFromExcel.class.getResource("/WEB-INF/sample_data.xlsx").getPath();
			FileInputStream excelFile = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			String[] headers = new String[55];
			String[] cRow = new String[55];
			boolean firstRow = true ;
			int index = 0;

			while (iterator.hasNext()) {

				index = 0;
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();
					//getCellTypeEnum shown as deprecated for version 3.15
					//getCellTypeEnum ill be renamed to getCellType starting from version 4.0
					if (currentCell.getCellTypeEnum() ==  CellType.STRING) {

						if(firstRow){
							headers[index] = currentCell.getStringCellValue();
							log.info("header from excel : " + headers[index]);
							index++;
						}
						else{
							cRow[index] = currentCell.getStringCellValue();
							index++;
						}


					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {

						//System.out.print(currentCell.getNumericCellValue() + "--");
						if(firstRow){
							headers[index] = currentCell.getStringCellValue();
							index++;
						}
						else{
							cRow[index] = currentCell.getStringCellValue();
							index++;
						}
					}
				}
				if(!firstRow){
					//insertTopic(conn,cRow[0]);
					insertTopic(cRow[0]);
					//insertSubTopic(conn, cRow[1], cRow[0], out);
					//insertState(conn, headers, "US", out);
					//insertLawDesc(headers, cRow);
					//insertQuestion(conn, cRow[2], cRow[1], cRow[2], out);
				}
				else
				{
					insertState(headers, "US");

				}
				firstRow = false;
				//System.out.println(cRow[0]);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void insertTopic(String topic)  {
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
