/**
 * 组装表单属性值
 * @param frame 表单所在frame对象
 * @param tableId 列表id
 * @returns {String}json数组字符串
 */
function getProperty(frame,tableId){
	//var grid=window.frames["propertyGrid"].$('#billGrid');
	var grid=frame.$('#'+tableId);
	var list='[';
	grid.find('tr').each(function(){
		if ($(this).index()!=0 && $(this).children().eq(2).attr('propertyName') && $(this).children().eq(2).attr('propertyName')!=''){
			var td = $(this).children().eq(2);
			var obj='';
			var id=td.attr('id').split('td')[1];
			var configName = td.attr('propertyName');
			obj+='{"id":'+id+',"name":"'+configName+'"';
			var type=td.attr('propertyType');
			switch(type){
			case '1':
				obj+=',"value":"'+($(frame.document).find('input[name="radio'+id+'"]:checked').val()||'');
				break;
			case '2':
				var val = '';
				$(frame.document).find('input[name="check'+id+'"]:checked').each(function(){ 
					val += $(this).val()+","; 
				});
				if(val.length>0){
					val = val.substr(0,val.length-1);
				}
				obj+=',"value":"'+val||'';
				break;
			case '0':
			case '3':
			case '4':
			case '6':
			case '8':
				obj+=',"value":"'+(td[0].firstChild.value||'');
				break;
			case '5':
				value = ($(frame.document).find('input[id="file'+id+'"]').val()||'');
				obj+=',"value":"';
				if(value!=null&&value!=""){
					arrayid.push(id);
				}
				break;
			case '7':
				obj+=',"value":"'+($(frame.document).find('select[id="select'+id+'"]').val()||'');
				break;
//			case '8':
//				obj+=',"value":"'+($(frame.document).find('select[id="auto'+id+'"]').val()||'');
//				break;
			}
			obj+='","type":"'+type+'"},';
			list+=obj;
		}
	});
	if(list.length>1){
		list = list.substring(0, list.length-1);
	}
	list+=']';
	return list;
}

/**
 * 验证必填项是否填值
 * @param frame 表单所在frame对象
 * @param tableId 列表id
 */
function isPropertyValid(frame,tableId){
	var hasEdit = frame.$('#hasEdit').val();
	var isAdd = frame.$('#isAdd').val();
	if(hasEdit==0&&isAdd==0){
		return "属性值未填写！";
	}
	if(isAdd==1&&hasEdit==0){
		return "";
	}
	var grid=frame.$('#'+tableId);
	var retMsg = "";
	var validMsg = "";
	grid.find('tr').each(function(){
		if ($(this).index()!=0 && $(this).children().eq(2).attr('propertyName') && $(this).children().eq(2).attr('propertyName')!=''){
			var td = $(this).children().eq(2);
			var id=td.attr('id').split('td')[1];
			var isNotNull = td.attr('propertyNotNull');
			var configName = td.attr('propertyName');
			var type=td.attr('propertyType');
			var value = "";
			switch(type){
			case '1':
				value = ($(frame.document).find('input[name="radio'+id+'"]:checked').val()||'');
				if(isNotNull==0&&(value==null||value=="")){
					retMsg += "["+configName+"],";
				}
				break;
			case '2':
				var val = '';
				$(frame.document).find('input[name="check'+id+'"]:checked').each(function(){ 
					val += $(this).val()+","; 
				});
				if(val.length>0){
					val = val.substr(0,val.length-1);
				}
				value = val;
				if(isNotNull==0&&(value==null||value=="")){
					retMsg += "["+configName+"],";
				}
				break;
			case '6':
				value = (td[0].firstChild.value||'');
				if(isNotNull==0&&(value==null||value=="")){
					retMsg += "["+configName+"],";
				}else{
					var reg = new RegExp("^[0-9]*$");
					if(!reg.test(value)){  
						validMsg = "["+configName+"],请输入数字!";
					}
				}
				break;
			case '0':
			case '3':
			case '4':
			case '8':
				value = (td[0].firstChild.value||'');
				if(isNotNull==0&&(value==null||value=="")){
					retMsg += "["+configName+"],";
				}
				break;
			case '5':
				value = ($(frame.document).find('input[id="file'+id+'"]').val()||'');
				if(isNotNull==0&&(value==null||value=="")){
					retMsg += "["+configName+"],";
				}
				break;
			case '7':
				value = ($(frame.document).find('select[id="select'+id+'"]').val()||'');
				if(isNotNull==0&&(value==null||value=="")){
					retMsg += "["+configName+"],";
				}
				break;
			}
		}
	});
	if(retMsg=="" && validMsg==""){
		return "";
	}else{
		if(retMsg==""){
			return validMsg;
		}else{
			return '以下项目'+retMsg+'不能为空！'+validMsg;
		}
	}
}