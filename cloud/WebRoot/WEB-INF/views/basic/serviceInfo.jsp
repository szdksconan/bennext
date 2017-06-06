<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>终端列表</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/serviceInfo/getServiceInfo',
				striped : true,
				rownumbers : true,
				pagination : true,
				toolbar : '#toolbar',
				idField : 'id',
				pageSize : 20,
				pageList : [  20, 30, 40, 50],
				columns : [ [ {
					width : '150',
					title : '商户号',
					field : 'clientId'
				},{
					width : '150',
					title : '商户名称',
					field : 'name'
				},{
					width : '150',
					title : '终端号',
					field : 'terminalCode'
				},{
					field : 'stat',
					width : '150',
					title : '连接状态',
					formatter : function(value, row, index) {
						var str = '';
						if(value==1) {
							str += $.formatString('<span style="color:green">连接正常</span>');
						}else if(value==2){
							str+= $.formatString('<span style="color:red">连接超时</span>');
						}else{
							str+= $.formatString('<span style="color:red">未检测到连接</span>');
						}
						return str;
					}
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


		setTimeout(function(){
			Push();
		},1000);

		setInterval(function(){
			Push();
		},30000);

		function Push(){
			$.post('${ctx}/serviceInfo/intervalCheck',{}, function(result) {
				if (result.success) {
				}else{
					$.messager.show({ title: '连接异常通知',
						msg:result.msg,timeout:20000,height:200});
				}
			}, 'JSON');
		}


		function deleteFun(id) {
			if (id == undefined) {//点击右键菜单才会触发这个
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {//点击操作里面的删除图标会触发这个
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			parent.$.messager.confirm('提示', '您是否要删除当前出口信息？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/serviceInfo/serviceInfoDel', {
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

		function editFun(id) {
			if (id == undefined) {
				var rows = dataGrid.datagrid('getSelections');
				id = rows[0].id;
			} else {
				dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
			}
			parent.$.modalDialog({
				title : '编辑',
				width : 650,
				height : 200,
				href : '${ctx}/serviceInfo/toServiceInfoEdit?id=' + id,
				buttons : [ {
					text : '保存',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#serviceInfoForm');
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


		function addFun() {
			parent.$.modalDialog({
				title : '添加',
				width : 650,
				height : 200,
				href : '${ctx}/serviceInfo/toServiceInfoAdd',
				buttons : [ {
					text : '保存',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#exitWayAddForm');
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

		function searchFun() {
			dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
		}
		function cleanFun() {
			$('#searchForm input').val('');
			dataGrid.datagrid('load', {});
		}
		function downFun(){
				$('#searchForm').submit();
		}

	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'north',border:false" style="height: 35px; overflow: hidden;background-color: #f4f4f4">
	<form id="searchForm" method="post" action="${ctx}/rights/exportBill">
		<table>
			<tr>
				<th>商户名称:</th>
				<td>
					<input name="name" placeholder="输入名称"   />
				</td>
				<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'center',border:false,title:'监测列表'" >
	<table id="dataGrid" data-options="fit:true,border:false">
		<div id="toolbar" style="display: none;">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</div>
	</table>
</div>
</body>
</html>