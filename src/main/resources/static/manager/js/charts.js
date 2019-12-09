
$(function(){
	
	$('[data-toggle="tooltip"]').tooltip();
	
	var jvm_heap_data = [];
	var cpu_utilization_data = [];
	var system_load_data = [];
	var events_received_rate_data=[];
	var events_emitted_rate_data = [];
	var even_latency_data = [];
	var jvm_heap_graph, cpu_utilization_graph, system_load_graph, event_received_graph , even_latency_graph, events_emitted_rate_graph;
	
	$.makeChart = function(){
		jvm_heap_graph = new Morris.Line({
			  element: 'jvm_heap_graph',
			  data: jvm_heap_data,
			  xkey: 'timestamp',
			  ykeys: ['heap_max_in_bytes', 'heap_used_in_bytes'],
			  labels: ['heap_max_in_bytes', 'heap_used_in_bytes'],
			  resize: true,
			  units: 'GB',
			  pointSize: 2
		});
	
		cpu_utilization_graph = new Morris.Line({
				  element: 'cpu_utilization_graph',
				  data: cpu_utilization_data,
				  xkey: 'timestamp',
				  ykeys: ['percent'],
				  labels: ['percent'],
				  resize: true,
				  units: '%',
				  pointSize: 2
		});
		
		system_load_graph = new Morris.Line({
				  element: 'system_load_graph',
			 	  data: system_load_data,
			   	  xkey: 'timestamp',
			 	  ykeys: ['one_min', 'five_min', 'fifteen_min'],
			 	  labels: ['one_min', 'five_min', 'fifteen_min'],
			 	  //parseTime: false,
				  resize: true,
				  pointSize: 2
		});
	}

	/*
	 * Event Graph 
	 * */
	$.makeEventChart = function(){
		event_received_graph = new Morris.Line({
			  element: 'event_received_graph',
			  data: events_received_rate_data,
			  xkey: 'timestamp',
			  ykeys: ['events_out'],
			  labels: ['events_out'],
			  resize: true,
			  pointSize: 2
		});
		
		events_emitted_rate_graph = new Morris.Line({
			  element: 'events_emitted_rate_graph',
			  data: events_emitted_rate_data,
			  xkey: 'timestamp',
			  ykeys: ['rate'],
			  labels: ['rate'],
			  resize: true,
			  pointSize: 2
		});
		
		even_latency_graph = new Morris.Line({
			  element: 'even_latency_graph',
			  data: even_latency_data,
			  xkey: 'timestamp',
			  ykeys: ['millis'],
			  labels: ['millis'],
			  resize: true,
			  units: 'ms',
			  pointSize: 2
		});
	}
	
	$('input[name="fromTime"]').daterangepicker({
	    timePicker: true,
	    timePicker24Hour: true,
	    startDate: moment().startOf('hour'),
	    endDate: moment().startOf('hour', 23),
	    minDate : moment().startOf('hour').subtract(6, 'days'),
	    maxDate : moment().startOf('hour'),
	    locale: {
	    format: 'Y-MM-DDTHH:mm:ss.SSS'
	    }
	});
	

	function callChartData(formData){
		
		if(formData == undefined || formData == null) formData = null;
		
		$.ajax({
			url : "/manager/elasticGet",
			dataType : "text",
			type: "get",
			data : formData,
			success : function(data, textStatus, jqXHR){
				data = JSON.parse(data);
				stackup_genData(data);
			},
			error : function(jqXHR, textStatus, errorThrown){
				alert("error" + errorThrown);
			}
		});
	}
	
	function callEventData(formData){
		
		if(formData == undefined || formData == null) formData = null;
		
		$.ajax({
			url : "/manager/eventGet",
			dataType : "text",
			type: "get",
			data : formData,
			success : function(data, textStatus, jqXHR){
				data = JSON.parse(data);
				stackup_eventData(data);
			},
			error : function(jqXHR, textStatus, errorThrown){
				alert("error" + errorThrown);
			}
		});
	}
	
	$('.submit-time').bind('click', function(e) {
		
		var form = $('.timepicker-form').serializeArray();
		
		if(form[0].value == "") {
			alert("유효한 시간을 입력해주세요.");
		}else{
			if(repeat != null && repeat != undefined){
				clearInterval(repeat);
			}
			callChartData(form);
			callEventData(form);
		}
	});
	
	$('.search-realtime').bind('click', function(e) {
		if(repeat != null && repeat != undefined){
			clearInterval(repeat);
		}
		callChartData();
		callEventData();
		getTimer();
	});
	
	
	function stackup_genData(data) {
		
		//처음 한번만 실행
		if(first_gen_chk){
			$.makeChart();
			first_gen_chk = false;
		}
		
		clear_gen_data();
		
		if(data.jvm_heap.length > 0){
			jvm_heap_data = jvm_heap_data.concat(data.jvm_heap);
		}

		if(data.cpu_utilization.length > 0){
			cpu_utilization_data = cpu_utilization_data.concat(data.cpu_utilization);
		}
		
		if(data.system_load.length > 0){
			system_load_data = system_load_data.concat(data.system_load);
		}
		
		jvm_heap_graph.setData(jvm_heap_data);
		cpu_utilization_graph.setData(cpu_utilization_data);
		system_load_graph.setData(system_load_data);
	}
	
	function stackup_eventData(data){
		//처음 한번만 실행
		if(first_event_chk){
			$.makeEventChart();
			first_event_chk = false;
		}
		
		clear_event_data();
		
		if(data.events_recevied.length > 0){
			events_received_rate_data = events_received_rate_data.concat(data.events_recevied);
		 }
		 if(data.events_emitted_rate.length > 0){
			events_emitted_rate_data = events_emitted_rate_data.concat(data.events_emitted_rate);
		 }
		 if(data.even_latency.length > 0){
			even_latency_data = even_latency_data.concat(data.even_latency);
		 }
		 
		 event_received_graph.setData(events_received_rate_data);
		 even_latency_graph.setData(even_latency_data);
		 events_emitted_rate_graph.setData(events_emitted_rate_data);
	}
	
	function validate(form){
		var diff = form[1].value.substring(0,2) - form[0].value.substring(0,2);		
		return diff;
	}
	
	function clear_gen_data(){
		jvm_heap_data = [];
		cpu_utilization_data = [];
		system_load_data = [];
	}
	
	function clear_event_data(){
		events_received_rate_data=[];
		events_emitted_rate_data = [];
		even_latency_data = [];
	}
	
	var repeat;
	var first_gen_chk = true;
	var first_event_chk = true;
	
	function getTimer(){
		repeat = setInterval(function(){
			callChartData();
			callEventData();
		}, 10000);
	}
	
	getTimer();
	callChartData();
	callEventData();
});
