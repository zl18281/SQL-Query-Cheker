package com.fan.db.core;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Column {
  ArrayList<StringBuilder> col = new ArrayList<>();
  private String database;
  private String table;

  public Column(String database, String table) {
    this.database = database;
    this.table = table;
  }

  public JSONObject getColumn () {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database , "student", "");
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("DESC " + this.table + ";");
      while (resultSet.next()){
        this.col.add(new StringBuilder(resultSet.getString(1)));
      }
      connection.close();
    }catch(Exception e) {
      e.printStackTrace();
    }
    JSONObject j = new JSONObject();
    try {
      for(int i = 1; i <= col.size(); i++) {
        j.put(Integer.toString(i), col.get(i - 1).toString());
      }
      System.out.println(j.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return j;
  }
}
