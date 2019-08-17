package com.fan.ANTLR.core;

import java.util.ArrayList;

public class TableListener extends MySqlParserBaseListener {
  private ArrayList<String> tableSet = new ArrayList<>();
  MySqlParser parser;
  private String database;
  private String username;
  private String password;

  public TableListener(MySqlParser parser, String database, String username, String password) {
    this.parser = parser;
    this.database = database;
    this.username = username;
    this.password = password;
  }

  @Override
  public void enterTableName(MySqlParser.TableNameContext ctx) {
    super.enterTableName(ctx);

    this.tableSet.add(ctx.getText());
  }

  public ArrayList<String> getTableSet(){
    for(int i = 0; i < this.tableSet.size(); i++) {
      System.out.println(this.tableSet.get(i));
    }
    return this.tableSet;
  }
}
