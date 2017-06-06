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
        var dataGrid;
        function addgrid(){
        dataGrid = $('#dataGrid').datagrid({
            url : '${ctx}/park/getParkInfoFromDataCenter',
            striped : true,
            rownumbers : true,
            pagination : true,
            idField : 'parkId',
            toolbar : '#toolbar',
            queryParams :$.serializeObject($('#searchForm')),
            singleSelect:true,
            pageSize : 10,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ {
                width : '150',
                title : '停车场id',
                field : 'parkId'
            },{
                width : '150',
                title : '停车场名称',
                field : 'parkName'
            },{
                width : '150',
                title : '商户ID',
                field : 'supplyId'
            },{
                width : '150',
                title : '商户名称',
                field : 'supplyName'
            }] ]
        });
        }

        function searchFun() {
            if(dataGrid==undefined)addgrid();
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }

        function addPark(){
            var rows = dataGrid.datagrid('getSelections');
            if(rows.length>0){
            parent.$.messager.confirm('提示', '您是否要绑定当前停车场数据？', function(b) {
                if (b) {
                    $.post('${ctx}/park/addOrupdateParkInfo', {
                        "parkId" : rows[0].parkId,
                        "parkName" : rows[0].parkName,
                        "supplyId" : rows[0].supplyId,
                        "supplyName" : rows[0].supplyName
                    }, function(result) {
                        if (result.success) {
                            parent.$.messager.alert('提示', result.msg, 'info');
                            $('#supplyName').val(rows[0].supplyName);
                            $('#parkName').val(rows[0].parkName);
                            $('#parkId').val(rows[0].parkId);

                        }else{
                            parent.$.messager.alert('提示', result.msg, 'info');
                        }
                        progressClose();
                    }, 'JSON');
                }
            });
            }else{
                parent.$.messager.alert('提示', '请选择一条数据！', 'info');
            }
        }
        function addClientRights(){
                        var  parkId = $('#parkId').val();
                        if(parkId != ''){
                        $.post('${ctx}/park/getClientRightsListBySupplyId', {
                            "supplyId" : parkId
                        }, function(result) {
                            if (result.success) {
                                parent.$.messager.alert('提示', result.msg, 'info');

                            }else{
                                parent.$.messager.alert('提示', result.msg, 'info');
                            }
                            progressClose();
                        }, 'JSON');
                    }else{
                        parent.$.messager.alert('提示', '请绑定停车场信息！', 'info');
                    }

        }
        function addCardBin(){
                $.post('${ctx}/terminalTakeCloud/synchCardBinInfo', {
                }, function(result) {
                    if (result.success) {
                        parent.$.messager.alert('提示', result.msg, 'info');

                    }else{
                        parent.$.messager.alert('提示', result.msg, 'info');
                    }
                    progressClose();
                }, 'JSON');

        }
        function addRightsCount(){
            $.post('${ctx}/terminalTakeCloud/takeRightsCount', {
            }, function(result) {
                if (result.success) {
                    parent.$.messager.alert('提示', result.msg, 'info');

                }else{
                    parent.$.messager.alert('提示', result.msg, 'info');
                }
                progressClose();
            }, 'JSON');

        }

        function resetCharge(){
            var hb = $('#charge').val();
            var reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
            if (!reg.test(hb)) {
                parent.$.messager.alert('提示', "格式非法！", 'info');
                return false;
            }
            parent.$.messager.confirm('提示', '提交数据？', function(b) {
                if (b) {
                    $.post('${ctx}/park/addOrUpdateParkForCharge', {
                        "charge" : hb
                    }, function(result) {
                        if (result.success) {
                            parent.$.messager.alert('提示', result.msg, 'info');
                        }else{
                            parent.$.messager.alert('提示', result.msg, 'info');
                        }
                        progressClose();
                    }, 'JSON');
                }
            });
        }

    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow:scroll;padding: 3px;">
                <div data-options="region:'north',border:false" style="height: 120px; overflow: hidden;background-color: #f4f4f4">
                    <table class="grid">
                        <tr>
                            <td>停车场名称</td>
                            <td>
                                <input id="parkName" name="parkName" type="text" readonly="readonly"
                                       style="background:transparent;border:0;height: 20px" value="${parkName}"/>
                                <input id="parkId" name="parkId" type="hidden"  value="${parkId}"/>
                            </td>
                            <td>商户名称</td>
                            <td>
                                <input id="supplyName" name="supplyName" type="text" readonly="readonly"
                                       style="background:transparent;border:0;height: 20px" value="${supplyName}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>收费(小时):</td>
                            <td>
                                <input name="charge" id="charge" value="${charge}" placeholder="请输入价格"/>
                                <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_input',plain:true" onclick="resetCharge();">重设收费信息</a>
                            </td>
                        </tr>
                    </table>
                    </br>
                   <%-- <a onclick="addgrid();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">获取停车场数据</a>--%>
                    <form id="searchForm" method="post" action="">
                        <table>
                            <tr>
                                <th>停车场名称:</th>
                                <td>
                                    <input name="parkName" placeholder="请输入停车场名称"/></td>
                                <th>商户姓名:</th>
                                <td>
                                    <input name="supplyName" placeholder="请输入商户名称"/></td>
                                <td>
                                <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="addgrid();">查询</a>
                                    <a href="javascript:void(0);" onclick="addClientRights();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_sys'">同步商户权益信息</a>
                                    <a href="javascript:void(0);" onclick="addCardBin();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_sys'">同步卡片信息</a>
                                    <a href="javascript:void(0);" onclick="addRightsCount();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_sys'">同步权益类型信息</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div data-options="region:'center',border:false,title:'面板列表'" >
                    <table id="dataGrid"  data-options="fit:false,border:false"></table>
                </div>
                <div id="toolbar" style="display: none;">
                    <a onclick="addPark();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">绑定选中行</a>

                </div>
    </div>
</div>
</body>
</html>