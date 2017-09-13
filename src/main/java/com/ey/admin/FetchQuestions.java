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

public class FetchQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FetchQuestions.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchQuestions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info("Inside FetchQuestions");
		
		response.setContentType("application/json");
		
		String requestJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject requestObject = (JSONObject) parser.parse(requestJson);

			int topic_id = Integer.parseInt(requestObject.get("topic_id").toString());
			int sub_topic_id = Integer.parseInt(requestObject.get("sub_topic_id").toString());
				
			response.getWriter().write(DbOperation.fetchQuestionsFromDB(topic_id, sub_topic_id));	
			
		} catch (Exception e) {
			log.info("Error in doPost:"+e);

			response.getWriter().write("{}");	
		}
		
	}

}
