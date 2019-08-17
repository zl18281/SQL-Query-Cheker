package com.fan.ANTLR.core;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ColumnListener extends MySqlParserBaseListener {
  MySqlParser parser;
  private String database;
  private String username;
  private String password;
  private ArrayList<String> actualColumnSet = new ArrayList<>();
  private ArrayList<String> rightColumnSet = new ArrayList<>();
  private ArrayList<String> errorColumns = new ArrayList<>();
  private ArrayList<String> tableSet;

  public ColumnListener(MySqlParser parser, String database, String username, String password, ArrayList<String> tableSet) {
    this.parser = parser;
    this.database = database;
    this.username = username;
    this.password = password;
    this.tableSet = tableSet;
  }

  private void getRightColumnsForOneTable(String table) {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database, this.username, this.password);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("DESC " + table);
      while (resultSet.next()) {
        System.out.println(resultSet.getString("Field"));
        this.rightColumnSet.add(resultSet.getString("Field"));
      }
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void getRightColumns() {
    for(int i = 0; i < this.tableSet.size(); i++) {
      getRightColumnsForOneTable(this.tableSet.get(i));
    }
  }

  public void getErrorColumns(String[] actualColumns) {
    getRightColumns();

    for(int i = 0; i < actualColumns.length; i++) {
      if(!this.rightColumnSet.contains(actualColumns[i])) {
        System.out.println(actualColumns[i]);
        errorColumns.add(actualColumns[i]);
      }
    }

    File f = new File("../webapps/SQL/WEB-INF/resources/error/semanticError.json");
    try (PrintWriter pw = new PrintWriter(f)){
      if (this.errorColumns.size() == 0) {
        pw.print("{}");
      } else {
        System.out.println("*****");
        pw.print("{");
        for (int i = 0; i < this.errorColumns.size() - 1; i++) {
          pw.print("\"" + (i + 1) + "\":" + "\"" + this.errorColumns.get(i) + "\",");
        }
        pw.print("\"" + this.errorColumns.size() + "\":" + "\"" +
          this.errorColumns.get(this.errorColumns.size() - 1) + "\"}");
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  @Override
  public void enterSelectColumnElement(MySqlParser.SelectColumnElementContext ctx) {
    super.enterSelectColumnElement(ctx);
    System.out.println("*");
    System.out.println(ctx.getText());
    System.out.println("*");
    if(checkPrefix(ctx.getText())){
      this.actualColumnSet.add(removePrefix(ctx.getText()));
    }else{
      this.actualColumnSet.add(ctx.getText());
    }
  }

  private boolean checkPrefix(String columnName) {
    for(int i = 0; i < columnName.length(); i++) {
      if(columnName.charAt(i) == '.') {
        return true;
      }
    }
    return false;
  }

  private String removePrefix(String columnName) {
    int indexStart = 0;
    for(int i = 0; i < columnName.length(); i++) {
      if(columnName.charAt(i) == '.') {
        indexStart = (i + 1);
        return columnName.substring(indexStart);
      }
    }
    return columnName.substring(indexStart);
  }

  public String[] getActualColumnSet() {
    String[] actualColumns = new String[this.actualColumnSet.size()];

    for(int i = 0; i < this.actualColumnSet.size(); i++) {
      actualColumns[i] = this.actualColumnSet.get(i);
    }
    return actualColumns;
  }
}
