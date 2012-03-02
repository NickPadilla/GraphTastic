This project was created for Greg Jordan @ Methodist Hospitals.  We wanted to be able to graph and chart based on metrics piped out of logstash, www.logstash.net, so this project 
was born.  Right now this is a very basic graphing/charting application and can be integrated with using UDP, TCP, RMI, or REST.  Send Metrics via one of the integration points and
viola! you have some metrics to view!  Currently we require a Metric name, value, and timestamp in mills.  The only charting right now is x = timestamp and y = count of hits.  There
is a pie chart that gives percentages on the same data.  I do plan on adding additional features later and it would be helpful to have some more use cases so I can incorporate them
into the existing logic.  

It is preferred that you use UDP as it is quickest, however there are limitations to UDP.  If you need confirmation and retries of erred data you need to use REST or the RMI 
integrations.  

For RMI : You can find the needed interface in the build directory in the graphtastic-rmiclient.jar.  It provides the general contract for RMI integration.

For REST : You just need to send it to "<applicationContext>/addMetric/{name}&{value}&{timestamp}" in the default case it would go here
	graphtastic/addMetric/Name&Value&1330663932332
	
For UDP : you will just point to the IP or HOST and PORT that you are running GraphTastic and send - no reply.
For TCP : you just point to the TCP Port and IP or HOST and shoot - there are no responses so don't wait for a reply.

FUTURE ADDITIONS:

Sum of value 
Average of value
Turning series on and off


Know Problems:

I have tested with IE 9 and it seemed to work okay, however, I didn't test with any earlier versions.  Since this project is using HTML5 and the <canvas> element there may be some
problems with any browser that doesn't have default support for HTML5.  I am using excanvas.js to help relieve these issues however cannot be sure if everything will work the way
it does in the latest builds of Chrome or Fire Fox - these browsers are known to work correctly. 


NOTE: if you just want the WAR you can find the current build in the 'build' directory.  