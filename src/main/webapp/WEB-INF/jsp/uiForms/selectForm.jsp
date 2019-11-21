<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <script type="text/javascript">
    	$(function(){
    		$("#tabAddBtn").click(function(){
        		var itemNm = $("#itemSel").val();
        		if(itemNm != "none") $.tabItemAdd(itemNm, "add");
    		});
		});
    </script>
	<select id="itemSel" class="form-control">
		<option value="none"><c:out value="${confSection}" /> 설정 선택</option>
	<c:forEach var="item" items="${selectList}">
		<option value="${item}"><c:out value="${item}"/></option>
	</c:forEach>
	</select>
	
	<input type="button" id="tabAddBtn" class="btn btn-secondary" style="vertical-align:top;" value="추가" />