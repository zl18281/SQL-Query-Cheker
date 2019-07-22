package com.fan.gui;

import org.antlr.v4.gui.TestRig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ParseTreeServlet")
public class ParseTreeServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("inside tree servlet");
    String path = System.getProperty("user.dir");
    String[] args = {"MySql", "root", "-gui", "-ps", "/home/fan/tree.ps", "/home/fan/temp.sql"};
    try {
      TestRig.main(args);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
