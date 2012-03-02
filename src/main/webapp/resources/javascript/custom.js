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

function getData(name, from, to) {
	var adjustedData = [];
	$.getJSONAsync("countByMetricNameTimestamp/"+name+"&"+from+"&"+to, false, function(data){		
		// example data set for flot using count and response as an example 
		// var datasets = { 
		//   "200": { 
		//       label: "200", 
		//       data: [[timestamp,count],[timestamp,count]] 
		//    }, 
		//   "404": { status.options.length 
		//       label: "404", 
		//       data: [[timestamp,count],[timestamp,count]] 
		//    } 
		// } 
		for(var key in data){
			var map = data[key].data;
			var array = new Array();
			for(var time in map){
				var temp = [Number(time),map[time]];
				array.push(temp);
			}
			adjustedData.push({ label : key, data : array });
		}
	});
	return adjustedData;
}

function setDateVariables(){
	var start = $("#start").val();
	var end = $("#end").val();
	var startHour = $("#startTime a").text().substring(0, 2);
	var startMinute = $("#startTime a").text().substring(3, 5);
	var endHour = $("#endTime a").text().substring(0, 2);
	var endMinute = $("#endTime a").text().substring(3, 5);
	var yesterday = false;
	var today = false;
	
	if(start == "Yesterday"){
		start = new Date();
		start.setDate(start.getDate()-1);
		yesterday = true;
	}else{
		start = $.datepicker.parseDate(pattern, start, null);
	}
	if(end == "Today"){
		end = new Date();
		today = true;
	}else{
		end =  $.datepicker.parseDate(pattern, end, null);
	}
	
	var sHour = parseInt(startHour);
	var sMin = parseInt(startMinute);
	var eHour = parseInt(endHour);
	var eMin = parseInt(endMinute);
	// do we have original settings? then reset current time to now
	if(sHour == origHour && eHour == origHour 
			&& sMin == origMin && eMin == origMin
			&& yesterday && today){
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

function showMetrics(){	
	setDateVariables();
	$("#graphDiv").empty();
	$("#legend").empty();
	$("#title").empty();
	currentName = $("#selectType option:selected").text();
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
				grid: {
					show: true,
					labelMargin: 5,
					axisMargin: 5,
					clickable: true,
					hoverable: true,
					autoHighlight: true,
					mouseActiveRadius: 5
				},
				xaxis: { 
					zoomRange: [1,1000000], 
					panRange: [currentStart.getTime(), currentEnd.getTime()],
					mode: "time", 
					timeformat: "%y/%m/%d\n%H:%M:%S"
				},
				yaxis: { 
					zoomRange: [-1, 100], 
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
				}
		};
	   
	}
	$("#title").append($("<span>"+currentName+"</span>"));
	plot = $.plot($("#graphDiv"), getData(currentName, currentStart.getTime(), currentEnd.getTime()), options);
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

function updateGraph() {
	if($("#update:checked").length){
		showMetrics();
	}
	// update the current settings
    setTimeout(updateGraph, updateInterval);
}

function listTypes(){
	$.getJSONAsync("listUniqueMetricNames", false, function(data){
		var div = $('#typesDiv');
		var select = $('<select>').attr('id', 'selectType');
		$.each(data, function(index, name){
			var option = $('<option>').text(name).attr('value', name);
			if(index == 0){
				currentName = name;
				option.attr('selected', 'selected');
			}
			select.append(option);
		});
		div.append(select);
		$("#selectType").change(function(){
			showMetrics();
		});
	});
}

$(document).ready(function() {
	$(":date").dateinput({ trigger: true, format: 'dd mmmm yyyy', selectors: false });
	// use the same callback for two different events. possible with bind
	$(":date").bind("onShow onHide", function()  {
		$(this).parent().toggleClass("active"); 
	});
	$("#endTime, #startTime").timeselect();
	
	setTimeDefaults();

	listTypes();
	
	$('input:submit,input:button,button').button();
	/*$('form').form();*/
	updateGraph();
    showMetrics();
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
});