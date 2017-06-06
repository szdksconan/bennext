<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 35px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<td>客户名称：</td>
					<td><input type="text" name="custName" style="height:20px;"></td>
					<td>区域：</td>
					<td>
						<!--  <select id="area1" name="area1" class="easyui-combobox " data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							
						</select>
						-->
						<input type="text" name="address" style="height:20px;">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_refresh',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>
		</form>
		  
	</div>
	<div data-options="region:'center',border:false,title:'客户列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
</div>

<script type="text/javascript">
	var customerIds = "${customerIds}";
	var dataGrid;
	$(function() {
		$('#area1').combotree({
			url : '${ctx}/event/areaTree',
			parentField : 'pid',
			lines : true,
			panelHeight : '200',
			onLoadSuccess: function (node, data) {
				
            },
            onSelect : function(node){
            	//alert(node.text);
            	//定位到选择区域
            	//map.setCenter(node.text);
            	//给全局区域赋值，用于查询操作
            	//areaStr = node.text;
            }

		});
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/attWorkAddress/cusDataGrid?customerIds=' + customerIds,
			striped : true,
			rownumbers : true,
			pagination : true,
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			columns : [ [ {
				width : '50',
				field : 'id',
				checkbox:true
			},{
				width : '200',
				title : '客户名称',
				field : 'name',
				sortable : true
			},{
				width : '250',
				title : '办公地址',
				field : 'address'
			}] ]
			
		});
		
	});
	
	
	
	function getCheckIds(){
		var checknodes = dataGrid.datagrid('getChecked');
		var productIds = [];
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				productIds.push(checknodes[i].id);
			}
		}
		return productIds;
	}
	//查询方法
	function searchFun() {
		var fromObj =  $.serializeObject($('#searchForm'));
		
		dataGrid.datagrid('load',fromObj);
	}
	function cleanFun() {
		$('#searchForm input').val('');
		
		
		dataGrid.datagrid('load', {});
	}
	
	
	</script>