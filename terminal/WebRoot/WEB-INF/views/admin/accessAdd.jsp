<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	

	$(function() {
		
		$('#pid').combotree({
			url : '${ctx}/access/allTree?flag=false',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		});
		
		$('#accessAddForm').form({
			url : '${ctx}/access/add',
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
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为access.jsp页面预定义好了
					//parent.layout_west_tree.tree('reload');
					parent.$.modalDialog.handler.dialog('close');
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
	
	function seturl(data,x){
		var reg = /^\/([0-9a-zA-Z]+)+$/;
		if(!reg.test(data.value)){
			parent.$.messager.alert('提示','请输入正确的连接地址','warning');
		}
	}
</script>
<div style="padding: 3px;">
	<form id="accessAddForm" method="post">
		<table class="grid">
			<tr>
				<td>资源名称</td>
				<td><input name="name" type="text" placeholder="请输入资源名称" class="easyui-validatebox span2" data-options="required:true,validType:'length[1,16]'" ></td>
				<td>资源类型</td>
				<td><select name="accessType" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="0">菜单</option>
							<option value="1">按钮</option>
				</select></td>
			</tr>
			<tr>
				<td>资源路径</td>
				<td><input name="url" type="text" placeholder="请输入资源路径" onchange="" class="easyui-validatebox span2" data-options="width:140,height:29,required:true,validType:'route'"></td>
				<td>排序</td>
				<td><input name="seq" value="0"  class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false"></td>
			</tr>
			<tr>
				<td>菜单图标</td>
				<td ><input  name="icon" class="easyui-validatebox" data-options="required:true,validType:'length[1,20]'" /></td>
			</tr>
			<tr>
				<td>上级资源</td>
				<td colspan="3"><select id="pid" name="pid" style="width: 200px; height: 29px;"></select>
				<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a></td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="3"><textarea name="description" maxlength="255" rows="5" cols="50" class="easyui-validatebox" data-options="required:true,validType:'length[1,255]'"   ></textarea></td>
			</tr>
		</table>
	</form>
</div>