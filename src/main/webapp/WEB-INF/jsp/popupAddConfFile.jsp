<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
	<title>PopupAddFile</title>
	<style type="text/css" media="screen"></style>
	<script type="text/javascript" src="jquery/jquery.bpopup.min.js"></script>
	<script type="text/javascript">
	$(function(){
		$(".addFileBtn").click(function(){
			
			var confFileName = $("#confFileName").val();
			var special_chk = /^[a-zA-Z0-9]([-_]?[a-zA-Z0-9])*$/;

			if(confFileName.length == 0 || !special_chk.test(confFileName)){
				alert("파일 이름을 제대로 입력해 주세요.");
				return;
			}

			$.ajax({
				url : "/popup/addConfFileSave",
				dataType : "json",
				type: "post",
				data : {"confFileName" : confFileName},
				success : function(data, textStatus, jqXHR){
					var msg = data.msg;
					if(msg == "success"){
						location.reload(true);
					} else if(msg == "duplicate"){
						alert("같은 이름의 파일이 존재합니다.");
					}
				},
				error : function(jqXHR, textStatus, errorThrown){
					alert("error" + errorThrown);
				}
			});
		});
	});
	</script>
</head>
<body>

	<div class="modal-xl" role="document" style="min-width: 600px">
		<div class="modal-content">
			<div class="modal-header">
				<button class="close btnClose" type="button" data-dismiss="modal" aria-label="Close">
  					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				파일이름 : <input type="text" id="confFileName" class="form-control" placeholder="file name" style="min-width: 400px"> .conf
			</div>
			<div class="modal-footer">
				<button id="addFileBtn" class="btn btn-primary addFileBtn" type="button">SAVE</button>
				<button id="closeBtn" class="btn btn-primary btnClose" type="button">CANCEL</button>
			</div>
		</div>
	</div>
	
</body>
</html>