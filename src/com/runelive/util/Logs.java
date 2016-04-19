package com.runelive.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.GregorianCalendar;

public class Logs {

  /**
   * Writes data to a log file of your choice.
   * 
   */
  public static void write_data(String file, String folder, String text) {
    BufferedWriter bw = null;
    try {
      FileWriter fileWriter = new FileWriter("./logs/" + folder + "/" + file, true);
      bw = new BufferedWriter(fileWriter);
      bw.write("[" + new GregorianCalendar().getTime() + "]: " + text);
      bw.newLine();
      bw.flush();
      bw.close();
      fileWriter = null;
      bw = null;
    } catch (Exception exception) {
      System.out.print("Critical error while writing data: " + file);
    }
  }

}
