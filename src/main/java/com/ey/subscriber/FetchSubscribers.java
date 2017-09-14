package com.ey.subscriber;

import java.io.IOException;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ey.db.DbOperation;

/**
 * Servlet implementation class FetchSubscribers
 */
public class FetchSubscribers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FetchSubscribers.class
			.getName());

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		log.info("Inside FetchSubscribers");
		response.setContentType("application/json");
		String responseText = "{}";
		try {
			responseText = DbOperation.fetchSubscribers();
		} catch (Exception e) {
			log.severe("Exception in fetching subscribers");
		}
		response.getWriter().write(responseText);
	}

}