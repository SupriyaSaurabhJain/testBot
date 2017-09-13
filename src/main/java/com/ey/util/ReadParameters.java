package com.ey.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class ReadParameters {
	private static final Logger log = Logger.getLogger(ReadParameters.class.getName());
//Method to read parameters from request object body
public static String readPostParameter(HttpServletRequest request) {
	log.info("inside method read parameters");
	StringBuilder stringBuilder = new StringBuilder();  
    BufferedReader bufferedReader = null;  
    try {  
        InputStream inputStream = request.getInputStream(); 

        if (inputStream != null) {  
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));  

            char[] charBuffer = new char[128];  
            int bytesRead = -1;  

            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {  
                stringBuilder.append(charBuffer, 0, bytesRead);  
            }  
        } else {  
            stringBuilder.append("");  
        }  
    } catch (IOException ex) {  
        log.info(("Error reading the request body..."));  
    } finally {  
        if (bufferedReader != null) {  
            try {  
                bufferedReader.close();  
            } catch (IOException ex) {  
            	log.info("Error closing bufferedReader...");  
            }  
        }  
    }  
    log.info("response body : "+stringBuilder.toString());
    return stringBuilder.toString();
}
}
