/**
 * 
 */
package com.monstersoftwarellc.graphtastic.utility;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.monstersoftwarellc.graphtastic.model.GraphResponse;
import com.monstersoftwarellc.graphtastic.model.GraphResponseData;
import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.model.TimeAggregation;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository.Count;

/**
 * @author nicholas
 *
 */
public class MetricUtility {
	
	private static final Logger LOG = Logger.getLogger(MetricUtility.class);
	
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
	
	public static GraphResponse buildGraphResponse(List<Count> counts, TimeAggregation timeAgg){
		GraphResponse response = new GraphResponse();
		GraphResponseData data = null;
		long countInMap = 0;
		Calendar cal = Calendar.getInstance();
		Date start = new Date();
		for(Count count : counts){
			cal.setTimeInMillis(count.getTimestamp());
			cal.set(Calendar.MILLISECOND, 0);
			
			switch(timeAgg){
				case ONE:
					cal.set(Calendar.SECOND, 0);
					break;
				case TEN:
					cal.set(Calendar.SECOND, 0);
					int minute = cal.get(Calendar.MINUTE);
					if(minute < 10){
						cal.set(Calendar.MINUTE, 0);
					}else if(minute >= 10 && minute < 20){
						cal.set(Calendar.MINUTE, 10);
					}else if(minute >= 20 && minute < 30){
						cal.set(Calendar.MINUTE, 20);
					}else if(minute >= 30 && minute < 40){
						cal.set(Calendar.MINUTE, 30);
					}else if(minute >= 40 && minute < 50){
						cal.set(Calendar.MINUTE, 40);
					}else if(minute >= 50 && minute <= 59){
						cal.set(Calendar.MINUTE, 50);
					}
					break;
				case THIRTY:
					cal.set(Calendar.SECOND, 0);
					long min = cal.get(Calendar.MINUTE);
					if(min < 30){
						cal.set(Calendar.MINUTE, 0);
					}else{
						cal.set(Calendar.MINUTE, 30);
					}
					break;
				case HOUR:
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);					
					break;
				case DAY:// for weekly or monthly data view TODO: do we need to add a WEEK option?
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					break;
				case MONTH:// for yearly views
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					break;
				default:// default is hour
					cal.set(Calendar.MINUTE, 0);
					break;
			}							
			
			// now we only have minutes
			if(response.getResponses().containsKey(count.getValue())){
				if(response.getResponses().get(count.getValue()).getData().containsKey(cal.getTimeInMillis())){
					// contains key so we need to add to the count					
					countInMap = response.getResponses().get(count.getValue()).getData().get(cal.getTimeInMillis());
					response.getResponses().get(count.getValue()).getData().put(cal.getTimeInMillis(), countInMap+count.getCount());
				}else{
					response.getResponses().get(count.getValue()).getData().put(cal.getTimeInMillis(), count.getCount());
				}
			}else{
				data = new GraphResponseData();
				data.setLabel(String.valueOf(count.getValue()));
				data.getData().put(cal.getTimeInMillis(), count.getCount());
				response.getResponses().put(String.valueOf(count.getValue()), data);
			}
		}		
		Date end = new Date();
		LOG.debug("buildGraphResponse() took : " + (end.getTime() - start.getTime()) + "ms");
		return response;
	}

}
