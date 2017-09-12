package com.ey.admin;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ey.db.*;

public class ListComplianceDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ListComplianceDetails.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListComplianceDetails() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		//String requestJson  = ReadParameters.readPostParameter(request);
		//JSONParser parser = new JSONParser();
		
		try {
			//JSONObject requestObject = (JSONObject) parser.parse(requestJson);

			//int page_number = Integer.parseInt(requestObject.get("page_number").toString());
			
			response.setContentType("application/json");
			response.getWriter().write(DbOperation.fetchComplianceDetailsFromDB(0));	
		
		}
		catch(Exception e){
			
			log.info("Error in ListComplianceDetails : "+e);
		}
		
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
