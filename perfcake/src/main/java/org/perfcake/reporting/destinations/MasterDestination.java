package org.perfcake.reporting.destinations;

import org.perfcake.distribution.SlaveSocket;
import org.perfcake.reporting.Measurement;
import org.perfcake.reporting.MeasurementWrapper;
import org.perfcake.reporting.ReportingException;

public class MasterDestination implements Destination {

	private SlaveSocket s;
	public String reporterClazz;
	public String destinationClazz;

	@Override
	public void open() {
		this.s = SlaveSocket.getInstance();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void report(Measurement measurement) throws ReportingException {
		MeasurementWrapper wrapper = new MeasurementWrapper(measurement, reporterClazz, destinationClazz);//Wrap the measurement

		s.sendMeasurement(wrapper);
	}
}
