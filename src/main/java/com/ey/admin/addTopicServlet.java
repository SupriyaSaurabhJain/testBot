package com.ey.admin;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ey.apiai.Handler.*;
import com.ey.db.*;
import com.ey.service.ReadParameters;

public class addTopicServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(addTopicServlet.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		log.info("inside addTopicServlet");

		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			String topic = jsonResponseObject.get("topic").toString();
			String subTopic = jsonResponseObject.get("subTopic").toString();
			log.info("parameters received :  topic : " + topic + " subTopic : " + subTopic );
			response.getWriter().write(addTopic(topic, subTopic));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String addTopic(String topic, String subTopic) {
		// TODO Auto-generated method stub
		String response = "";
		int resultFromAPI ;
		int resultFromDb =  DbOperation.addNewTopicSubTopicToDB(topic, subTopic); // Method call to add topic & sub-topic to database
		log.info("result in addTopic :" + resultFromDb);
		if (resultFromDb == 1) {			// if topic/subtopic addition to database is succeeded result = 1, then add tp API AI
			resultFromAPI = APIHandler.addTopic(subTopic, subTopic); //Method call to add topic to API AI
			if (resultFromAPI == 1) { //check if subTopic successfully added to API AI
				response = getErrorResponse(resultFromAPI);  //if success set response message  
			}
			else{
				DbOperation.deleteSubTopicFromDb(subTopic); // if not added successfully roll back all the transactions, deleting entries from database
			}
		}
		else{
			response = getErrorResponse(resultFromDb) ; // if entity not added successfully set response

		}
		log.info("Response from assTopicServlet: "+ response);
		return response;
	}

	private static String getErrorResponse(int resultFromDb){
		String errorResponse ="";
		if (resultFromDb == 1) {
			errorResponse = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}";
		}
		else{
			errorResponse = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
		}
		return errorResponse;
	}

}
