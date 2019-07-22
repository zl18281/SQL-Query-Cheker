package com.fan;

import com.fan.parser.ParserDemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MainPageServlet")
public class MainPageServlet extends HttpServlet {

  private String result;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    System.out.println(0);

    System.out.println(1);
    String tempResult;
    request.setCharacterEncoding("utf-8");
    tempResult = request.getParameter("code");
    System.out.println(tempResult);
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
