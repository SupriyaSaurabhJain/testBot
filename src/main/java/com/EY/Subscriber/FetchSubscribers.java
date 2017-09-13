package com.EY.Subscriber;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.EY.Admin.AddNewQuestion;
import com.EY.DB.DbOperation;

/**
 * Servlet implementation class FetchSubscribers
 */
public class FetchSubscribers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("Inside FetchSubscribers");
		response.setContentType("application/json");
		try {
				
			response.getWriter().write(DbOperation.fetchSubscribers());	
			
		} catch (Exception e) {
			log.info("Error in doPost:"+e);

			response.getWriter().write("{}");	
		}
	}

}
