package com.fan.ANTLR.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "SemanticServlet")
public class columnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json");
      File f = new File("../webapps/SQL/WEB-INF/resources/error/semanticError.json");
      if(f.exists()) {
        try {
          Scanner fr = new Scanner(f);
          while(fr.hasNext()) {
            response.getWriter().print(fr.next());
          }
        }catch(Exception e) {
          System.out.println(e.toString());
        }
      }else{
        response.getWriter().print("{}");
      }

    }
}
