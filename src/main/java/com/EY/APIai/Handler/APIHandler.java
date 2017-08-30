package com.EY.APIai.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.EY.Admin.addTopicServlet;
import com.EY.DB.DbOperation;


public class APIHandler {
	private static final Logger log = Logger.getLogger(addTopicServlet.class.getName());
	private static final String USER_AGENT = "Mozilla/5.0";
	public String addTopic(String subTopic , String topic){
		String entityId = "d7b4ab70-c537-40e3-b1dc-083aba5ed555";
		String url = "https://api.api.ai/v1/entities/"+entityId+"/entries?v=20150910";
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
			log.info("add topic response : " + r);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.severe("not added sucessfully");
				new DbOperation().deleteFromDb(topic, subTopic);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return r;
	}
	private static String getJsonStringEntityForElement(String topic , String subTopic){
		String inputJson = "[{\"value\": \""+ subTopic+ "\",\"synonyms\": []}]" ;
		return inputJson;
	}
}
