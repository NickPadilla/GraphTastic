/**
 * 
 */
package com.monstersoftwarellc.graphtastic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.DataOutputStream;
import java.net.Socket;
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
public class TcpServiceTest extends TestApplicationContext {
	
	private static final Logger LOG = Logger.getLogger(UdpServiceTest.class);
	
	@Autowired
	private MetricRepository repo;

	@Test
	public void testTcpService(){
		try {
			long timestamp = new Date().getTime();
			String data = new String("TCP,value,"+timestamp+Metric.END_OF_LINE_NOTIFICATION_FOR_TCP_IMPL);
			Socket clientSocket = new Socket("localhost", 1299);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			// just write out to the server - there is no reply channel so nothing to respond
			outToServer.writeBytes(data);
			clientSocket.close();
			
			// need to wait for the sending process to happen
			Thread.currentThread();
			Thread.sleep(1000);
			
			List<Metric> metrics = repo.findByName("TCP");
			assertNotNull(metrics);
			assertTrue(!metrics.isEmpty() && metrics.size() == 1);
			Metric metric = metrics.get(0);
			assertEquals("TCP", metric.getName());
			assertEquals("value", metric.getValue());
			assertEquals(timestamp, metric.getTimestamp());
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric Inserted Through TCP Successfully!");
			}
			// ensure we delete all since this was done through RMI
			repo.deleteAll();
			if(LOG.isDebugEnabled()){
				LOG.debug("All Metrics Successfully Deleted After TCP Insert! : count : " + repo.count());
			}
		} catch (Exception e) {
			LOG.error("TCP Service Test Errored Out!",e);
		}
	}
}
