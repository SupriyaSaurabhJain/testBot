package com.EY.Admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

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
private static String addNewIntent(String topic , String subTopic){String url = "https://api.api.ai/v1/intents?v=20150910";

HttpClient client = HttpClientBuilder.create().build();
HttpPost post = new HttpPost(url);

// add header
post.setHeader("User-Agent", USER_AGENT);
 StringEntity entity;
 String r = "no ";
try {
	entity = new StringEntity("{\"name\": \"change appliance state\",\"auto\": true, \"contexts\": []," +
					" \"templates\": [\"turn @state:state the @appliance:appliance \",\"switch the @appliance:appliance @state:state \" " +
					"],\"userSays\": [{\"data\": [{\"text\": \"turn\" },{\"text\": \"on\",\"alias\": \"state\",\"meta\": \"@state\"},{\"text\": \" the \" " +
	       " }, {\"text\": \"kitchen lights\",\"alias\": \"appliance\",\"meta\": \"@appliance\"}], "+
	    " \"isTemplate\": false,\"count\": 0 },{\"data\": [{\"text\": \"switch the \" " +
	        "}, {" +
	          " \"text\": \"heating\", " +
	          " \"alias\": \"appliance\", " +
	          " \"meta\": \"@appliance\" " +
	       " }, { " +
	         "  \"text\": \" \" " +
	       " }, { " +
	           "\"text\": \"off\", " +
	             "\"alias\": \"state\", " +
	           "\"meta\": \"@state\" " + 
	       " }         ], " +
	     "\"isTemplate\": false, " +
	     "\"count\": 0  " +
	 " } ]," + 
	"\"responses\": [ " +
	  "{\"resetContexts\": false, " +
	     "\"action\": \"set-appliance\", " +
	     "\"affectedContexts\": [  { " +
	           "\"name\": \"house\",    \"lifespan\": 10           } " +
	     "], " + 
	     "\"parameters\": [ " +
	        "{ " +
	           "\"dataType\": \"@appliance\", " +
	           "\"name\": \"appliance\", " +
	           "\"value\": \"\\$appliance\" " +
	        "},     { " +
	           "\"dataType\": \"@state\", " + 
	           "\"name\": \"state\", " + 
	           "\"value\": \"\\$state\" " +
	        "}         ], " +
	     "\"speech\": \"Turning the \\$appliance \\$state\\!\" " +
	 " }   ],  \"priority\": 500000 }");
	post.setEntity(entity);

	HttpResponse response = client.execute(post);
	log.severe("Response Code : " + response.getStatusLine().getStatusCode());

	BufferedReader rd = new BufferedReader(
	        new InputStreamReader(response.getEntity().getContent()));
	 r  = response.getEntity().toString();
	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
	log.severe("result " + result);
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 return  r;
}
}
