package org.perfcake.distribution;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SlaveHandler implements Runnable {

	private Socket sock;

	private DistributionManager manager;

	public SlaveHandler(DistributionManager manager, Socket s) {
		this.manager = manager;
		this.sock = s;
		
		sendScenarioModel();
	}

	@Override
	public void run() {
		// FIXME
		while (true) {
			
		}
	}

	private void sendScenarioModel() {
		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(manager.getSlaveScenarioModel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
