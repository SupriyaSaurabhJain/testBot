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
import com.ey.db.DbOperation;
import com.ey.service.ReadParameters;

public class AddLawDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddLawDescription.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String responseJson = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		log.info("inside addLawDescription");
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			// Fetching parameters from request object
			int topicId = Integer.parseInt(jsonResponseObject.get("topicId").toString());
			int subTopicId = Integer.parseInt(jsonResponseObject.get("subTopicId").toString());
			int countryId = Integer.parseInt(jsonResponseObject.get("countryId").toString());
			int stateId = Integer.parseInt(jsonResponseObject.get("stateId").toString());
			String description = jsonResponseObject.get("description").toString();
			log.info("parameters received :  topicId : " + topicId + " subTopicId : " + subTopicId + " countryId : "
					+ countryId + " stateId : " + stateId + " description : " + description);
			response.getWriter().write(addLawDescription(topicId, subTopicId, countryId, stateId, description)); // function call to add law description 

		} catch (ParseException e) {

			log.severe("Exception while extracting parameters in addLawDescription : " + e);
		}
	}

	private String addLawDescription(int topicId, int subTopicId, int countryId, int stateId, String description) {
		String response = "";
		int result = DbOperation.addLawDescriptionToDB(topicId, subTopicId, countryId, stateId, description); // function call to add law description to database
		log.info("result from DbOperation.addLawDescriptionToDB :" + result);
		response = getErrorResponse(result);
		log.info("Response to be sent : " + response);
		return response;
	}
// Method to format response
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
