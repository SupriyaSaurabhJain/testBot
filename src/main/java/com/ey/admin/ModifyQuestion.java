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
import com.ey.service.*;
 class ModifyQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ModifyQuestion.class.getName());

    public ModifyQuestion() {
        super();
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			int question_id = Integer.parseInt(requestObject.get("question_id").toString());
			String question = requestObject.get("question").toString();
			response.getWriter().write(ModifyQuestion(question_id , question));
					
		} catch (Exception e) {
			log.info("Error in doPost:"+e);

			response.getWriter().write("{}");	
		}
	}

	private String ModifyQuestion(int question_id, String question) {
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
