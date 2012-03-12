/**
 * 
 */
package com.monstersoftwarellc.graphtastic.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.monstersoftwarellc.graphtastic.TestApplicationContext;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository;
import com.monstersoftwarellc.graphtastic.utility.MetricUtility;

/**
 * @author nicholas
 *
 */
public class GraphControllerTest extends TestApplicationContext {
	
	private static final Logger LOG = Logger.getLogger(GraphControllerTest.class);

	@Autowired
	private MetricRepository metricRepository;
	
	@Autowired
	private GraphController controller;

	@Test
	@Transactional
	public void testGraphController() throws InterruptedException, IOException{
		// all service methods in the controller are tested
		// to test or not to test the controllers - that is the question!
		long start = new Date().getTime();
		saveGroup(100, "Controller");
		Thread.currentThread();
		Thread.sleep(1000);
		long end = new Date().getTime();
		controller.countByMetricNameTimestamp("Controller", "ONE", start, end);
	}
	
	/**
	 * 
	 */
	private void saveGroup(int numberOfItems, String... names) {
		Date start = new Date();
		for(int i = 0; i < numberOfItems; i++){
			String name = names.length == 1 ? names[0] : ((i % 2 == 0 ? names[0] : names[1]));
			String value = (i % 2 == 0 ? "INFO" : "DEBUG");
			metricRepository.save(MetricUtility.buildMetric(name, value, new Date().getTime()));
		}
		Date end = new Date();
		if(LOG.isDebugEnabled()){
			LOG.debug("Number saved : " + metricRepository.count() + "  In  " + (end.getTime() - start.getTime()) + "ms");
		}
	}
}
