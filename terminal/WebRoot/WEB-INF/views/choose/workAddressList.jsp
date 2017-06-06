<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<div class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'north',border:false" style="height: 35px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<td>工作地点名称：</td>
					<td>
						<input type="text" name="name" style="height:20px;">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_refresh',plain:true" onclick="cleanFun();">清空</a>
						<input type="hidden" name="id" id="id" value="${id }">
					</td>
					
				</tr>
			</table>
		</form>
		  
	</div>
	<div data-options="region:'center',fit:true,border:false,title:'工作地点列表'">
		<table id="attgrid" data-options="fit:true,border:false"></table>
	</div>
</div>	

<script type="text/javascript">
	var attGrid;
	$(function() {
		attGrid = $('#attgrid').datagrid({
			url : '${ctx}/attWorkAddress/chooseAtt?queryId=' + "${id}",
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			columns : [ [  {
				field : 'id',
				title : 'id',
				width : '80',
				checkbox : true
			}, {
				field : 'name',
				title : '工作地点名称',
				width : '180'
			}, {
				field : 'address',
				title : '位置',
				width : '300'
			}] ]
		});
	});
	
	//查询方法
	function searchFun() {
		var fromObj =  $.serializeObject($('#searchForm'));
		attGrid.datagrid('load',fromObj);
	}
	function cleanFun() {
		$('#searchForm input').val('');
		attGrid.datagrid('load', {});
	}
	
	</script>