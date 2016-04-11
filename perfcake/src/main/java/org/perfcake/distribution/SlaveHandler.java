package org.perfcake.distribution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.perfcake.reporting.Measurement;
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
			while (true)
			{
				try //try to read from socket
				{
					MeasurementWrapper wrapper = (MeasurementWrapper) in.readObject();
					if(wrapper != null)
					{
						reportMeasurement(wrapper);
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
	
	private void reportMeasurement(MeasurementWrapper measurement)
	{
		
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
