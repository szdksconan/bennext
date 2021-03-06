<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.accessList, '/access/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.accessList, '/access/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<title>资源管理</title>
<script type="text/javascript">
	var treeGrid;
	$(function() {
		
		treeGrid = $('#treeGrid').treegrid({
			url : '${ctx}/access/treeGrid',
			idField : 'id',
			treeField : 'name',
			parentField : 'pid',
			fit : true,
			fitColumns : false,
			border : false,
			frozenColumns : [ [ {
				title : '编号',
				field : 'id',
				width : 40
			} ] ],
			columns : [ [ {
				field : 'name',
				title : '资源名称',
				width : 200
			}, {
				field : 'url',
				title : '资源路径',
				width : 230
			}, {
				field : 'seq',
				title : '排序',
				width : 40
			}, {
				field : 'icon',
				title : '图标',
				width : 150
			}, {
				field : 'accessType',
				title : '资源类型',
				width : 80,
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						return '菜单';
					case 1:
						return '按钮';
					}
				}
			}, {
				field : 'pid',
				title : '上级资源ID',
				width : 150,
				hidden : true
			}, {
				field : 'action',
				title : '操作',
				width : 130,
				align : 'center',
				formatter : function(value, row, index) {
					var str = '';
					if ($.canEdit) {
						str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" ><span style="color:red">编辑</span></a>', row.id);
					}
					str += '&nbsp;|&nbsp;';
					if ($.canDelete) {
						str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" ><span style="color:red">删除</span></a>', row.id);
					} 
					return str;
				}
			} ] ],
			toolbar : '#toolbar'
		});
	});
	
	function editFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			parent.$.modalDialog({
				title : '编辑',
				width : 500,
				height : 350,
				href : '${ctx}/access/editPage?id=' + node.id,
				buttons : [ {
					text : '保存',
					handler : function() {
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#accessEditForm');
						f.submit();
					}
				} ,{
					text : '关闭',
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
			});
		}
	}
	
	function deleteFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			parent.$.messager.confirm('提示', '您是否要删除当前资源？删除当前资源会连同子资源一起删除!', function(b) {
				if (b) {
					progressLoad();
					$.post('${pageContext.request.contextPath}/access/delete', {
						id : node.id
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							treeGrid.treegrid('reload');
							parent.layout_west_tree.tree('reload');
						}
						progressClose();
					}, 'JSON');
				}
			});
		}
	}
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 500,
			height : 350,
			href : '${ctx}/access/addPage',
			buttons : [ {
				text : '保存',
				handler : function() {
					parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#accessAddForm');
					f.submit();
				}
			} ,{
				text : '关闭',
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	}
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false"  style="overflow: hidden;">
			<table id="treeGrid"></table>
		</div>
	</div>
	
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.accessList, '/access/add')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</c:if>
	</div>
</body>
</html>