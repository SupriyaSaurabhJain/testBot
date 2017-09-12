package com.ey.chatbot;
//https://beta-007-dot-poc-iot.appspot.com/webhook

import java.util.HashMap;
import java.util.logging.Logger;

import com.ey.db.DbOperation;
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
		String response = "No Response!!!" ;

		if (law_scope.equals("FEDERAL")) {
			try {
				response = "This is what I found about federal law on" + topic+ ". \n" ;
				//response += obj1.get(law_scope).toString();
				response += DbOperation.getResponse(topic, law_scope, "1");
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
		String response = "No Response!!!" ;
		AIOutputContext contextOut = new AIOutputContext();
		try {
			response = "This is what I found on" + topic+ ". \n" ;
			response += DbOperation.getResponse(topic, state, "1");
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