<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="inc.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--[if IE 8]> <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /><![endif]-->
<title></title>
<script type="text/javascript">
	var index_layout;
	var index_tabs;
	var index_tabsMenu;
	var layout_west_tree;
	var layout_west_tree_url = '';
	
	var sessionInfo_userId = '${sessionInfo.id}';
	if (sessionInfo_userId) {//如果没有登录,直接跳转到登录页面
		layout_west_tree_url = '${ctx}/access/tree';
	}else{
		window.location.href='${ctx}/admin/index';
	}
	$(function() {
		index_layout = $('#index_layout').layout({
			fit : true
		});
		index_tabs = $('#index_tabs').tabs({
			fit : true,
			border : false,
			tools : []
		});
		
		layout_west_tree = $('#layout_west_tree').tree({
			url : layout_west_tree_url,
			parentField : 'pid',
			lines : false,
			onClick : function(node) {
				$("#layout_west_tree").tree('toggle', node.target);
				if (node.attributes && node.attributes.url) {
					var url = '${ctx}' + node.attributes.url;
					addTab({
						url : url,
						title : node.text,
						iconCls : 'ico_blank',
						nodeid:node.id
					});
				}
			}
		});
		$('#ptTab').tabs({ 
			border:false, 
			onSelect:function(title,index){ 
				if(index==0){
					$("#process1").attr('src','${ctx}/process/processViewShow?dataType=0');
				}else{
					$("#task1").attr('src','${ctx}/tasks/taskViewShow?dataType=0');
				}
			} 
		});
		$('#proceTab').tabs({ 
			border:false, 
			onSelect:function(title,index){ 
				if(index==0){
					$("#process1").attr('src','${ctx}/process/processViewShow?dataType=0');
				}else if(index==1){
					$("#process2").attr('src','${ctx}/process/processViewShow?dataType=1');
				}else{
					$("#process3").attr('src','${ctx}/process/processViewShow?dataType=2');
				}
			} 
		});
		$('#taskTab').tabs({ 
			border:false, 
			onSelect:function(title,index){ 
				if(index==0){
					$("#task1").attr('src','${ctx}/tasks/taskViewShow?dataType=0');
				}else if(index==1){
					$("#task2").attr('src','${ctx}/tasks/taskViewShow?dataType=1');
				}else{
					$("#task3").attr('src','${ctx}/tasks/taskViewShow?dataType=2');
				}
			} 
		});
		getShortcutList();
	});
	
	function getShortcutList(){
		$.get('${ctx}/shortcutmenu/userShortMenuList',{stamp: Math.random() }, function(result){
			var ob;
			var htmlStr = "";
			result = $.parseJSON(result);
			for(var i=0;i<result.length;i++){
				ob = result[i];
				htmlStr += '<span class="l-btn-function l-btn-text" id="'+ob.id+'"><a href="javascript:void(0);" onclick="createTab(\''+ob.accessMenuUrl+'\',\''+ob.accessMenuName+'\',\''+ob.accessMenuId+'\')">'+ob.accessMenuName+'</a><label onclick="reMove(\''+ob.id+'\');" style="font-size:17px;"> ×</label></span>';
			}
			$("#cutMenu").html(htmlStr);
		});

	}

	//新增新开页签方法
	function createTabNew(url,text,tid){
		if (url) {
			var url = '${ctx}' +url;
			addTabNew({
				url : url,
				title : text,
				iconCls : 'ico_blank',
				nodeid:tid
			});
		}
	}
	//新增页签
	function addTabNew(params) {
		var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		var t = $('#index_tabs');
		var opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true,
			nodeid:params.nodeid
		};
		//如果页签已存在，先关闭，再新增
		if (t.tabs('exists', opts.title)) {
			
			t.tabs('close', opts.title);
		} 
		t.tabs('add', opts);
		
	}
	
	function createTab(url,text,tid){
		if (url) {
			var url = '${ctx}' +url;
			addTab({
				url : url,
				title : text,
				iconCls : 'ico_blank',
				nodeid:tid
			});
		}
	}
	function addTab(params) {
		var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		var t = $('#index_tabs');
		var opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true,
			nodeid:params.nodeid
		};
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
	}
	
	function removeTab(title){
		var t = $('#index_tabs');
		t.tabs('close', title);
	}
	
	function addMainTab(url,title,iconCls) {
		var iframe = '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		var t = $('#index_tabs');
		var opts = {
			title : title,
			closable : true,
			iconCls : iconCls,
			content : iframe,
			border : false,
			fit : true
		};
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
	}
	
	
	function logout(){
		$.messager.confirm('提示','确定要退出?',function(r){
			if (r){
				progressLoad();
				$.post( '${ctx}/admin/logout', function(result) {
					if(result.success){
						progressClose();
						$(window).unbind('beforeunload');
						window.location.href='${ctx}/admin/index';
					}
				}, 'json');
			}
		});
	}
	

	function editUserPwd() {
		parent.$.modalDialog({
			title : '修改密码',
			width : 300,
			height : 220,
			href : '${ctx}/user/editPwdPage',
			buttons : [ {
				text : '保存',
				handler : function() {
                    parent.$.modalDialog.logout=function () {
                        $.post( '${ctx}/admin/logout', function(result) {
                            if(result.success){
                                progressClose();
                                $(window).unbind('beforeunload');
                                window.location.href='${ctx}/admin/index';
                            }
                        }, 'json');
                    };
					var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
					f.submit();
				}
			} ]
		});
	}
	function addCount(url,title){
		addTab({
			url : '${ctx}'+url,
			title : title,
			iconCls : 'ico_blank'
		});
	}
	function addMan(){
		var numButt = $('.content-top-funcbutton span').size();
		if(numButt<8){
			parent.$.modalDialog({
				title : '添加',
				width : 300,
				height : 130,
				href : '${ctx}/shortcutmenu/addPage',
				buttons : [ {
					text : '保存',
					handler : function() {
						var f = parent.$.modalDialog.handler.find('#userShortcutMenuAddForm');
						f.submit();
						getShortcutList();
					}
				} ]
			});
		}else{
			$.messager.confirm('提示','添加标签过多，最多只能添加5个','info');
		} 
	}
	
	function reMove(id){
		$.post('${ctx}/shortcutmenu/delete', {
			id : id
		}, function(result) {
			if (result.success) {
				$.messager.alert('提示', result.msg, 'info');
				$("#"+id).remove();
			}else{
				$.messager.alert('提示', result.msg, 'info');
			}
			progressClose();
		}, 'JSON');	
	}
	
