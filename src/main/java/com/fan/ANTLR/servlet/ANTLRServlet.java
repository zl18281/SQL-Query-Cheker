package com.fan.ANTLR.servlet;

import com.fan.ANTLR.core.ParserDemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "ANTLRServlet")
public class ANTLRServlet extends HttpServlet {

  private String result;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    System.out.println(0);

    System.out.println(1);
    String tempResult;
    request.setCharacterEncoding("utf-8");
    tempResult = request.getParameter("code");
    System.out.println(tempResult);


    File f = new File("../webapps/SQL/WEB-INF/resources/sql/temp.sql");
    System.out.println(new File("").getAbsolutePath());
    try (PrintWriter pw = new PrintWriter(f)) {
      pw.print(tempResult);
    }catch(IOException e) {
      e.printStackTrace();
    }
    ParserDemo p = new ParserDemo(tempResult);
    this.result = p.parseSql();
    System.out.println(this.result);

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");//setting the content type
    //get the stream to write the data
    try (PrintWriter pw = response.getWriter()) {
      //writing html in the stream
      pw.print(this.result);

    }
  }
}
