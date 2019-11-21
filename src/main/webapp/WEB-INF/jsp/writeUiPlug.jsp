<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
	<title><div id="pageTitle">Plugin-Manager : WritePlug</div></title>
	<div id="pageHeader">
		<style type="text/css" media="screen"></style>
		<script type="text/javascript" src="js/writeUiPlug.js"></script>
		<script type="text/javascript" src="jquery/jquery.bpopup.min.js"></script>
		
		<!-- 
		writeForm.jsp쪽 스크립트
		ajax로 html동적으로 넣을 때 외부 스크립트 로드 시 경고가 나와서 밖으로 뺌 
		-->
		<link rel="stylesheet" type="text/css" href="css/easy-responsive-tabs.css"/>
		<script type="text/javascript" src="js/easyResponsiveTabs.js"></script>
	    <script type="text/javascript" src="js/writeForm.js"></script>
	</div>
</head>
<body id="page-top">
	<div id="pageBreadcrumb">Plugin Manager2</div>
	<div id="pageContents">
		<!-- Page Content -->
		<h1>Plugin Manager2</h1>
		<hr/>
		<p>Please write plugin configuration include input, filter, output.</p>
		
		<div>
			<div class="plugin-buttons">
				<button id="inputBtn" class="btn btn-primary" type="button" value="input">INPUT</button>
				<button id="filterBtn" class="btn btn-primary" type="button" value="filter">FILTER</button>
				<button id="outputBtn" class="btn btn-primary" type="button" value="output">OUTPUT</button>
				<button id="saveBtn" class="btn btn-success" type="button" value="START ENGINE">SAVE</button>
				<a id="popUpView" class="btn btn-info" href="javascript:;">VIEW</a>
			</div>
			
			<input type="hidden" name="pageMove" id="pageMove" value="${pageMove}" />
			<form name="confForm" id="confForm" >
				<input type="hidden" name="confSection" id="confSection" value="" />
				<div class="selectFormDiv"></div>
				<br/>
				<div class="writeFormDiv"></div>
			</form>
		</div>
		
		<div class="popWritePlug" id="popWritePlug"></div>
	</div>
</body>
</html>
