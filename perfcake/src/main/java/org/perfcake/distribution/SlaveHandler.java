package org.perfcake.distribution;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.perfcake.reporting.MeasurementWrapper;

public class SlaveHandler implements Runnable {

	private Socket sock;
	private ObjectInputStream objInputStream;

	private DistributionManager manager;

	public SlaveHandler(DistributionManager manager, Socket s) {
		this.manager = manager;
		this.sock = s;

		try {
			this.objInputStream = new ObjectInputStream(this.sock.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendScenarioModel();
	}

	@Override
	public void run() {
		while (manager.isRunning() && !sock.isClosed()) {
			MeasurementWrapper wrapper = null;
			try {
				wrapper = (MeasurementWrapper) objInputStream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				break;
			}
			if(wrapper != null)
			{
				manager.report(wrapper);
			}
		}

		manager.handlerFinished(Thread.currentThread());
	}

	private void sendScenarioModel() {
		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(manager.getScenarioModel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
