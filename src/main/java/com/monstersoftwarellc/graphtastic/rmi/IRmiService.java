package com.monstersoftwarellc.graphtastic.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author nicholas
 *
 */
public interface IRmiService extends Remote, Serializable {

	/**
	 * This is the contract for RMI integration with GraphTastic. 
	 * @param name - metric name
	 * @param value - metric value
	 * @param timestamp - metric timestamp in mills
	 * @throws RemoteException
	 */
	public abstract void insertMetric(String name, String value, long timestamp) throws RemoteException;
	
}
