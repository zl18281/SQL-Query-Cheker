package com.fan.ANTLR.core;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


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

    CommonTokenStream tokens =
      (CommonTokenStream) recognizer.getInputStream();

    error[3] = tokens.getTokens().get(((Token)offendingSymbol).getTokenIndex()).getText()
      + tokens.getTokens().get(((Token)offendingSymbol).getTokenIndex()).toString();

    error[4] = "";

    ArrayList<StringBuilder> errorMsg = new ArrayList<>();
    StringTokenizer t = new StringTokenizer(msg);
    while(t.hasMoreTokens()) {
      errorMsg.add(new StringBuilder(t.nextToken()));
    }
    error[4] += "{";
    for(int i = 0; i < errorMsg.size() - 1; i++) {
      error[4] += ("\"" + i + "\":" + "\"" + errorMsg.get(i).toString() + "\",");
    }
    error[4] += "\"" + (errorMsg.size() - 1) + "\":" + "\"" + errorMsg.get(errorMsg.size() - 1) + "\"}";


    File errorInfo = new File("../webapps/SQL/WEB-INF/resources/error/error.json");

    try (FileWriter f = new FileWriter(errorInfo)){
      f.write("");
    }catch (Exception ex) {

    }

    try (PrintWriter p = new PrintWriter(errorInfo)) {
      p.println("{");
      p.println("\"stack\":" + "\"" + error[0] + "\",");
      p.println("\"line\":" + "\"" + error[1] + "\",");
      p.println("\"charPositionInLine\":" + "\"" + error[2] + "\",");
      p.println("\"offendingSymbol\":" + "\"" + error[3] + "\",");
      p.println("\"msg\":" + error[4] + ",");

      CommonTokenStream tokensTwo =
        (CommonTokenStream) recognizer.getInputStream();

      Token offendingToken = (Token)offendingSymbol;

      offendingToken = tokensTwo.getTokens().get((offendingToken).getTokenIndex());

      int cnt = 0;
      for(int i = 0; i < tokensTwo.getTokens().size(); i++) {
        cnt += tokensTwo.getTokens().get(i).getText().length();
      }
      Interval interval = new Interval(0, cnt);
      String input = tokensTwo.getTokenSource().getInputStream().getText(interval);

      String[] lines = input.split("\n");
      String errorLine;
      if(lines.length == 1) {
        errorLine = lines[0];
      }else{
        errorLine = lines[line - 1];
      }

      ArrayList<StringBuilder> errorLineText = new ArrayList<>();
      StringTokenizer t2 = new StringTokenizer(errorLine);
      while(t2.hasMoreTokens()) {
        errorLineText.add(new StringBuilder(t2.nextToken()));
      }
      StringBuilder errorLineInJson = new StringBuilder("{");
      for(int i = 0; i < errorLineText.size() - 1; i++) {
        String temp = "\"" + i + "\":" + "\"" + errorLineText.get(i).toString() + "\",";
        errorLineInJson.append(temp);
      }
      String temp = "\"" + (errorLineText.size() - 1) + "\":" + "\"" + errorLineText.get(errorLineText.size() - 1) + "\"}";
      errorLineInJson.append(temp);

      p.println("\"errorLine\":" + errorLineInJson.toString() + ",");

      p.println("\"numOfSpaces\":" + "\"" + (charPositionInLine) + "\",");
      int start = offendingToken.getStartIndex();
      int stop = offendingToken.getStopIndex();
      p.println("\"numOfArrows\":" + "\"" + offendingToken.getText().length() + "\"");
      p.println("}");

    } catch (IOException ex) {

    }

    //for research only
    File f = new File("/home/fan/error.json");
    try(Scanner fr = new Scanner(errorInfo); FileWriter fw = new FileWriter(f)) {
      while(fr.hasNext()) {
        fw.write(fr.next());
      }
    }catch(Exception ex) {

    }
  }
}


