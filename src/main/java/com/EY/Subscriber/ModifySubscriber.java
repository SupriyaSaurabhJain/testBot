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
 * Servlet implementation class ModifySubscriber
 */
public class ModifySubscriber extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifySubscriber() {
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
			String username = jsonResponseObject.get("username").toString();
			String email = jsonResponseObject.get("email").toString();
			boolean isadmin = Boolean.parseBoolean(jsonResponseObject.get("isadmin").toString());
			
			response.getWriter().write(modifySubscriber(User_ID, username, email, "ACTIVE", isadmin));
		
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String modifySubscriber(int User_ID, String Username, String Email, String Status, boolean IsAdmin){
		String response = "";
		int result = DbOperation.ModifySubscriberFromDb(User_ID,Username,Email,Status,IsAdmin);
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
