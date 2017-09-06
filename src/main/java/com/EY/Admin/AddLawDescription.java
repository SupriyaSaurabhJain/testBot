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
import com.EY.ChatBot.MyWebhookServlet;
import com.EY.DB.DbOperation;
import com.EY.Service.ReadParameters;

public class AddLawDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());

	public AddLawDescription() {
		super();
	}

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
			String country = jsonResponseObject.get("country").toString();
			String state = jsonResponseObject.get("state").toString();
			String description = jsonResponseObject.get("description").toString();	
			int descriptionId = Integer.parseInt(jsonResponseObject.get("descriptionId").toString());			

			response.getWriter().write(addLawDescription(topic, subTopic, country , state, description,descriptionId));

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String addLawDescription(String topic, String subTopic, String country, String state, String description , int descriptionId) {
		String response = "";
		int result =  DbOperation.addLawDescriptionToDB(topic, subTopic, country , state, description ,descriptionId);
		log.info("result in add desc :" + result);

		response = getErrorResponse(result) ;


		log.info("Response : "+ response);
		return response;	
	}
	private static String getErrorResponse(int result){


		String response ;
		if (result == 1 ) {
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Sucess\"  }}" ;

		}
		else {
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
		}
		return response;
	}

}
