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
public class addTopic extends HttpServlet {
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());

	private static final long serialVersionUID = 1L;
	private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addTopic() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String topic = request.getParameter("topic");
		String subTopic = request.getParameter("subTopic");
		String entityId = "d7b4ab70-c537-40e3-b1dc-083aba5ed555" ; // weather bot topicEntity 
		response.getWriter().write(addNewIntent(topic, subTopic , entityId));
	}
	private static String getJsonStringEntityForElement(String topic , String subTopic){
		String inputJson = "[{\"value\": \""+ subTopic+ "\",\"synonyms\": []}]" ;
		return inputJson;
	}
	private static String addNewIntent(String topic , String subTopic , String entityID){
		//topic = "topic" ;  subTopic = "sub" ;
		 
	String url = "https://api.api.ai/v1/entities/"+entityID+"/entries?v=20150910";
	log.severe("topic : "+ topic + "   subTopic : "+subTopic + "\n  url : "+url);
	HttpClient client = HttpClientBuilder.create().build();
	HttpPost post = new HttpPost(url);

	// add header
	post.setHeader("User-Agent", USER_AGENT);
	post.setHeader("Content-Type" , "application/json");
	post.setHeader("Authorization" , "Bearer 36f114a183b241ad8fda33e11c962a5f");

	StringEntity entity;
	String r = "no ";
	try {
		entity = new StringEntity(getJsonStringEntityForElement(topic, subTopic));
		post.setEntity(entity);
		
		HttpResponse response = client.execute(post);
		log.severe("Response Code : " + response.getStatusLine().getStatusCode());
		log.severe("Response Message :" + response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		r  = response.getStatusLine().toString();
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		log.severe("result " + result);
		r = result.toString();
		/* 
		
		{  "id": "6845cca9-412a-40de-9b1e-fb67ee4b1c68",  "timestamp": "2017-08-29T09:37:16.474Z",  "lang": "en", 
		 "status": {    "code": 409,    "errorType": "conflict",   
		  "errorDetails": "Entity with the name \u0027testEntity\u0027 already has entry with value \u0027null\u0027.",   
		   "errorID": "042d7183-af43-4bc0-a6f0-11f0a66c65c1"  }}
		*/
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return  r;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	log.info(request.getAttribute("topic").toString());
		//log.info("" + request.getParameter("json"));
		doGet(request, response);
	}
}
