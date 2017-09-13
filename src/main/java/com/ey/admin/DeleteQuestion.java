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
import com.ey.db.*;
import com.ey.service.*;
import com.ey.util.ReadParameters;

public class DeleteQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DeleteQuestion.class.getName());

	public DeleteQuestion() {
		super();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("inside deletQuestion ");		
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			int questionId = Integer.parseInt(jsonResponseObject.get("questionId").toString());
			log.info("Request parameters in DeleteQuestion : questionId : "+ questionId);
			response.getWriter().write(removeQuestion(questionId));

		} catch (ParseException e) {
			log.severe("Exception fetching request parameters in DeleteQuestion : "+ e);		}	}
	
	private String removeQuestion(int questionId) {
		String response = "";
		int result = DbOperation.deleteQuestionFromDb(questionId); //method to delete question from database returns 1 if success
		log.info("result in delete que :" + result);
		if (result == 1) { 
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Sucess\"  }}" ;
		}
		else{
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
		}
		log.info("Response from removeQuestion method : "+ response);
		return response;
	}

}
