package com.fan.db.servlet;

import com.fan.db.core.DB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet(name = "getDBServlet")
public class getDBServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
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
    response.getWriter().print((new DB(username, password)).getDB());
  }
}
