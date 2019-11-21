<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
	<title>PopupWritePlug</title>
	<style type="text/css" media="screen"></style>
	<script src="js/writePlug.js"></script>
</head>
<body>
	<div class="modal-xl" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button class="close writeClose" type="button" data-dismiss="modal" aria-label="Close">
  					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="card mx-auto" style="border-width: 0 0 0 0">
					<div id="editor"></div>
				</div>
				<div>
					<select id="fileSel" class="form-control">
						<option value="none">파일 선택</option>
					<c:forEach var="item" items="${fileNmList}">
						<option value="${item}"><c:out value="${item}"/></option>
					</c:forEach>
					</select>
					
					<input type="hidden" id="section" class="form-control" value="" />
					<br><br>
					
					<div class="plugin-buttons">
						<button id="inputBtn" class="btn btn-primary" type="button" value="input">INPUT</button>
						<button id="filterBtn" class="btn btn-primary" type="button" value="filter">FILTER</button>
						<button id="outputBtn" class="btn btn-primary" type="button" value="output">OUTPUT</button>
						<button id="saveBtn" class="btn btn-success" type="button" value="START ENGINE">SAVE</button>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a class="btn btn-info" href="/start">START ENGINE</a>
				<a class="btn btn-info" href="/shutdown">STOP ENGINE</a>
			</div>
		</div>
	</div>
</body>
</html>