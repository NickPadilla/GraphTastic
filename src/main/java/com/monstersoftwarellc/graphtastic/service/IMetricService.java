/**
 * 
 */
package com.monstersoftwarellc.graphtastic.service;

import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository;

/**
 * Service that will contain required {@link Metric} logic.  
 * @author nicholas
 *
 */
public interface IMetricService extends IRepositoryService<MetricRepository> {
	
	/**
	 * Add metric to backend storage.  We will perform graph algros on the fly, this will save upfront
	 * overhead, just save metric. 
	 * @param type
	 * @param name
	 * @param value
	 */
	public abstract void addMetric(String name, String value, long timestamp);
	
	/**
	 * This method will take in a byte[] and create a {@link Metric} out of the data. This method is 
	 * used for our UDP/TCP integrations. The data must be delimited by ','(comma) and the data must
	 * be layed out as such:
	 * <br/><br/>
	 * <b>type,name,value,timestamp</b>
	 * <br/><br/>
	 * @param metricData
	 */
	public abstract void addMetrics(byte[] metricData); 
	
	public abstract void addMetrics(String csvMetrics);

}
