/**
 * 
 */
package com.monstersoftwarellc.graphtastic.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository;
import com.monstersoftwarellc.graphtastic.utility.MetricUtility;

/**
 * Service will provide actual implementation of metric logic.
 * @author nicholas
 *
 */
@Service
public class MetricService implements IMetricService {
	
	private static final Logger LOG = Logger.getLogger(MetricService.class);
	
	@Autowired
	private MetricRepository metricRepository;
	
	@Value("#{graphtastic.retries}")
	private Integer retries;

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.service.IRepositoryService#getRepository()
	 */
	@Override
	public MetricRepository getRepository() {
		return metricRepository;
	}

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.service.IMetricService#addMetric(java.lang.String, java.lang.String, long)
	 */
	@Override
	@Transactional
	public void addMetric(String name, String value, long timestamp) {
		add(MetricUtility.buildMetric(name, value, timestamp), 0);
	}
	
	private void add(Metric metric, int retry){
		try {
			metric = metricRepository.save(metric);
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric Saved! : " + metric.toString());
			}
		}catch(Exception ex){
			LOG.error("Exception Saving Metric - Will attempt "+retries+" retries!", ex);
			while(retry < retries){
				add(metric, ++retry);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.service.IMetricService#addMetric(byte[])
	 */
	@Override
	public void addMetric(byte[] metricData) {
		String[] data = new String(metricData).split(",");
		addMetric(data[0], data[1], Long.parseLong(data[2]));
	}

}
