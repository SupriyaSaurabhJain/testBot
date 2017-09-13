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
import com.ey.service.*;
import com.ey.db.*;

public class AddNewQuestion extends HttpServlet {
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		log.info("inside AddNewQuestion");
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			String topic = jsonResponseObject.get("topic").toString();
			String subTopic = jsonResponseObject.get("subTopic").toString();
			String question =  jsonResponseObject.get("question").toString();
			int userId = Integer.parseInt(jsonResponseObject.get("userId").toString());
			log.info("parameters received :  topic : " + topic + " subTopic : " + subTopic + " question : "
					+ question + " userId : " + userId);

			response.getWriter().write(addQuestion(topic, subTopic , question ,userId));

		} catch (ParseException e) {
			log.severe("Exception while extracting parameters inside addNewQuestion : "+ e);
		}
	}
	private static String addQuestion(String topic, String subTopic ,String question, int userId) {
		// TODO Auto-generated method stub
		String response = "";
		
		int result = DbOperation.addNewQuestionToDB(topic, subTopic , question ,userId);
		response = getErrorResponse(result);
		log.info("Response to be sent : " + response);
		return response;
	}
	
	private static String getErrorResponse(int result) {
		String response;
		if (result == 1) {
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}";

		} else {
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}";
		}
		return response;
	}
}
