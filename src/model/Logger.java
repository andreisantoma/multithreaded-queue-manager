package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	
	public static void log(String message) { 
	      PrintWriter out;
			try {
				out = new PrintWriter(new FileWriter("output.txt", true), true);
			    out.println(message);
			    out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
