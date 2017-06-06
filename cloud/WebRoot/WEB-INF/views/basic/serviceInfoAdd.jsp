<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {

		$('#exitWayAddForm').form({
			url : '${ctx}/serviceInfo/serviceInfoAdd',
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

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="新增" style="overflow:scroll;padding: 3px;">
		<form id="exitWayAddForm" method="post">
			<table class="grid">
				<tr>
					<td width="60">商户号</td>
					<td><input id="clientId" name="clientId" type="text"  class="easyui-validatebox" data-options="required:true" /></td>
					<td width="60">商户名称</td>
					<td><input id="name" name="name" type="text"  class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<td width="60">终端号</td>
					<td><input id="terminalCode" name="terminalCode" type="text"  class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>