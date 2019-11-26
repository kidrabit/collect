;(function($) {
	$(function() {
		var data = {};
	    var operators;
	    var links;
	    
	    $.init_flowChart_data = function(){
	    	
	    	var inputList = $("#inputList").val();
	    	var filterList = ["logstash"];//수집 모듈은 하나
	    	var outputList = $("#outputList").val();
	    	var typeListCnt = {input : 0, output : 0};
	    	
	    	if(inputList != ""){
	    		inputList = inputList.split(",");
	    		typeListCnt["input"] = inputList.length;
	    	}
	    	
	    	if(outputList != ""){
	    		outputList = outputList.split(",");
	    		typeListCnt["output"] = outputList.length;
	    	}
	    	
	    	operators = {};
	    	links = {};
	    	
	    	$.init_operators("input", inputList);
	    	$.init_operators("filter", filterList, typeListCnt); //예외
	    	$.init_operators("output", outputList);
	    	
	    	$.init_links("input", inputList);
	    	$.init_links("output", outputList);
	    	
	    	data["operators"] = operators;
	    	data["links"] = links;
	    	
	    	$.make_flowChart();
	    }
	    
	    //typeListCnt 는 filter만 사용
	    $.init_operators = function(type, list, typeListCnt){
	    	
	    	var top = 0, left = 0;
	    	
	    	if(type == "input"){
	    		top = 50;
	    		left = 50;
	    	}else if(type == "filter"){
	    		top = 100;
	    		left = 350;
	    	}else if(type == "output"){
	    		top = 50;
	    		left = 650;
	    	}
	    	
	    	var operators_nm = "";
	    	var properties = {};
	    	var input = {}, output = {};
	    	var input_nm = "", output_nm = "";
	    	
	    	for(var i=0; i<list.length; i++){
	    		
	    		input = {};
	    		output = {};
	    		properties = {};
	    		
		    	if(type == "input" || type == "output"){
		    		top = 50 + (i * 120);
		    	}else if(type == "filter"){
		    		top = 100 + (i * 120);
		    	}
	    		
	    		operators_nm = "operator_"+type+"_"+list[i]+"_"+(i+1);
	    		
		    	operators[operators_nm] = {};
		    	operators[operators_nm]["top"] = top;
		    	operators[operators_nm]["left"] = left;
		    	
		    	if(type == "filter"){
		    		properties["title"] = "수집모듈";
		    	}else{
		    		properties["title"] = list[i];
		    	}
		    	
		    	if(type == "input"){
		    		output_nm = "output_"+list[i]+"_"+(i+1);
			    	output[output_nm] = {};
			    	output[output_nm]["label"] = "Output";
		    	}else if(type == "filter"){
		    		for(var j=0; j<typeListCnt["input"]; j++){
			    		input_nm = "input_"+list[i]+"_"+(j+1);
				    	input[input_nm] = {};
				    	input[input_nm]["label"] = "Input";
		    		}
		    		for(var j=0; j<typeListCnt["output"]; j++){
			    		output_nm = "output_"+list[i]+"_"+(j+1);
				    	output[output_nm] = {};
				    	output[output_nm]["label"] = "Output";
		    		}
		    	}else if(type == "output"){
		    		input_nm = "input_"+list[i]+"_"+(i+1);
			    	input[input_nm] = {};
			    	input[input_nm]["label"] = "Input";
		    	}
		    	
		    	properties["inputs"] = input;
		    	properties["outputs"] = output;
		    	
		    	operators[operators_nm]["properties"] = properties;
	    	}
	    }
	    
	    $.init_links = function(type, list){
	    	
	    	var links_nm = "", fromOperator = "", fromConnector = "", toOperator = "", toConnector = "";
	    	
	    	for(var i=0; i<list.length; i++){
	    		
	    		links_nm = "link_"+list[i]+"_"+(i+1);
	    		
	    		if(type == "input"){
		    		fromOperator = "operator_"+type+"_"+list[i]+"_"+(i+1);
	    			fromConnector = "output_"+list[i]+"_"+(i+1);
		    		toOperator = "operator_filter_logstash_1";
		    		toConnector = "input_logstash_"+(i+1);
	    		}else if(type == "output"){
	    			fromOperator = "operator_filter_logstash_1";
	    			fromConnector = "output_logstash_"+(i+1);
	    			toOperator = "operator_"+type+"_"+list[i]+"_"+(i+1);
	    			toConnector = "input_"+list[i]+"_"+(i+1);
	    		}
	    		
		    	links[links_nm] = {};
		    	links[links_nm]["fromOperator"] = fromOperator;
		    	links[links_nm]["fromConnector"] = fromConnector;
		    	links[links_nm]["toOperator"] = toOperator;
		    	links[links_nm]["toConnector"] = toConnector;
	    	}
	    }
	    
	    $.make_flowChart = function(){
	    	$('#flowDiv').flowchart({
	    		data: data,
	    		defaultLinkColor:'#3366ff',
	    		canUserEditLinks: false,
	    		canUserMoveOperators: false,
	    		onOperatorSelect: function(operatorId) {
	    			if(operatorId != ""){
		    			var id_array = operatorId.split("_");
		    			var form = $('<form action="/manager/writeUi" method="post"><input type="hidden" name="pageMove" value="'+id_array[1]+'" /></form>');
		    			$("#page-top").append(form);
		    			$(form).submit();
	    			}
	    		}
	    	});
	    }
	    
		$.init_flowChart_data();
	});
 })(jQuery);