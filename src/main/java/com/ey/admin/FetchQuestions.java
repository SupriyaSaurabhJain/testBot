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
import com.ey.util.ReadParameters;

public class FetchQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FetchQuestions.class.getName());
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		String responseString = "{}";
		
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			
			int topic_id = Integer.parseInt(requestObject.get("topic_id").toString());
			int sub_topic_id = Integer.parseInt(requestObject.get("sub_topic_id").toString());
				
			responseString = DbOperation.fetchQuestions(topic_id, sub_topic_id);	
			
		} catch (ParseException e) {
			log.info("Error in doPost:"+e);
	
		}

		response.setContentType("application/json");
		response.getWriter().write(responseString);
		
	}

}
