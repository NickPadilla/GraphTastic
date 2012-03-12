/**
 * 
 */
package com.monstersoftwarellc.graphtastic.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.monstersoftwarellc.graphtastic.TestApplicationContext;
import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.utility.MetricUtility;

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
		metricService.addMetrics(bytes.toString().getBytes());
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
	public void save1000Entries() throws InterruptedException{
		metricService.getRepository().save(saveGroup(1000, "Bulk"));
		Thread.currentThread();
		Thread.sleep(1000);
		List<Metric> metrics = metricService.getRepository().findByName("Bulk");
		assertTrue(!metrics.isEmpty());
		assertTrue(metrics.size()==1000);
	}
	
	@Test
	@Transactional
	public void saveMetricsString() throws InterruptedException{
		metricService.addMetrics(buildCsvData());
		List<Metric> metrics = metricService.getRepository().findByName("CSV");
		assertTrue(!metrics.isEmpty());
		assertTrue(metrics.size()==500);
	}
	
	/**
	 * @return
	 */
	private String buildCsvData() {
		StringBuffer buffer = new StringBuffer();
		int numbAdded = 0;
		while(numbAdded < 500){
			if(numbAdded > 0){
				buffer.append(",");
			}
			buffer.append("CSV,TEST"+numbAdded+","+new Date().getTime());
			numbAdded++;
		}
		return buffer.toString();
	}

	private List<Metric> saveGroup(int numberOfItems, String... names) {
		List<Metric> metrics = new ArrayList<Metric>();
		for(int i = 0; i < numberOfItems; i++){
			String name = names.length == 1 ? names[0] : ((i % 2 == 0 ? names[0] : names[1]));
			String value = (i % 2 == 0 ? "INFO" : "DEBUG");
			metrics.add(MetricUtility.buildMetric(name, value, new Date().getTime()));
		}
		return metrics;
	}
}
