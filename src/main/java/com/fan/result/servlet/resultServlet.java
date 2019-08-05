package com.fan.result.servlet;

import com.fan.result.core.ConstructJSON;
import com.fan.result.core.ShowResult;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet(name = "resultServlet")
public class resultServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      Cookie[] cook = request.getCookies();
      String username = null;
      String password = null;
      for(Cookie c:cook) {
        if(c.getName().equals("username")) {
          username = URLDecoder.decode(c.getValue(), "utf-8");
        }
        if(c.getName().equals("password")) {
          password = URLDecoder.decode(c.getValue(), "utf-8");
        }
      }

      JSONObject data = new JSONObject(request.getParameter("codeArr"));
      ShowResult sr = new ShowResult(data.get("db").toString(), username,
        password, request.getParameter("codeArr"));

      response.setContentType("application/json");
      response.getWriter().print(ConstructJSON.convert(sr.execute()));
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
