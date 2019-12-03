<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<title><div id="pageTitle">Plugin-Manager : Dashboard</div></title>
	<div id="pageHeader"><script src="js/dashboard.js"></script></div>
</head>
<body id="page-top">
	<div id="pageBreadcrumb">Overview</div>
	<div id="pageContents">
		<!-- top info -->
		<div class="container" style="max-width: 100%;padding-right: 0px;padding-left: 0px;">
			<div class="card mx-auto">
				<div class="card-body">
					<div class="form-group">
						<div class="form-row">
							<div class="col-md-3">
								<div class="form-label-group">
			                		<h5>Nodes</h5>
			                		<div id="" style="max-width: 100%;">1</div>
			              		</div>
			            	</div>
			            	<div class="col-md-3">
								<div class="form-label-group">
						        	<h5>Memory</h5>
			                    	<div id="data_memory" style="max-width: 100%;"></div>
			                  	</div>
			            	</div>
			            	<div class="col-md-3">
			              		<div class="form-label-group">
						    		<h5>Events Received</h5>
			                		<div id="data_event_in" style="max-width: 100%;"></div>
								</div>
			            	</div>
			            	<div class="col-md-3">
			              		<div class="form-label-group">
						    		<h5>Events Emitted</h5>
			                		<div id="data_event_out" style="max-width: 100%;"></div>
			              		</div>
			            	</div>
			          	</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END top info -->
		
		<br/>
		
		<!-- pipeline info -->
		<div class="container" style="max-width: 100%;padding-right: 0px;padding-left: 0px;">
			<div class="card mx-auto">
				<div class="card-body">
					<div class="form-group">
						<div class="form-row">
							<div class="col-md-3">
								<div class="form-label-group">
									<h5>Inputs</h5>
									<div id="data_input_name" style="max-width: 100%;"></div>
								</div>
							</div>
			            	<div class="col-md-3">
								<div class="form-label-group">
									<h5>Queue</h5>
									<div id="data_queue_name" style="max-width: 100%;"></div>
								</div>
							</div>
			            	<div class="col-md-3">
								<div class="form-label-group">
									<h5>Filters</h5>
									<div id="data_filter_name" style="max-width: 100%;"></div>
								</div>
							</div>
			            	<div class="col-md-3">
			            		<div class="form-label-group">
			            			<h5>Outputs</h5>
			            			<div id="data_output_name" style="max-width: 100%;"></div>
			            		</div>
			            	</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END pipeline info -->
		
		<br/>
		<br/>
		
		<!-- Chart -->
		<div class="card mb-3" style="width: 33%; float: left;">
			<div class="card-header">
				<i class="fas fa-chart-area"></i> Events Received (/s)
				<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="input 단계에서 Logstash 노드가 초 당 수신한 이벤트 수"><i class="fas fa-question-circle"></i></span>
			</div>
			<div class="card-body">
				<div id="event_received_graph"></div>
			</div>
			<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>
		        
		<div class="card mb-3"  style="width: 34%; float: left;">
			<div class="card-header">
				<i class="fas fa-chart-area"></i> Events Emitted (/s)
				<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="output 단계에서 Logstash 노드가 초 당 수신한 이벤트 수"><i class="fas fa-question-circle"></i></span>
			</div>
			<div class="card-body">
				<div id="events_emitted_rate_graph"></div>
			</div>
			<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>

		<div class="card mb-3"  style="width: 33%; ">
			<div class="card-header">
				<i class="fas fa-chart-area"></i> Events Latency (ms)
				<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="필터 및 출력 단계에서 이벤트가 소비한 평균 시간. 이벤트를 처리하는 데 걸리는 총 시간을 생성 된 이벤트 수로 나눈 값"><i class="fas fa-question-circle"></i></span>
			</div>
			<div class="card-body">
			<div id="even_latency_graph"></div>
		</div>
		<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>
		<!-- END Chart -->
	</div>
</body>
</html>
