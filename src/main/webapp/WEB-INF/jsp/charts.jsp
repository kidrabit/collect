<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>
	<title><div id="pageTitle">Plugin-Manager : Charts</div></title>
	<div id="pageHeader"><script src="js/charts.js"></script></div>
</head>

<body id="page-top">


	<div id="pageBreadcrumb">Charts
	<div class="abcd">
		<button class="search-realtime btn btn-primary">실시간 조회</button>
		<form class="timepicker-form">
			<input class="fromTime form-control" type="text" name="fromTime" placeholder="now-1h/h"/>
			<!-- <input class="fromTime form-control" type="text" name="fromTime" placeholder="now-1h/h"/>
			<input class="toTime form-control" type="text" name="toTime" placeholder="now"/> -->
			<button class="submit-time btn btn-success" type="button">조회</button>
		</form>
	</div>
	</div>
	<div id="pageContents">
        <!-- Chart -->
		<div class="card mb-3" style="width: 50%; float: left;">
			<div class="card-header">
            	<i class="fas fa-chart-area"></i> JVM Heap(MB) 
				<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="JVM heap 상태"><i class="fas fa-question-circle"></i></span>
			</div>
			<div class="card-body">
				<div id="jvm_heap_graph"></div>
	        </div>
	        <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
        </div>
        <div class="card mb-3">
			<div class="card-header">
				<i class="fas fa-chart-area"></i> CPU Utilization(%)
				<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="CPU 사용률"><i class="fas fa-question-circle"></i></span>
			</div>
			<div class="card-body">
				<div id="cpu_utilization_graph"></div>
			</div>
			<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>
        
		<div class="card mb-3" style="width: 50%; float: left;">
  			<div class="card-header">
    			<i class="fas fa-chart-area"></i> System Load
    			<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="시간 당 CPU 평균 로드 값"><i class="fas fa-question-circle"></i></span>
  			</div>
  			<div class="card-body">
   				<div id="system_load_graph"></div>
   				
  			</div>
  			<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>

		<div class="card mb-3">
  			<div class="card-header">
    			<i class="fas fa-chart-area"></i> Events Received(/s)
    			<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="input 단계에서 Logstash 노드가 초 당 수신한 이벤트 수"><i class="fas fa-question-circle"></i></span>
  			</div>
  			<div class="card-body">
   				<div id="event_received_graph"></div>
  			</div>
  			<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>

		<div class="card mb-3" style="width: 50%; float: left;">
  			<div class="card-header">
    			<i class="fas fa-chart-area"></i> Events Emitted(/s)
    			<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="output 단계에서 Logstash 노드가 초 당 수신한 이벤트 수"><i class="fas fa-question-circle"></i></span>
  			</div>
  			<div class="card-body">
    			<div id="events_emitted_rate_graph"></div>
  			</div>
  			<div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
		</div>

		<div class="card mb-3">
  			<div class="card-header">
    			<i class="fas fa-chart-area"></i> Event Latency (ms)
    			<span class="tooltip-mark" data-toggle="tooltip" data-placement="left" title="필터 및 출력 단계에서 이벤트가 소비한 평균 시간. 
이벤트를 처리하는 데 걸리는 총 시간을 생성 된 이벤트 수로 나눈 값"><i class="fas fa-question-circle"></i></span>
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