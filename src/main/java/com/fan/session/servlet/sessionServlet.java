package com.fan.session.servlet;

import com.fan.session.core.Validate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "sessionServlet")
public class sessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Validate v = new Validate(request.getParameter("username"), request.getParameter("password"));
      System.out.println(v.userValid());
      if(v.userValid()) {
        Cookie cUsername = new Cookie("username", request.getParameter("username"));
        Cookie cPassword = new Cookie("password", request.getParameter("password"));
        response.addCookie(cUsername);
        response.addCookie(cPassword);
      }
      request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
