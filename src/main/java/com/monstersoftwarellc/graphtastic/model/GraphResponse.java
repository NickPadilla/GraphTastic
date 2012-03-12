/**
 * 
 */
package com.monstersoftwarellc.graphtastic.model;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * @author nicholas
 *
 */
public class GraphResponse {
	
	private HashMap<String, GraphResponseData> responses = new HashMap<String, GraphResponseData>();

	/**
	 * @return the responses
	 */
	public HashMap<String, GraphResponseData> getResponses() {
		return responses;
	}

	/**
	 * @param responses the responses to set
	 */
	public void setResponses(HashMap<String, GraphResponseData> responses) {
		this.responses = responses;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, true);
	}
}
