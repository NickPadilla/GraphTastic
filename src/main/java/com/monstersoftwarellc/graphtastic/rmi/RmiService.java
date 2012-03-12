/**
 * 
 */
package com.monstersoftwarellc.graphtastic.rmi;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monstersoftwarellc.graphtastic.service.IMetricService;


/**
 * Class will provide a RMI end point.  We use this method of remote services due to is performance overall and is the preferred 
 * way of adding metrics.  There is also a RESTful interface if you prefer, however please keep in mind the overhead the RESTful
 * call entails. 
 * @author nicholas
 *
 */
@Service
public class RmiService implements IRmiService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IMetricService metricService;

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.service.IRmiService#insertMetric(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void insertMetric(String name, String value, long timestamp) {
		metricService.addMetric(name, value, timestamp);
	}

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.rmi.IRmiService#insertMetrics(java.lang.String)
	 */
	@Override
	public void insertMetrics(String csvData) throws RemoteException {
		metricService.addMetrics(csvData);
	}

}
