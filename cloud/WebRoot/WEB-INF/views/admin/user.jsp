<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.accessList, '/user/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.accessList, '/user/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
	<script type="text/javascript">
	var dataGrid;
//	var departmentTree;
	$(function() {
		<%--departmentTree = $('#departmentTree').tree({--%>
			<%--url : '${ctx}/department/tree',--%>
			<%--parentField : 'pid',--%>
			<%--lines : true,--%>
			<%--onClick : function(node) {--%>
				<%--$("#dId").val(node.id);--%>
				<%--dataGrid.datagrid('load', {--%>
				    <%--departmentId: node.id--%>
				<%--});--%>
			<%--}--%>
		<%--});--%>
	
		$('#roleId').combotree({
			url : '${ctx}/role/tree',
			lines : true,
			panelHeight : 'auto'
		});
		
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/user/dataGrid',
			striped : true,
			rownumbers : true,
			pagination : true,
			idField : 'id',
			sortName : 'createTime',
			sortOrder : 'asc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [ [ {
				width : '50',
				field : 'id',
				checkbox:true
			}, {
				width : '80',
				title : '登录名',
				field : 'loginName',
				sortable : true
			}, {
				width : '80',
				title : '姓名',
				field : 'name',
				sortable : true,
				formatter : function(value, row, index) {
					str = $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" ><span style="color:red">' + value + '</span></a>', row.id);
					
					return str;
				}
			},{
				width : '80',
				title : '所属部门',
				field : 'departmentName'
			},{
				width : '80',
				title : '所属角色',
				field : 'roleName'
			},{
				width : '120',
				title : '工作区域',
				field : 'workAreaNames'
			}, {
				field : 'action',
				title : '操作',
				width : 100,
				formatter : function(value, row, index) {
					var str = '';
					if(row.isdefault!=0){
						if ($.canEdit) {
							str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" ><span style="color:red">编辑</span></a>', row.id);
						}
						str += '&nbsp;|&nbsp;';
						if ($.canDelete) {
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" ><span style="color:red">删除</span></a>', row.id);
						}
					}
					return str;
				}
			}] ],
			columns : [ [ {
				width : '120',
				title : '创建时间',
				field : 'createTime',
				sortable : true
			}, {
				width : '50',
				title : '员工号',
				field : 'userNumber',
				sortable : true
			}, {
				width : '100',
				title : '手机号',
				field : 'phoneNumber',
				sortable : true
			}, {
				width : '70',
				title : '职位',
				field : 'position',
				sortable : true
			}, {
				width : '120',
				title : '入职时间',
				field : 'entryDate',
				sortable : true
			}, {
				width : '50',
				title : '性别',
				field : 'sex',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						return '男';
					case 1:
						return '女';
					}
				}
			}, {
				width : '120',
				title : '邮箱',
				field : 'email',
				sortable : true
			}, {
				width : '80',
				title : '是否考勤',
				field : 'isCheckWork',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						return '是';
					case 1:
						return '否';
					}
				}
			}, {
				width : '80',
				title : '是否默认',
				field : 'isDefault',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						return '默认';
					case 1:
						return '否';
					}
				}
			},{
				width : '80',
				title : '是否启用',
				field : 'isEnabled',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						return '启用';
					case 1:
						return '停用';
					}
				}
			}] ],
			toolbar : '#toolbar'
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 600,
			height : 500,
			href : '${ctx}/user/addPage',
			buttons : [ {
				text : '保存',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userAddForm');
					f.submit();
				}
			},{
				text : '关闭',
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	}
	
	function inputFun() {
		parent.$.modalDialog.openner_dataGrid = dataGrid;
		parent.$.modalDialog({
			title : '导入员工',
			width : 400,
			height : 100,
			href : '${ctx}/user/inputPage',
			/*buttons : [ {
				text : '导入员工',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userInputForm');
					f.submit();
				}
			} ]*/
		});
	}

	
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('提示', '您是否要删除当前用户？', function(b) {
			if (b) {
				var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
				if (currentUserId != id) {
					progressLoad();
					$.post('${ctx}/user/delete', {
						id : id
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}else{
							parent.$.messager.alert('提示', result.msg, 'info');
						}
						progressClose();
					}, 'JSON');
				} else {
					parent.$.messager.show({
						title : '提示',
						msg : '不可以删除自己！'
					});
				}
			}
		});
	}
	
	function batchUsersFun(batchType) {
		var checknodes = dataGrid.datagrid('getChecked');
		var userIds = [];
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				userIds.push(checknodes[i].id);
			}
		}
		if(userIds.length > 0){
			if(batchType == '0'){
				parent.$.messager.confirm('提示', '您是否要删除已选中用户？', function(b) {
					if (b) {
						var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
						var isTrue = true;
						if (checknodes && checknodes.length > 0) {
							for ( var i = 0; i < checknodes.length; i++) {
								if(currentUserId == checknodes[i].id){
									isTrue = false;
								}
							}
						}
						if (isTrue) {
							progressLoad();
							$.post('${ctx}/user/batchUsers', {
								userIds : userIds+"",
								batchType : batchType
							}, function(result) {
								if (result.success) {
									parent.$.messager.alert('提示', result.msg, 'info');
									dataGrid.datagrid('reload');
								}else{
									parent.$.messager.alert('提示', result.msg, 'info');
								}
								progressClose();
							}, 'JSON');
						} else {
							parent.$.messager.show({
								title : '提示',
								msg : '不可以删除自己！'
							});
						}
					}
				});
			}else{
				var str = "";
				if(batchType == '1'){
					str = "您是否要批量停用已选中用户？";
				}else{
					str = "您是否要批量启用已选中用户？";
				}
				parent.$.messager.confirm('提示', str, function(b) {
					progressLoad();
					$.post('${ctx}/user/batchUsers', {
						userIds : userIds+"",
						batchType : batchType
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}else{
							parent.$.messager.alert('提示', result.msg, 'info');
						}
						progressClose();
					}, 'JSON');
				});
			}
		}else{
			parent.$.messager.alert('提示', '请至少选中一个后进行操作', 'info');
		}
	}
	
	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 500,
			height : 500,
			href : '${ctx}/user/editPage?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userEditForm');
					f.submit();
				}
			},{
				text : '关闭',
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			}  ]
		});
	}
	
	
	
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
	function downFun(){
		var data = dataGrid.datagrid('getData');
		var total = data.total;
		if(total > 0){
			$("#temp").val("");
			$('#searchForm').submit();
		}
		else{
			parent.$.messager.alert('提示', '您选择的部门没有员工,无法导出!', 'info');
		}
		
	}
	function downTemplate(){
		$("#temp").val("yes");
		$('#searchForm').submit();
	}
	
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 35px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm" method="post" action="${ctx}/user/userDown">
			<table>
				<tr>
					<th>姓名:</th>
					<td><input id="temp" name="template" type="hidden"/>
					<input name="name" placeholder="请输入用户姓名"/></td>
					<!--  <th>创建时间:</th>
					<td>
					<input id="timeStart" name="createTimeStart" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />至<input id="timeEnd" name="createTimeEnd" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
					</td>
					-->
					<th>登录名:</th>
					<td>
						<input name="loginName" placeholder="请输入登录名"/>
					</td>
					<th>手机号:</th>
					<td>
						<input name="phoneNumber" placeholder="请输入手机号"/>
					</td>
					<th>邮箱:</th>
					<td>
						<input name="email" placeholder="请输入邮箱"/>
					</td>
					<th>角色:</th>
					<td>
						<input  id="roleId" name="roleId" style="width: 140px; height: 29px;" class="easyui-combobox" />
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'用户列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<%--<div data-options="region:'west',border:false,split:true,title:'部门列表'"  style="width:200px;overflow: hidden; ">--%>
		<%--<ul id="departmentTree"  style="width:180px;margin: 10px 10px 10px 10px">--%>
		<%--</ul>--%>
	<%--</div>--%>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.accessList, '/user/add')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.accessList, '/user/batchdelete')}">
			<a onclick="batchUsersFun('0');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_sys'">批量删除</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.accessList, '/user/batchdisable')}">
			<a onclick="batchUsersFun('1');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_sys'">批量停用</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.accessList, '/user/batchenable')}">
			<a onclick="batchUsersFun('2');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_sys'">批量启用</a>
		</c:if>
		<c:if test="${fn:contains(sessionInfo.accessList, '/user/userInput')}">
			<a onclick="inputFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_input'">导入员工</a>
		</c:if>
		<a onclick="downFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_down'">导出员工</a>
		<a onclick="downTemplate();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_down'">下载导入模版</a>
	</div>
</body>
</html>