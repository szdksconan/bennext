<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		$('#editUserPwdForm').form({
			url : '${ctx}/user/editUserPwd',
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
					parent.$.modalDialog.handler.dialog('close');
					parent.$.modalDialog.logout();
				} else {
					parent.$.messager.alert('提示', result.msg, 'info');
				}
			}
		});
	});
	
	function setxzcd(data,x){
		var reg1 = /^[A-Za-z0-9]+$/;
		if(!reg1.test(data.value)){
			parent.$.messager.alert('提示','请重新输入1到'+x+'位数字、字母','warning');
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<c:if test="${sessionInfo.name == null}">
			<div>登录已超时，请重新登录.</div>
			<script type="text/javascript" charset="utf-8">
				try {
					window.location.href='${ctx}/admin/index';
				} catch (e) {
				}
			</script>
		</c:if>
		<c:if test="${sessionInfo.name != null}">
			<form id="editUserPwdForm" method="post">
				<table class="grid">
					<tr>
						<td>登录名</td>
						<td>${sessionInfo.loginname}</td>
					</tr>
					<tr>
						<td>原密码</td>
						<td><input name="oldPwd" type="password" placeholder="请输入原密码" class="easyui-validatebox" data-options="required:true,validType:'length[1,32]'"></td>
					</tr>
					<tr>
						<td>新密码</td>
						<td><input name="pwd" type="password" placeholder="请输入新密码"  class="easyui-validatebox" data-options="required:true,validType:['length[1,32]','password']"></td>
					</tr>
					<tr>
						<td>重复密码</td>
						<td><input name="rePwd" type="password" placeholder="请再次输入新密码" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#editUserPwdForm input[name=pwd]\']'"></td>
					</tr>
				</table>
			</form>
		</c:if>
	</div>
</div>