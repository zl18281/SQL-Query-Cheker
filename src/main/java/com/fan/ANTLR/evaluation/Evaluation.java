package com.fan.ANTLR.evaluation;

import com.fan.ANTLR.core.ParserDemo;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class Evaluation {

  public static void main(String[] args) {

    StringBuilder query = new StringBuilder();
    File f = new File("/home/fan/testing.sql");

    for(int j = 5; j <= 200; j+=5) {
      try (PrintWriter fw = new PrintWriter(f)) {
        for (int i = 0; i < (j - 1); i++) {
          fw.println("SELECT Student.nam AS n, Enrol.grade AS g, Unit.title AS t\n" +
            "FROM Student INNER JOIN Enrol ON Enrol.studen=Student.id\n" +
            "INNER JOIN Unit ON Enrol.unit=Unit.id\n" +
            "WHERE Unit.titl='Databases' UNION ");
        }
        fw.println("SELECT Student.nam AS n, Enrol.grade AS g, Unit.title AS t\n" +
          "FROM Student INNER JOIN Enrol ON Enrol.studen=Student.id\n" +
          "INNER JOIN Unit ON Enrol.unit=Unit.id\n" +
          "WHERE Unit.titl='Databases';");
      } catch (Exception e) {
        e.printStackTrace();
      }

      try (Scanner s = new Scanner(f)) {
        while (s.hasNext()) {
          query.append(s.next() + " ");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      ParserDemo p = new ParserDemo(query.toString(), "uni", "student", "", j);
      long startTime = new Date().getTime();
      p.parseSql();
      long endTime = new Date().getTime();

      File fs = new File("/home/fan/tot_time.txt");
      BufferedWriter out = null;
      try {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fs, true)));
        out.write(j + " " + Double.toString((endTime - startTime) / 1000.0) + "\n");
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (out != null) {
            out.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    File ftot = new File("/home/fan/tot_time.txt");
    File ftotJson = new File("/home/fan/tot_time.json");
    convertToJson(ftot, ftotJson);

    File fSyntax = new File("/home/fan/syntax.txt");
    File fSyntaxJson = new File("/home/fan/syntax.json");
    convertToJson(fSyntax, fSyntaxJson);

    File fSemantic = new File("/home/fan/semantic.txt");
    File fSemanticJson = new File("/home/fan/semantic.json");
    convertToJson(fSemantic, fSemanticJson);

    File fTree = new File("/home/fan/tree.txt");
    File fTreeJson = new File("/home/fan/tree.json");
    convertToJson(fTree, fTreeJson);

    File fOther = new File("/home/fan/other.txt");
    File fOtherJson = new File("/home/fan/other.json");
    convertToJson(fOther, fOtherJson);

  }

  private static void convertToJson(File input, File output) {
    try (Scanner s = new Scanner(input);PrintWriter pw = new PrintWriter(output)) {
      pw.println("{");
      while(s.hasNextLine()) {
        String[] temp = s.nextLine().split(" ");
        if(s.hasNextLine()) {
          pw.println("\"" + temp[0] + "\":" + "\"" + temp[1] + "\",");
        }else{
          pw.println("\"" + temp[0] + "\":" + "\"" + temp[1] + "\"");
        }
      }
      pw.println("}");
    }catch(Exception eOne) {
      eOne.printStackTrace();
    }
  }
}
