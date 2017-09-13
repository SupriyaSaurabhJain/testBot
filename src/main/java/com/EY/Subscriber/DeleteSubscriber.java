package com.EY.Subscriber;

import java.io.IOException;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.EY.DB.DbOperation;
import com.EY.Service.ReadParameters;

/**
 * Servlet implementation class DeleteSubscriber
 */
public class DeleteSubscriber extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DeleteSubscriber.class.getName());
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonResponseObject = (JSONObject) parser.parse(responseJson);
			
			int userId = Integer.parseInt(jsonResponseObject.get("userId").toString());
			int result = DbOperation.deleteSubscriber(userId);
			
			response.getWriter().write(getResponseJson(result));

		} catch (ParseException e) {
			log.severe("Parse Exception in deleting subscriber");
		}
		
	}
	
	private static String getResponseJson(int result) {
		log.info("inside getResponse json");
		String response;
		if (result == 1) {
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}";

		}else {
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}";
		}
		return response;
	}

}
