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

public class DeleteQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());

	public DeleteQuestion() {
		super();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			int questionId = Integer.parseInt(jsonResponseObject.get("questionId").toString());
			response.getWriter().write(removeQuestion(questionId));

		} catch (ParseException e) {
			e.printStackTrace();
		}	}
	private String removeQuestion(int questionId) {
		String response = "";

		int result = DbOperation.deleteQuestionFromDb(questionId);
		log.info("result in delete que :" + result);
		if (result == 1) {
			// to api ai 
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Sucess\"  }}" ;


		}
		else{
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;

		}
		log.info("Response : "+ response);
		return response;
	}

}
