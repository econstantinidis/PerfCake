package org.perfcake.distribution;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.perfcake.model.Scenario;

public class MasterServerThread implements Runnable {

	   Socket csocket;
	   Scenario scenario;
	   MasterServerThread(Socket csocket, Scenario scenario) {
	      this.csocket = csocket;
	      this.scenario = scenario;
	   }

	   public void run() {
	      try {
	         PrintStream pstream = new PrintStream(csocket.getOutputStream());
	         pstream.close();
	         csocket.close();
	      }
	      catch (IOException e) {
	         System.out.println(e);
	      }
	        while (true) {
	            ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(csocket.getOutputStream());
					oos.writeObject(scenario);
		            oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.println("Connection ended");
	        }
	   }
	}

