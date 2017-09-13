package com.ey.service;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import ai.api.AIServiceException;
import ai.api.model.AIResponse;
import ai.api.web.AIServiceServlet;

public class MyServiceServlet extends AIServiceServlet {
//Servelet servicing chat bot 
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("inside MyServiceServlet");
		try {

			AIResponse aiResponse = request(request.getParameter("message"), request.getParameter("sessionId")); //send request to API AI
			response.setContentType("application/json");
			JSONObject obj = new JSONObject();
			obj.put("displayText", aiResponse.getResult().getFulfillment().getSpeech());
			obj.put("speech", aiResponse.getResult().getFulfillment().getSpeech());
			if(aiResponse.getResult().getFulfillment().getDisplayText()!=null)
			{
				obj.put("displayText", aiResponse.getResult().getFulfillment().getDisplayText()); // send response to chat bot UI
			}	
			PrintWriter out = response.getWriter();
			out.print(obj);	
		} catch (AIServiceException e) {
			System.out.println("Exception accesing API AI");
		}
			
	}
}