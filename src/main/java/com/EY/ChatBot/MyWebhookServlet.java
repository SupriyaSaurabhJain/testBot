package com.EY.ChatBot;

import java.io.*;
import java.util.HashMap;
import javax.servlet.ServletContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.logging.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import ai.api.model.AIOutputContext;
import ai.api.model.Fulfillment;
import ai.api.web.AIWebhookServlet;

@SuppressWarnings("serial")
public class MyWebhookServlet extends AIWebhookServlet  { 
	//https://ai-ml-eychat.appspot.com/webhook  
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());
	@Override
	protected void doWebhook(AIWebhookRequest input, Fulfillment output) {
		String action = input.getResult().getAction();
		BotHandlerServlet handler = new BotHandlerServlet();
		HashMap<String, JsonElement> parameter = input.getResult().getParameters();
		switch (action) {
		case "overtime":
		case "overtime_federal":
		case "overtime_federal_more_yes":
		case "overtime_states":
		case "overtime_states_no":
		case "compliance_expert_yes":
			output = handler.compilanceExpertYesHandler(output);
			break;
		case "query":
			String topic = parameter.get("topics").toString().replaceAll("^\"|\"$", "");
			String law_scope = parameter.get("law_scope").toString().replaceAll("^\"|\"$", "");
			output  = getQueryResponse(topic,law_scope.toUpperCase() , output );
			break;
		case "state_laws": 
			topic = parameter.get("topic").toString().replaceAll("^\"|\"$", "");
			log.info(topic);
			String state = parameter.get("state").toString().replaceAll("^\"|\"$", "");
			log.info(state);
			output  = getStateActionResponse(topic,state.toUpperCase() , output );
			break;

		default:
			break;
		}
		//output.setSpeech(input.getResult().toString());

	}
	protected Fulfillment getQueryResponse(String topic , String law_scope , Fulfillment output){
		ServletContext conetxt = getServletContext();
		String pathToDd = conetxt.getRealPath("/WEB-INF/db.txt");
		JSONParser parser = new JSONParser();
		Object obj = null;
		String response = "No Response!!!" ;
	
		if (law_scope.equals("FEDERAL")) {
			try {
				obj = parser.parse(new FileReader(pathToDd));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject obj1 = (JSONObject)	jsonObject.get(topic.toUpperCase().trim());
				response = "This is what I found about federal law on" + topic+ ". \n" ;
				response += obj1.get(law_scope).toString();
				output.setDisplayText(response + "\n\nDoes this help?\n :obYes:cb :obNo:cb");
				output.setSpeech(response + "\n \n This is what I found. Does it help ?");
				//webhook_res["contextOut"].append({"name":"complaince_expert", "lifespan":2,"parameters":{ "topic": topic} })
				AIOutputContext contextOut = new AIOutputContext();
				HashMap<String, JsonElement> outParameters = new HashMap<String , JsonElement>();
				
				JsonElement contextOutParameter ;
				contextOutParameter = new JsonPrimitive(topic);
				outParameters.put("topic",contextOutParameter );
				
				contextOut.setLifespan(2);
				contextOut.setName("complaince_expert");
				contextOut.setParameters(outParameters);
				log.info("Context out parameters" + contextOutParameter.toString());

				output.setContextOut(contextOut);
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(law_scope.equals("STATE")){
			output.setSpeech("Which state ?");
			output.setDisplayText("Which State ?");
			AIOutputContext contextOut = new AIOutputContext();
			contextOut.setLifespan(5);
			contextOut.setName("state_law");
			HashMap<String, JsonElement> outParameters = new HashMap<String , JsonElement>();
			JsonElement contextOutParameter ;
			contextOutParameter = new JsonPrimitive(topic);
			outParameters.put("topic",contextOutParameter );
			contextOut.setParameters(outParameters);
			log.info("" + contextOut.getLifespan() + " : " + contextOut.getName() );
			output.setContextOut(contextOut);

		}

		return output ;
	}
	protected Fulfillment getStateActionResponse(String topic , String state , Fulfillment output){
		log.info("inside funb");
		ServletContext conetxt = getServletContext();

		String pathToDd = conetxt.getRealPath("/WEB-INF/db.txt");

		JSONParser parser = new JSONParser();
		Object obj = null;
		String response = "No Response!!!" ;
		AIOutputContext contextOut = new AIOutputContext();
		try {
			obj = parser.parse(new FileReader(pathToDd));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject obj1 = (JSONObject)	jsonObject.get(topic.toUpperCase().trim());
			response = "This is what I found on" + topic+ ". \n" ;
			response += obj1.get(state).toString();
			output.setDisplayText(response + "\n\nDoes this help?\n :obYes:cb :obNo:cb");
			output.setSpeech(response + "\n \n This is what I found. Does it help ?");
			//webhook_res["contextOut"].append({"name":"complaince_expert", "lifespan":2,"parameters":{ "topic": topic} })
			HashMap<String, JsonElement> outParameters = new HashMap<String , JsonElement>();
			JsonElement contextOutParameter ;
			contextOutParameter = new JsonPrimitive(topic);
			outParameters.put("topic",contextOutParameter );
			contextOut.setLifespan(2);
			contextOut.setName("complaince_expert");
			contextOut.setParameters(outParameters);
			output.setContextOut(contextOut);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("exception  "+ e);
			e.printStackTrace();
		}


		log.info("out");
		return output ;
	}
}