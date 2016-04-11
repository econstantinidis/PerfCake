package org.perfcake.reporting;

public class MeasurementWrapper
{
	
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
