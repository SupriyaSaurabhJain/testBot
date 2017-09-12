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
import com.ey.service.*;

public class addTopicServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(addTopicServlet.class.getName());

	private static final long serialVersionUID = 1L;



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			String topic = jsonResponseObject.get("topic").toString();
			String subTopic = jsonResponseObject.get("subTopic").toString();
			response.getWriter().write(addTopic(topic, subTopic));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String addTopic(String topic, String subTopic) {
		// TODO Auto-generated method stub
		String response = "";
		int result =  DbOperation.addNewTopicToDB(topic, subTopic);
		log.info("result in addTopic :" + result);
		if (result == 1) {
			response =new APIHandler().addTopic(subTopic, subTopic);
		
		}
		else{
			response = getErrorResponse() ;
			
		}
		log.info("Response : "+ response);
		return response;
	}
	private static String getJsonStringEntityForElement(String topic , String subTopic){
		String inputJson = "[{\"value\": \""+ subTopic+ "\",\"synonyms\": []}]" ;
		return inputJson;
	}
	private static String getErrorResponse(){
		String errorResponse = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
		return errorResponse;
	}
	
}
