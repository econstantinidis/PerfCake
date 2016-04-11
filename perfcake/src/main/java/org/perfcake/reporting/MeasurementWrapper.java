package org.perfcake.reporting;

public class MeasurementWrapper
{
	
	public Measurement measurement;
	public String ReporterClazz;
	
	public MeasurementWrapper(Measurement measurement, String ReporterClazz)
	{
		this.measurement = measurement;
		this.ReporterClazz = ReporterClazz;
	}
}
