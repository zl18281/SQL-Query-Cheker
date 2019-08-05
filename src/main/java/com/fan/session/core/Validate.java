package com.fan.session.core;

import java.sql.Connection;
import java.sql.DriverManager;

public class Validate {

  private String username;
  private String password;

  public Validate (String username, String password) {
    this.username = username;
    this.password = password;
  }

  public boolean userValid() {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      System.out.println(username + " " + password);
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/uni" , this.username, this.password);
      if(connection != null) {
        connection.close();
        return true;
      }
      return false;
    }catch(Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
