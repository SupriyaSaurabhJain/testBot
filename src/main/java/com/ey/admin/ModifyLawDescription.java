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
import com.ey.util.ReadParameters;

public class ModifyLawDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ModifyLawDescription.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		log.info("Inside ModifyLawDescription ");
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			int law_description_id = Integer.parseInt(requestObject.get("law_description_id").toString());
			String law_description = requestObject.get("law_description").toString();
			log.info("law_description_id : " + law_description_id + " law_description : "+law_description);
			response.getWriter().write(modifyLawDescription(law_description_id , law_description));
					
		} catch (Exception e) {
			log.info("Exception in fetching request parameters in ModifyLawDescription:"+e);
		}
		
	}

	private String modifyLawDescription(int law_description_id, String law_description) {
		log.info("inside method modify law description");
		int result = DbOperation.updateLawDescription(law_description_id, law_description);		//function call to modify law description in database
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
