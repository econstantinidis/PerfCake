package org.perfcake.scenario;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.perfcake.PerfCakeException;

public class SlaveFactory implements ScenarioFactory {

	private static final Logger log = LogManager.getLogger(SlaveFactory.class);
	
	public SlaveFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(URL scenarioURL) throws PerfCakeException {
		// expect a null scenarioURL - ignore this parameter
		
		// Request scenario data - socket already setup
		
	}

	@Override
	public Scenario getScenario() throws PerfCakeException {
		// FIXME actually get the scenario
		throw new PerfCakeException("LOAD SCENARIO FROM MASTER NOT IMPLEMENTED");
	}

}
