/**
 * 
 */
package com.monstersoftwarellc.graphtastic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.monstersoftwarellc.graphtastic.TestApplicationContext;
import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository;

/**
 * @author nicholas
 *
 */
public class UdpServiceTest extends TestApplicationContext {
	
	private static final Logger LOG = Logger.getLogger(UdpServiceTest.class);
	
	@Autowired
	private MetricRepository repo;

	@Test
	public void testUdpService() throws IOException{
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress ipAddress = InetAddress.getByName("localhost");
			long timestamp = new Date().getTime();
			String data = new String("Name,Metric,"+timestamp);
			DatagramPacket sendPacket = new DatagramPacket(data.getBytes(), data.getBytes().length, ipAddress, 1399);
			clientSocket.send(sendPacket);
			clientSocket.close();
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric SENT Through UDP!");
			}

			// need to wait for the sending process to happen
			Thread.currentThread();
			Thread.sleep(1000);
			
			List<Metric> metrics = repo.findByName("Name");
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric List Size : " + metrics.size());
			}
			assertNotNull(metrics);
			assertTrue(!metrics.isEmpty() && metrics.size() == 1);
			Metric metric = metrics.get(0);
			assertEquals("Name", metric.getName());
			assertEquals("Metric", metric.getValue());
			assertEquals(timestamp, metric.getTimestamp());
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric Inserted Through UDP Successfully!");
			}
			// ensure we delete all since this was done through RMI
			repo.deleteAll();
			if(LOG.isDebugEnabled()){
				LOG.debug("All Metrics Successfully Deleted After UDP Insert! : count : " + repo.count());
			}
		} catch (Exception e) {
			LOG.error("UDP Service Test Errored Out!",e);
		}
	}
}
