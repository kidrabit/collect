<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/morris.js/0.5.1/morris.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/morris.js/0.5.1/morris.min.js"></script>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">

<title><div id="pageTitle">Plugin-Manager : Charts</div></title>
<div id="pageHeader"><script src="js/charts.js"></script></div>

</head>
<body>
<div id="system_load" style="height: 250px;"></div>
<div id="pageContents">${json}</div>
<script>
	var res = eval(${json});
	console.log(res);
	new Morris.Line({
	  element: 'system_load',
	  data: res,
	  xkey: 'timestamp',
	  ykeys: ['event_latency'],
	  labels: ['event_latency']
	}); 
</script>
</body>
</html>
