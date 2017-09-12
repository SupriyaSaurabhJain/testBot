package com.EY.Subscriber;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.EY.Admin.AddNewQuestion;
import com.EY.DB.DbOperation;
import com.EY.Service.ReadParameters;

/**
 * Servlet implementation class DeleteSubscriber
 */
public class DeleteSubscriber extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteSubscriber() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			int User_ID = Integer.parseInt(jsonResponseObject.get("User_ID").toString());
			response.getWriter().write(removeSubscriber(User_ID));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public String removeSubscriber(int User_ID){
		String response = "";
		int result = DbOperation.deleteSubscriberFromDb(User_ID);
		log.info("result in delete que :" + result);
		if (result == 1) {
			// to api ai 
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Sucess\"  }}" ;


		}
		else{
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;

		}
		log.info("Response : "+ response);
		return response;
	}

}
