package org.perfcake.distribution;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.perfcake.model.Scenario;

public class MasterServerThread implements Runnable {

	   Socket csocket = null;
	   Scenario scenario;
	   MasterServerThread(Socket csocket, Scenario scenario) {
	      this.csocket = csocket;
	      this.scenario = scenario;
	   }

	   public void run() {
		   PrintStream pstream = null;
		   try {
			   pstream = new PrintStream(csocket.getOutputStream());
	      }
	      catch (IOException e) {
	         System.out.println(e);
	      }
	            ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(csocket.getOutputStream());
					oos.writeObject(scenario);
		            oos.close();
			        pstream.close();
			        csocket.close();
				} catch (IOException e) {
			        pstream.close();
			        try {
						csocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
	            System.out.println("Connection ended");

	   }
	}

