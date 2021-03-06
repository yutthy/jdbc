/**
 * 
 */
package com.rupp.sample.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rupp.sample.domain.TestDomain;
import com.rupp.sample.jdbc.DBCP2DataSourceUtils;

/**
 * @author sopheamak
 *
 */
public class SampleSqlSelect {

    private final String jdbcDriverStr ="com.mysql.jdbc.Driver";
    private final String jdbcURL = "jdbc:mysql://localhost/test";
    private final String username = "root";
    private final String pwd = "root";
    
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
 
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        /**
         * create new database name : test
         * run the following sql in console
         * 
         DROP DATABASE IF EXISTS test;
         CREATE DATABASE test;
         USE test;
         
         DROP TABLE IF EXISTS test_table;
         CREATE TABLE test_table (
             id INT NOT NULL AUTO_INCREMENT,
             message VARCHAR(400) NOT NULL,
             PRIMARY KEY (ID)
         ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
 
         INSERT INTO test_table values (default, 'test text message');
         */
        SampleSqlSelect jdbc = new SampleSqlSelect();
        jdbc.readData();
        
        //jdbc.writeData("my test message");
        
    }
 // SQL Select
    public List<TestDomain> readData() throws Exception {
        final List<TestDomain> list = new ArrayList<>();
        try {
            // load jdbc driver
            Class.forName(jdbcDriverStr);
            // load connection driver
            connection = DriverManager.getConnection(jdbcURL, username, pwd);
            // create statement
            statement = connection.createStatement();
            // execute select statement
            resultSet = statement.executeQuery("select * from test_table limit 500");

            // get result
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String message = resultSet.getString("message");
                System.out.println("id:" + id + ", message:" + message);
                //add TestDomain to List
                list.add(new TestDomain(id, message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return list;
    }

    // SQL Select
    public List<TestDomain> readDataFromDataSource() throws Exception {
        final List<TestDomain> list = new ArrayList<>();
        try {
            // get Connection from datasource
            connection = DBCP2DataSourceUtils.getConnection();
            // create statement
            statement = connection.createStatement();
            // execute select statement
            resultSet = statement.executeQuery("select * from test_table order by id desc limit 100 ;");

            // get result
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String message = resultSet.getString("message");
                //System.out.println("id:" + id + ", message:" + message);
                //add TestDomain to List
                list.add(new TestDomain(id, message));
            }
            DBCP2DataSourceUtils.printDataSourceState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return list;
    }
 
    private void close(){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
