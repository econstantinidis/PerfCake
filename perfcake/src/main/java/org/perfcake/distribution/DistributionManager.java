package org.perfcake.distribution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.perfcake.model.Scenario;

public class DistributionManager {
	int port;
	String address;

	public DistributionManager(int port, String address)
	{
		this.port = port;
		this.address = address;
	}
	
	@SuppressWarnings("resource")
	public void SendToSlaves(Scenario scenario)
	{
		ServerSocket ssock;
		try {
			ssock = new ServerSocket(port);
	      System.out.println("Listening");
	      while (true) {
	         Socket sock;			
				sock = ssock.accept();
	         System.out.println("Connected");
	         new Thread(new MasterServerThread(sock, scenario)).start();
	      }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
