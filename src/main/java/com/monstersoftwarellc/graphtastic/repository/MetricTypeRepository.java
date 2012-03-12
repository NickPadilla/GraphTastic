/**
 * 
 */
package com.monstersoftwarellc.graphtastic.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import com.monstersoftwarellc.graphtastic.model.MetricTypes;

/**
 * @author nicholas
 *
 */
public interface MetricTypeRepository extends GraphRepository<MetricTypes>, CypherDslRepository<MetricTypes>, NamedIndexRepository<MetricTypes> {

	@Query("start type=node:__types__(className='com.monstersoftwarellc.graphtastic.model.MetricTypes') return type.label")
	List<String> getAllMetricTypeLabels();
	
	MetricTypes getMetricTypesByLabel(String label);
}