</script>
  <link rel="stylesheet" href="${ctx}/style/css/reset.css"/>
    <link rel="stylesheet" href="${ctx}/style/css/index.css"/>
</head>
<style>
	#index_tabs .tabs-title{background:#E8E7E7;display:inline-block;padding:0 25px;border-top:solid 3px #5DBEF4;}
	#index_tabs .tabs li.tabs-selected .tabs-title{background:#F5F5F5;}
	#index_tabs .datagrid-row-selected{background:#EA5415;color:#ffffff;border:solid 1px #ffffff;}
	#content-left div.tabs-header{background-color:#EBECED !important;}
	#ptTab div.tabs-header{background-color:#EBECED !important;}
</style>
<body>
<div class="wrap">
    <div class="header">
        <div class="header-left"></div>
        <div class="header-right">
            <span><img src="${ctx}/style/img/rc-bg.png"> </span>
            <div class="out">
                <p><i class="supadm"></i><a href="javascript:;" class="super" style="font-size:14px;">${sessionInfo.name}</a>|<i class="iconPwd"></i><a href="javascript:;" onclick="editUserPwd()" class="tuichu">修改密码</a>|<i class="goout"></i><a href="javascript:;" onclick="logout()" class="tuichu">退出</a></p>
            </div>
        </div>
    </div>
    <div class="main">
        <div class="main-left">
            <div class="sfq">
			  <dl  >
				<dt class="change"><span class="bg1"><img src="${ctx}/style/img/12.png" width="30px"> </span><a href="javascript:;">终端管理</a></dt>
				<dd>
					<ol>
						<li onclick="createTab('/park/toPayPanel','面板管理','mbgl')"><i><img src="${ctx}/style/img/13.png"></i><a href="javascript:;">面板管理</a></li>
					</ol>
					<ol>
						<li onclick="createTab('/park/toExitWay','出口管理','ckgl')"><i><img src="${ctx}/style/img/13.png"></i><a href="javascript:;">出口管理</a></li>
					</ol>
					<ol>
						<li onclick="createTab('/park/toParkInfo','停车场信息绑定','tccxxbd')"><i><img src="${ctx}/style/img/13.png"></i><a href="javascript:;">停车场信息绑定</a></li>
					</ol>
					<ol>
						<li onclick="createTab('/park/toBillInfo','交易流水','jyls')"><i><img src="${ctx}/style/img/13.png"></i><a href="javascript:;">交易流水</a></li>
					</ol>
                    <ol>
                        <li onclick="createTab('/park/toCloudSet','云端设置','jyls')"><i><img src="${ctx}/style/img/13.png"></i><a href="javascript:;">云端设置</a></li>
                    </ol>
                    <ol>
                        <li onclick="createTab('/terminalController/initSetTerminalId','终端编号设置','zdbhsz')"><i><img src="${ctx}/style/img/13.png"></i><a href="javascript:;">终端编号设置</a></li>
                    </ol>
				</dd>
			  </dl>
            </div>
        </div>
        
        <div class="main-right" style="height: 800px;">
			<div id="index_tabs" >
				 <div class="rc-system" title="首页">
                <div class="tab">
                    <div class="tab-content">
                        <div class="system-content-top">
	                        <div class="content-top-funcbutton">
	                        </div>
                       	</div>
                    </div>
                </div>
            </div> 
			</div>
		</div>
 
    </div>
    <div class="clear"></div>
    <div class="footer"style="height:35px;width:1349px;width:1345px \0;background:#5DBEF4;">
    </div>

</div>
<script src="${ctx}/style/js/sfq.js"></script>
</body></html>