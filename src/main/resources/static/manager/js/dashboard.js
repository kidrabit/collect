$(function(){

	var events_received_rate_data=[];
	var events_emitted_rate_data = [];
	var even_latency_data = [];
	var event_received_graph , even_latency_graph, events_emitted_rate_graph;
	
	$.makeChart = function(){
		event_received_graph = new Morris.Line({
			element: 'event_received_graph',
			data: events_received_rate_data,
			xkey: 'timestamp',
			ykeys: ['events_out'],
			labels: ['events_out'],
			resize: true,
			xLabels : "5min",
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
			xLabels : "5min",
			pointSize: 2
		});
		
		events_emitted_rate_graph = new Morris.Line({
			element: 'events_emitted_rate_graph',
			data: events_emitted_rate_data,
			xkey: 'timestamp',
			ykeys: ['rate'],
			labels: ['rate'],
			resize: true,
			xLabels : "5min",
			pointSize: 2
		});
	}

	$.callDashboardData = function(){
		$.ajax({
			url : "/manager/elasticDashboardGet",
			dataType : "json",
			type: "get",
			data : null,
			success : function(data, textStatus, jqXHR){
				data = JSON.parse(data);
				
				if(data.top_info.length > 0){
					$("#data_memory").text(data.top_info[0].heap_used_in_bytes + " / " + data.top_info[0].heap_max_in_bytes);
					$("#data_event_in").text(data.top_info[0].events_in);
					$("#data_event_out").text(data.top_info[0].events_out);
				}
				
				if(data.pipeline.length > 0){
					$("#data_input_name").text(data.pipeline[0].input_name);
					$("#data_queue_name").text(data.pipeline[0].queue_name);
					$("#data_filter_name").text(data.pipeline[0].filter_name);
					$("#data_output_name").text(data.pipeline[0].output_name);
				}else{
					$("#data_input_name").text("not available");
					$("#data_queue_name").text("not available");
					$("#data_filter_name").text("not available");
					$("#data_output_name").text("not available");
				}
				
				if(data.events_recevied.length > 0){
					events_received_rate_data = events_received_rate_data.concat(data.events_recevied);
				}
				
				if(data.events_emitted_rate.length > 0){
					events_emitted_rate_data = events_emitted_rate_data.concat(data.events_emitted_rate);
				}
				
				if(data.even_latency.length > 0){
					even_latency_data = even_latency_data.concat(data.even_latency);
				}
				
				if(first_chk){
					$.makeChart();
					first_chk = false;
				}
			},
			error : function(jqXHR, textStatus, errorThrown){
				alert(jqXHR.responseText);
			}
		});
	}
	
	var repeat;
	var first_chk = true;
	
	$.getTimer = function(){
		repeat = setInterval(function(){
			$.callDashboardData();
		}, 10000);
	}
	
	$.callDashboardData();
	$.getTimer();
});
