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
            var cloudUrl = $('#cloudUrl').val();
         /*   var reg = /^\+?[1-9][0-9]*$/;
            if (!reg.test(hb)) {
                parent.$.messager.alert('提示', "请输入正整数", 'info');
                return false;
            }*/
            parent.$.messager.confirm('提示', '提交数据？', function(b) {
                if (b) {
                    $.post('${ctx}/park/addOrUpdateCloudSet', {
                        "cloudUrl" : cloudUrl
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
                                <th>云平台url:</th>
                                <td>
                                    <input name="cloudUrl" id="cloudUrl" value="${cloudUrl}" placeholder=""/></td>
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