/**
 * 
 */
package com.monstersoftwarellc.graphtastic.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monstersoftwarellc.graphtastic.service.IMetricService;

/**
 * @author nicholas
 *
 */
@Service
public class MetricAccumulator {
	
	@Autowired
	private IMetricService metricService;


	/**
	 * @param metric
	 */
	public void insertMetric(byte[] metric) {
		metricService.addMetrics(metric);
	}
}
