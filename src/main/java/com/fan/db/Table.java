package com.fan.db;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Table {
  ArrayList<StringBuilder> tb = new ArrayList<>();
  private String database;

  public Table(String database) {
    this.database = database;
  }

  JSONObject getTable () {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database , "student", "");
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SHOW TABLES;");
      while (resultSet.next()){
        this.tb.add(new StringBuilder(resultSet.getString(1)));
      }
      connection.close();
    }catch(Exception e) {
      e.printStackTrace();
    }
    JSONObject j = new JSONObject();
    try {
      for(int i = 1; i <= tb.size(); i++) {
        j.put(Integer.toString(i), tb.get(i - 1).toString());
      }
      System.out.println(j.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return j;
  }
}
