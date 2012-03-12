/**
 * 
 */
package com.monstersoftwarellc.graphtastic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.MapResult;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import com.monstersoftwarellc.graphtastic.model.Metric;


/**
 * @author nicholas
 *
 */
public interface MetricRepository extends GraphRepository<Metric>, CypherDslRepository<Metric>, NamedIndexRepository<Metric>  {
	
	/**
	 * Method will get all {@link Metric} by {@link Metric#getName()}. 
	 * @param type
	 * @return
	 */
	List<Metric> findByName(String name);
	
	/**
	 * Method will get all {@link Metric} by {@link Metric#getName()}. Pass in an
	 * implementation of {@link Pageable} and we will only pass back that data.
	 * @param type
	 * @return
	 */
	Page<Metric> findByName(String name, Pageable page);
	
	/**
	 * Find all {@link Metric} objects that are of the given {@link Metric#getName()} 
	 * and between the <code>long from</code> and the <code>long to</code>.  These last two 
	 * parameters need to be in milliseconds since January 1, 1970, 00:00:00 GMT. Timestamps 
	 * are exclusive.
	 * @param type
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("start metric=node:nameIndex(name={0}) where metric.timestamp >= {1} and metric.timestamp <= {2} return metric")	
	List<Metric> findByNameAndTimestampGreaterThanAndTimestampLessThan(String name, long from, long to);
	
	/**
	 * Find all {@link Metric} objects that are of the given {@link Metric#getValue()} and between
	 * the <code>long from</code> and the <code>long to</code>.  These last two parameters need
	 * to be in milliseconds since January 1, 1970, 00:00:00 GMT. Timestamps are exclusive.
	 * @param type
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("start metric=node:__types__(className='com.monstersoftwarellc.graphtastic.model.Metric') where metric.value = {0} and metric.timestamp >= {1} and metric.timestamp <= {2} return metric")		
	List<Metric> findByValueAndTimestampGreaterThanAndTimestampLessThan(Object value, long from, long to);
	
	@Query("start metric=node:nameIndex(name={0}) where metric.timestamp >= {1} and metric.timestamp <= {2} return distinct metric.timestamp, count(metric.timestamp), metric.value, metric.name")
	List<Count> getCountByMetricNameBetween(String name, long start, long end);
	
	@MapResult
	public interface Count {
	    @ResultColumn("metric.timestamp")
	   	long getTimestamp();

	    @ResultColumn("count(metric.timestamp)")
	    long getCount();	    

	    @ResultColumn("metric.value")
	    Object getValue();	        

	    @ResultColumn("metric.name")
	    String getName();	
	}
}
