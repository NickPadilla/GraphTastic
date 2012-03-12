/**
 * 
 */
package com.monstersoftwarellc.graphtastic.model;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * @author nicholas
 *
 */
@NodeEntity
public class MetricTypes {
	
	@GraphId
	private Long id;

	@Indexed
	private String label;
	
	/**
	 * @param label
	 */
	public MetricTypes() {
	}

	/**
	 * @param label
	 */
	public MetricTypes(String label) {
		this.label = label;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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

}