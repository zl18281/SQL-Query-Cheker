package com.fan.ANTLR.servlet;

import com.fan.ANTLR.core.ParserDemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

@WebServlet(name = "ANTLRServlet")
public class ANTLRServlet extends HttpServlet {

  private String result;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String tempResult;
    request.setCharacterEncoding("utf-8");
    tempResult = request.getParameter("code");

    File f = new File("../webapps/SQL/WEB-INF/resources/sql/temp.sql");
    try (PrintWriter pw = new PrintWriter(f)) {
      pw.print(tempResult);
    }catch(IOException e) {
      e.printStackTrace();
    }

    String username = null;
    String password = null;
    Cookie[] cookieArr = request.getCookies();

    for(Cookie c: cookieArr) {
      if(c.getName().equals("username")) {
        username = URLDecoder.decode(c.getValue(), "utf-8");
      }
      if(c.getName().equals("password")) {
        password = URLDecoder.decode(c.getValue(), "utf-8");
      }
    }
    try {
      ParserDemo p = new ParserDemo(tempResult, request.getParameter("db"), username, password, 0);
      this.result = p.parseSql();
    }catch(Exception e) {
      e.printStackTrace();
    }

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/plain");//setting the content type
    //get the stream to write the data
    try (PrintWriter pw = response.getWriter()) {
      //writing html in the stream
      pw.print(this.result);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
