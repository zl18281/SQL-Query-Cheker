package com.fan.ANTLR.core;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ColumnListener extends MySqlParserBaseListener {
  MySqlParser parser;
  private String database;
  private String username;
  private String password;
  private ArrayList<String> actualColumnSet = new ArrayList<>();
  private ArrayList<String> rightColumnSet = new ArrayList<>();
  private ArrayList<String> errorColumns = new ArrayList<>();
  private ArrayList<String> tableSet;
  private HashMap<String, String> alias;
  private HashMap<String, String> columnAlias = new HashMap<>();

  public ColumnListener(MySqlParser parser, String database, String username,
                        String password, ArrayList<String> tableSet,
                        HashMap<String, String> alias) {
    this.parser = parser;
    this.database = database;
    this.username = username;
    this.password = password;
    this.tableSet = tableSet;
    this.alias = alias;
    getRightColumns();
  }

  private void removeDuplicate() {
    for (int i = 0; i < this.errorColumns.size() - 1; i++) {
      for (int j = errorColumns.size() - 1; j > i; j--) {
        if (errorColumns.get(j).equals(errorColumns.get(i))) {
          errorColumns.remove(j);
        }
      }
    }
  }

  public void getErrorColumns(String[] actualColumns) {

    try {
      for (int i = 0; i < actualColumns.length; i++) {
        if (checkPrefix(actualColumns[i])) {
          System.out.println(actualColumns[i]);
          if (this.tableSet.contains(getPrefix(actualColumns[i]))) {
            if (!this.getRightColumnsForOneTable(getPrefix(actualColumns[i])).contains(removePrefix(actualColumns[i]))) {
              errorColumns.add(removePrefix(actualColumns[i]));
            }
          } else {
            System.out.println("2");
            if (!this.alias.containsKey(getPrefix(actualColumns[i]))) {
              errorColumns.add(actualColumns[i]);
            } else {
              System.out.println("3");
              System.out.println(this.alias.get(getPrefix(actualColumns[i])));
              if (!this.getRightColumnsForOneTable(this.alias.get(getPrefix(actualColumns[i]))).contains((removePrefix(actualColumns[i])))) {
                errorColumns.add(removePrefix(actualColumns[i]));
                System.out.println("*****");
              }
            }
          }
        } else {
          System.out.println("#");
          if (!this.rightColumnSet.contains(actualColumns[i])) {
            System.out.println("##");
            if (!this.columnAlias.containsKey(actualColumns[i])) {
              System.out.println("###");
              errorColumns.add(actualColumns[i]);
            }
          } else {
            if (checkAmbiguity(actualColumns[i])) {
              System.out.println("####");
              errorColumns.add(actualColumns[i]);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    removeDuplicate();

    File f = new File("../webapps/SQL/WEB-INF/resources/error/semanticError.json");
    try (PrintWriter pw = new PrintWriter(f)) {
      if (this.errorColumns.size() == 0) {
        pw.print("{}");
      } else {
        pw.print("{");
        for (int i = 0; i < this.errorColumns.size() - 1; i++) {
          pw.print("\"" + (i + 1) + "\":" + "\"" + this.errorColumns.get(i) + "\",");
        }
        pw.print("\"" + this.errorColumns.size() + "\":" + "\"" +
          this.errorColumns.get(this.errorColumns.size() - 1) + "\"}");
      }
    } catch (Exception e) {

    }

  }

  @Override
  public void enterFullColumnName(MySqlParser.FullColumnNameContext ctx) {
    super.enterFullColumnName(ctx);

    try {
      var asClause = ctx.getParent();
      var aggFunction = ctx.getParent().getParent().getParent();
      if (aggFunction instanceof MySqlParser.AggregateFunctionCallContext) {
        this.rightColumnSet.add(aggFunction.getParent().getChild(2).getText());
      }
      if (asClause.getChild(1).getText().equals("AS")) {
        if (!this.columnAlias.containsKey(asClause.getChild(1).getText())) {
          this.columnAlias.put(ctx.getParent().getChild(2).getText(), ctx.getText());
        }
      }

    } catch (Exception e) {

    }

    this.actualColumnSet.add(ctx.getText());
  }

  private boolean checkPrefix(String columnName) {
    for (int i = 0; i < columnName.length(); i++) {
      if (columnName.charAt(i) == '.') {
        return true;
      }
    }
    return false;
  }

  private String removePrefix(String columnName) {
    int indexStart = 0;
    for (int i = 0; i < columnName.length(); i++) {
      if (columnName.charAt(i) == '.') {
        indexStart = (i + 1);
        return columnName.substring(indexStart);
      }
    }
    return columnName.substring(indexStart);
  }

  private String getPrefix(String columnName) {
    int indexStart = 0;
    for (int i = 0; i < columnName.length(); i++) {
      if (columnName.charAt(i) == '.') {
        indexStart = i;
        return columnName.substring(0, indexStart);
      }
    }
    return columnName.substring(indexStart);
  }

  public String[] getActualColumnSet() {
    String[] actualColumns = new String[this.actualColumnSet.size()];

    for (int i = 0; i < this.actualColumnSet.size(); i++) {
      actualColumns[i] = this.actualColumnSet.get(i);
    }
    return actualColumns;
  }

  private ArrayList<String> getRightColumnsForOneTable(String table) {
    ArrayList<String> columnsForOneTable = new ArrayList<>();
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" +
        this.database, this.username, this.password);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("DESC " + table);
      while (resultSet.next()) {
        this.rightColumnSet.add(resultSet.getString("Field"));
        columnsForOneTable.add(resultSet.getString("Field"));
      }
      connection.close();
      return columnsForOneTable;
    } catch (Exception e) {

    }
    return null;
  }

  private void getRightColumns() {
    for (int i = 0; i < this.tableSet.size(); i++) {
      getRightColumnsForOneTable(this.tableSet.get(i));
    }
  }

  private boolean checkAmbiguity(String s) {
    int cnt = 0;
    for (int j = 0; j < rightColumnSet.size(); j++) {
      if (rightColumnSet.get(j).equals(s)) {
        cnt++;
      }
    }
    if (cnt > 1) {
      return true;
    } else {
      return false;
    }
  }
}
