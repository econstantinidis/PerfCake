package org.perfcake.distribution;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.perfcake.model.Scenario.Reporting;
import org.perfcake.model.Scenario.Reporting.Reporter;
import org.perfcake.model.Scenario.Reporting.Reporter.Destination;

public class DistributionManager {

	private MasterListener listener;
	private Thread listenerThread;
	
	private Scenario slaveScenarioModel;
	
	public DistributionManager(InetAddress listenAddress, int port)
	{
		listener = new MasterListener(this, listenAddress, port);
		listenerThread = new Thread(listener);
		listenerThread.start();
	}
	
	public void accept(Socket s) {
		new Thread(new SlaveHandler(this, s)).start();
	}

	public Scenario getSlaveScenarioModel() {
		return slaveScenarioModel;		
	}
	
	public void setScenarioModel(Scenario scenarioModel) {
		// FIXME setup mappings once at set time - throw if anyone tries to reset
		
		this.slaveScenarioModel = transformToSlaveScenarioModel(scenarioModel);
	}

	private Scenario transformToSlaveScenarioModel(Scenario model) {
		Reporting reporting = model.getReporting();
		
		if (reporting != null) {
			List<Reporter> reporters = reporting.getReporter();
			
			if (reporters != null) {
				for (Reporter r : reporters) {
					if (r.isEnabled()) {
						List<Destination> destinations = r.getDestination();
						if (destinations != null) {
							for (Destination d : destinations) {
								d.setClazz(PerfCakeConst.MASTER_REPORTING_DESTINATION);
							}
						}
					}
				}
			}
		}
		
		return model;
	}
	
}
