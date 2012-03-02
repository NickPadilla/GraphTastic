/**
 * 
 */
package com.monstersoftwarellc.graphtastic.service;

import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * All Services created that will be backed by a Domain Object need to use this interface.
 * Just makes it easier to make sure we can always get at the needed {@link GraphRepository}
 * from the {@link IRepositoryService} when the service is needed over the plain repo. 
 * @author nicholas
 *
 */
public interface IRepositoryService<T extends GraphRepository<?>> {
	
	/**
	 * Will return the fully initialized {@link GraphRepository} class for this {@link IRepositoryService}. 
	 * @return
	 */
	public abstract T getRepository();
}
