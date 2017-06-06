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
			onSelect:function(node){
				if(node.id == 1){
					//parent.$.messager.alert('提示', '更改之后，之前的考勤暂时无法查找！', 'info');
					$("#attSystem").css('display','none');
					$('#attSystemId').combotree('clear');
				}else{
					$("#attSystem").css('display','');
				}
	        }
		});*/

		/*$('#departmentId').combotree({
			url : '\${ctx}/department/tree',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			onLoadSuccess : function(){
				$("#departmentId").combotree("setValue",'\${depId}');
			}

		});*/
		
		$('#roleId').combotree({
		    url: '${ctx}/role/tree',
		    lines : true,
		    panelHeight : 'auto'
		});
		
		<%--$('#workAreaIds').combotree({--%>
		    <%--url: '${ctx}/workArea/tree',--%>
		    <%--parentField : 'pid',--%>
		    <%--lines : true,--%>
		    <%--multiple: true,--%>
		    <%--required: true,--%>
		    <%--panelHeight : 'auto',--%>
		    <%--onCheck : function(item){--%>
		    	<%--setWorkAreaShowText();--%>
		    <%--},--%>
		    <%--onClick : function(node){--%>
		    	<%--setWorkAreaShowText();--%>
		    <%--}--%>
		<%--});--%>
		
		<%--$('#attSystemId').combotree({--%>
			<%--url : '${ctx}/attSystem/tree',--%>
			<%--lines : true,--%>
			<%--panelHeight : 'auto',--%>
		<%--});--%>
		
		$('#userAddForm').form({
			url : '${ctx}/user/add',
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
	
	/*function addAreaFun() {
		parent.$.modalDialogTwo({
			title : '添加区域',
			width : 300,
			height : 150,
			href : '\${ctx}/workArea/addPage',
			buttons : [ {
				text : '保存',
				handler : function() {
					//parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialogTwo.handler.find('#workAreaAddForm');
					var workareaProvinceName = parent.$.modalDialogTwo.handler.find('input[name="workareaProvinceName"]').val();
					var pid = parent.$.modalDialogTwo.handler.find('#pid').combotree('getValue');
					$.ajax({
							url : '\${ctx}/workArea/add',
							data:{pid:pid,workareaProvinceName:workareaProvinceName},
							success: function(result){
							 result = $.parseJSON(result);
						  	 if(result.success){
						  		parent.$.messager.alert('提示','区域保存成功', 'info');
						  		parent.$.modalDialogTwo.handler.dialog('close');
								$('#workAreaIds').combotree('reload');
						  	 }
						  	 else{
						  		parent.$.messager.alert('提示','区域保存失败', 'info');

						  	 }
						  }
						});
				}
			} ,{
				text : '关闭',
				handler : function() {

					parent.$.modalDialogTwo.handler.dialog('close');
				}
			}]
		});
	}*/
	
	function getPwd(){
		$.ajax({
		  url: "${ctx}/user/getPwd",
		  success: function(result){
			 result = $.parseJSON(result);
		  	 $("#pwd").val(result.msg);
		  }
		});
	}
	function setlname(data){
		var elStr = $("#el").val();
		var idx = elStr.indexOf('@');
		elStr = elStr.substring(0,idx);
		$("#lname").val(elStr);
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow:scroll;padding: 3px;">
		<form id="userAddForm" method="post">
			<table class="grid">
				<tr>
					<td>登录名</td>
					<td><input id="lname" name="loginName" type="text" placeholder="默认为邮箱前缀" readonly="readonly" class="easyui-validatebox" style="background:transparent;border:0" value=""></td>
					<td>邮箱</td>
					<td><input id="el" name="email" type="text" placeholder="请输入邮箱" class="easyui-validatebox" data-options="required:true,validType:'emailx'" onchange="setlname(this);" /></td>
				</tr>
				<tr>
					<td>密码</td>
					<td colspan="3"><input id="pwd" name="loginPwd" type="text" placeholder="请输入密码" class="easyui-validatebox" data-options="required:true,validType:['length[1,32]','nonChinese','password']" />
					<a class="easyui-linkbutton" href="javascript:void(0)" onclick="getPwd();">生成密码</a></td>
					</td>
				</tr>
				<tr>
					<td>姓名</td>
					<td><input name="name" type="text" placeholder="请输入姓名" class="easyui-validatebox" data-options="required:true,validType:['notSpace','length[1,32]']" value=""></td>
					<td>员工号</td>
					<td>
						<!--  <input name="userNumber" type="text" placeholder="请输入员工号" class="easyui-validatebox" data-options="required:true,validType:'length[1,30]'" value="">-->
						<label style="color:red;">保存后自动生成</label>
					</td>
				</tr>
				<tr>
					<td>手机号</td>
					<td><input name="phoneNumber" type="text" placeholder="请输入手机号" class="easyui-validatebox" data-options="required:true,validType:'mobile'"></td>
					<td>性别</td>
					<td><select name="sex" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${sexList}" var="sexList">
							<option value="${sexList.key}" >${sexList.value}</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>职位</td>
					<td><input name="position" type="text" placeholder="请输入职位" class="easyui-validatebox" data-options="required:true,validType:'length[1,30]'"></td>
					<td>是否启用</td>
					<td><select name="isEnabled" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
						<c:forEach items="${enabledlist}" var="enabledlist">
							<option value="${enabledlist.key}" >${enabledlist.value}</option>
						</c:forEach>
					</select></td>
				</tr>
				<tr>
					<%--<td>部门</td>--%>
					<%--<td><select id="departmentId" name="departmentId" style="width: 140px; height: 29px;" class="easyui-combobox" data-options="required:true"></select></td>--%>
					<td>入职日期</td>
					<td><input name="entryDate" placeholder="点击选择入职时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="easyui-validatebox" data-options="required:true" readonly="readonly" /></td>
					<td>角色</td>
					<td><select id="roleId"  name="roleId" style="width: 140px; height: 29px;" class="easyui-combobox" data-options="required:true"></select></td>
				</tr>
				<%--<tr>--%>
					<%--<td>区域设置</td>--%>
					<%--<td><select id="workAreaIds" name="workAreaIds" style="width: 140px; height: 29px;" class="easyui-combobox" data-options="required:true"></select>--%>
						<%--&nbsp;<a onclick="addAreaFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加区域</a>--%>
					<%--</td>--%>
				<%--</tr>--%>
				<%--<tr>--%>
					<%--<td>是否考勤</td>--%>
					<%--<td colspan="3"><select id="isCheckWork" name="isCheckWork" class="easyui-combobox"  style="width: 150px; height: 25px;" data-options="required:true"></select>--%>
				<%--</tr>--%>
				<%--<tr id="attSystem" style="display:none">--%>
					<%--<td>考勤制度</td>--%>
					<%--<td colspan="3"><select id="attSystemId"  name="attSystemId" style="width: 150px; height: 25px;" class="easyui-combobox"></select>--%>
					<%--</td>--%>
				<%--</tr>--%>
				<tr>
					<td>描述</td>
					<td colspan="3"><textarea rows="4" cols="50" placeholder="请输入描述" name="description" class="textarea easyui-validatebox" data-options="required:true,validType:'length[1,255]'"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>