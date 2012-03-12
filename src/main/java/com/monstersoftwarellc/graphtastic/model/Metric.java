/**
 * 
 */
package com.monstersoftwarellc.graphtastic.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * @author Nick(Work)
 *
 */
@NodeEntity
public class Metric {
	
	public static final String END_OF_LINE_NOTIFICATION_FOR_TCP_IMPL = "\r\n";
	
	@GraphId
	private Long id;
	
	@Indexed(indexName="nameIndex",fieldName="name")
	private String name;
	
	private Object value;

	@Indexed(indexName="timestampIndex",fieldName="timestamp")
	private long timestamp;

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// performance optimization
		if(obj == this){
			return true;
		}
		if(!(obj instanceof Metric)){
			return false;
		}
		Metric metric = (Metric) obj;
		
		return new EqualsBuilder()
								.append(metric.getId(), getId())
								.append(metric.getName(), getName())
								.append(metric.getTimestamp(), getTimestamp())
								.append(metric.getValue(), getValue())
								.isEquals();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(3, 31)
								.append(getId())
								.append(getName())
								.append(getTimestamp())
								.append(getValue())
								.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, true);
	}
	
}
