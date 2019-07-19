package com.fan;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MainPageServlet")
public class MainPageServlet extends HttpServlet {

    private String result = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        System.out.println(request.getParameter("code"));
        this.result = request.getParameter("code");

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
