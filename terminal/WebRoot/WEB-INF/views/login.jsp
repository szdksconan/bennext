<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
<script>

	var sessionInfo_userId = '${sessionInfo.id}';
	if (sessionInfo_userId) {//如果登录,直接跳转到index页面
		window.location.href='${ctx}/admin/index';
	}
		
	$(function() {
		var vcc = 0;
		$('#loginform').form({
		    url:'${ctx}/admin/login',
		    onSubmit : function() {
		    	var vcode = $("#vcode").val();
// 		    	var loginPwd = $("#loginPwd").textbox('getText');
// 		    	var loginName = $("#loginName").textbox('getText');
				var loginPwd = $("#loginPwd").val();
				var loginName = $("#loginName").val();
		    	if(loginName == null || "" == loginName){
		    		$.messager.alert('提示', '账号不能为空!', 'info');
		    		//vcc = vcc+1;
		    		return false;
		    	}
		    	
		    	//alert(loginPwd);
		    	if(loginPwd == null || "" == loginPwd){
		    		$.messager.alert('提示', '密码不能为空!', 'info');
		    		//vcc = vcc+1;
		    		return false;
		    	}
		    	//$("#loginPwd").textbox('setValue',loginPwd);
		    	if(vcc >= 3){
		    		if(vcode != null && vcode != ""){
		    			progressLoad();
						/*var isValid = $(this).form('validate');
						if(!isValid){
							progressClose();
						}
						return isValid;*/
		    		}else{
		    			$.messager.alert('提示', '请输入验证码', 'info',function(){
		    				$('#vcode').focus();
		    			});
 		    			
		    			//$('#vcode').textbox('getValue');
			    		return false;
		    		}
		    	}
		    	/*if(loginName == null || "" == loginName){
		    		$.messager.alert('提示', '账号不能为空', 'info');
		    		vcc = vcc+1;
		    		return false;
		    	}
		    	if(loginPwd == null || "" == loginPwd){
		    		$.messager.alert('提示', '密码不能为空', 'info');
		    		vcc = vcc+1;
		    		return false;
		    	}*/
		    	
		    	
			},
		    success:function(result){
		    	result = $.parseJSON(result);
		    	progressClose();
		    	if (result.success) {
		    		window.location.href='${ctx}/admin/index';
		    	}else{
		    		vcc = vcc+1;
		    		$("#vcc").val(vcc);
		    		if(vcc >= 3){
		    			$("#verifyImg").attr('src','${ctx}/img/verifyCode?t='+Math.random());
		    			$("#yz").css('display','block');
		    		}
		    		$.messager.alert('提示', result.msg, 'info',function(){
	    				$('#vcode').focus();
	    			});
		    	}
	    	
		    }
		});
			document.onkeypress = function(event_e){    
		        if(window.event)    
		         event_e = window.event;    
		         var int_keycode = event_e.charCode||event_e.keyCode;    
		         if(int_keycode ==13){   
		        	 $('#loginform').submit();
		        }  
		    } 
	});
	function submitForm(){
		$('#loginform').submit();
	}
	
	function clearForm(){
		$('#loginform').form('clear');
	}
// 	function enterlogin(){
// 		if (event.keyCode == 13){
// 			//$.messager.window('close');
// 		//	$('.l-btn-text').click();
// 			//$.messager.alert('close');
// 			document.getElementById('login').focus(); 
// 	        event.returnValue=false;
// 	        event.cancel = true;
// 	        $('#loginform').submit();
// 	    }
// 	}
	
	/*$(function(){
		document.onkeydown = function(event_e){    
	        if(window.event)    
	         event_e = window.event;    
	         var int_keycode = event_e.charCode||event_e.keyCode;    
	         if(int_keycode ==13){   
	        	 $('#loginform').submit();
	        }  
	    } 
	})*/
	
	//单击验证码图片重新加载
	function reloadVerifyImg(){
		$('#verifyImg').attr("src", "${ctx}/img/verifyCode?t="+Math.random()); 
	}
	
</script>
</head>
<body>
	<div align="center" style="background-color:#5AC0F5;min-height:100%">
	<hr style="background-color:#1e9adb;height:4px;border:0;">
	<div style="width:670px; margin-top:160px;" >
		<div  align="center" style="margin-bottom:56px;" ><img width="300px" src="${ctx}/static/style/images/index/logo-index.png" /></div>
		<div  align="center" style="margin-bottom:98px;"  ><img width="500px" src="${ctx}/static/style/images/index/by-index.png" /></div>
		<div style="padding:10px;" align="center" >
	    <form id="loginform"  method="post">
<!-- 	    	<div style="margin-bottom:25px"> -->
<!--     	    	<input class="easyui-textbox" name="loginName" id="loginName" style="width:100%;height:40px;padding:12px" data-options="prompt:'输入账号',iconCls:'',iconWidth:38"> -->
<!-- 	       	</div> -->
<!--             <div style="margin-bottom:25px"> -->
<!--             	<input class="easyui-textbox" type="password" name="loginPwd" id="loginPwd" style="width:100%;height:40px;padding:12px" data-options="prompt:'输入密码',iconCls:'',iconWidth:38"> -->
<!-- 	    	</div> -->

			<div style="margin-bottom:25px">
    	    	<input class="loginput" name="loginName" id="loginName"  placeholder="输入账号">
	       	</div>
            <div style="margin-bottom:25px">
            	<input class="loginput" type="password" name="loginPwd" id="loginPwd" placeholder="输入密码">
	    	</div>
	    	<div id="yz" style="margin-bottom:25px;display:none;">
	    		<input type="hidden" id="vcc" value="0" />
            	<input class="yzcode" type="text" id="vcode" name="verify" placeholder="输入验证码">
	    		<span class="textbox-yz" style="background:none;border:0;">
	    			<img id="verifyImg" src="${ctx}/img/verifyCode" alt="验证码" onclick="reloadVerifyImg()" style="width:150px;height:50px;float:left;" />
	    		</span>
	    	</div>
	    	<div>
<!-- 	    	<input type="submit" id="login" href="javascript:void(0)" vlue="登录" onkeypress="if(event.keyCode==13){document.getElementById('login')。focus(); document.getElementById('loginform').submit();return false;}"> -->
	    		<a id="login" onclick="submitForm()" href="javascript:void(0)" class="easyui-linkbutton"  style="padding:5px 0px;width:100%;"><span>登 录</span></a>
			</div>
	    </form>
	    </div>
	</div>
	</div>
	
	<!--[if lte IE 7]>
	<div id="ie6-warning"><p>您正在使用 低版本浏览器，在本页面可能会导致部分功能无法使用。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">Internet Explorer 8</a> 或以下浏览器：
	<a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> / <a href="http://www.operachina.com/" target="_blank">Opera</a></p></div>
	<![endif]-->
	
	<style>
	/*ie6提示*/
	#ie6-warning{width:100%;position:absolute;top:0;left:0;background:#fae692;padding:5px 0;font-size:12px}
	#ie6-warning p{width:960px;margin:0 auto;}
	</style>
	<script>
	/* jQuery(function ($) {
	 if ( jQuery.browser.msie && ( jQuery.browser.version == "6.0" )&& ( jQuery.browser.version == "7.0" ) && !jQuery.support.style ){
	  jQuery('#ie6-warning').css({'top':jQuery(this).scrollTop()+'px'});
	 }
	}); */
	</script>
	</body>
</html>