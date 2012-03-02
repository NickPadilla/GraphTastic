package com.monstersoftwarellc.graphtastic.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jmx.support.MetricType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.monstersoftwarellc.graphtastic.model.GraphResponse;
import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.model.ResponseStatus;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository.Count;
import com.monstersoftwarellc.graphtastic.service.IMetricService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/")
public class GraphController {
	
	private static final Logger LOG = Logger.getLogger(GraphController.class);
	
	@Autowired
	private IMetricService metricService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String toGraph() {
		return "graph";
	}
	
	/**
	 * Will provide a {@link Page} object that contains a list of {@link Metric}'s.  This will 
	 * be able to provide all of the needed data to save a metric. 
	 * @param page
	 * @param numberPerPage
	 * @return
	 */
	@RequestMapping(value="addMetric/{name}&{value}&{timestamp}", method=RequestMethod.PUT, produces="application/json")
	public @ResponseBody ResponseStatus addMetric(@PathVariable String name, @PathVariable String value, @PathVariable long timestamp) {
		try{
			metricService.addMetric(name, value, new Date().getTime());
		}catch(Exception ex){
			LOG.error("Exception Adding Metric Through GUI! : ", ex);
			return ResponseStatus.ERROR;
		}
		return ResponseStatus.OK;
	}
	
	/**
	 * Will provide a {@link Page} object that contains a list of {@link Metric}'s.  This will 
	 * be able to provide all of the needed data to save a metric. We will use the time this
	 * method was called as the {@link Metric#setTimestamp(long)} value.
	 * @param page
	 * @param numberPerPage
	 * @return
	 */
	@RequestMapping(value="addMetric/{name}&{value}", method=RequestMethod.PUT, produces="application/json")
	public @ResponseBody ResponseStatus addMetric(@PathVariable String name, @PathVariable String value) {
		try{
			metricService.addMetric(name, value, new Date().getTime());
		}catch(Exception ex){
			LOG.error("Exception Adding Metric Through GUI! : ", ex);
			return ResponseStatus.ERROR;
		}
		return ResponseStatus.OK;
	}
	
	/**
	 * Will provide a list of all unique {@link Metric#getName()}. These
	 * are the only possible queries at this time.
	 * TODO: add additional filtering options.
	 * @param page
	 * @param numberPerPage
	 * @return
	 */
	@RequestMapping(value="listUniqueMetricNames", method=RequestMethod.GET)
	public @ResponseBody List<String> listMetricTypes() {
		LOG.debug("Total Count Of Metrics : " + metricService.getRepository().count());
		return metricService.getRepository().findAllUniqueNames(); 
	}
	
	/**
	 * Will provide a list of all acceptable {@link MetricType}'s. In the GUI we use the 
	 * actual value of the {@link MetricType}. So we will need to use the {@link MetricType#valueOf(String)}
	 * here.
	 * @param page
	 * @param numberPerPage
	 * @return
	 */
	@RequestMapping(value="findMetricsByName/{name}&{page}&{numberPerPage}", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Page<Metric> findMetricsByType(@PathVariable String name, @PathVariable Integer page, @PathVariable Integer numberPerPage) {
		Page<Metric> testMetric = metricService.getRepository().findByName(name, new PageRequest(page, numberPerPage));
		LOG.debug("Total Count Of Metrics : " + metricService.getRepository().count());
		return testMetric; 
	}
	
	/**
	 * Will provide a {@link Page} object that contains a list of {@link Metric}'s.  This will 
	 * be able to provide all of the needed data for the display. 
	 * @param page
	 * @param numberPerPage
	 * @return
	 */
	@RequestMapping(value="listMetrics/{page}&{numberPerPage}", method=RequestMethod.GET)
	public @ResponseBody Page<Metric> listMetrics(@PathVariable Integer page, @PathVariable Integer numberPerPage) {
		return metricService.getRepository().findAll(new PageRequest(page, numberPerPage)); 
	}
	
	@RequestMapping(value="countByMetricNameTimestamp/{name}&{start}&{end}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, GraphResponse> countByMetricNameTimestamp(@PathVariable String name, @PathVariable Long start, @PathVariable Long end){
		List<Count> counts = metricService.getRepository().getCountByMetricName(name, start, end);
		HashMap<String, GraphResponse> responses = new HashMap<String, GraphResponse>();
		for(Count count : counts){
			// now we only have minutes
			if(responses.containsKey(count.getValue())){
				if(responses.get(count.getValue()).getData().containsKey(count.getTimestamp())){
					// contains key so we need to add to the count					
					long countInMap = responses.get(count.getValue()).getData().get(count.getTimestamp());
					responses.get(count.getValue()).getData().put(count.getTimestamp(), countInMap+count.getCount());
				}else{
					responses.get(count.getValue()).getData().put(count.getTimestamp(), count.getCount());
				}
			}else{
				GraphResponse resp = new GraphResponse();
				resp.setLabel(String.valueOf(count.getValue()));
				resp.getData().put(count.getTimestamp(), count.getCount());
				responses.put(String.valueOf(count.getValue()), resp);
			}
		}
		return responses;
	}
}
