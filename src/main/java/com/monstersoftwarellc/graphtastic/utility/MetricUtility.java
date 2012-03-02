/**
 * 
 */
package com.monstersoftwarellc.graphtastic.utility;

import java.math.BigDecimal;

import com.monstersoftwarellc.graphtastic.model.Metric;

/**
 * @author nicholas
 *
 */
public class MetricUtility {
	
	/**
	 * Method will test to ensure the given string is a {@link Number}. Method uses
	 * this regex to test this condition:
	 * <br/><br/>
	 * <pre>[\\d.]+</pre>
	 * <br/><br/>
	 * Basically this will ensure we have either a digit or single '.'.
	 * 
	 * @param possibleNumber
	 * @return true of string is a {@link Number}
	 */
	public static boolean isNumber(String possibleNumber){
		if((possibleNumber.contains(".") && possibleNumber.matches("[\\d]+\\.[\\d]+"))
				|| possibleNumber.matches("[\\d]+")){
			return true;
		}
		return false;
	}
	
	/**
	 * Build a {@link Metric} object from the given data.  If we end up having more than one
	 * builder we can move this to a builder factory pattern.  
	 * @param type
	 * @param name
	 * @param value
	 * @param timestamp
	 * @return
	 */
	public static Metric buildMetric(String name, String value, long timestamp){
		Metric metric = new Metric();
		metric.setName(name);
		if(MetricUtility.isNumber(value)){
			// use big decimal to help solve floating point value problems.
			metric.setValue(new BigDecimal(value));
		}else{
			// just a string
			metric.setValue(value);
		}
		metric.setTimestamp(timestamp);
		return metric;
	}

}
