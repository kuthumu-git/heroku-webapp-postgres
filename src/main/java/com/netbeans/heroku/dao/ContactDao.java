/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeans.heroku.dao;

import com.netbeans.heroku.model.Contact;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author UI600212
 */
public class ContactDao {

    String JDBC_DATABASE_URL, JDBC_DATABASE_USERNAME, JDBC_DATABASE_PASSWORD;
    InputStream input;

    public void getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
//            String projectpath = System.getProperty("user.dir");
//            input=new FileInputStream(projectpath+"/src/main/java/com/netbeans/heroku/config/config.properties");
//            prop.load(input);
            URL resource = getClass().getResource("/");
            String path = resource.getPath();
//String path = new File(".").getCanonicalPath();
            System.out.println(getClass().getClassLoader().getResourceAsStream("com/netbeans/heroku/config/config.properties"));
            System.out.println("classpath:" + System.getProperty("java.class.path"));
            String propFileName = "com/netbeans/heroku/config/config.properties";

            input = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (input != null) {
                prop.load(input);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            //Date time = new Date(System.currentTimeMillis());
            // get the property value and print it out
            JDBC_DATABASE_URL = prop.getProperty("JDBC_DATABASE_URL");
            JDBC_DATABASE_USERNAME = prop.getProperty("JDBC_DATABASE_USERNAME");
            JDBC_DATABASE_PASSWORD = prop.getProperty("JDBC_DATABASE_PASSWORD");

        } catch (IOException e) {
            System.out.println("Exception: " + e);

        } finally {
            input.close();

        }

        //return result;
    }

    public int updateContact(Contact contact) throws ClassNotFoundException, IOException, SQLException {

        getPropValues();

        int result = 0;

        System.out.println("Java JDBC PostgreSQL Example1");
        //String UPDATE_CONTACT_SQL = "UPDATE salesforce.Contact SET Phone = $1, MobilePhone = $1 WHERE LOWER(FirstName) = LOWER($2) AND LOWER(LastName) = LOWER($3) AND LOWER(Email) = LOWER($4)";
        String UPDATE_CONTACT_SQL = "UPDATE salesforce.Contact SET Phone = ?, MobilePhone = ? WHERE LOWER(FirstName) = LOWER(?) AND LOWER(LastName) = LOWER(?) AND LOWER(Email) = LOWER(?)";

        try {
            System.out.println("Java JDBC PostgreSQL Example2");
            Class.forName("org.postgresql.Driver");
            System.out.println("Java JDBC PostgreSQL Example");
            System.out.println(JDBC_DATABASE_URL + ',' + JDBC_DATABASE_USERNAME + ',' + JDBC_DATABASE_PASSWORD);
            Connection connection = DriverManager.getConnection(JDBC_DATABASE_URL, JDBC_DATABASE_USERNAME, JDBC_DATABASE_PASSWORD);

            System.out.println("Java JDBC PostgreSQL Example");
            // When this class first attempts to establish a connection, it automatically loads any JDBC 4.0 drivers found within 
            // the class path. Note that your application must manually load any JDBC drivers prior to version 4.0.
//          Class.forName("org.postgresql.Driver"); 

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            System.out.println("Reading contact records...");
//            ResultSet resultSet = statement.executeQuery("SELECT Phone,Email FROM salesforce.contact");
//            while (resultSet.next()) {
//                System.out.printf(resultSet.getString("Phone"), resultSet.getString("Email"));
//            }
            System.out.println(contact.getFirstName());
            System.out.println(contact.getLastName());
            System.out.println(contact.getPhone());
            System.out.println(contact.getEmail());
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CONTACT_SQL);
            preparedStatement.setString(1, contact.getPhone());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setString(3, contact.getFirstName());
            preparedStatement.setString(4, contact.getLastName());
            preparedStatement.setString(5, contact.getEmail());

            System.out.println(preparedStatement);
            result = preparedStatement.executeUpdate();
            System.out.println(result);
        }  catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);

        }

        return result;

    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
//        } /*catch (ClassNotFoundException e) {
//            System.out.println("PostgreSQL JDBC driver not found.");
//            e.printStackTrace();
//        }*/ catch (SQLException e) {
//            System.out.println("Connection failure.");
//        }
//        return 0;
//        }
}
