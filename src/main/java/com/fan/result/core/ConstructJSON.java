package com.fan.result.core;

import java.util.ArrayList;

public class ConstructJSON {

  public static String convert(ArrayList<String[][]> tables) {
    StringBuilder result = new StringBuilder();

    result.append("{");
    for(int i = 0; i < tables.size(); i++) {
      result.append("\""+ (i + 1) + "\":");
      result.append("{");
      for(int j = 0; j < tables.get(i).length; j++) {
        result.append("\""+ (j + 1) + "\":");
        result.append("{");
        for(int k = 0; k < tables.get(i)[j].length; k++) {
          result.append("\""+ (k + 1) + "\":");
          if(k < tables.get(i)[j].length - 1) {
            result.append("\""+ tables.get(i)[j][k] + "\",");
          }else{
            result.append("\""+ tables.get(i)[j][k] + "\"");
          }
        }
        if(j < tables.get(i).length - 1) {
          result.append("},");
        }else{
          result.append("}");
        }
      }
      if(i < tables.size() - 1) {
        result.append("},");
      }else {
        result.append("}");
      }
    }
    result.append("}");
    System.out.println(result.toString());
    return result.toString();
  }
}
