package com.fan.ANTLR.core;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParserDemo {

  private String code;

  public ParserDemo(String code) {
    System.out.println("hahaha");
    this.code = code;
  }

  public String parseSql() {

    ANTLRErrorListener error = new UnderlineListener();

    CharStream input = CharStreams.fromString(this.code.toUpperCase());
    MySqlLexer lexer = new MySqlLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MySqlParser parser = new MySqlParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(error);
    ParseTree tree = parser.root();

    //
    System.out.println("1");
    List<String> rules = new ArrayList<>();
    System.out.println("2");
    String[] rulesNames = parser.makeRuleNames();
    for(int i = 0; i < rulesNames.length; i++) {
      rules.add(rulesNames[i]);
    }
    System.out.println("3");
    TreeViewer tv = new TreeViewer(rules, (Tree)tree);
    System.out.println("4");


    try {
      File svgFile = new File("../webapps/SQL/img/tree.svg");
      BufferedWriter writer = new BufferedWriter(new FileWriter(svgFile));
      writer.write("<svg width=\"" + tv.getSize().getWidth() * 1.1 + "\" height=\"" +
        tv.getSize().getHeight() * 1.1 + "\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
      Method m = tv.getClass().getDeclaredMethod("paintSVG", Writer.class);
      m.setAccessible(true);
      m.invoke(tv, writer);
      writer.write("</svg>");
      writer.flush();
      writer.close();
    }catch(Exception e) {
      e.printStackTrace();
    }



    //
    try(FileWriter fwTree = new FileWriter("../webapps/SQL/WEB-INF/resources/img/tree.txt")){
      fwTree.write(tree.toStringTree(parser));
    }catch(IOException e) {
      e.printStackTrace();
    }

    System.out.println("right after parsing");

    File f = new File("../webapps/SQL/WEB-INF/resources/error/error.json");
    System.out.println("file opened");
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
