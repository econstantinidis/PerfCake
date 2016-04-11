package org.perfcake.reporting;

import java.io.Serializable;

public class MeasurementWrapper implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	public Measurement measurement;
	public String reporterClazz;
	public String destinationClazz;
	
	public MeasurementWrapper(Measurement measurement, String reporterClazz, String destinationClazz)
	{
		this.measurement = measurement;
		this.reporterClazz = reporterClazz;
		this.destinationClazz = destinationClazz;
	}
}
