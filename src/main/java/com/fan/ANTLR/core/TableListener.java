package com.fan.ANTLR.core;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class TableListener extends MySqlParserBaseListener {
  private ArrayList<String> actualTableSet = new ArrayList<>();
  MySqlParser parser;
  private String database;
  private String username;
  private String password;
  private ArrayList<String> rightTableSet = new ArrayList<>();
  private ArrayList<String> errorTables = new ArrayList<>();
  private HashMap<String, String> alias = new HashMap<>();

  public TableListener(MySqlParser parser, String database, String username, String password) {
    this.parser = parser;
    this.database = database;
    this.username = username;
    this.password = password;
    getRightTables();
  }

  @Override
  public void enterTableName(MySqlParser.TableNameContext ctx) {
    super.enterTableName(ctx);

    this.actualTableSet.add(ctx.getText());
    collectTableAlias(ctx);
  }

  private void collectTableAlias(MySqlParser.TableNameContext ctx) {
    try {
      ctx.getParent();
      if(ctx.getParent().getChild(1).getText().equals("AS") ||
        ctx.getParent().getChild(1).getText().equals("as")) {
        this.alias.put(ctx.getParent().getChild(2).getText(), ctx.getText());
      }
      if(ctx.getParent().getChild(1) instanceof MySqlParser.UidContext) {
        this.alias.put(ctx.getParent().getChild(1).getText(), ctx.getText());
      }
    }catch(Exception e) {
      System.out.println(e.toString());
    }

  }

  public HashMap<String, String> getAlias() {
    return this.alias;
  }

  private void getRightTables() {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database, this.username, this.password);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SHOW TABLES");
      while(resultSet.next()) {
        rightTableSet.add(resultSet.getString(1));
      }
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void getErrorTables(ArrayList<String> actualTableSet) {
    for(int i = 0; i < actualTableSet.size(); i++) {
      if(!this.rightTableSet.contains(actualTableSet.get(i))) {
        errorTables.add(actualTableSet.get(i));
      }
    }

    File f = new File("../webapps/SQL/WEB-INF/resources/error/tableError.json");
    try (PrintWriter pw = new PrintWriter(f)) {
      if (this.errorTables.size() == 0) {
        pw.print("{}");
      } else {
        pw.print("{");
        for (int i = 0; i < this.errorTables.size() - 1; i++) {
          pw.print("\"" + (i + 1) + "\":" + "\"" + this.errorTables.get(i) + "\",");
        }
        pw.print("\"" + this.errorTables.size() + "\":" + "\"" +
          this.errorTables.get(this.errorTables.size() - 1) + "\"}");
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  public ArrayList<String> getActualTableSet() {
    return this.actualTableSet;
  }


  public ArrayList<String> getTableSet() {
    return this.actualTableSet;
  }

}
