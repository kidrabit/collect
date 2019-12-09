$(function(){

    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.session.setMode("ace/mode/plain_text");

	$("#inputBtn,#filterBtn,#outputBtn").click(function(){

		var fileName = $("#fileSel").val();
		var confSection = this.value;
		$("#section").val(confSection);

		if(fileName != "none"){
			$.ajax({
				url : "/manager/loadEditDt.ajax",
				dataType : "json",
				type: "post",
				data : {"fileName" : fileName, "confSection" : confSection},
				success : function(data, textStatus, jqXHR){
					editor.session.setValue(data.contents);
				},
				error : function(jqXHR, textStatus, errorThrown){
					alert(jqXHR.responseText);
				}
			});
		}
	});

	$("#saveBtn").click(function(){

		var fileName = $("#fileSel").val();
		var confSection = $("#section").val();
		
		if(fileName != "none" && confSection != ""){
			$.ajax({
				url : "/manager/saveEditDt.ajax",
				dataType : "json",
				type: "post",
				data : {"fileName" : fileName, "contents" : editor.session.getValue(), "confSection" : confSection},
				success : function(data, textStatus, jqXHR){  
					editor.session.setValue(data.contents);
					alert(data.msg);
				},
				error : function(jqXHR, textStatus, errorThrown){
					alert(jqXHR.responseText);
				}
			});
		}
	});
	
	$("#fileSel").change(function(){
		$("#section").val("");
		editor.session.setValue("");
	});
	
	
	$("#addBtn").click(function(){
        $('#popAddConfFile').bPopup({
            modalClose : false,
            content:'ajax',
            contentContainer:'.popAddConfFile',
            loadUrl: '/popup/addConfFile',
            closeClass:'btnClose',
            onOpen: function() {
           	 //$(".content").html("");
            }, 
            onClose: function() {
            	$(".popAddConfFile").html("");
            }
        });
	});
	
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
