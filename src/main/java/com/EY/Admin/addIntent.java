package com.EY.Admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.EY.ChatBot.MyWebhookServlet;
/**
 * Servlet implementation class addTopic
 */
public class addIntent extends HttpServlet {
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());

	private static final long serialVersionUID = 1L;
	private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String topic = request.getParameter("topic");
		String subTopic = request.getParameter("subTopic");
		response.getWriter().write(addNewIntent(topic, subTopic));
	}
	private static String getJsonStringEntityForIntent(String topic , String subTopic){
		String inputJson = "{\"name\": \"" +  topic  +"\",\"auto\": true, \"contexts\": []," +
				" \"templates\": [ ],\"userSays\": [ ], "+
				" \"isTemplate\": false,\"count\": 0 },{\"data\": [ ], " +
				"\"isTemplate\": false, " +
				"\"count\": 0  " +
				" } ]," + 
				"\"responses\": [ " +
				"{\"resetContexts\": false, " +
				"\"action\": \"" + topic+"\", " +
				"\"affectedContexts\": [  { " +
				"\"name\": \"house\",    \"lifespan\": 10           } " +
				"], " + 
				"\"parameters\": [ " +
				"{ " +
				"\"dataType\": \"@subTopic\", " +
				"\"name\": \"subTopic\", " +
				"\"value\": \"\\$subTopic\" " +
				"}    ], " +
				"\"speech\": \" \"" +
				" }   ],  \"priority\": 500000 }";
		return inputJson;
	}
	private static String addNewIntent(String topic , String subTopic){String url = "https://api.api.ai/v1/intents?v=20150910";

	HttpClient client = HttpClientBuilder.create().build();
	HttpPost post = new HttpPost(url);

	// add header
	post.setHeader("User-Agent", USER_AGENT);
	post.setHeader("Content-Type" , "application/json");
	post.setHeader("Authorization" , "Bearer 36f114a183b241ad8fda33e11c962a5f");

	StringEntity entity;
	String r = "no ";
	try {
		entity = new StringEntity(getJsonStringEntityForIntent(topic, subTopic));
		post.setEntity(entity);

		HttpResponse response = client.execute(post);
		log.severe("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		r  = response.getEntity().toString();
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		log.severe("result " + result); // Gives result Json
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return  r;
	}
}
