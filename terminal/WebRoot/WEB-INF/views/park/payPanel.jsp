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
	<title>面板管理</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/park/payPanelInfo',
				striped : true,
				rownumbers : true,
				pagination : true,
				idField : 'id',
				toolbar : '#toolbar',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
				columns : [ [ {
					width : '150',
					title : '面板名称',
					field : 'payPanelName'
				},{
					width : '150',
					title : '面板编号',
					field : 'payPanelCode'
				},{
					width : '200',
					title : 'IP',
					field : 'ip'
				},{
					width : '150',
					title : '创建时间',
					field : 'createTime'
				},{
					width : '150',
					title : '更新时间',
					field : 'updateTime'
				},{
					field : 'action',
					title : '操作',
					width : 100,
					formatter : function(value, row, index) {
						var str = '';
						if(row.isdefault!=0){
								str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" ><span style="color:red">编辑</span></a>', row.id);

							str += '&nbsp;|&nbsp;';
								str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" ><span style="color:red">删除</span></a>', row.id);

						}
						return str;
					}
				}] ]
			});
		});

		function addFun() {
			parent.$.modalDialog({
				title : '添加',
				width : 600,
				height : 200,
				href : '${ctx}/park/toPayPanelAdd',
				buttons : [ {
					text : '保存',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#payPanelAddForm');
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
			parent.$.messager.confirm('提示', '您是否要删除当前面板？', function(b) {
				if (b) {
						progressLoad();
						$.post('${ctx}/park/payPanelDel', {
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
				height : 200,
				href : '${ctx}/park/toPayPanelEdit?id=' + id,
				buttons : [ {
					text : '保存',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#payPanelEditForm');
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
<div data-options="region:'center',border:false,title:'面板列表'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
</div>
</body>
</html>