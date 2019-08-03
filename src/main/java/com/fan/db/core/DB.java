package com.fan.db.core;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {
  ArrayList<StringBuilder> db = new ArrayList<>();

  private String username = null;
  private String password = null;

  public DB (String username, String password) {
    this.username = username;
    this.password = password;
  }

  public JSONObject getDB () {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/uni" , username, password);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SHOW DATABASES;");
      while (resultSet.next()){
        this.db.add(new StringBuilder(resultSet.getString(1)));
      }
      connection.close();
    }catch(Exception e) {
      e.printStackTrace();
    }
    JSONObject j = new JSONObject();
    try {
      for(int i = 1; i <= db.size(); i++) {
        j.put(Integer.toString(i), db.get(i - 1).toString());
      }
      System.out.println(j.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return j;
  }

}
