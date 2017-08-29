package com.EY.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonObject;

import ai.api.AIServiceException;
import ai.api.model.AIResponse;
import ai.api.web.AIServiceServlet;

public class MyServiceServlet extends AIServiceServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			AIResponse aiResponse = request(request.getParameter("message"), request.getParameter("sessionId"));
			response.setContentType("application/json");
			JSONObject obj = new JSONObject();
			obj.put("displayText", aiResponse.getResult().getFulfillment().getSpeech());
			obj.put("speech", aiResponse.getResult().getFulfillment().getSpeech());
			if(aiResponse.getResult().getFulfillment().getDisplayText()!=null)
			{
				obj.put("displayText", aiResponse.getResult().getFulfillment().getDisplayText());
			}	
			PrintWriter out = response.getWriter();
			out.print(obj);	
		} catch (AIServiceException e) {
			System.out.println("Exception accesing API AI");
			e.printStackTrace();
		}
			
	}
}