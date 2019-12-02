$(function(){
	
	var formJson, dataJsonArray;
	
	$.onloadData = function(){
		formJson = $("#formJson").val();
		dataJsonArray = $("#dataJsonArray").val();

		if(formJson != "" && formJson != null){
			formJson = jQuery.parseJSON(formJson);
		}

		if(dataJsonArray != "" && dataJsonArray != null){
			dataJsonArray = jQuery.parseJSON(dataJsonArray);
		}

		if(formJson != "" && dataJsonArray != ""){
			for(var i=0; i<dataJsonArray.length; i++){
				for(var key in dataJsonArray[i]){
					$.tabItemAdd(key, "onload", dataJsonArray[i]);
					$.initTabData(dataJsonArray[i], key, i);
				}
			}
		}else{
			$("#parentVerticalTab").hide();
		}
	}

	$.initTabData = function(dataJson, itemNm, idx){
		var tabNum = "tab"+(idx+1);
		
		for(var key in formJson[itemNm]){
			if(formJson[itemNm][key].type == "checkbox"){
				if(dataJson[itemNm][key] == true){
					$("#"+tabNum+"_"+key).attr('checked', true);
				}
			}else if(formJson[itemNm][key].type == "hash"){
				if(dataJson[itemNm][key] != null){
					var cnt = 1;
					for(var dataKey in dataJson[itemNm][key]){
						$("#"+tabNum+"_"+key+"_name"+cnt).val(dataKey);
						$("#"+tabNum+"_"+key+"_value"+cnt).val(dataJson[itemNm][key][dataKey]);
						cnt++;
					}
				}
			}else{
				$("#"+tabNum+"_"+key).val(dataJson[itemNm][key]);
			}
		}
	}

	$.tabItemAdd = function(itemNm, mode, dataJson){
		var $tab = $(".resp-tabs-list");
		var $tab_list = $(".resp-tabs-list > li");
		var len = $tab_list.length;
		var temp_id = "";

		//탭 아이디 숫자 사용
		if(len > 0){
			temp_id = $tab_list[--len].id;
			len = parseInt(temp_id.split("_")[2]);
		}
		
		len ++;
		
		var strNode = "<li id='item_li_"+len+"'>" + itemNm + "</li>";
		$tab.append(strNode);

		$tab = $(".resp-tabs-container");
		
		var tabNum = "tab"+len;

		strNode = "<div id='item_div_"+len+"'>";
		strNode += "<input type='hidden' name='tabItemNm' id='tabItemNm' value='"+tabNum+":"+itemNm+"' />";
		
		strNode += "<input type='button' id='itemRemoveBtn"+len+"' class='btn btn-info' value='삭제' style='float: right;' />";
		strNode += "<br/><br/>";
		
		strNode += "<table class='table table-bordered' id='dataTable' width='100%' cellspacing='0' >";
		strNode += "<tbody>";

		var str_type, str_add_event;

		for(var key in formJson[itemNm]){
			strNode += "<tr>";
			strNode += "<td style='vertical-align: middle; width: 30%'>"+key+" : </td>";

			if(formJson[itemNm][key].data == "number"){
				str_add_event = "onkeypress='$.numberOnly()'";
			}else{
				str_add_event = "";
			}

			str_type = formJson[itemNm][key].type;

			if(str_type == "text"){
				strNode += "<td><input type='"+str_type+"' name='"+tabNum+"_"+key+"' id='"+tabNum+"_"+key+"' class='form-control' style='width:80%;' "+str_add_event+" /></td>";
			}else if(str_type == "checkbox"){
				strNode += "<td><input type='"+str_type+"' name='"+tabNum+"_"+key+"' id='"+tabNum+"_"+key+"' value='true'/></td>";
			}else if(str_type == "hash"){

				strNode += "<td id='td_"+tabNum+"_"+key+"'>";
				
				//처음 세팅할떄 사용
				if(mode == "onload"){
					if(dataJson[itemNm][key] != null){

						var cnt = Object.keys(dataJson[itemNm][key]).length+1;
						
						for(var i=1; i<cnt; i++){
							strNode += "<div style='padding-bottom:5px;'>";
							strNode += "<input type='text' name='"+tabNum+"_"+key+"_name' id='"+tabNum+"_"+key+"_name"+i+"' class='form-control' style='width:40%;' />";
							strNode += "<input type='text' name='"+tabNum+"_"+key+"_value' id='"+tabNum+"_"+key+"_value"+i+"' class='form-control' style='width:40%;' />";
							strNode += "<input type='button' class='btn btn-info' style='vertical-align:top;' value='+' onclick='javascript:$.addBtnClick(this);' />";
							strNode += "<input type='button' class='btn btn-secondary' style='vertical-align:top;' value='-' onclick='javascript:$.removeBtnClick(this);' />";
							strNode += "</div>";
						}
					}else{
						strNode += "<div style='padding-bottom:5px;'>";
						strNode += "<input type='text' name='"+tabNum+"_"+key+"_name' id='"+tabNum+"_"+key+"_name1' class='form-control' style='width:40%;' />";
						strNode += "<input type='text' name='"+tabNum+"_"+key+"_value' id='"+tabNum+"_"+key+"_value1' class='form-control' style='width:40%;' />";
						strNode += "<input type='button' class='btn btn-info' style='vertical-align:top;' value='+' onclick='javascript:$.addBtnClick(this);' />";
						strNode += "<input type='button' class='btn btn-secondary' style='vertical-align:top;' value='-' onclick='javascript:$.removeBtnClick(this);' />";
						strNode += "</div>";
					}
				}else{
					strNode += "<div style='padding-bottom:5px;'>";
					strNode += "<input type='text' name='"+tabNum+"_"+key+"_name' id='"+tabNum+"_"+key+"_name1' class='form-control' style='width:40%;' />";
					strNode += "<input type='text' name='"+tabNum+"_"+key+"_value' id='"+tabNum+"_"+key+"_value1' class='form-control' style='width:40%;' />";
					strNode += "<input type='button' class='btn btn-info' style='vertical-align:top;' value='+' onclick='javascript:$.addBtnClick(this);' />";
					strNode += "<input type='button' class='btn btn-secondary' style='vertical-align:top;' value='-' onclick='javascript:$.removeBtnClick(this);' />";
					strNode += "</div>";
				}

				strNode += "</td>";
			}
			
			strNode += "</tr>"; 
		}

		strNode += "</tbody>";
		strNode += "</table>";
		strNode += "</div>";

		$tab.append(strNode);

		//버튼에 삭제 이벤트 추가
		$("#itemRemoveBtn"+len).bind("click", $.removeTab);

		$.initTabContainer();

		//추가 버튼 클릭시 포커스
		if(mode == "add") $("#item_li_"+len).click();
	}

	$.addBtnClick = function(button){
		var td_id = $(button).parent().parent().attr("id");
		var key = td_id.substring(td_id.indexOf("_")+1);
		
		var cnt = $("#"+td_id + " > div").length + 1;
		
		var strNode = "<div style='padding-bottom:5px;'>";
		strNode += "<input type='text' name='"+key+"_name' id='"+key+"_name"+cnt+"' class='form-control' style='width:40%;' />";
		strNode += "<input type='text' name='"+key+"_value' id='"+key+"_value"+cnt+"' class='form-control' style='width:40%;' />";
		strNode += "<input type='button' class='btn btn-info' style='vertical-align:top;' value='+' onclick='javascript:$.addBtnClick(this);' />";
		strNode += "<input type='button' class='btn btn-secondary' style='vertical-align:top;' value='-' onclick='javascript:$.removeBtnClick(this);' />";
		strNode += "</div>";

		$("#"+td_id).append(strNode);
	}

	$.removeBtnClick = function(button){
		$(button).parent().remove();
	}
	
	$.numberOnly = function(){
		if(event.keyCode<48 || event.keyCode>57){
            event.returnValue=false;
		}
	}

	$.removeTab = function(){
		
    	var $tab = $(this).parent();
    	var id = $tab.attr("id");
    	var id_num = id.split("_")[2];

    	$("#item_div_"+id_num).remove();
    	$("#item_li_"+id_num).remove();

    	$.initTabContainer();

    	//자식 노드 확인
    	//자식 노드 없으면 안보이게
		var $tab_list = $(".resp-tabs-list > li");
		var len = $tab_list.length;

		if(len == 0){
			$("#parentVerticalTab").css("display", "none");
		}
	}

	$.initTabContainer = function(){
        $('#parentVerticalTab').easyResponsiveTabs({
            type: 'vertical', //Types: default, vertical, accordion
            width: 'auto', //auto or any width like 600px
            fit: true, // 100% fit in a container
            closed: 'accordion', // Start closed if in accordion view
            tabidentify: 'hor_1' // The tab groups identifier
        });
	}
});