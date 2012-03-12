// TODO: add ability to turn on/off the different series : http://people.iola.dk/olau/flot/examples/turning-series.html : http://code.google.com/p/flot/

var pattern = "dd MM yy";
var origHour, origMin;
var currentStart = new Date();
var currentEnd = new Date();
var plot;
var updateInterval = 3000;
var previousPoint = null;

function send(){
	// here we can open a dialog that takes in the metric data and then sends it out 
	$.putData("addMetric/AWESOME&String", function(stat){alert("data sent "+stat);});
}

function setDateVariables(){
	var start = $("#start").val();
	var end = $("#end").val();
	var startHour = $("#startTime a").text().substring(0, 2);
	var startMinute = $("#startTime a").text().substring(3, 5);
	var endHour = $("#endTime a").text().substring(0, 2);
	var endMinute = $("#endTime a").text().substring(3, 5);
	var twelveHoursAgo = false;
	var today = false;

	if(end == "Now"){
		end = new Date();
		today = true;
	}else{
		end =  $.datepicker.parseDate(pattern, end, null);
	}
	if(start == "12 Hours Ago"){
		start = new Date(end.getTime() - (((1000*60)*60)*12));
		twelveHoursAgo = true;
	}else{
		start = $.datepicker.parseDate(pattern, start, null);
	}
	
	var sHour = parseInt(startHour);
	var sMin = parseInt(startMinute);
	var eHour = parseInt(endHour);
	var eMin = parseInt(endMinute);
	// do we have original settings? then reset current time to now
	if(sHour == origHour && eHour == origHour 
			&& sMin == origMin && eMin == origMin
			&& twelveHoursAgo && today){
		setTimeDefaults();
		sHour = origHour;
		sMin = origMin;
		eHour = origHour;
		eMin = origMin;
	}
	// set hours minutes selections 
	start.setHours(sHour, sMin, 0, 0);
	end.setHours(eHour, eMin, 0, 0);
	
	currentStart = start;
	currentEnd = end;
}

function setTimeDefaults(){
	var now = new Date();
	origHour = now.getHours();
	origMin = now.getMinutes();
	if(origMin < 10){
		var min = "0" + origMin;
		origMin = parseInt(min);
	}
	$("#endTime, #startTime").trigger('change-time', {hours: origHour, minutes: origMin});
}

function getData() {
	var adjustedData = [];
	setDateVariables();
	$("#graphDiv").empty();
	$("#legend").empty();
	$("#title").empty();
	$('#loadingIndicator').show();
	$.getJSONAsync("countByMetricNameTimestamp/"+$("#selectType option:selected").text()+"&"+$("#selectTimeAgg option:selected").text()+"&"+currentStart.getTime()+"&"+currentEnd.getTime(), true, function(data){		
		// example data set for flot using count and response as an example 
		// var datasets = { 
		//   "200": { 
		//       label: "200", 
		//       data: [[timestamp,count],[timestamp,count]] 
		//    }, 
		//   "404": {
		//       label: "404", 
		//       data: [[timestamp,count],[timestamp,count]] 
		//    } 
		// } 
		for(var key in data.responses){
			var map = data.responses[key].data;
			var array = new Array();
			for(var time in map){
				var temp = [Number(time),map[time]];
				array.push(temp);
			}
			adjustedData.push({ label : key, data : array });
		}
		$('#loadingIndicator').hide();
		showMetrics(adjustedData);
	});
}

function getOptions(){
	var options;
	var currentType = $(":radio:checked").val();
	if(currentType == 'pie'){
		options = {
				grid: {
					show: true,
					labelMargin: 5,
					axisMargin: 5,
					clickable: true,
					hoverable: true,
					autoHighlight: true,
					mouseActiveRadius: 5
				},				
				legend: {
					show: false
				},
				series: {
					pie: { 
						show: true,
						radius: 1,
						label: {
							show: true,
							radius: 1,
							formatter: function(label, series){
								return '<span style="font-size:8pt;text-align:center;padding:2px;color:white;">'+label+'<br/>'+Math.round(series.percent)+'%</span>';
							},
							background: { 
								opacity: 0.5,
								color: '#000'
							}
						}
					}
				}
		};		
		$("#graphDiv").bind("plothover", pieHover);
	//	$("#graphDiv").bind("plotclick", pieClick);
	}else{// default to graph by timeline
		options = {
				grid: {/*
					show: true,*/
					labelMargin: 5,
					axisMargin: 5,
					clickable: true,
					hoverable: true,
					//autoHighlight: true,
					//mouseActiveRadius: 5
				},
				xaxis: { 
					zoomRange: [60000,60000*60*24*30*12],// from minute to year as its outer most range
					panRange: [currentStart.getTime(), currentEnd.getTime()],
					mode: "time", 
					timeformat: "%y/%m/%d\n%H:%M:%S"
				},
				yaxis: { 
					zoomRange: [-1, 100000], 
					panRange: [-1, null] 
				},
				zoom: {
					interactive: true
				},
				pan: {
					interactive: true
				},
				legend: {
					show: true,
					container: "#legend",
					noColumns: 10
				},
				series: {
					points: { show: false },
					lines: { show: true }
				}
		};
	   
	}
	return options;
}

