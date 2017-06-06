<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	/*function setWorkAreaShowText(){
		var text = '';
		var tree = $('#workAreaIds').combotree('tree'); // 得到树对象 
		var nodes = tree.tree('getChecked');	// 获取选中的所有节点数组
		//遍历每个节点
		for(var i=0;i<nodes.length;i++){
			//获取该节点的父节点
			var parent = tree.tree('getParent',nodes[i].target);
			//如果所选择节点父节点为空，就表示根节点被选中，那么此时文本框就只展示根节点
			if(parent == null || parent == undefined || parent == 'undefined')
			{
				text = text + nodes[i].text + ",";
			}
			//如果所选择节点父节点存在
			else{
				var parentIsExist = false;//该节点的父节点是否被选中
				//判断该节点的父节点是否被选中
				for(var j=0;j<nodes.length;j++){
					if(parent.id == nodes[j].id){
						//如果该节点的父节点已经存在则设置标识为true
						parentIsExist = true;
						//销毁该节点id值存放的input元素
					//	$("input[name='workAreaIds'][value='" + nodes[i].id + "']").remove();
						break;
					}
					
				}
				//当该节点的父节点未选中时才展示该节点内容到文本框中
				if(!parentIsExist){
					text = text + nodes[i].text + ",";
				}
			}
		}
		if(text.length > 0)
			text = text.substr(0,text.length - 1);
		$("#workAreaIds").combotree('setText',text);
	}*/
	
	$(function() {
		/*$('#isCheckWork').combotree({
			url : '\${ctx}/user/isOrNotTree',
			lines : true,
			panelHeight : 'auto',
			value : '\${user.isCheckWork}',
			onClick:function(node){
				if(node.id == 1 && node.id != '\${user.isCheckWork}'){
					parent.$.messager.alert('提示', '更改之后，之前的考勤暂时无法查找！', 'info');
					
					$("#attSystem").css('display','none');
					$('#attSystemId').combotree('clear');
				}else if(node.id == 1){
					$("#attSystem").css('display','none');
					$('#attSystemId').combotree('clear');
				}else{
					$("#attSystem").css('display','');
				}
	        },
	        onLoadSuccess : function(){
	        	$('#isCheckWork').combotree("setValue",'\${user.isCheckWork}');
	        }
		});*/
		
		<%--$('#departmentId').combotree({--%>
			<%--url : '${ctx}/department/tree',--%>
			<%--parentField : 'pid',--%>
			<%--lines : true,--%>
			<%--panelHeight : 'auto',--%>
			<%--value : '${user.departmentId}',--%>
	        <%--onLoadSuccess : function(){--%>
	        	<%--$('#departmentId').combotree("setValue",'${user.departmentId}');--%>
	        <%--}--%>
		<%--});--%>
		
		$('#roleId').combotree({
			url : '${ctx}/role/tree',
			lines : true,
			panelHeight : 'auto',
			value : '${user.roleId}'
		});
		
		<%--$('#attSystemId').combotree({--%>
			<%--url : '${ctx}/attSystem/tree',--%>
			<%--lines : true,--%>
			<%--panelHeight : 'auto',--%>
			<%--value :'${user.attSystemId}',--%>
			<%--onClick:function(node){--%>
				<%--if(null != '${user.attSystemId}' && '' != '${user.attSystemId}' && node.id != '${user.attSystemId}'){--%>
					<%--parent.$.messager.alert('提示', '更改之后，之前的考勤暂时无法查找！', 'info');--%>
				<%--}--%>
	        <%--}--%>
		<%--});--%>
		
		/*$('#workAreaIds').combotree({
			url: '\${ctx}/workArea/tree',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			multiple : true,
			required: true,
			//value : $.stringToList('\${user.workAreaIds}'),
	        onLoadSuccess : function(){
	        	var idArray;
	        	//编辑页面初始化默认值
				var ids = "\${user.workAreaIds}";
				if(null != ids && "" != ids){
					idArray = ids.split(",");
				}
				//循环遍历树节点，设置初始默认值
				if(null != idArray && idArray.length > 0){
					var tree = $("#workAreaIds").combotree('tree');
					for ( var i = 0; i < idArray.length; i++) {
						if (tree.tree('find', idArray[i])) {
							tree.tree('check', tree.tree('find', idArray[i]).target);
						}
					}
				}
	        },
	        onCheck : function(item){
		    	setWorkAreaShowText();
		    },
		    
		    onClick : function(node){
		    	setWorkAreaShowText();
		    }
		});*/
		
		$('#userEditForm').form({
			url : '${ctx}/user/edit',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
					parent.$.messager.alert('提示', result.msg, 'info');
				} else {
					parent.$.messager.alert('提示', result.msg, 'info');
				}
			}
		});
	});
	
	function setlname(data){
		var elStr = $("#el").val();
		var idx = elStr.indexOf('@');
		elStr = elStr.substring(0,idx);
		$("#lname").val(elStr);
	}
	function getPwd(){
		$.ajax({
		  url: "${ctx}/user/getPwd",
		  success: function(result){
			 result = $.parseJSON(result);
		  	 $("#pwd").val(result.msg);
		  }
		});
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow:scroll;padding: 3px;">
		<form id="userEditForm" method="post">
			<!-- <div class="light-info" style="overflow:hidden;padding: 3px;">
				<div>密码不可修改</div>
			</div> -->
			<table class="grid">
				<tr>
					<td>登录名</td>
					<td><input name="id" type="hidden" value="${user.id}"/>
					<input id="lname" name="loginName" type="text" placeholder="默认为邮箱前缀" readonly="readonly" class="easyui-validatebox" style="background:transparent;border:0" data-options="required:true" value="${user.loginName}" /></td>
					<td>邮箱</td>
					<td><input id="el" name="email" type="text" placeholder="请输入邮箱" class="easyui-validatebox" data-options="required:true,validType:'emailx'" value="${user.email}" onchange="setlname(this);"/></td>
				</tr>
				<tr>
					<td>密码</td>
					<td colspan="3">
						<input name="loginPwd" type="text" id="pwd" placeholder="请输入密码" class="easyui-validatebox" data-options="required:true,validType:['password']" value="${user.loginPwd }">
					<a class="easyui-linkbutton" href="javascript:void(0)" onclick="getPwd();">重置密码</a></td>
					</td>
					</td>
				</tr>
				<tr>
					<td>姓名</td>
					<td><input name="name" type="text" placeholder="请输入姓名" class="easyui-validatebox" data-options="required:true,validType:['notSpace','length[1,32]']" value="${user.name}"></td>
					<td>员工号</td>
					<td><input name="userNumber" type="text" placeholder="请输入登录名称" class="easyui-validatebox" data-options="required:true,validType:'length[1,30]'" readonly="readonly" value="${user.userNumber}"></td>
				</tr>
				<tr>
					<td>手机号</td>
					<td><input name="phoneNumber" type="text" placeholder="请输入手机号" class="easyui-validatebox" data-options="required:true,validType:'length[1,30]'" readonly="readonly" value="${user.phoneNumber }"></td>
					<td>性别</td>
					<td><select name="sex" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${sexList}" var="sexList">
							<option value="${sexList.key}" <c:if test="${sexList.key == user.sex}">selected="selected"</c:if>>${sexList.value}</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>职位</td>
					<td><input name="position" type="text" placeholder="请输入职位" class="easyui-validatebox" data-options="required:true,validType:'length[1,30]'" value="${user.position}"></td>
					<td>是否启用</td>
					<td><select name="isEnabled" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${enabledlist}" var="enabledlist">
							<option value="${enabledlist.key}" <c:if test="${enabledlist.key == user.isEnabled}">selected="selected"</c:if>>${enabledlist.value}</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<%--<td>部门</td>--%>
					<%--<td><select id="departmentId" name="departmentId" style="width: 140px; height: 29px;" class="easyui-combobox" data-options="required:true"></select></td>--%>
					<td>入职日期</td>
					<td><input name="entryDate" placeholder="点击选择入职时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" value="${user.entryDate}" /></td>
					<td>角色</td>
					<td><input  id="roleId" name="roleId" style="width: 140px; height: 29px;" class="easyui-combobox" data-options="required:true"/></td>
				</tr>
				<%--<tr>--%>
					<%--<td>区域设置</td>--%>
					<%--<td><select id="workAreaIds" name="workAreaIds" style="width: 140px; height: 29px;" class="easyui-combobox" data-options="required:true"></select></td>--%>
					<%--</select></td>--%>
				<%--</tr>--%>
				<%--<tr>--%>
					<%--<td>是否考勤</td>--%>
					<%--<td colspan="3"><select id="isCheckWork" name="isCheckWork" class="easyui-combobox"  style="width: 150px; height: 25px;" data-options="required:true"></select>--%>
				<%--</tr>--%>
				<%--<c:if test="${user.isCheckWork == 1 }">--%>
					<%--<tr id="attSystem" style="display:none">--%>
						<%--<td>考勤制度</td>--%>
						<%--<td colspan="3"><select id="attSystemId"  name="attSystemId" style="width: 150px; height: 25px;" class="easyui-combobox" ></select>--%>
						<%--</td>--%>
					<%--</tr>--%>
				<%--</c:if>--%>
				<%--<c:if test="${user.isCheckWork == 0 }">--%>
					<%--<tr id="attSystem">--%>
						<%--<td>考勤制度</td>--%>
						<%--<td colspan="3"><select id="attSystemId"  name="attSystemId" style="width: 150px; height: 25px;" class="easyui-validatebox" ></select>--%>
						<%--</td>--%>
					<%--</tr>--%>
				<%--</c:if>--%>
				<tr>
					<td>描述</td>
					<td colspan="3"><textarea rows="4" cols="50" name="description" class="textarea easyui-validatebox" data-options="required:true,validType:'length[1,255]'">${user.description }</textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>