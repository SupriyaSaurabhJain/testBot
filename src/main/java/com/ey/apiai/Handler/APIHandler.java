package com.ey.apiai.Handler;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class APIHandler {
	private static final Logger log = Logger.getLogger(APIHandler.class.getName());
	private static final String USER_AGENT = "Mozilla/5.0";
	public static int addTopic(String subTopic , String topic){
		log.info("inside APIHandler add topic method");
		String entityId = "037dfac8-f6e6-40c3-8448-6b868c8f5a85";  // Id of entity in API AI
		String url = "https://api.api.ai/v1/entities/"+entityId+"/entries?v=20150910";  //REST API for adding elements in API AI
		log.severe("Inside API Handler : topic : "+ topic + "   subTopic : "+subTopic + "\n  url : "+url);
		// Client program to add entity to API AI
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type" , "application/json");
		post.setHeader("Authorization" , "Bearer ff28c61a38424a3684af062f491003ca");
		StringEntity entity;
		try {
			entity = new StringEntity(getJsonStringEntityForElement(topic, subTopic)); //method to get JSON string format for topic/subtopic
			post.setEntity(entity);
			HttpResponse responseFromAPI = client.execute(post);
			log.severe("responseFromAPI Code : " + responseFromAPI.getStatusLine().getStatusCode());
			log.severe("responseFromAPI Message :" + responseFromAPI.getStatusLine().getReasonPhrase());
			if (responseFromAPI.getStatusLine().getStatusCode() != 200) {
				log.severe("element not added sucessfully to API AI");
				return 0;
			}
		} catch (IOException e) {
			log.severe("Exception inside API Handler : "+ e);
		}
		
		return 1;
	}
	//function to create Json format string required by API AI
	private static String getJsonStringEntityForElement(String topic , String subTopic){
		String inputJson = "[{\"value\": \""+ subTopic+ "\",\"synonyms\": []}]" ;
		return inputJson;
	}
}
