package com.ey.subscriber;

import java.io.IOException;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ey.db.DbOperation;
import com.ey.util.ReadParameters;

/**
 * Servlet implementation class ModifySubscriber
 */
public class ModifySubscriber extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ModifySubscriber.class.getName());
	private static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("Inside Modify Subscriber");
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		try {

			JSONObject jsonResponseObject = (JSONObject) parser.parse(responseJson);
			int userId = Integer.parseInt(jsonResponseObject.get("userId").toString());
			String username = jsonResponseObject.get("username").toString();
			String email = jsonResponseObject.get("email").toString();
			String status = jsonResponseObject.get("status").toString();
			boolean isadmin = Boolean.parseBoolean(jsonResponseObject.get("isadmin").toString());
			
			boolean isUserAlreadyAdmin = DbOperation.isUserAdmin(userId);
			boolean isRoleChange =false;
			String encryptedPassword = null;
			
			if(!isUserAlreadyAdmin&&isadmin)
			{
				log.info("User getting Modified to Admin");
				String password = RandomStringUtils.random( 9 , characters );
				encryptedPassword = EncryptDecrypt.encrypt(password);
	            isRoleChange = true;
			}
			response.getWriter().write(modifySubscriber(userId,username, email, encryptedPassword, status, isadmin, isRoleChange));
			
		} catch (ParseException e) {
			log.severe("Parse exception in modifying the subscriber");
		} catch (Exception e) {
			log.severe("exception in modifying the subscriber");
		}
	}
	
	public String modifySubscriber(int User_ID, String Username, String Email, String Password,String Status, boolean IsAdmin, boolean IsRoleChange){
		
		int result = DbOperation.modifySubscriber(User_ID,Username,Email,Password,Status,IsAdmin,IsRoleChange);
		log.info("result in delete subscriber :" + result);
		return getResponseJson(result);
	}
	
	private static String getResponseJson(int result) {
		log.info("inside getResponse json");
		String response;
		if (result == 1) {
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}";

		}else {
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}";
		}
		return response;
	}

}