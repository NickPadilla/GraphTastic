/**
 * 
 */
package com.monstersoftwarellc.graphtastic.model;

import java.util.Map;
import java.util.TreeMap;

import com.monstersoftwarellc.graphtastic.utility.ComparatorUtility;

/**
 * @author nicholas
 *
 */
public class GraphResponseData {
	
	private String label;
	
	private Map<Long, Long> data = new TreeMap<Long, Long>(ComparatorUtility.byTime());

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the data
	 */
	public Map<Long, Long> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Map<Long, Long> data) {
		this.data = data;
	}
	
}
