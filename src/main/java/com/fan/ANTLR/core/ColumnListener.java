package com.fan.ANTLR.core;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
    System.out.println(250);
    System.out.println(ctx.getText());
    System.out.println(ctx.getChild(0).getText());
    System.out.println(ctx.getChild(0).getChild(0).getText());
    System.out.println(ctx.getChild(0).getChild(1).getText());
    System.out.println(ctx.getChild(0).getChild(2).getText());

    try {
      String table = ctx.getChild(0).getChild(2).getChild(1).getText();
      System.out.println(260);
      getRightColumns(table);
      System.out.println(270);
      getErrorColumns(columns);
      System.out.println(280);
    }catch(Exception e) {

      String[] wrongColumns = pickWrongColumn(columns);
      if(wrongColumns != null) {
        File f = new File("../webapps/SQL/WEB-INF/resources/error/semanticError.json");
        try (PrintWriter pw = new PrintWriter(f)) {
          pw.print("{");
          for (int i = 0; i < wrongColumns.length - 1; i++) {
            pw.print("\"" + (i + 1) + "\":" + "\"" + wrongColumns[i] + "\",");
          }
          pw.print("\"" + wrongColumns.length + "\":" + "\"" +
            wrongColumns[wrongColumns.length - 1] + "\"}");
        }catch(Exception ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
    }
  }

  private String[] pickWrongColumn(String[] columns) {
    int cnt = 0;
    for(int i = 0; i < columns.length; i++) {
      if(!isNumeric(columns[i])) {
        cnt++;
      }
    }
    if(cnt != 0) {
      String[] wrongColumns = new String[cnt];
      int j = 0;
      for(int i = 0; i < columns.length; i++) {
        if(!isNumeric(columns[i])) {
          wrongColumns[j] = columns[i];
          j++;
        }
      }
      return wrongColumns;
    }
    return null;
  }

  public static boolean isNumeric(String str) {
    String bigStr;
    try {
      bigStr = new BigDecimal(str).toString();
    } catch (Exception e) {
      return false;
    }
    return true;
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
