package org.perfcake.distribution;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterListener implements Runnable {

	private ServerSocket serverSock;

	private DistributionManager manager;

	public MasterListener(DistributionManager manager, InetAddress listenAddress, int port) {
		this.manager = manager;

		try {
			this.serverSock = new ServerSocket(port, 0, listenAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			Socket slave = null;
			try {
				slave = serverSock.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (slave != null) {
				manager.accept(slave);
			}
		}
	}

}
