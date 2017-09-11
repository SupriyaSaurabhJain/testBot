package com.EY.Admin;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.EY.DB.DbOperation;
import com.EY.Service.ReadParameters;

public class ModifyLawDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ModifyLawDescription.class.getName());

    public ModifyLawDescription() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			int law_description_id = Integer.parseInt(requestObject.get("law_description_id").toString());
			String law_description = requestObject.get("law_description").toString();
			response.getWriter().write(ModifyLawDescription(law_description_id , law_description));
					
		} catch (Exception e) {
			log.info("Error in doPost:"+e);

			response.getWriter().write("{}");	
		}
		
	}

	private String ModifyLawDescription(int law_description_id, String law_description) {
		int result = DbOperation.updateLawDescription(law_description_id, law_description);
		
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
