package com.fan.parser;

import org.antlr.v4.runtime.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UnderlineListener extends BaseErrorListener {
  public void syntaxError(Recognizer<?, ?> recognizer,
                          Object offendingSymbol,
                          int line, int charPositionInLine,
                          String msg,
                          RecognitionException e) {

    List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
    Collections.reverse(stack);
    String error = "";
    error += "rule stack: " + stack.toString() + '\n';
    error += "line " + line + ":" + charPositionInLine+" at "+ offendingSymbol + ": " + msg + '\n';

    File errorInfo = new File("/home/fan/error.txt");
    try (FileWriter fi = new FileWriter(errorInfo)) {
        fi.write(error);
    } catch (IOException ex) {
      System.err.println("file not found !");
    }

  }

}

