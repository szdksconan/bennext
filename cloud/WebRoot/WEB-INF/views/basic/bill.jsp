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
	<title>流水列表</title>
	<script type="text/javascript">
		var dataGrid;
		$(function() {
			dataGrid = $('#dataGrid').datagrid({
				url : '${ctx}/rights/billInfo',
				striped : true,
				rownumbers : true,
				pagination : true,
				idField : 'id',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50],
				columns : [ [
				{
					width : '150',
					title : '流水号',
					field : 'billNo'
				},{
					width : '150',
					title : '银行卡号',
					field : 'cardNo'
				},{
					width : '150',
					title : '车牌号',
					field : 'carNo'
				},{
					width : '100',
					title : '停车场名称',
					field : 'parkName'
				},{
					width : '100',
					title : '出口名称',
					field : 'exitWayName'
				},{
					width : '50',
					title : '金额',
					field : 'totalPrice'
				},{
					width : '100',
					title : '停车小时',
					field : 'stopTime'
				},{
					width : '100',
					title : '权益类型',
					field : 'rightsType'
				},{
					width : '150',
					title : '生成时间',
					field : 'createTime'
				},{
						width : '150',
						title : '状态',
						field : 'uploadTag'
					}] ]
			});
		});



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
				<th>开始时间:</th>
				<td>
					<input name="startTime" placeholder="点击选择开始时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  />
				</td>
				<th>结束时间:</th>
				<td>
					<input name="endTime" placeholder="点击选择结束时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  />
				</td>
				<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_input',plain:true" onclick="downFun();">导出</a>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'center',border:false,title:'流水列表'" >
	<table id="dataGrid" data-options="fit:true,border:false"></table>
</div>
</body>
</html>