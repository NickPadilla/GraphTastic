package com.monstersoftwarellc.graphtastic.controller;

import java.util.Date;
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

import scala.actors.threadpool.Arrays;

import com.monstersoftwarellc.graphtastic.model.GraphResponse;
import com.monstersoftwarellc.graphtastic.model.Metric;
import com.monstersoftwarellc.graphtastic.model.ResponseStatus;
import com.monstersoftwarellc.graphtastic.model.TimeAggregation;
import com.monstersoftwarellc.graphtastic.repository.MetricRepository.Count;
import com.monstersoftwarellc.graphtastic.repository.MetricTypeRepository;
import com.monstersoftwarellc.graphtastic.service.IMetricService;
import com.monstersoftwarellc.graphtastic.utility.MetricUtility;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/")
public class GraphController {
	
	private static final Logger LOG = Logger.getLogger(GraphController.class);
	
	@Autowired
	private IMetricService metricService;
	
	@Autowired
	private MetricTypeRepository metricTypeRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public String toGraph() {
		return "graph";
	}
	

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
	

	@RequestMapping(value="addMetric/{metrics}", method=RequestMethod.PUT, produces="application/json")
	public @ResponseBody ResponseStatus addMetric(@PathVariable String metrics) {
		try{
			metricService.addMetrics(metrics);
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
		long start = new Date().getTime();
		List<String> names = metricTypeRepository.getAllMetricTypeLabels();
		long end = new Date().getTime();
		LOG.info("listMetricTypes() : " + names.size() + "  took : " + (end - start) + "ms");
		return names; 
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
	
	/**
	 * Count of metric per timestamp and {@link Metric#getName()}.
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 */
	@RequestMapping(value="countByMetricNameTimestamp/{name}&{timeAgg}&{start}&{end}", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody GraphResponse countByMetricNameTimestamp(@PathVariable String name, @PathVariable String timeAgg, @PathVariable Long start, @PathVariable Long end){	
		long startT = new Date().getTime();
		List<Count> counts = metricService.getRepository().getCountByMetricNameBetween(name, start, end);
		long endT = new Date().getTime();
		LOG.info("we have loaded metrics count from repo : " + counts.size() + "  took : " + (endT - startT) + "ms");
		return MetricUtility.buildGraphResponse(counts, TimeAggregation.valueOf(timeAgg));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="timeAggregation", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<TimeAggregation> getTimeAggregation(){
		return Arrays.asList(TimeAggregation.values());
	}
}
