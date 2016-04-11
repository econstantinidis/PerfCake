package org.perfcake.distribution;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.perfcake.reporting.MeasurementWrapper;

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
		
		//Create communication method
		ObjectInputStream in = null;
		try //try to grab stream from socket
		{
			in = new ObjectInputStream(sock.getInputStream());
			while (manager.isRunning())
			{
				try //try to read from socket
				{
					MeasurementWrapper wrapper = (MeasurementWrapper) in.readObject();
					if(wrapper != null)
					{
						manager.report(wrapper);
					}
					
				}
				catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
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
