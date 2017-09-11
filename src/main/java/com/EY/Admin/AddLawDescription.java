package com.EY.Admin;

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

public class AddLawDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddLawDescription.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String responseJson = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			// System.out.println(jsonResponseObject);
			int topicId = Integer.parseInt(jsonResponseObject.get("topicId").toString());
			int subTopicId = Integer.parseInt(jsonResponseObject.get("subTopicId").toString());
			int countryId = Integer.parseInt(jsonResponseObject.get("countryId").toString());
			int stateId = Integer.parseInt(jsonResponseObject.get("stateId").toString());
			String description = jsonResponseObject.get("description").toString();
			response.getWriter().write(addLawDescription(topicId, subTopicId, countryId, stateId, description));

		} catch (ParseException e) {

			e.printStackTrace();
		}
	}

	private String addLawDescription(int topicId, int subTopicId, int countryId, int stateId, String description) {
		String response = "";
		int result = DbOperation.addLawDescriptionToDB(topicId, subTopicId, countryId, stateId, description);
		log.info("result in add desc :" + result);

		response = getErrorResponse(result);

		log.info("Response : " + response);
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
