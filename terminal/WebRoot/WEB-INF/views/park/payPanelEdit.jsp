<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">

	$(function() {
		$('#payPanelEditForm').form({
			url : '${ctx}/park/payPanelEdit',
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
	<div data-options="region:'center',border:false" title="" style="overflow:scroll;padding: 3px;">
		<form id="payPanelEditForm" method="post">
			<!-- <div class="light-info" style="overflow:hidden;padding: 3px;">
				<div>密码不可修改</div>
			</div> -->
			<table class="grid">
				<tr>
					<td>面板名称</td>
					<td><input name="id" type="hidden" value="${payPanel.id}"/>
						<input id="payPanelName" name="payPanelName" type="text" value="${payPanel.payPanelName}"  class="easyui-validatebox" data-options="required:true" /></td>
					<td>面板编号</td>
					<td><input id="payPanelCode" name="payPanelCode" type="text" value="${payPanel.payPanelCode}"  class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>IP</td>
					<td><input id="ip" name="ip" type="text" value="${payPanel.ip}"  class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>