package com.fan.gui.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "ParseTreeServlet")
public class ParseTreeServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("inside tree servlet");
    StringBuilder s = new StringBuilder();
    try (Scanner in = new Scanner(new File("../webapps/SQL/WEB-INF/resources/img/tree.txt"))) {
      while(in.hasNext()) {
        s.append(in.next());
      }
    }catch(Exception exc) {
      exc.printStackTrace();
    }
    System.out.println(s.toString());
    response.getWriter().print(s.toString());
  }
}
