package com.fan.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;


public class UnderlineListener extends BaseErrorListener {
  public void syntaxError(Recognizer<?, ?> recognizer,
                          Object offendingSymbol,
                          int line, int charPositionInLine,
                          String msg,
                          RecognitionException e) {

    List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
    Collections.reverse(stack);

    String[] error = new String[5];
    error[0] = stack.toString();
    error[1] = "" + line;
    error[2] = "" + charPositionInLine;
    error[3] = offendingSymbol.toString();
    error[4] = msg;

    for(int i = 0; i < error.length; i++) {
      System.out.println(error[i]);
    }

    File errorInfo = new File("../webapps/SQL/WEB-INF/resources/error/error.json");

    try (FileWriter fw = new FileWriter(errorInfo)) {
      fw.write("");
    } catch (IOException ex) {
      System.err.println("file not found * !");
    }

    try (PrintWriter p = new PrintWriter(errorInfo)) {
      p.println("{");
      p.println("\"stack\":" + "\"" + error[0] + "\",");
      p.println("\"line\":" + "\"" + error[1] + "\",");
      p.println("\"charPositionInLine\":" + "\"" + error[2] + "\",");
      p.println("\"offendingSymbol\":" + "\"" + error[3] + "\",");
      p.println("\"msg\":" + "\"" + error[4] + "\"");
      p.println("}");
    } catch (IOException ex) {
      System.err.println("file not found *** !");
    }

  }

}

