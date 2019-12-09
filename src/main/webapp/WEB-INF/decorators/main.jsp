<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="header.jsp"/>
	<title><sitemesh:write property="div.pageTitle"/></title>
	<sitemesh:write property="div.pageHeader"/>
</head>
<body id="page-top">
  	<!-- nav -->
	<nav class="navbar navbar-expand navbar-dark bg-dark static-top">
		<a class="navbar-brand mr-1" href="index">Plugin Manager</a>

		<button class="btn btn-link btn-sm text-white order-1 order-sm-0" id="sidebarToggle" href="#">
			<i class="fas fa-bars"></i>
		</button>

	    <!-- Navbar -->
		<ul class="navbar-nav ml-auto ml-md-0">
			<li class="nav-item dropdown no-arrow">
				<a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<i class="fas fa-user-circle fa-fw"></i>
				</a>
	        	<div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
	          		<a class="dropdown-item" href="#">Settings</a>
	          		<a class="dropdown-item" href="#">Activity Log</a>
	          		<div class="dropdown-divider"></div>
	          		<a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">Logout</a>
	        	</div>
	      	</li>
	    </ul>
	    <!-- END Navbar -->

	</nav>
  	<!-- END nav -->

  	<!-- wrapper -->
  	<div id="wrapper">
  
		<jsp:include page="left_menu.jsp" />
	
		<!-- content-wrapper -->	
    	<div id="content-wrapper">
    
			<!-- container-fluid -->
			<div class="container-fluid">
		
	        	<!-- Breadcrumbs-->
	        	<ol class="breadcrumb">
	          		<li class="breadcrumb-item" style="width:100%; float:right;">
	          			<sitemesh:write property="div.pageBreadcrumb" />
	          		</li>
	        	</ol>
	        	<!-- END Breadcrumbs-->
	        
				<sitemesh:write property="div.pageContents"/>
    		</div>
    		<!-- END container-fluid -->
    	
    		<jsp:include page="footer.jsp" />
    	</div>
    	<!-- END content-wrapper -->
  	</div>
  	<!-- END wrapper -->

	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top">
    	<i class="fas fa-angle-up"></i>
	</a>
  	<!-- END Scroll to Top Button-->

  	<!-- Logout Modal-->
  	<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
      		<div class="modal-content">
        		<div class="modal-header">
          			<h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
          			<button class="close" type="button" data-dismiss="modal" aria-label="Close">
            			<span aria-hidden="true">×</span>
          			</button>
        		</div>
        		<div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
        		<div class="modal-footer">
          			<button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
          			<a class="btn btn-primary" href="login.html">Logout</a>
        		</div>
      		</div>
    	</div>
	</div>
	<!-- END Logout Modal-->
</body>
</html>