package com.fan.ANTLR.core;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.Tree;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParserDemo {

  private String database;
  private String username;
  private String password;

  private static Logger logger = Logger.getLogger(ParserDemo.class);

  private String code;

  public ParserDemo(String code, String database, String username, String password) {
    this.code = code;
    this.database = database;
    this.username = username;
    this.password = password;
  }

  public String parseSql() {

    ANTLRErrorListener error = new UnderlineListener();

    CharStream input = CharStreams.fromString(this.code);
    CaseChangingCharStream upper = new CaseChangingCharStream(input, true);
    MySqlLexer lexer = new MySqlLexer(upper);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MySqlParser parser = new MySqlParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(error);

    ParseTree tree = parser.root();
    ParseTreeWalker ptw = new ParseTreeWalker();

    TableListener tl = new TableListener(parser, this.database,
      this.username, this.password);
    ptw.walk(tl, tree);
    var tableSet = tl.getTableSet();

    ColumnListener cl = new ColumnListener(parser, this.database,
      this.username, this.password, tableSet, tl.getAlias());
    ptw.walk(cl, tree);

    SelectListener sl = new SelectListener(parser, this.database,
      this.username, this.password, cl.getActualColumnSet(),
      tl.getActualTableSet(), cl, tl);
    ptw.walk(sl, tree);

    List<String> rules = new ArrayList<>();
    String[] rulesNames = parser.makeRuleNames();
    for(int i = 0; i < rulesNames.length; i++) {
      rules.add(rulesNames[i]);
    }
    TreeViewer tv = new TreeViewer(rules, (Tree)tree);

    try {
      File svgFile = new File("../webapps/SQL/img/tree.svg");
      BufferedWriter writer = new BufferedWriter(new FileWriter(svgFile));
      writer.write("<svg width=\"" + tv.getSize().getWidth() * 1.1 + "\" height=\"" +
        tv.getSize().getHeight() * 1.1 +
        "\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
      Method m = tv.getClass().getDeclaredMethod("paintSVG", Writer.class);
      m.setAccessible(true);
      m.invoke(tv, writer);
      writer.write("</svg>");
      writer.flush();
      writer.close();
    }catch(Exception e) {
      e.printStackTrace();
    }

    File f = new File("../webapps/SQL/WEB-INF/resources/error/error.json");
    StringBuilder errorInfo = new StringBuilder();
    try (Scanner in = new Scanner(f)) {
      while (in.hasNext()) {
        errorInfo.append(in.next());
      }
    } catch (IOException e) {
      System.err.println("file not found ** !");
    }
    try (FileWriter fw = new FileWriter(f)){
      fw.write("");
    }catch (Exception ex) {
      ex.printStackTrace();
    }

    return errorInfo.toString();
  }

}
