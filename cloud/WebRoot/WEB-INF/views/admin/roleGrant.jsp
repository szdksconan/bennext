<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	var accessTree;
	$(function() {
		accessTree = $('#accessTree').tree({
			url : '${ctx}/access/allTree?flag=true',
			parentField : 'pid',
			lines : true,
			checkbox : true,
			onClick : function(node) {
			},
			onLoadSuccess : function(node, data) {
				progressLoad();
				$.post( '${ctx}/role/get', {
					id : '${role.id}'
				}, function(result) {
					var ids;
					if (result.id != undefined&&result.accessIds!= undefined) {
						ids = $.stringToList(result.accessIds);
					}
					if (ids.length > 0) {
						for ( var i = 0; i < ids.length; i++) {
							//alert(ids[i]);
							//alert(accessTree.tree('find', ids[i]).children);
							var child = accessTree.tree('find', ids[i]).children;
							if(child == null || child == undefined){//当该节点没有子节点时设置选择状态
								accessTree.tree('check', accessTree.tree('find', ids[i]).target);
							}
							//var node = accessTree.tree('find', ids[i]);
							
							/*if (node != null) {
								
								var child = accessTree.tree('getChildren', accessTree.tree('find', ids[i]).target);
								if(null ==child || child.length <= 0){
									accessTree.tree('check', accessTree.tree('find', ids[i]).target);
								}
									
							}*/
						}
					}
				}, 'json');
				progressClose();
			},
			cascadeCheck : true
		});

		$('#roleGrantForm').form({
			url : '${ctx}/role/grant',
			onSubmit : function() {
				var dataType = $('input:radio[name="dataType"]:checked').val();
				$('#dataRightsType').val(dataType);
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				//var checknodes = accessTree.tree('getChecked');
				//获取被选中的节点 包括模糊不清楚的，及父节点的子节点未被选中时 也要包含父节点
				var checknodes = accessTree.tree('getChecked',['checked','indeterminate']);
				//alert(checknodes1)
				var ids = [];
				if (checknodes && checknodes.length > 0) {
					for ( var i = 0; i < checknodes.length; i++) {
						ids.push(checknodes[i].id);
					}
				}
				$('#accessIds').val(ids);
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('提示', result.msg, 'info');
				}
			}
		});
	});

	function checkAll() {
		var nodes = accessTree.tree('getChecked', 'unchecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				accessTree.tree('check', nodes[i].target);
			}
		}
	}
	function uncheckAll() {
		var nodes = accessTree.tree('getChecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				accessTree.tree('uncheck', nodes[i].target);
			}
		}
	}
	function checkInverse() {
		var unchecknodes = accessTree.tree('getChecked', 'unchecked');
		var checknodes = accessTree.tree('getChecked');
		if (unchecknodes && unchecknodes.length > 0) {
			for ( var i = 0; i < unchecknodes.length; i++) {
				accessTree.tree('check', unchecknodes[i].target);
			}
		}
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				accessTree.tree('uncheck', checknodes[i].target);
			}
		}
	}
</script>
<div id="roleGrantLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'west'" title="系统资源" style="width: 300px; padding: 1px;">
		<div class="well well-small">
			<form id="roleGrantForm" method="post">
				<input name="id" type="hidden"  value="${role.id}" readonly="readonly"/>
				<input id="dataRightsType" name="dataRightsType" type="hidden" />
				<ul id="accessTree"></ul>
				<input id="accessIds" name="accessIds" type="hidden" />
			</form>
		</div>
	</div>
	<div data-options="region:'center'" title="" style="overflow: hidden; padding: 10px;">
		<div>
			<button class="btn btn-success" onclick="checkAll();">全选</button>
			<br /> <br />
			<button class="btn btn-warning" onclick="checkInverse();">反选</button>
			<br /> <br />
			<button class="btn btn-inverse" onclick="uncheckAll();">取消</button>
			<HR style="border:3 double #6CAEF5" width="100%" color=#6CAEF5 SIZE=3>
			<span style="font-size:13px;"><b>数据访问权限</b></span>
			<br />
			<c:forEach items="${dataTypeList}" var="dataTypeList">
				<br />
				<br />
				<c:if test="${role.dataRightsType == dataTypeList.key}">
					<lable><input name="dataType" type="radio" value="${dataTypeList.key}" checked="checked"/>${dataTypeList.value}</lable>
				</c:if>
				<c:if test="${role.dataRightsType != dataTypeList.key}">
					<lable><input name="dataType" type="radio" value="${dataTypeList.key}" />${dataTypeList.value}</lable>
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>