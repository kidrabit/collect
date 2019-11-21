<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
	<title>PopupWritePlug</title>
	<style type="text/css" media="screen"></style>
	<script src="js/popupWritePlugView.js"></script>
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
					<div id="editor" style="max-width: 100%;">${contents}</div>
				</div>
			</div>
			<div class="modal-footer">
				<a id="startEngine" class="btn btn-info" href="javascript:;">START ENGINE</a>
				<a id="downEngine" class="btn btn-info" href="javascript:;">STOP ENGINE</a>
			</div>
		</div>
		<div class="engineLoading" id="engineLoading" style="display: none;"><img alt="loadingImg" src="img/loader4.gif"></div>
	</div>
</body>
</html>