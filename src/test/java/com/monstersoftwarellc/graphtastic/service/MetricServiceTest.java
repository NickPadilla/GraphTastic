/**
 * 
 */
package com.monstersoftwarellc.graphtastic.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.monstersoftwarellc.graphtastic.TestApplicationContext;
import com.monstersoftwarellc.graphtastic.model.Metric;

/**
 * Tests the functionality provided in {@link IMetricService}.
 * @author nicholas
 *
 */
public class MetricServiceTest extends TestApplicationContext {
	
	@Autowired
	private IMetricService metricService;
	
	@Test
	@Transactional
	public void testAddMetric(){
		long timestamp = new Date().getTime();
		metricService.addMetric("LOG", "INFO", timestamp);
		List<Metric> metrics = metricService.getRepository().findByName("LOG");
		assertTrue(!metrics.isEmpty());
		Metric metric = metrics.get(0);
		assertNotNull(metric.getId());
		assertEquals("LOG", metric.getName());
		assertEquals(timestamp, metric.getTimestamp());
		assertEquals("INFO", metric.getValue());
	}
	
	@Test
	@Transactional
	public void testAddMetricBytes(){
		long timestamp = new Date().getTime();
		StringBuffer bytes = new StringBuffer("LOG,INFO,"+timestamp);
		metricService.addMetric(bytes.toString().getBytes());
		List<Metric> metrics = metricService.getRepository().findByName("LOG");
		assertTrue(!metrics.isEmpty());
		Metric metric = metrics.get(0);
		assertNotNull(metric.getId());
		assertEquals("LOG", metric.getName());
		assertEquals(timestamp, metric.getTimestamp());
		assertEquals("INFO", metric.getValue());
	}
	
}
