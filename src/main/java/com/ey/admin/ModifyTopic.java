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

public class ModifyTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ModifyTopic.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();

		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			int topicId = Integer.parseInt(requestObject.get("topicId").toString().trim());
			String topic = requestObject.get("topic").toString().trim();
			int subTopicId = Integer.parseInt(requestObject.get("subtopicId").toString().trim());
			String subTopic = requestObject.get("subtTopic").toString().trim();
			log.info("topicId : " + topicId + " subTopicId : "+subTopicId+ "  topic : "+topic + " subTopic : "+subTopic);
			response.getWriter().write(modifyTopic(topicId , topic , subTopicId , subTopic));

		} catch (Exception e) {
			log.info("Exception in fetching request parameters in ModifyTopic :"+e);
		}
	}

	private String modifyTopic(int topicId, String topic, int subTopicId, String subTopic) {
		int result = DbOperation.modifyTopic(topicId , topic, subTopicId, subTopic); //Method call to modify in DB
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