function showMetrics(data){
	$("#title").append($("<span>"+$("#selectType option:selected").text()+"</span>"));
	plot = $.plot($("#graphDiv"), data, getOptions());
	var currentType = $(":radio:checked").val();
	// after creation logic
	if(currentType == "pie"){
		
	}else{
		 // add zoom out button 
	    $('<div class="button" style="right:45px;top:25px">zoom out</div>').appendTo($("#graphDiv")).click(function (e) {
	        e.preventDefault();
	        plot.zoomOut();
	    });
	    addArrow('left', 75, 60, { left: -120 });
	    addArrow('right', 45, 60, { left: 120 });
	    addArrow('up', 60, 45, { top: -130 });
	    addArrow('down', 60, 75, { top: 110 });
	}
	return false;
}

function pieHover(event, pos, obj) {
	if (!obj)
		return;
	percent = parseFloat(obj.series.percent).toFixed(2);
	$("#hover").html('<span style="font-weight: bold; color: '+obj.series.color+'">'+obj.series.label+' ('+percent+'%)</span>');
}

function pieClick(event, pos, obj) {
	if (!obj)
		return;
	percent = parseFloat(obj.series.percent).toFixed(2);
	alert(''+obj.series.label+': '+percent+'%');
}

function showTooltip(x, y, contents) {
    $('<div id="tooltip">' + contents + '</div>').css( {
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 5,
        border: '1px solid #fdd',
        padding: '2px',
        'background-color': '#fee',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
}

function addArrow(dir, right, top, offset) {
 $('<img class="button" src="resources/images/arrow-' + dir + '.gif" style="right:' + right + 'px;top:' + top + 'px">').appendTo($("#graphDiv")).click(function (e) {
     e.preventDefault();
     plot.pan(offset);
 });
}

function replot() {
	plot.setData(plot.getData());
	plot.setOptions(getOptions());
	plot.draw();
}

function updateGraph() {
	if($("#update:checked").length){
		getData();
	}
	// update the current settings
    setTimeout(updateGraph, updateInterval);
}

function listTypes(){
	var div = $('#typesDiv');
	var select = $('<select>').attr('id', 'selectType');
	div.append(select);
	$.getJSONAsync("listUniqueMetricNames", true, function(data){
		$.each(data, function(index, name){
			var option = $('<option>').text(name).attr('value', name);
			if(index == 0){
				option.attr('selected', 'selected');
			}
			select.append(option);
		});
	});
}

function listTimeAgg(){	
	var div = $('#timeAggDiv');
	var select = $('<select>').attr('id', 'selectTimeAgg');
	div.append(select);
	$.getJSONAsync("timeAggregation", true, function(data){
		$.each(data, function(index, name){
			var option = $('<option>').text(name).attr('value', name);
			if(name == 'HOUR'){
				option.attr('selected', 'selected');
			}
			select.append(option);
		});
	});
}

$(document).ready(function() {
	// get options from server
    listTimeAgg();
	listTypes();
	
	$(":date").dateinput({ trigger: true, format: 'dd mmmm yyyy', selectors: false });
	// use the same callback for two different events. possible with bind
	$(":date").bind("onShow onHide", function()  {
		$(this).parent().toggleClass("active"); 
	});
	$("#endTime, #startTime").timeselect();
	
	setTimeDefaults();
	
	/*$('form').form();*/
	updateGraph();
    $("#graphDiv").bind("plothover", function (event, pos, item) {
        if (item) {
            if (previousPoint != item.dataIndex) {
                previousPoint = item.dataIndex;
                
                $("#tooltip").remove();
                var x = item.datapoint[0],
                    y = item.datapoint[1];
                showTooltip(item.pageX, item.pageY, "Metric: " + item.series.label + "<br/>X Value : " + new Date(x).toString() + "<br/>Y Value : " +  + y);
            }
        } else {
            $("#tooltip").remove();
            previousPoint = null;            
        }
    });
	
	$('#loadingIndicator').hide();
	$('input:submit,input:button,button').button();
});