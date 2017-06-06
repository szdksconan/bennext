<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {

		$('#roleAddForm').form({
			url : '${pageContext.request.contextPath}/role/add',
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
	
	function setxzzs(data,x){
		var reg1 = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
		if(!reg1.test(data.value)){
			parent.$.messager.alert('提示','请重新输入1到'+x+'位数字、汉字、字母','warning');
			
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;" >
		<form id="roleAddForm" method="post">
			<table class="grid">
				<tr>
					<td>角色名称</td>
					<td><input name="name" type="text" placeholder="请输入角色名称"  class="easyui-validatebox span2" data-options="required:true,validType:['length[1,32]','enterName']" value=""></td>
				</tr>
				<!-- <tr>
					<td>排序</td>
					<td><input name="seq" value="0" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false"></td>
				</tr> -->
				<tr>
					<td>描述</td>
					<td colspan="3"><textarea name="description" rows="5" cols="45" class="easyui-validatebox" data-options="validType:'length[1,255]'" ></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>