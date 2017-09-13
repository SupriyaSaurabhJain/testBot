package com.ey.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Queries {
    private static final String propertyFileName = "queries.properties";
    private static Properties property;

    public static Properties getQueries() throws SQLException {
        InputStream inputStream =  Queries.class.getResourceAsStream("/" + propertyFileName);
        if (inputStream == null){
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
