package com.EY.Admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.EY.ChatBot.MyWebhookServlet;
import com.EY.DB.DbOperation;
import com.EY.Service.ReadParameters;

public class ManageGeo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ManageGeo.class.getName());

    public ManageGeo() {
       
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);
			int token = Integer.parseInt(requestObject.get("token").toString());
			switch (token) {
			case 1:
				response.getWriter().write(getCountries());
				break;
			case 2:
				int countryId = Integer.parseInt(requestObject.get("country_id").toString());
				response.getWriter().write(getStates(countryId));
				break;
			case 3:
				int topic_id = Integer.parseInt(requestObject.get("topic_id").toString());
				int sub_topic_id = Integer.parseInt(requestObject.get("sub_topic_id").toString());
				int stateId = Integer.parseInt(requestObject.get("state_id").toString());
				response.getWriter().write(getLawDescription(sub_topic_id ,stateId));
			default:
				break;
			}
				
			
		} catch (Exception e) {
			log.info("Error in doPost:"+e);

			response.getWriter().write("{}");	
		}
		
	}

	private String getLawDescription(int sub_topic_id, int stateId) {
		// TODO Auto-generated method stub
		DbOperation.getResponse(sub_topic_id, stateId);
		return null;
	}

	@SuppressWarnings("unchecked")
	private String getCountries() {
		
		JSONObject response = new JSONObject();

		HashMap<String,Integer> countryList = DbOperation.getCountryList();
		for (String country : countryList.keySet()) {
			response.put(country, countryList.get(country));
		}
		return response.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private String getStates(int countryId) {
		
		JSONObject response = new JSONObject();

		HashMap<String,Integer> stateList = DbOperation.getStateList(countryId);
		for (String state : stateList.keySet()) {
			response.put(state, stateList.get(state));
		}
		
		return response.toJSONString();
	}


}
