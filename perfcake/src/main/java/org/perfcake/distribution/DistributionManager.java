package org.perfcake.distribution;

import java.net.InetAddress;
import java.net.Socket;

import org.perfcake.model.Scenario;

public class DistributionManager {

	private MasterListener listener;
	private Thread listenerThread;
	
	private Scenario scenarioModel;
	
	public DistributionManager(InetAddress listenAddress, int port)
	{
		listener = new MasterListener(this, listenAddress, port);
		listenerThread = new Thread(listener);
		listenerThread.start();
	}
	
	public void accept(Socket s) {
		new Thread(new SlaveHandler(this, s)).start();
	}

	public Scenario getScenarioModel() {
		return scenarioModel;		
	}
	
	public void setScenarioModel(Scenario scenarioModel) {
		this.scenarioModel = scenarioModel;		
	}
	
}
