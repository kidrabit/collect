<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title><div id="pageTitle">Plugin-Manager : FlowChart</div></title>
	<div id="pageHeader">
		<script type="text/javascript" src="jquery/jquery-ui.min.js"></script>
		<script src="flowchart/jquery.flowchart.min.js"></script>
		<script type="text/javascript" src="jquery/jquery.bpopup.min.js"></script>
		<script type="text/javascript" src="js/flowChart.js"></script>
		<link rel="stylesheet" href="flowchart/jquery.flowchart.min.css" >
	</div>
</head>
<body id="page-top">
	<div id="pageBreadcrumb">FlowChart</div>
	<div id="pageContents">
		<input type="hidden" name="inputList" id="inputList" value="${inputList}" />
		<input type="hidden" name="outputList" id="outputList" value="${outputList}" />
		<div class="card mb-3">
			<div class="card-body">
				<div id="flowDiv" style="height: auto;min-height: 600px;"></div>
			</div>
		</div>
	</div>
</body>
</html>