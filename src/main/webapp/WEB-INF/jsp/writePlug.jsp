<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
	<title><div id="pageTitle">Plugin-Manager : WritePlug</div></title>
	<div id="pageHeader">
		<style type="text/css" media="screen"></style>
		<script src="js/writePlug.js"></script>
		<script type="text/javascript" src="jquery/jquery.bpopup.min.js"></script>
	</div>
</head>
<body id="page-top">
	<div id="pageBreadcrumb">Plugin Manager</div>
	<div id="pageContents">
		<!-- Page Content -->
		<h1>Plugin Manager</h1>
		<hr/>
		<p>Please write plugin configuration include input, filter, output.</p>
	      
	    <div class="card mx-auto" style="border-width: 0 0 0 0">
			<div id="editor"></div>
		</div>
			
		<div>
			<br/>
			<button id="addBtn" class="btn btn-secondary" type="button" value="addFile">ADD CONFIG FILE</button>
			<br/>
			<select id="fileSel" class="form-control">
				<option value="none">파일 선택</option>
		<c:forEach var="item" items="${fileNmList}">
				<option value="${item}"><c:out value="${item}"/></option>
		</c:forEach>
			</select>
			
			<input type="hidden" id="section" class="form-control" value="" />
			<br/><br/>
			
			<div class="plugin-buttons">
				<button id="inputBtn" class="btn btn-primary" type="button" value="input">INPUT</button>
				<button id="filterBtn" class="btn btn-primary" type="button" value="filter">FILTER</button>
				<button id="outputBtn" class="btn btn-primary" type="button" value="output">OUTPUT</button>
				<button id="saveBtn" class="btn btn-success" type="button" value="START ENGINE">SAVE</button>
				<a id="startEngine" class="btn btn-info" href="javascript:;">START ENGINE</a>
				<a id="downEngine" class="btn btn-info" href="javascript:;">STOP ENGINE</a>
			</div>
		</div>
		
		<div class="popAddConfFile" id="popAddConfFile"></div>
		<div class="engineLoading" id="engineLoading" style="display: none;"><img alt="loadingImg" src="img/loader4.gif"></div>
	</div>
</body>
</html>
