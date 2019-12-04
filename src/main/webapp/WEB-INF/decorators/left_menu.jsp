<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Sidebar -->
<ul class="sidebar navbar-nav">
	<li class="nav-item <c:if test='${menu eq "dashboard"}'>active</c:if>">
		<a class="nav-link" href="dashboard">
			<i class="fas fa-fw fa-tachometer-alt"></i>
			<span>Dashboard</span>
		</a>
	</li>
	<li class="nav-item <c:if test='${menu eq "charts"}'>active</c:if>">
		<a class="nav-link" href="charts">
			<i class="fas fa-fw fa-chart-area"></i>
			<span>Charts</span>
		</a>
	</li>
	<li class="nav-item <c:if test='${menu eq "writeUi"}'>active</c:if>">
		<a class="nav-link" href="writeUi">
			<i class="fas fa-fw fa-table"></i>
			<span>Plugin Manager</span>
		</a>
	</li>
	<li class="nav-item <c:if test='${menu eq "flowChart"}'>active</c:if>">
		<a class="nav-link" href="flowChart">
			<i class="fas fa-fw fa-table"></i>
			<span>FlowChart</span>
		</a>
	</li>
</ul>
<!-- END Sidebar -->