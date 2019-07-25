package com.fan.parser;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ParserDemo {

  private String code;

  public ParserDemo(String code) {
    System.out.println("hahaha");
    this.code = code;
  }

  public String parseSql() {

    ANTLRErrorListener error = new UnderlineListener();
    CharStream input = CharStreams.fromString(this.code);
    MySqlLexer lexer = new MySqlLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MySqlParser parser = new MySqlParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(error);
    parser.root();
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

  public static void main(String[] args) {
    ParserDemo pd = new ParserDemo("SELECT FROM hero WHERE name='fan';");
    System.out.println(pd.parseSql());
  }
}
