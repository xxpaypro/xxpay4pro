layui.define(['admin', 'util', 'form', 'laydate'], function(exports){
  var $ = layui.$
  ,admin = layui.admin
  ,form = layui.form
  ,element = layui.element
  ,laydate = layui.laydate
  ,util = layui.util;
	
	var obj = {
		init: function(startStr,endStr){
		var timeDiv = '<div class="layui-input-inline">'+
	                  	'<select id="dataSelect" lay-filter="dateSelectFilter">'+
	                       '<option value="">全部时间</option>'+
	                       '<option value="today" selected="selected">今天</option>'+
	                       '<option value="yesterday">昨天</option>'+
	                       '<option value="lastweek">近一周</option>'+
	                       '<option value="select">自定义时间</option>'+
	                    '</select>'+
	                '</div>'+
	                '<div class="layui-input-inline hideDateInputDiv">'+
	                	'<input type="text" class="layui-input" name="createTimeStart" id="createTimeStart" readonly >'+
	                '</div>'+
	                '<div class="layui-input-inline hideDateInputDiv">'+
	                	'<input type="text" class="layui-input" name="createTimeEnd" id="createTimeEnd" readonly >'+
	                '</div>'+
	                '<div class="layui-input-inline layui-hide ctrlDateInputDiv">'+
	                   '<input type="text" id="ctrlTimeStart" readonly autocomplete="off" placeholder="起始时间" class="layui-input" >'+
	                '</div>'+
	                '<div class="layui-input-inline layui-hide ctrlDateInputDiv">'+
	                   '<input type="text" id="ctrlTimeEnd"  readonly autocomplete="off" placeholder="结束时间" class="layui-input">'+
	                '</div>';
	    $("#timeDiv").prepend(timeDiv); 
	    
	    //默认某天，传入str
	    typeof(startStr)=="undefined" ? startStr = "0" : startStr;
	    typeof(endStr)=="undefined" ? endStr = "0" : endStr;
		  $("#createTimeStart").val(getDateOffset(util, startStr, " 00:00:00"));
		  $("#createTimeEnd").val(getDateOffset(util, endStr, " 23:59:59"));
		  
		  $("#dataSelect").val("select");
		  if(startStr == "0" && endStr == "0"){$("#dataSelect").val("today");}
		  if(startStr == "-1" && endStr == "-1"){$("#dataSelect").val("yesterday");}
		  if(startStr == "-7" && endStr == "0"){$("#dataSelect").val("lastweek");}
		  
		  
		  form.on('select(dateSelectFilter)', function(data){
			  
			  $("#createTimeStart").val("");  //清空选择时间
			  $("#createTimeEnd").val("");
			  $("#ctrlTimeStart").val("");
			  $("#ctrlTimeEnd").val("");
			  
			  if(!data.value){ //全部时间
				  $(".hideDateInputDiv").addClass('layui-hide');   //只读时间 DIV 隐藏
				  $(".ctrlDateInputDiv").addClass('layui-hide');    //可选控件时间DIV  隐藏
				  return ;
			  }
			  
			  var isSelect = false; //判断是否为 用户自定义时间
			  
			  var startTime = "";
			  var endTime = "";
			  if(data.value == 'today'){
				  
				  startTime = getDateOffset(util, 0, " 00:00:00"); ///时间偏移 0 
				  endTime = getDateOffset(util, 0, " 23:59:59");
				
			  }else if(data.value == 'yesterday'){
				  
				  startTime = getDateOffset(util, -1, " 00:00:00"); // 
				  endTime = getDateOffset(util, -1, " 23:59:59");
				  
			  }else if(data.value == 'lastweek'){
				  
				  startTime = getDateOffset(util, -6, " 00:00:00"); // 
				  endTime = getDateOffset(util, 0, " 23:59:59");
				  
			  }else if(data.value == 'select'){
				  isSelect = true;
			  }
			  
			  if(!isSelect){ //只读时间  仅用作回显
				  $(".hideDateInputDiv").removeClass('layui-hide');
				  $(".ctrlDateInputDiv").addClass('layui-hide');
				  
				  $("#createTimeStart").val(startTime);
				  $("#createTimeEnd").val(endTime);
				  
			  }else{ //控件选择
				  $(".hideDateInputDiv").addClass('layui-hide'); 
				  $(".ctrlDateInputDiv").removeClass('layui-hide');
			  }
			  
		  });
		  
		  element.render('breadcrumb', 'breadcrumb');
		
		  laydate.render({
		    elem: '#ctrlTimeStart'
		    ,type: 'datetime'
		    ,format: 'yyyy-MM-dd HH:mm:ss'
		 	,done: function(value, date, endDate){
		 		   $("#createTimeStart").val(value);
		    }
		  });
		
		  laydate.render({
		    elem: '#ctrlTimeEnd'
		    ,type: 'datetime'
		    ,format: 'yyyy-MM-dd HH:mm:ss'
		    ,done: function(value, date, endDate){
		 		 $("#createTimeEnd").val(value);
		    }
		  });
		
		  // 渲染表单
		  form.render();
		}
	}
	//util工具， days=时间偏移     拼接字符串
	function getDateOffset(util, days, appendStr) {
		
		var thisTime = new Date() * 1; //时间戳
		var thisDateFormat  = "yyyy-MM-dd";
		
	    if(!days || days == 0){
	    	return util.toDateString(thisTime, thisDateFormat) + appendStr;
	    }
	    
	    thisTime = new Date(thisTime + (24 * 60 * 60 * 1000 * days ));
	    
	    return util.toDateString(thisTime, thisDateFormat) + appendStr;
	}
	
	exports('timePick',obj);
})