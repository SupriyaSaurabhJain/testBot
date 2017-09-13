package com.ey.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class Queries {
    private static final String propertyFileName = "queries.properties";
    private static Properties property;
	private static final Logger log = Logger.getLogger(Queries.class.getName());

    public static Properties getQueries() throws SQLException {
    	log.info("inside get Queries method");

        InputStream inputStream =  Queries.class.getResourceAsStream("/" + propertyFileName);
        if (inputStream == null){
        	log.severe("input stream null");
            throw new SQLException("Unable to load property file: " + propertyFileName);
        }
        //singleton
        if(property == null){
        	property = new Properties();
            try {
            	property.load(inputStream);
            } catch (IOException e) {
                throw new SQLException("Unable to load property file: " + propertyFileName + "\n" + e.getMessage());
            }           
        }
        return property;
    }

    public static String getQuery(String query) throws SQLException{
        return getQueries().getProperty(query);
    }
}
