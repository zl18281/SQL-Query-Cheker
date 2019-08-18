package com.fan.ANTLR.core;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SelectListener extends MySqlParserBaseListener {

  MySqlParser parser;
  private String database;
  private String username;
  private String password;
  String[] actualColumnSet;
  private ArrayList<String> actualTableSet;
  private ArrayList<String> rightColumnSet = new ArrayList<>();
  private ArrayList<String> errorColumns = new ArrayList<>();
  private ColumnListener cl;
  private TableListener tl;

  public SelectListener(MySqlParser parser, String database,
                        String username, String password, String[] actualColumns, ArrayList<String> actualTableSet,
                        ColumnListener cl, TableListener tl) {
    this.parser = parser;
    this.database = database;
    this.username = username;
    this.password = password;
    this.actualColumnSet = actualColumns;
    this.actualTableSet = actualTableSet;
    this.cl = cl;
    this.tl = tl;
  }

  @Override
  public void enterSimpleSelect(MySqlParser.SimpleSelectContext ctx) {
    super.enterSimpleSelect(ctx);

    try {
      cl.getErrorColumns(this.actualColumnSet);
      tl.getErrorTables(this.actualTableSet);
    }catch(Exception e) {

      String[] wrongColumns = pickWrongColumn(this.actualColumnSet);
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



}
