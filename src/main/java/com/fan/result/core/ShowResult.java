package com.fan.result.core;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class ShowResult {

  private String database;
  private String username;
  private String password;
  private String querySet;

  public ShowResult(String database, String username, String password, String querySet) {
    this.database = database;
    this.username = username;
    this.password = password;
    this.querySet = querySet;
  }

  private ArrayList<String> dissect () {
    JSONObject j = new JSONObject(querySet);
    ArrayList<String> queryCollection = new ArrayList<>();
    j.remove("db");
    int numOfQuery = 0;
    while(j.has(Integer.toString(numOfQuery))) {
      queryCollection.add(j.getString(Integer.toString(numOfQuery)));
      numOfQuery++;
    }
    return queryCollection;
  }

  public ArrayList<String[][]> execute() {

    ArrayList<String[][]> tables = new ArrayList<>();

    ArrayList<String> queryCollection = dissect();
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database , username, password);
      for(int i = 0; i < queryCollection.size(); i++) {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryCollection.get(i));
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int numOfCol = rsmd.getColumnCount();
        resultSet.last();
        int numOfRow = resultSet.getRow();
        String[][] table = new String[numOfRow][numOfCol];
        resultSet.first();
        for(int k = 0; k < numOfRow; k++) {
          for(int s = 0; s < numOfCol; s++) {
            table[k][s] = resultSet.getString(s + 1);
          }
          resultSet.next();
        }
        tables.add(table);
      }
      connection.close();
      return tables;
    }catch (Exception e) {
      System.out.println(e.toString());
    }
    return null;
  }
}

