package com.fan.ANTLR.evaluation;

import com.fan.ANTLR.core.ParserDemo;

import java.io.*;
import java.util.*;

public class Evaluation {

  public static void main(String[] args) {

    File f = new File("/home/fan/testing.sql");

    String sl = "SELECT \n" +
      "t1.region1 AS region,\n" +
      "t1.men AS men,\n" +
      "t2.women AS women,\n" +
      "t2.women / t3.population * 100 AS percent\n" +
      "FROM\n" +
      "((SELECT \n" +
      "r.name AS region1, SUM(s.data) AS men\n" +
      "FROM\n" +
      "Statistic s\n" +
      "INNER JOIN Ward w ON s.wardId = w.code\n" +
      "INNER JOIN County c ON w.parent = c.code\n" +
      "INNER JOIN Region r ON c.parent = r.code\n" +
      "WHERE\n" +
      "s.occId = 1 AND s.gender = 0\n" +
      "GROUP BY r.code) AS t1\n" +
      "INNER JOIN (SELECT \n" +
      "r.name AS region2, SUM(s.data) AS women\n" +
      "FROM\n" +
      "Statistic s\n" +
      "INNER JOIN Ward w ON s.wardId = w.code\n" +
      "INNER JOIN County c ON w.parent = c.code\n" +
      "INNER JOIN Region r ON c.parent = r.code\n" +
      "WHERE\n" +
      "s.occId = 1 AND s.gender = 1\n" +
      "GROUP BY r.code) AS t2 ON t1.region1 = t2.region2\n" +
      "INNER JOIN (SELECT \n" +
      "r.name AS region3, SUM(s.data) AS population\n" +
      "FROM\n" +
      "Statistic s\n" +
      "INNER JOIN Ward w ON s.wardId = w.code\n" +
      "INNER JOIN County c ON w.parent = c.code\n" +
      "INNER JOIN Region r ON c.parent = r.code\n" +
      "WHERE\n" +
      "s.occId = 1\n" +
      "GROUP BY r.code) AS t3 ON t1.region1 = t3.region3)\n" +
      "ORDER BY percent ASC;";

    StringTokenizer st = new StringTokenizer(sl);
    int cnt = 0;
    ArrayList<String> token = new ArrayList<>();
    while(st.hasMoreTokens()){
      token.add(st.nextToken());
      cnt++;
    }
    System.out.println(cnt);

    int r = 0;
    for(int j = 1; j <= 1000; j++) {
      StringBuilder query = new StringBuilder();
      try (PrintWriter fw = new PrintWriter(f)) {
        ArrayList<String> tokenCopy = new ArrayList<>();
        for(int i = 0; i < token.size(); i++) {
          tokenCopy.add(token.get(i));
        }
        Random rand = new Random(System.currentTimeMillis());
        int position = rand.nextInt(cnt);
        r = position;
        token.remove(position);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < token.size(); i++) {
          sb.append(token.get(i) + " ");
        }
        fw.println(sb);
        token = tokenCopy;


        //test syntax and semantic errors

/*
        for (int i = 1; i < j; i++) {
          fw.println("SELECT SUM(s.data) AS num_people, o.name AS occ_class\n" +
            "FROM Statistic s\n" +
            "INNER JOIN Occupation o ON s.occId = o.id\n" +
            "WHERE s.wardId = 'E05008884'\n" +
            "GROUP BY o.id;");
        }
        fw.println("SELECT SUM(s.data) AS num_people, o.nam AS occ_class\n" +
          "FROM Statistic s\n" +
          "INNER JOIN Occupation o ON s.occId = o.id\n" +
          "WHERE s.wardId = 'E05008884'\n" +
          "GROUP BY o.id;");
        for (int i = 1; i <= (200 - j); i++) {
          fw.println("SELECT SUM(s.data) AS num_people, o.name AS occ_class\n" +
            "FROM Statistic s\n" +
            "INNER JOIN Occupation o ON s.occId = o.id\n" +
            "WHERE s.wardId = 'E05008884'\n" +
            "GROUP BY o.id;");
        }

 */


        //test right syntax
/*
        for (int i = 0; i < j; i++) {
          fw.println("SELECT \n" +
            "    c.name AS CLU,\n" +
            "    o.name AS occupation,\n" +
            "    CASE s.gender\n" +
            "        WHEN 1 THEN 'female'\n" +
            "        WHEN 0 THEN 'male'\n" +
            "        ELSE 'X'\n" +
            "    END AS gender,\n" +
            "    SUM(s.data) AS N\n" +
            "FROM\n" +
            "    Occupation o\n" +
            "        INNER JOIN\n" +
            "    Statistic s ON o.Id = s.occId\n" +
            "        INNER JOIN\n" +
            "    Ward w ON s.wardId = w.code\n" +
            "        INNER JOIN\n" +
            "    County c ON w.parent = c.code\n" +
            "        INNER JOIN\n" +
            "    Region r ON c.parent = r.code\n" +
            "WHERE\n" +
            "    r.code = 'E12000002'\n" +
            "GROUP BY c.code , o.Id , s.gender\n" +
            "HAVING N >= 10000\n" +
            "ORDER BY N;");
        }

 */

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
      ParserDemo p = new ParserDemo(query.toString(), "census", "student", "", r);
      long startTime = new Date().getTime();
      p.parseSql();
      long endTime = new Date().getTime();

      File fs = new File("/home/fan/tot_time.txt");
      BufferedWriter out = null;
      try {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fs, true)));
        out.write(r + " " + Double.toString((endTime - startTime) / 1000.0) + "\n");
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
    if(input.exists()) {
      input.delete();
    }
  }
}
