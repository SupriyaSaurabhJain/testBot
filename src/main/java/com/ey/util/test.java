package com.ey.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class test
 */
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(test.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.getWriter().write("ok");

			response.getWriter().write(Queries.getQuery("GetTopicDetails"));
			log.info("hi");
			log.info(Queries.getQuery("GetTopicDetails"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
log.severe("exception : " +e);		}
	}


}
