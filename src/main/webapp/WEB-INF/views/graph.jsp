<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="<c:url value="/resources/javascript/flot/excanvas.min.js"/> "></script><![endif]-->
<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/ui-darkness/jquery-ui-1.8.18.custom.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/custom.css" />" />

<script type="text/javascript" src="<c:url value="/resources/javascript/jquery/core/jquery-1.7.1-core.js" /> "></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/jquery/core/jquery-ui-1.8.18.custom.min.js" /> "></script>

<script type="text/javascript" src="<c:url value="/resources/javascript/jquery/timepicker/jquery.timeselect.js" /> "></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/jquery/main.js" /> "></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/flot/jquery.flot.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/flot/jquery.flot.navigate.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/flot/jquery.flot.pie.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/jquery/tools/jquery.tools.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/jquery/tools/jquery.tools.dateinput.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/json-min.js" /> "></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/custom.js" /> "></script>
<title>GraphTastic</title>
</head>
<body>
	<div class="xouter3">
		<form>
			<div id="operations" class="xleftcol3">
				<!-- <button id="sendNewMetric" onclick="send()">New</button> -->
				<button id="refresh">Refresh Page</button>
				<input type="button" id="updateGraph" onclick="getData()" value="Update Graph">
				<!-- <input type="button" id="replot" onclick="replot()" value="Replot Graph"> -->
				<br />
				<span style="padding: 5px;">Auto Update<input id="update" type="checkbox" title="Auto Update" /></span>
				<br />
				<span style="padding: 5px;">Charts/Graphs</span>
				<input type="radio" id="count" name="operation" value="count" checked="checked" onclick="replot()">
				<label for="count">Count</label>
				<input type="radio" id="percentage" name="operation" value="pie" onclick="replot()"> 
				<label for="percentage">Pie</label>
			</div>
			<div class="xrightcol3" style="padding: 5px;">
				Metric Types  <span id="typesDiv" ></span>
				<br/><br/>
				Time Agg  <span id="timeAggDiv" ></span>		
			</div>
		</form>
		<div class="xmiddlecol3">
			<div id="graphSetup" class="xinner3">
				<label> Start Time <span id="startTime"> <select id="hours">
							<option selected="selected" value="00">00</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
					</select> <select id="minutes">
							<option selected="selected" value="00">00</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
							<option value="32">32</option>
							<option value="33">33</option>
							<option value="34">34</option>
							<option value="35">35</option>
							<option value="36">36</option>
							<option value="37">37</option>
							<option value="38">38</option>
							<option value="39">39</option>
							<option value="40">40</option>
							<option value="41">41</option>
							<option value="42">42</option>
							<option value="43">43</option>
							<option value="44">44</option>
							<option value="45">45</option>
							<option value="46">46</option>
							<option value="47">47</option>
							<option value="48">48</option>
							<option value="49">49</option>
							<option value="50">50</option>
							<option value="51">51</option>
							<option value="52">52</option>
							<option value="53">53</option>
							<option value="54">54</option>
							<option value="55">55</option>
							<option value="56">56</option>
							<option value="57">57</option>
							<option value="58">58</option>
							<option value="59">59</option>
					</select>
				</span>
				</label> <label> End Time <span id="endTime"> <select
						id="hours">
							<option selected="selected" value="00">00</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
					</select> <select id="minutes">
							<option selected="selected" value="00">00</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
							<option value="32">32</option>
							<option value="33">33</option>
							<option value="34">34</option>
							<option value="35">35</option>
							<option value="36">36</option>
							<option value="37">37</option>
							<option value="38">38</option>
							<option value="39">39</option>
							<option value="40">40</option>
							<option value="41">41</option>
							<option value="42">42</option>
							<option value="43">43</option>
							<option value="44">44</option>
							<option value="45">45</option>
							<option value="46">46</option>
							<option value="47">47</option>
							<option value="48">48</option>
							<option value="49">49</option>
							<option value="50">50</option>
							<option value="51">51</option>
							<option value="52">52</option>
							<option value="53">53</option>
							<option value="54">54</option>
							<option value="55">55</option>
							<option value="56">56</option>
							<option value="57">57</option>
							<option value="58">58</option>
							<option value="59">59</option>
					</select>
				</span>
				</label> <br /> <label> Start Date<br /> <input id="start"
					type="date" name="start" value="12 Hours Ago" maxlength="10"/>
				</label> <label> End Date<br /> <input id="end" type="date"
					name="end" value="Now" maxlength="10"/>
				</label>		
			</div>
		</div>
	</div>
	<div id="graph" align="center" style="width: 100%; height: 90%">
		<div id="title"></div>
		<div id="legend" ></div>
		<div id="loadingIndicator" >
			<div class="circle"></div>
			<div class="circle1"></div>
		</div>
		<div id="graphDiv" style="width: 85%; height: 80%"></div>
	</div>
</body>
</html>