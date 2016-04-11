package org.perfcake.distribution;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.perfcake.PerfCakeException;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.model.Scenario.Reporting;
import org.perfcake.reporting.Measurement;
import org.perfcake.reporting.MeasurementWrapper;
import org.perfcake.reporting.ReportingException;
import org.perfcake.util.ObjectFactory;
import org.w3c.dom.Element;

public class DistributionManager {

	private static final Logger log = LogManager.getLogger(DistributionManager.class);

	private MasterListener listener;
	private Thread listenerThread;

	private Scenario scenarioModel;
	private Map<String, Map<String, org.perfcake.reporting.destinations.Destination>> destinationMap;

	private String DEFAULT_REPORTER_PACKAGE = "org.perfcake.reporting.reporters";
	private String DEFAULT_DESTINATION_PACKAGE = "org.perfcake.reporting.destinations";

	private boolean reportDestinationsSetup = false;;

	public DistributionManager(InetAddress listenAddress, int port)
	{
		destinationMap = new ConcurrentHashMap<>();

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
		if (!reportDestinationsSetup) {
			try {
				setupReportDestinations(scenarioModel);
			} catch (PerfCakeException e) {
				log.warn("Error setting up destinations on master", e);
			}

			// Debug setup
			for (Entry<String, Map<String, org.perfcake.reporting.destinations.Destination>> e : destinationMap.entrySet()) {
				log.info("Reporter Key: " + e.getKey());
				for (Entry<String, org.perfcake.reporting.destinations.Destination> d : e.getValue().entrySet()) {
					log.info("Destination Key: " + d.getKey());
					log.info("Destination Class: " + d.getValue().getClass().getName());
				}
			}

			this.scenarioModel = scenarioModel;

			openAllReporterDestinations();

			reportDestinationsSetup = true;
		} else {
			log.warn("Cannot set the distributed scenario model more than once");
		}
	}

	private void openAllReporterDestinations() {
		destinationMap.entrySet().stream()
		.flatMap(e -> e.getValue().values().stream())
		.forEach(d -> d.open());
	}

	private void setupReportDestinations(Scenario model) throws PerfCakeException {
		try {
			final Reporting reporting = model.getReporting();

			if (reporting != null) {
				for (final Reporting.Reporter r : reporting.getReporter()) {
					if (r.isEnabled()) {
						String reportClass = r.getClazz();
						if (!reportClass.contains(".")) {
							reportClass = DEFAULT_REPORTER_PACKAGE + "." + reportClass;
						}

						for (final Reporting.Reporter.Destination d : r.getDestination()) {
							if (d.isEnabled()) {
								String destClass = d.getClazz();
								if (!destClass.contains(".")) {
									destClass = DEFAULT_DESTINATION_PACKAGE + "." + destClass;
								}

								final Properties currentDestinationProperties = getPropertiesFromList(d.getProperty());

								final org.perfcake.reporting.destinations.Destination currentDestination = (org.perfcake.reporting.destinations.Destination) ObjectFactory.summonInstance(destClass, currentDestinationProperties);

								// get exisiting
								Map<String, org.perfcake.reporting.destinations.Destination> existingDestinations = destinationMap.get(reportClass);
								if (existingDestinations == null) {
									// setup a set and map
									existingDestinations = new ConcurrentHashMap<>();
									destinationMap.put(reportClass, existingDestinations);
								}
								// add to set
								existingDestinations.put(destClass, currentDestination);
							}
						}
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
			throw new PerfCakeException("Cannot parse reporting configuration: ", e);
		}
	}

	private static Properties getPropertiesFromList(final List<Property> properties) throws PerfCakeException {
		final Properties props = new Properties();

		for (final Property p : properties) {
			final Element valueElement = p.getAny();
			final String valueString = p.getValue();

			if (valueElement != null && valueString != null) {
				throw new PerfCakeException(String.format("A property tag can either have an attribute value (%s) or the body (%s) set, not both at the same time.", valueString, valueElement.toString()));
			} else if (valueElement == null && valueString == null) {
				throw new PerfCakeException("A property tag must either have an attribute value or the body set.");
			}

			props.put(p.getName(), valueString == null ? valueElement : valueString);
		}

		return props;
	}

	public void report(MeasurementWrapper wrapper)
	{
		Measurement m = wrapper.measurement;

		org.perfcake.reporting.destinations.Destination d =
				destinationMap
				.get(wrapper.reporterClazz)
				.get(wrapper.destinationClazz);

		if (d != null) {
			try {
				d.report(m);
			} catch (ReportingException e) {
				log.info("Reporting error on master", e);
			}
		} else {
			log.warn("No destination found for recieved measurement [reporterClazz="
					+ wrapper.reporterClazz
					+ " destinationClazz="
					+ wrapper.destinationClazz + "]");
		}
	}

}
