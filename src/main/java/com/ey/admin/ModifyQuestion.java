package com.ey.admin;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ey.db.*;
import com.ey.util.ReadParameters;
 public class ModifyQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ModifyQuestion.class.getName());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Inside ModifyQuestion servlet");
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			int question_id = Integer.parseInt(requestObject.get("question_id").toString());
			String question = requestObject.get("question").toString();
			log.info("question_id : " + question_id + " question : "+question);
			response.getWriter().write(modifyQuestion(question_id , question));				
		} catch (Exception e) {
			log.info("Exception in fetching request parameters in ModifyQuestion :"+e);
	
		}
	}
//Method to modify question in database
	private String modifyQuestion(int question_id, String question) {
		int result = DbOperation.ModifyQuestion(question_id,question);
		return getErrorResponse(result);
	}
	private static String getErrorResponse(int result){
		String response ;
		if (result == 1 ) {
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}" ;

		}
		else {
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
		}
		return response;
	}

}
