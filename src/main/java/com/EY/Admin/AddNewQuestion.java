package com.EY.Admin;

import java.io.IOException;
import java.util.logging.Logger;
import com.EY.DB.DbOperation;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.EY.Service.ReadParameters;

/**
 * Servlet implementation class AddNewQuestion
 */
public class AddNewQuestion extends HttpServlet {
	private static final Logger log = Logger.getLogger(AddNewQuestion.class.getName());

	private static final long serialVersionUID = 1L;

    public AddNewQuestion() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseJson  = ReadParameters.readPostParameter(request);
		JSONParser parser = new JSONParser();
		Object responseObject = null;
		try {
			responseObject = parser.parse(responseJson);
			JSONObject jsonResponseObject = (JSONObject) responseObject;
			//System.out.println(jsonResponseObject);
			String topic = jsonResponseObject.get("topic").toString();
			String subTopic = jsonResponseObject.get("subTopic").toString();
			String question =  jsonResponseObject.get("question").toString();
			int userId = Integer.parseInt(jsonResponseObject.get("userId").toString());
			response.getWriter().write(addQuestion(topic, subTopic , question ,userId));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String addQuestion(String topic, String subTopic ,String question, int userId) {
		// TODO Auto-generated method stub
		String response = "";
		
		int result = DbOperation.addNewQuestionToDB(topic, subTopic , question ,userId);
		log.info("result in addTopic :" + result);
		if (result == 1) {
			// to api ai 
			response = " {  \"status\": {    \"code\": 200,    \"errorType\": \"Success\"  }}" ;

		
		}
		else{
			response = " {  \"status\": {    \"code\": 400,    \"errorType\": \"Request Failed\"  }}" ;
			
		}
		log.info("Response : "+ response);
		return response;
	}
	

}
