<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
${msg}
<script type="text/javascript" charset="utf-8">
	var reLogin='*'+${reLogin}+'*';
	var top = getTopWinow(); //获取当前页面的顶层窗口对象
	/** 
	  * 在页面中任何嵌套层次的窗口中获取顶层窗口 
	  * @return 当前页面的顶层窗口对象 
	  */
	function getTopWinow(){  
	    var p = window;  
	    while(p != p.parent){  
	        p = p.parent;  
	    }  
	    return p;  
	}
	//调用：
	if(top != window){
	    setTimeout("top.location.href='${ctx}/admin/index'",1500);
	}
// 	setTimeout("parent.location.href='${ctx}/admin/index'",1500);
</script>