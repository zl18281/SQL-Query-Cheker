package com.fan.ANTLR.core;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class ColumnListener extends MySqlParserBaseListener {

  MySqlParser parser;
  private String database;
  private String username;
  private String password;
  private ArrayList<String> columnSet = new ArrayList<>();
  private ArrayList<String> errorColumns = new ArrayList<>();
  //MySqlParser.SelectColumnElementContext ctx;

  public ColumnListener(MySqlParser parser, String database, String username, String password) {
    this.parser = parser;
    this.database = database;
    this.username = username;
    this.password = password;
  }

  @Override
  public void enterSimpleSelect(MySqlParser.SimpleSelectContext ctx) {
    super.enterSimpleSelect(ctx);
    String[] columns = ctx.getChild(0).getChild(1).getText().split(",");
    String table = ctx.getChild(0).getChild(2).getChild(1).getText();
    getRightColumns(table);
    getErrorColumns(columns);

    System.out.println(ctx.getText());
    System.out.println(ctx.getChild(0).getText());
    System.out.println(ctx.getChild(0).getChild(0).getText());
    System.out.println(ctx.getChild(0).getChild(1).getText());
    System.out.println(ctx.getChild(0).getChild(2).getText());

  }

  private void getRightColumns(String table) {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database, this.username, this.password);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("DESC " + table);
      while (resultSet.next()) {
        System.out.println(resultSet.getString("Field"));
        this.columnSet.add(resultSet.getString("Field"));
      }
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getErrorColumns(String[] actualColumns) {

    for(int i = 0; i < actualColumns.length; i++) {
      if(!this.columnSet.contains(actualColumns[i])) {
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

}
