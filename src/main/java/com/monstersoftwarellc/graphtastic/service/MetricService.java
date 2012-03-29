/**
 * 
 */
package com.monstersoftwarellc.graphtastic.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.model.MetricTypes;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository;
import com.monstersoftwarellc.graphtastic.repository.MetricTypeRepository;
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
	
	@Autowired
	private MetricTypeRepository metricTypeRepository;

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
	public void addMetric(String name, String value, long timestamp) {
		addMetricTypeIfNeeded(name);
		metricRepository.save(MetricUtility.buildMetric(name, value, timestamp));
	}

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.service.IMetricService#addMetric(byte[])
	 */
	@Override
	public void addMetrics(byte[] metricData) {
		String[] data = new String(metricData).split(",");
		saveDataArray(data);
	}

	/* (non-Javadoc)
	 * @see com.monstersoftwarellc.graphtastic.service.IMetricService#addMetrics(java.lang.String)
	 */
	@Override
	public void addMetrics(String csvMetrics) {
		saveDataArray(csvMetrics.split(","));
	}
	
	private void saveDataArray(String[] data){
		List<Metric> metrics = new ArrayList<Metric>();
		List<String> labels = new ArrayList<String>();
		for(int i = 0; i < data.length; i++){
			String name = data[i];
			String value = data[++i];
			// make sure we are don't deal with any decimal values.
			String timestamp = data[++i];
			if(timestamp.contains(".")){
				timestamp = timestamp.substring(0, timestamp.indexOf("."));
			}
			metrics.add(MetricUtility.buildMetric(name, value, Long.parseLong(timestamp)));
			if(!labels.contains(name)){
				labels.add(name);
			}
		}
		for(String label : labels){
			addMetricTypeIfNeeded(label);
		}
		saveList(metrics, 0);
	}
	
	private void addMetricTypeIfNeeded(String label){
		if(metricTypeRepository.getMetricTypesByLabel(label) == null){
			metricTypeRepository.save(new MetricTypes(label));
		}
	}

	/**
	 * 
	 */
	private void saveList(List<Metric> metrics, int retry) {		
		try {
			metricRepository.save(metrics);
			if(LOG.isDebugEnabled()){
				LOG.debug("Metric's Saved! : " + metrics.size());
			}
		}catch(Exception ex){
			LOG.error("Exception Saving Metric - Will attempt "+retries+" retries!", ex);
			while(retry < retries){
				saveList(metrics, retry++);
			}
		}
	}

}
