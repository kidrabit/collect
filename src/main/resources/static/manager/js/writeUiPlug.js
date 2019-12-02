$(function(){
	$("#inputBtn,#filterBtn,#outputBtn").click(function(){

		var confSection = this.value;
		$("#confSection").val(confSection);

		if(confSection != "" && confSection != null && confSection != undefined){
			$(".selectFormDiv").load("/form/selectForm", {"confSection" : confSection}, function(response, status, xhr){
				if(status == "success"){
					$(".writeFormDiv").load("/form/writeForm", {"confSection" : confSection}, function(response, status, xhr){
						if(status == "success"){
							$.onloadData();
						}else if(status == "error"){
							alert(status);
						}
					});
				}else if(status == "error"){
					alert(status);
				}
			});
		}
	});
	
	$("#saveBtn").click(function(){
		var params = $("#confForm").serialize();
		
		$.ajax({
			url : "/manager/writeUiSave",
			dataType : "json",
			type: "post",
			data : params,
			success : function(data, textStatus, jqXHR){
				alert(data.msg);
			},
			error : function(jqXHR, textStatus, errorThrown){
				alert("error" + errorThrown);
			}
		});
	});

	$("#popUpView").click(function(){

       $('#popWritePlug').bPopup({
            modalClose : false,
            content:'ajax',
            contentContainer:'#popWritePlug',
            loadUrl: '/popup/writeView',
            closeClass:'writeClose',
            onOpen: function() {
           	 //$(".content").html("");
            }, 
            onClose: function() {
            	$("#popWritePlug").html("");
            }
        });
	});
	
	$.move_chk = function(){
		var btn_id = "";
		var move = $("#pageMove").val();
		if(move == "input" || move == "filter" || move == "output"){
			btn_id = "#"+move+"Btn";
			$(btn_id).click();
		}
	}
	
	$.move_chk();
});