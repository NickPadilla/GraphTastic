/**
 * 
 */
package com.monstersoftwarellc.graphtastic.utility;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.monstersoftwarellc.graphtastic.model.Metric;

/**
 * Tests {@link MetricUtility} methods.
 * @author nicholas
 *
 */
public class MetricUtilityTest {

	@Test
	public void testIsNumber(){
		assertEquals(true, MetricUtility.isNumber("1243574"));
		assertEquals(true, MetricUtility.isNumber("1243574.125485"));
		assertEquals(false, MetricUtility.isNumber("1243574.1254.85"));
		assertEquals(false, MetricUtility.isNumber("1243574/125485"));
		assertEquals(false, MetricUtility.isNumber("1243574 125485"));
		assertEquals(false, MetricUtility.isNumber("hello"));		
	}
	
	@Test
	public void testBuildMetric(){
		long timestamp = new Date().getTime();
		Metric metric = MetricUtility.buildMetric("LOG", "INFO", timestamp);
		assertEquals("LOG", metric.getName());
		assertEquals(timestamp, metric.getTimestamp());
		assertEquals("INFO", metric.getValue());
	}
}
