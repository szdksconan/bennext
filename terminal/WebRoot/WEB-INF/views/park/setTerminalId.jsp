<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../inc.jsp"></jsp:include>
	<meta http-equiv="X-UA-Compatible" content="edge"/>
	<title>停车场管理</title>
	<script type="text/javascript">
		$(function() {
			$('#terminalIdForm').form({
				url : '${ctx}/terminalController/saveTerminalId',
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
						parent.$.messager.alert('提示', result.msg, 'info');
					} else {
						parent.$.messager.alert('提示', result.msg, 'info');
					}
				}
			});

			$('#btn').click(function () {
				$('#terminalIdForm').submit();
			});
		});
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow:scroll;padding: 3px;">
			<div data-options="region:'north',border:false" style="height: 120px; overflow: hidden;background-color: #f4f4f4">
				<form id="terminalIdForm" method="post">
					<input type="hidden" value="${dic.key}" name="key"/>
						<table class="grid" style="width: 50%">
							<tr>
								<td style="text-align: right">终端编号：</td>
								<td>
									<input id="value" name="value" type="text" value="${dic.value}"
										   class="easyui-validatebox" data-options="required:true"/>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: center">
									<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
								</td>
							</tr>
						</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>