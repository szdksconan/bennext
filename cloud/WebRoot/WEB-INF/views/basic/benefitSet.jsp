<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc.jsp"></jsp:include>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <title>心跳设置</title>
    <script type="text/javascript">

        function addcs(){
            var benefitUrl = $('#benefitUrl').val();
            parent.$.messager.confirm('提示', '提交数据？', function(b) {
                if (b) {
                    $.post('${ctx}/serviceInfo/saveBenefitUrl', {
                        "benefitUrl" : benefitUrl
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
        <div data-options="region:'north',border:false" style="height: 80px; overflow: hidden;background-color: #f4f4f4">
            <form id="addForm" method="post" action="${ctx}/rights/exportBill">
                <table>
                    <tr>
                        <th>权益平台url:</th>
                        <td>
                            <input name="benefitUrl" id="benefitUrl" value="${benefitUrl}" placeholder=""/></td>
                        <td>
                            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_input',plain:true" onclick="addcs();">保存</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>