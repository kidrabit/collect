$(function(){
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.session.setMode("ace/mode/plain_text");
    editor.setReadOnly(true);
    editor.setAutoScrollEditorIntoView(true);
    //editor.setOptions({maxLines: Infinity});
    //document.getElementById('editor').style.fontSize='19px';
    
	var engineLoading;//로딩화면 제어
	
	$.loading_img = function(){
		engineLoading = $('#engineLoading').bPopup({modalClose : false});
	}

	var engChk = true;//Engine 버튼 제어
	
	$("#startEngine").click(function(){
		if(engChk == true){
			
			engChk = false;
			
			$.loading_img();
			
			$.ajax({
				url : "/manager/start.ajax",
				dataType : "json",
				type: "post",
				success : function(data, textStatus, jqXHR){
					alert(data.engineMsg);
				},
				error : function(jqXHR, textStatus, errorThrown){
					alert(jqXHR.responseText);
				},
				complete : function () {
					if(engineLoading != null && engineLoading != undefined) engineLoading.close();
					engChk = true;
				}
			});
		}
	});
	
	$("#downEngine").click(function(){
		if(engChk == true){
			
			engChk = false;
			
			$.ajax({
				url : "/manager/shutdown.ajax",
				dataType : "json",
				type: "post",
				success : function(data, textStatus, jqXHR){
					alert(data.engineMsg);
				},
				error : function(jqXHR, textStatus, errorThrown){
					alert(jqXHR.responseText);
				},
				complete : function () {
					engChk = true;
				}
			});
		}
	});
});