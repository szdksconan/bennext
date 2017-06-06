<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script language="javascript" type="text/javascript">
$(function() {
	$('#userInputForm').form({
		url : '${ctx}/user/userInput',
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
	
	function setphone(data){
		var reg = /0?(13|14|15|18)[0-9]{9}/;
		if(!reg.test(data.value)){
			parent.$.messager.alert('提示','请输入正确的手机号码','warning');
		}
	}


	function uploadFile() {
		var fd = new FormData();
		fd.append("fileToUpload",
				document.getElementById('fileToUpload').files[0]);
		var xhr = new XMLHttpRequest();
		xhr.upload.addEventListener("progress", uploadProgress, false);
		xhr.addEventListener("load", uploadComplete, false);
		xhr.addEventListener("error", uploadFailed, false);
		xhr.addEventListener("abort", uploadCanceled, false);
		xhr.open("POST", "/Goods/ToLead");
		xhr.send(fd);
	}

	function uploadProgress(evt) {
		if (evt.lengthComputable) {
			var percentComplete = Math.round(evt.loaded * 100 / evt.total);
			$('#progressNumber').progressbar('setValue', percentComplete);
		} else {
			document.getElementById('progressNumber').innerHTML = '无法计算';
		}
	}

	function uploadComplete(evt) {
		/* 服务器返回数据*/
		var message = evt.target.responseText;

	}

	function uploadFailed(evt) {
		alert("上传出错.");
	}

	function uploadCanceled(evt) {
		alert("上传已由用户或浏览器取消删除连接.");
	}
	
	function inputFun(){
		var upfile = $("#upExcel").val();
		if(null == upfile || "" == upfile){
			parent.$.messager.alert('提示', "请选择上传文件！", 'warning');
			return;
		}
		$('#userInputForm').submit();
	}

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow:scroll;padding: 3px;">
		<form id="userInputForm" method="post" enctype="multipart/form-data">
			<table data-options="fit:true,border:false" style="width: 100%">
				<tr>
					<td style="text-align: left;width: 70%">
						<input type="file" name="upExcel" id="upExcel" multiple="multiple" />
					</td>
					<td style="text-align: right;width: 30%">
						<a onclick="inputFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_input'">导入</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>