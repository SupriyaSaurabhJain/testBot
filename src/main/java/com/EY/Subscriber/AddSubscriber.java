package com.EY.Subscriber;

import java.io.IOException;
import java.util.logging.Logger;
import java.security.Key;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.EY.Admin.AddNewQuestion;
import com.EY.DB.DbOperation;
import com.EY.Service.ReadParameters;



/**
 * Servlet implementation class AddSubscriber
 */
public class AddSubscriber extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddSubscriber() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String responseJson  = ReadParameters.readPostParameter(request);
		response.setContentType("application/json");
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			String username = jsonResponseObject.get("username").toString();
			String email = jsonResponseObject.get("email").toString();
			boolean isadmin = Boolean.parseBoolean(jsonResponseObject.get("isadmin").toString());
			
			if(isadmin)
			{
				String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
				String password = RandomStringUtils.random( 9 , characters );
				log.info("randomly created plain password :" + password);
				
	            String encryptedPassword = EncryptDecrypt.encrypt(password);
	            log.info("encrypted pass="+encryptedPassword);
	            String decryptedPassword = EncryptDecrypt.decrypt(encryptedPassword);    
	            log.info("decrypted pass="+decryptedPassword);
				
				response.getWriter().write(addSubscriber(username, email, encryptedPassword, "ACTIVE", isadmin));
			}
			else{
				response.getWriter().write(addSubscriber(username, email, "NULL", "ACTIVE", isadmin));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private String addSubscriber(String username, String email, String password, String status, boolean isadmin) {
		String response = "";
		int result =  DbOperation.addSubscriber(username, email, password, isadmin, status);
		log.info("result in add subscriber :" + result);

		response = getErrorResponse(result) ;


		log.info("Response : "+ response);
		return response;	
	}
	private static String getErrorResponse(int result){


		String response ;
		if (result == 1 ) {
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}" ;

		}
		else if (result == -2) {
			response = " {  \"status\": {    \"code\": 201,    \"errorType\": \"ExistingMailID\"  }}" ;
		}
		else {
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
		}
		return response;
	}
	
}
