package com.EY.ChatBot;

import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import ai.api.model.AIOutputContext;
import ai.api.model.Fulfillment;

// [START example]
@SuppressWarnings("serial")
public class BotHandlerServlet extends HttpServlet{
	private static final Logger log = Logger.getLogger(MyWebhookServlet.class.getName());

	protected Fulfillment compilanceExpertYesHandler(Fulfillment output) {
		// TODO Auto-generated method stub
		output.setDisplayText("You may write your detailed query via email and our compliance expert will get back to you within 3 working days. If your query is urgent in nature then you may opt for urgent response desk and you may receive response within 24 hours. \n\nWould you like to write a query with standard response time?\n :obYes:cb :obNo:cb");
		output.setSpeech("You may write your detailed query via email and our compliance expert will get back to you within 3 working days. If your query is urgent in nature then you may opt for urgent response desk and you may receive response within 24 hours.       Would you like to write a query with standard response time?");
		return output;
	}
	protected Fulfillment getQueryActionResponseHandler(HashMap<String, JsonElement> parameter , Fulfillment output) {
		String topic = parameter.get("topics").toString().replaceAll("^\"|\"$", "");
		String law_scope = parameter.get("law_scope").toString().replaceAll("^\"|\"$", "");
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
		return output;
	}
	protected Fulfillment getStateActionResponseHandler(HashMap<String, JsonElement> parameter , Fulfillment output) {
		String topic = parameter.get("topic").toString().replaceAll("^\"|\"$", "");
		log.info(topic);
		String state = parameter.get("state").toString().replaceAll("^\"|\"$", "");
		log.info(state);
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