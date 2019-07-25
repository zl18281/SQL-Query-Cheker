package com.fan.gui;

import java.io.IOException;

public class PSConvertor {

  public static void execute(String command) throws InterruptedException {

    Runtime runTime = Runtime.getRuntime();
    try {
      runTime.exec(command);
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void convert() throws Exception {

    execute("gs -sDEVICE=jpeg -g6400x4800 -r300 -sOutputFile=../webapps/SQL/img/tree.jpg ../webapps/SQL/WEB-INF/resources/img/tree.ps");
    System.out.println("After convert");

  }

}
