package com.ey.apiai.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.ey.admin.*;
import com.ey.db.*;
public class APIHandler {
	private static final Logger log = Logger.getLogger(addTopicServlet.class.getName());
	private static final String USER_AGENT = "Mozilla/5.0";
	public String addTopic(String subTopic , String topic){
		String entityId = "037dfac8-f6e6-40c3-8448-6b868c8f5a85";
		String url = "https://api.api.ai/v1/entities/"+entityId+"/entries?v=20150910";
		log.severe("topic : "+ topic + "   subTopic : "+subTopic + "\n  url : "+url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type" , "application/json");
		post.setHeader("Authorization" , "Bearer ff28c61a38424a3684af062f491003ca");

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
				new DbOperation().deleteSubTopicFromDb(subTopic);
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
