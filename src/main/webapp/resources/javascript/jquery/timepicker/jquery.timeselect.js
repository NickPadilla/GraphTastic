/*
*	jQuery timeselect plugin by Jesse Ikonen @ http://yoka.github.com/jquery-timeselect/
*
*	Version: 1.05
*/
(function($){
	$.fn.timeselect = function( options ) {

		var helpers = {
			map_option_values: function(dom){
				return dom.children("option").map(function(i,o){if ($(o).val()) return $(o).val()});
			},
			button_text: function(hours, minutes, settings){
				if (isNaN(parseInt(hours, 10))) hours = "00";
				if (isNaN(parseInt(minutes, 10))) minutes = "00";
				var minutesString = minutes;
				if(minutes < 10){
					minutesString = "0"+minutes;
				}
				return settings.format.replace("{hours}", hours).replace("{minutes}", minutesString);
			},
			update_base_time: function(hours, minutes, base_template, save, settings){
				base_template.find("span").text(helpers.button_text(hours, minutes, settings));
				if (save) {settings.h_dom.val(hours); settings.m_dom.val(minutes);}
			},
			update_base_time_from_templates: function(hours_template, minutes_template, base_template, save, settings){
				var hours 	= hours_template.children("li.ui-selected").text(),
					minutes = minutes_template.children("li.ui-selected").text();
				
				helpers.update_base_time(hours, minutes, base_template, save, settings)	
			},
			current_value: function(point, settings){
				return ((point == "hours") ? settings.h_dom.val() : settings.m_dom.val())
			},
			reset_current_values: function(hours_template, minutes_template, settings){
				hours_template.children("li:contains('"+helpers.current_value('hours', settings)+"')").addClass("ui-selected").siblings().removeClass("ui-selected");;
				minutes_template.children("li:contains('"+helpers.current_value('minutes', settings)+"')").addClass("ui-selected").siblings().removeClass("ui-selected");;
			},
			set_as_current: function(element, hours_template, minutes_template, base_template, settings){
				element.addClass("ui-state-hover ui-selected").siblings().removeClass("ui-state-hover ui-selected");
				helpers.update_base_time_from_templates(hours_template, minutes_template, base_template, false, settings);
			}
		}

		var templates = {
			base: function(button_text){
				return $("<a href='#' title='click to select time'>"+button_text+"</a>").button()
			},
			list: function(values){
				var elClass = ((values.length==24) ? "hours" : "minutes");
				return $("<ol id='timeselect' class='"+elClass+" ui-widget-content ui-corner-all'>" +
						values.map(function(i,val){
							return "<li id='' class='"+elClass+"-child ui-state-default ui-widget ui-corner-all ui-button'><a href='#'>"+val+"</a></li>"
						}).toArray().join("") +
						"</ol>");
			}
		}

		return this.each(function() {  
			var settings = {};
			settings.button_element = $(this)
			settings.h_dom 			= settings.button_element.find("select:first")
			settings.m_dom			= settings.button_element.find("select:last")
			settings.defaults		= {'hours': settings.h_dom.val(), 'minutes': settings.m_dom.val()}
			settings.format			= "{hours}:{minutes}"
			settings.after			= undefined;
			      
			// merge opts
			if ( options ) { 
		  		$.extend( settings, options );
			}
			
			var	hours_template = templates.list(helpers.map_option_values(settings.h_dom.hide())),
				minutes_template = templates.list(helpers.map_option_values(settings.m_dom.hide())),
				base_template = templates.base(helpers.button_text(settings.defaults.hours, settings.defaults.minutes, settings));
			
			// binding for after - option
			$(this).bind("time-changed", function(){
				if (typeof(settings.after) == "function") settings.after({  element: settings.button_element, 
																			hours: settings.h_dom.val(),
																			minutes: settings.m_dom.val() });
			});
			
			// binding to change time code, skip_callback skips callback ;)
			$(this).bind("change-time", function(event, timeobj){
				if (typeof(timeobj) == "undefined") timeobj = {hours: helpers.current_value('hours', settings), minutes: helpers.current_value('minutes', settings)}
				helpers.update_base_time(timeobj.hours, timeobj.minutes, base_template, true, settings);
				if (!timeobj.skip_callback) $(this).trigger("time-changed");
			});
				
			// EVENTS:
				
			hours_template.children("li").click(function(){
				$(this).addClass("ui-selected")
						.attr("tabindex", -1)
						.siblings().removeClass("ui-selected")
									.attr("tabindex", -1);
				//$(this).trigger("mouseover");
				minutes_template.children("li:first").click();
				return false;
			}).mouseover(function(){
 				// dont flicker
				if (!$(this).parent().is(":animated")){
					minutes_template.show().position({my:"left top", at: "left bottom", of: $(this), collision: "fit flip"})
					helpers.set_as_current($(this), hours_template, minutes_template, base_template, settings);
					$.each(minutes_template.children(), function(i){
						$(this).attr("tabindex", i+1);
					});
					minutes_template.find("li:first").focus();
				}
			}).mouseout(function(){
				helpers.update_base_time_from_templates(hours_template, minutes_template, base_template, false, settings);
			});
			
			minutes_template.children("li").click(function(){
				helpers.update_base_time_from_templates(hours_template, minutes_template, base_template, true, settings);
				$(minutes_template).hide("blind", "fast", function(){if (!$(hours_template).is(":animated")) $(hours_template).hide("drop", "fast")});
				$("body").unbind("click.timeselect");
				$(this).trigger("time-changed");
				return false;
			}).mouseover(function(){
				helpers.set_as_current($(this), hours_template, minutes_template, base_template, settings);
			}).mouseout(function(){
				helpers.update_base_time_from_templates(hours_template, minutes_template, base_template, false, settings);
			});
			
			// key nav
			$.each([minutes_template, hours_template], function(){
				$.each(this.children(), function(i){
					var current = $(this);
					$(this).focus(function(){
								helpers.set_as_current($(this), hours_template, minutes_template, base_template, settings);
							})
							.keydown(function(e){
								// enter or space
								if ((e.keyCode || e.which) == 13 || (e.keyCode || e.which) == 32) {
									current.click();
								// right
								} else if ((e.keyCode || e.which) == 39) {
									var last_tab = parseInt($(this).parent().children(":last").attr("tabindex"),10),
										this_tab = parseInt($(this).attr("tabindex"));

									if (this_tab < last_tab) { $(this).next().focus(); } else { $(this).siblings(":first").focus(); };
								// left
								} else if ((e.keyCode || e.which) == 37) {
									var first_tab = parseInt($(this).parent().children(":first").attr("tabindex"), 10),
										this_tab = parseInt($(this).attr("tabindex"), 10);

									if (this_tab > first_tab) { $(this).prev().focus(); } else { $(this).siblings(":last").focus(); };
								}
							});
				});
			});

			// RENDER:
			settings.button_element.append(
				base_template.click(function(){
					if (hours_template.is(":hidden")){
						$("body").trigger("click.timeselect");
						$("body").one("click.timeselect", function(){
							hours_template.children().attr("tabindex", -1);
							hours_template.hide("drop", "fast");
							minutes_template.hide();
							helpers.update_base_time(helpers.current_value("hours", settings), helpers.current_value("minutes", settings), base_template, false, settings);
							return false;
						});
						hours_template.show("fade", "fast").position({my:"left top", at: "left bottom", of: $(this), collision: "fit flip"});
						$.each(hours_template.children(), function(i){
							$(this).attr("tabindex", i+1);
						});
						hours_template.find("li:first").focus();
					} else {
						hours_template.children().attr("tabindex", -1);
						hours_template.hide("drop", "fast");
						$("body").unbind("click.timeselect");
					}
					minutes_template.hide();
					helpers.update_base_time(helpers.current_value("hours", settings), helpers.current_value("minutes", settings), base_template, false, settings);
					return false;
				}),
				hours_template.hide(),
				minutes_template.hide()
			);
			helpers.reset_current_values(hours_template, minutes_template, settings);
		});	
	};
})( jQuery );