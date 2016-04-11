package org.perfcake.reporting.destinations;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.perfcake.distribution.SlaveSocket;
import org.perfcake.reporting.Measurement;
import org.perfcake.reporting.MeasurementWrapper;
import org.perfcake.reporting.ReportingException;

public class MasterDestination implements Destination {

	ObjectOutputStream objectStream;
	public String ReporterClazz;
	
	@Override
	public void open() {
		SlaveSocket output = SlaveSocket.getInstance();
		try {
			objectStream = new ObjectOutputStream(output.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void report(Measurement measurement) throws ReportingException {
		
		MeasurementWrapper wrapper = new MeasurementWrapper(measurement, ReporterClazz);//Wrap the measurement
		
		try {
			objectStream.writeObject(wrapper);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
