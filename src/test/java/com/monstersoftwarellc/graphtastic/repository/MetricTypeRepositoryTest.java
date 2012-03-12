/**
 * 
 */
package com.monstersoftwarellc.graphtastic.repository;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.monstersoftwarellc.graphtastic.TestApplicationContext;
import com.monstersoftwarellc.graphtastic.service.MetricService;

/**
 * @author nicholas
 *
 */
public class MetricTypeRepositoryTest extends TestApplicationContext {
	
	private static final Logger LOG = Logger.getLogger(MetricTypeRepositoryTest.class);
	
	@Autowired
	private MetricService metricService;
	@Autowired
	private MetricTypeRepository metricTypeRepository;
	
	
	@Test
	@Transactional
	public void saveMetricTypeAndRetrieve(){
		saveGroup(100, "Response","Log");
		List<String> names = metricTypeRepository.getAllMetricTypeLabels();
		if(LOG.isDebugEnabled()){
			LOG.debug("number of MetricType's found : " + names.size());
		}
		assertTrue(names.size() == 2);
	}
	
	
	/**
	 * 
	 */
	private void saveGroup(int numberOfItems, String... names) {
		for(int i = 0; i < numberOfItems; i++){
			String name = names.length == 1 ? names[0] : ((i % 2 == 0 ? names[0] : names[1]));
			String value = (i % 2 == 0 ? "INFO" : "DEBUG");
			metricService.addMetric(name, value, new Date().getTime());
		}
	}

}
