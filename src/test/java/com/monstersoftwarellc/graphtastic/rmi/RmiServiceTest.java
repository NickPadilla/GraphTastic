package com.monstersoftwarellc.graphtastic.rmi;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.monstersoftwarellc.graphtastic.TestApplicationContext;
import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository;

public class RmiServiceTest extends TestApplicationContext {
	
	private static final Logger LOG = Logger.getLogger(RmiServiceTest.class);
	
	@Autowired
	private MetricRepository repo;

	@Test
	public void testRmiIntegration() throws RemoteException, MalformedURLException {
		Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1199);
		IRmiService service;
		try {
			long timestamp = new Date().getTime();
			service = (IRmiService) registry.lookup("RmiMetricService");
			service.insertMetric("LOG","INFO", timestamp);
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric Inserted Through RMI Successfully!");
			}
			List<Metric> metrics = repo.findByName("LOG");
			assertNotNull(metrics);
			assertTrue(!metrics.isEmpty() && metrics.size() == 1);
			Metric metric = metrics.get(0);
			assertEquals("LOG", metric.getName());
			assertEquals("INFO", metric.getValue());
			assertEquals(timestamp, metric.getTimestamp());
			
			// ensure we delete all since this was done through RMI
			repo.deleteAll();
			if(LOG.isDebugEnabled()){
				LOG.debug("All Metrics Successfully Deleted After RMI Insert! : count : " + repo.count());
			}
		} catch (NotBoundException e) {
			LOG.error("RMI Service Test Errored Out!",e);
		}
		
	}
}