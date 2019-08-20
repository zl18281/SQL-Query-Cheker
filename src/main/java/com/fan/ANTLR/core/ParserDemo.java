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
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ParserDemo {

  private String database;
  private String username;
  private String password;

  private static Logger logger = Logger.getLogger(ParserDemo.class);

  private String code;
  private int run;

  public ParserDemo(String code, String database, String username, String password, int run) {
    this.code = code;
    this.database = database;
    this.username = username;
    this.password = password;
    this.run = run;
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

    long startSyntaxCheck = new Date().getTime(); //check time consumed for syntax analysis
    ParseTree tree = parser.root();
    long endSyntaxCheck = new Date().getTime(); //check time consumed for syntax analysis
    writeSyntaxTimeToFile(Double.toString((endSyntaxCheck - startSyntaxCheck)/1000.0), run);

    long startSemanticCheck = new Date().getTime(); //check time consumed for semantic analysis
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
    long endSemanticCheck = new Date().getTime(); //check time consumed for semantic analysis
    writeSemanticTimeToFile(Double.toString((endSemanticCheck - startSemanticCheck)/1000.0), run);

    long startBuildingTree = new Date().getTime();
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
        System.out.println(System.getProperty("user.dir"));
        e.printStackTrace();
    }
    long endBuildingTree = new Date().getTime();
    writeTreeTimeToFile(Double.toString((endBuildingTree - startBuildingTree)/1000.0), run);

    long startOtherTask = new Date().getTime();
    File f = new File("../webapps/SQL/WEB-INF/resources/error/error.json");
    StringBuilder errorInfo = new StringBuilder();
    try (Scanner in = new Scanner(f)) {
      while (in.hasNext()) {
        errorInfo.append(in.next());
      }
    } catch (IOException e) {

    }
    try (FileWriter fw = new FileWriter(f)){
      fw.write("");
    }catch (Exception ex) {

    }
    long endOtherTask = new Date().getTime();
    writeOtherTimeToFile(Double.toString((endOtherTask - startOtherTask)/1000.0), run);

    return errorInfo.toString();
  }

  private void writeSyntaxTimeToFile(String s, int j) {
    File f = new File("/home/fan/syntax.txt");
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
      out.write(j + " " + s + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {

      }
    }
  }

  private void writeSemanticTimeToFile(String s, int j) {
    File f = new File("/home/fan/semantic.txt");
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
      out.write(j + " " + s + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {

      }
    }
  }

  private void writeTreeTimeToFile(String s, int j) {
    File f = new File("/home/fan/tree.txt");
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
      out.write(j + " " + s + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {

      }
    }
  }

  private void writeOtherTimeToFile(String s, int j) {
    File f = new File("/home/fan/other.txt");
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
      out.write(j + " " + s + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {

      }
    }
  }

}
