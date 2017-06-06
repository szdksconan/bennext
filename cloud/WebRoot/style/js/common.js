//设置cookie
function setCookie(name,value){
	var Days =30;
	var exp =new Date();
	exp.setTime(exp.getTime()+Days*24*60*60*1000);
	document.cookie = name +"="+encodeURIComponent(value)+";expires="+exp.toUTCString();
}

//获取cookie
function getCookie (name){
	var arr,reg =new RegExp("(^|)"+name+"=([^;]*)(;|$)");
	if(arr = document.cookie.match(reg)){
		return decodeURIComponent(arr[2]);
	}else{
		return null;
	}
}
//删除cookie
function delCookie(name){
	var exp =new Date();
	exp.setTime(exp.getTime()-1);
	var cval =getCookie(name);
	if(cval!=null){
		document.cookie = name+ "="+cval+";expires="+exp.toUTCString();
	}
}

// url 参数
var getArgs = function (){
	var args = new Object(); 
	var query = window.location.search.substring(1); 
	var pairs = query.split("&"); 
	for(var i = 0; i < pairs.length; i++) {
		var pos = pairs[i].indexOf('=');
		if (pos == -1) continue; 
		var argname = pairs[i].substring(0,pos);
		var value = pairs[i].substring(pos+1); 
		value = decodeURIComponent(value); 
		args[argname] = value; 
	}
	return args; 
}

//弹框
function ReminderArert(text,callBack)
{
	 $.messager.alert('提示', text, 'info',function(){callBack()});
}

	
function message(text){
	$.messager.show({
		title:'提示',
		msg:'<div class="light-info"><div class="light-tip icon-tip"></div><div>'+text+'</div></div>',
		showType:'show'
	});
}
	

//登录	
function loginFn(userCode,password,verifyCode){  
	var reqUrl = '/supplier/login';
	var reqData = {'number':userCode,'password':password,'verify':verifyCode};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				window.location.href='/supplier/index';
			}else{
				$.messager.show({
	    			title:'提示',
	    			msg:'<div class="light-info"><div class="light-tip icon-tip"></div><div>'+data.msg+'</div></div>',
	    			showType:'show'
	    		});
			}
		}
	});
}


//修改密码
function editPassword(usrId,oldPwd,pwd){  
	var reqUrl = '/supplier/editUserPwd';
	var reqData = {'usrId':usrId,'oldPwd':oldPwd,'pwd':pwd};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				$.messager.alert('提示', data.msg, 'info',function(){
					$.ajax({
						url:'/supplier/logout',
						type : 'post',
						dataType : "json",
						success: function(data){  
							if(data.success){
								location.href="/shopLogin.jsp";
							}
						}
					});
				});
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else if(data.msg=='新密码与原密码相同！'){
					$('#spa2221').show();
				}else{
					$('#spa11').show();
				}
			}
		}
	});
}


//忘记密码	
function forgetPassword(comId,password){  
	var reqUrl = '/supplier/editPassword';
	var reqData = {'comId':comId,'password':password};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				//location.href="shopMessage2.jsp?loginName="+data.obj.loginName;
				ReminderArert('密码已修改成功',function(){
					location.href="login.jsp";
				});
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}


//发送验证码	
function getValidateCode(loginName,phone){ 
	var reqUrl = '/supplier/getValidateCode';
	var reqData = {'loginName':loginName,'phone':phone};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				//location.href="shopMessage2.jsp?loginName="+data.obj.loginName;
				phoneCode = data.obj.code;
				comId = data.obj.comId;			
				getCode.style.background = '#ccc';
				countTime.style.display = 'inline-block';
				oTime = setInterval(function(){	
					iTime--;	
					if(iTime==0){
						clearInterval(oTime);
						getCode.style.background = '#09abf3';
						countTime.style.display = 'none';
						iTime = 60;
						iBtn = false;
					}
					countTime.innerHTML = iTime+'秒';
				},1000);
				$('.forgetNext').css('background','#F0725A');
				$('.forgetNext').click(function(){
					var forgetName = $('.forgetName input').val();
					var forgetPhone = $('.forgetPhone input').val();
					if(forgetName==''){
						ReminderArert('请输入用户名');
					}else if(forgetPhone==''){
						ReminderArert('请输入手机号');
					}else if($('.vfiCode input').val()==''){
						ReminderArert('请输入验证码');
					}else if($('.vfiCode input').val() != phoneCode){
						ReminderArert('验证码错误，请重新输入');
					}else{
						location.href = "forgetPsdTex.jsp?comId="+ comId;
					}

				});

			}else{
				ReminderArert(data.msg,function(){
					iBtn = false;
				});
				
			}
		}
	});
}


//注册
function resign(comName,comLogo,loginName,verify,password,email,phone,address,tel,communityId,modelId,commentInfo){  
	
	checkName($("#comName").val());
	checkAddress($("#address").val());
	checkLoginName($("#loginName").val());
	checkPhone($("#phone").val());
	checkPassword($("#password").val());
	checkCertPassword($("#certPassword").val());
	checkCommentInfo($("#commentInfo").val());
	checkVerify($("#verify").val());
	if(!$("#cityId").val()){
		$("#city_").html("请选择城市");
	}
	if(!$("#communityId").val()){
		$("#community_").html("请选择社区");
	}
	if(!$("#serviceId").val()){
		$("#service_").html("请选择服务类型");
	}
	if($("#verify_").val()||$("#comment_").val()||$("#name_").val()||$("#address_").val()||$("#loginName_").val()||$("#phone_").val()||$("#password_").val()||$("#certPassword_").val()||$("city_").val()||$("community_").val()||$("service_").val()){
		return;
	}
	if($("#id").attr("checked")==false){
		return;
	}
	var reqUrl = '/supplier/register';
	var reqData = {'comName':comName,'comLogo':comLogo,'verify':verify,'loginName':loginName,'password':password,'email':email,'phone':phone,'address':address,'tel':tel,'communityId':communityId,'modelId':modelId,'commentInfo':commentInfo};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				$.messager.alert('提示', "注册成功，待审核", 'info');
				/*ReminderArert('注册成功，待审核',function(){
					location.href="login.jsp";
				});*/
				
			}else{
				$.messager.alert('提示', data.msg, 'info');
				//ReminderArert(data.msg);
			}
		}
	});
}


//城市
function cityData(){  
	var reqUrl = '/static/city.json';
	var reqData = '';
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			var Province = '';  //省
			var citys = '';		//城市
			var chirdens = [];
			var oVal = '';    //城市ID
			for(var i=0;i<data.length;i++){
				Province += '<option value="'+data[i].id+'">'+data[i].text+'</option>';
				chirdens.push(data[i].children);
			}
			for(var j=0;j<data[0].children.length;j++){
				citys += '<option value="'+data[0].children[j].id+'">'+data[0].children[j].text+'</option>';
			}
			$('#Province').html(Province);
			$('#citys').html(citys);
			oVal = $("#citys  option:selected").val();
			$('#Province').change(function(){
				citys = '';
				var _index = $('#Province').prop('selectedIndex');
				for(var j=0;j<chirdens[_index].length;j++){
					citys += '<option value="'+chirdens[_index][j].id+'">'+chirdens[_index][j].text+'</option>';
				}
				$('#citys').html(citys);
				oVal = $("#citys  option:selected").val();
				communityList(oVal);
			});
			$('#citys').change(function(){
				oVal = $("#citys  option:selected").val();
				communityList(oVal);
			});
			communityList(oVal);
		}
	});
}



//社区	
function communityList(province){  
	var reqUrl = '/supplier/provinceCommunity';
	var reqData = {'province':province};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				var aBodyList = data.obj;
				var options = '';
				
				for(var i=0;i<aBodyList.length;i++){
					options += '<option value="'+aBodyList[i].id+'">'+aBodyList[i].name+'</option>';
				}
				$('#CommunityList').html(options);
				communityId = $("#CommunityList  option:selected").val();
				$('#CommunityList').change(function(){
					communityId = $("#CommunityList  option:selected").val();
					serveTypeList(communityId);
				});

				serveTypeList(communityId);
			}else{
				ReminderArert(data.msg);
			}
		}
	});
}

//服务类型	
function serveTypeList(communityId){ 
	var reqUrl = '/supplier/getServiceTypeByCommunity';
	var reqData = {'communityId':communityId};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				var aBodyList = data.obj;
				var options = '';
				
				for(var i=0;i<aBodyList.length;i++){
					options += '<option value="'+aBodyList[i].id+'">'+aBodyList[i].name+'</option>';
				}
				$('#severaTypes').html(options);
				severashowDes = $("#severaTypes  option:selected").val();
				$('#severaTypes').change(function(){
					severashowDes = $("#severaTypes  option:selected").val();
				});
			}else{
				ReminderArert(data.msg);
			}
		}
	});
}


//验证是否可注册	
function checkExist(id,inputStr,flag){  
	var reqUrl = '/supplier/checkExist';
	var reqData = {'id':id,'inputStr':inputStr,'flag':flag};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(!data.success){
				if(flag=="1"){
					$("#email_").html("此邮箱已被使用");
				}else if(flag=="2"){
					$("#loginName_").html("此登录名已被使用");
				}else if(flag=="3"){
					$("#phone_").html("此手机号码已被使用");
				}
				
			}
		}
	});
}

//商家信息
function shopInfo(shopId){  
	var reqUrl = '/supplier/getComInfo';
	var reqData = {'usrId':shopId,'id':shopId};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				
				var src= $('#imgShow').attr("src");
				communityId = data.obj.communityId;
				modelId = data.obj.modelId;
				comLogo = data.obj.comLogo;
				$('#preview1').attr('src','/static/'+data.obj.comLogo);
				$('#community').val(data.obj.communityName);
				$('#severType').val(data.obj.modelName);
				$('#shopName').val(data.obj.comName);
				$('#loginName').val(data.obj.loginName);
				$('#comName').val(data.obj.comName);
				$('#comLogo').val(data.obj.comLogo);
				$('#logSrc').val(data.obj.comLogo);
				$('#address').val(data.obj.address);
				$('#phone').val(data.obj.phone);
				$('#tel').val(data.obj.tel);
				$('#email').val(data.obj.email);
				
				$('#comLogoSrc').val(data.obj.comLogo);
				$('#imgShow').attr("src",src+data.obj.comLogo);
				
				//$('#imgShow').attr("src","imgs/1429068175866.png");
				$('#commentInfo').val(data.obj.commentInfo);
				$('#modelId').val(data.obj.modelId);
				$('#communityId').val(data.obj.communityId);
			
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}

//保存商家信息
function saveComInfo(id,comName,comLogo,email,phone,address,tel,commentInfo,modelId,communityId){ 
	
	var reqUrl = '/supplier/saveComInfo';
	var reqData = {
			'usrId':id,
			'id':id,
			'comName':comName,
			'comLogo':comLogo,
			'email':email,
			'phone':phone,
			'address':address,
			'tel':tel,
			'commentInfo':commentInfo,
			'modelId':modelId,
			'communityId':communityId
		};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				ReminderArert(data.msg);
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}


//商品信息
function ComResourcesList(shopId){  
	var reqUrl = '/supplier/getComResourcesList';
	
	var reqData = {'usrId':shopId,'id':shopId};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				var oBodyList = data.obj;
				var HTMLlist = '';
				HTMLlist += '<div class="nav">';
			    HTMLlist += '<h1>商品图片</h1>';
			    HTMLlist += '<h1>状态</h1>';
			    HTMLlist += '<h1>商品描述</h1>';
			    HTMLlist += '<b class="listdo">操作</b>';
			    HTMLlist += '</div>';
			    
			    HTMLlist += '<form id="modifyResource" action="${ctx}/supplier/modifyResource" method="post"  enctype="multipart/form-data">';
			    HTMLlist += '<input type="text"  style="display: none;" name="comId" value="'+shopId+'">';
                	
//			    //展示已审核
				for(var i=0;i<oBodyList.length;i++){
					var mess="无数据";
					var status=oBodyList[i].status;
					
					if(status=='1')
						mess="已审核";
					
					if(status=='1'){
						HTMLlist += '<div class="show-one">';
	                    HTMLlist += '<p><img style="width:60px;height:70px" src="'+oBodyList[i].picUrl+'" alt="商品图片" class="one-p"></p>';
	                   // HTMLlist += '<p>'+mess+'</p>';
	                    HTMLlist +='<p id="'+oBodyList[i].id+"11"+'" style="width:60px;margin-top: 40px;margin-left: 60px;" >'+ mess+'</p>';
	                    HTMLlist += '<textarea readOnly=true style="width:550px;height:82px;display:block;float:left;margin:15px 0 0 50px;border:solid 1px #ABA9AA;">'+oBodyList[i].picDescription+'</textarea>';
	                    HTMLlist += '<div class="show-btn">';
	                    HTMLlist += '<button style="margin-left: -50px;" class="zheding" onclick="setResourceTop('+oBodyList[i].comId+','+oBodyList[i].id+')">置顶</button><button style="margin-left: -50px;" class="del" onclick="removeComRes('+oBodyList[i].comId+','+oBodyList[i].id+')">删除</button>';
	                    HTMLlist += '</div>';
	                    HTMLlist += '</div>';
	                    HTMLlist += '<div class="clear">';
	                    HTMLlist += '</div>';
					} 
				}
//				//展示未通过
				for(var i=0;i<oBodyList.length;i++){
					var mess="无数据";
					var status=oBodyList[i].status;
					//alert("kkk:"+status);
					
					if(status=='2')
						mess="未通过";
					if(status=='2'){
							HTMLlist += '<div class="show-one">';
							HTMLlist += '<p><img style=" border: 0px solid black;width:60px;height:70px" id="'+oBodyList[i].id+"1"+'" src="'+oBodyList[i].picUrl+'" alt="商品图片" class="one-p"><input name="newIcon" style="display: none;margin-left: 50px;"   disabled="true" id="'+oBodyList[i].id+"2"+'"  type="file" onchange="changeImg('+oBodyList[i].id+')"/></p>';
							HTMLlist += '<p id="'+oBodyList[i].id+"11"+'" style="width:60px;margin-top: 40px;margin-left: 60px;" >'+ mess+'</p>';
							HTMLlist += '<textarea name="picDescription" id="'+oBodyList[i].id+"3"+'" readOnly=true  style="width:550px;height:82px;display:block;float:left;margin:15px 0 0 50px;border:solid 1px #ABA9AA;">'+oBodyList[i].picDescription+'</textarea>';
							HTMLlist += '<div class="show-btn">';
							HTMLlist += '<input type="text" id="'+oBodyList[i].id+"7"+'" style="display: none;" name="status" value="2">';
			                HTMLlist += '<input type="text" id="'+oBodyList[i].id+"8"+'" style="display: none;" name="id" value="'+oBodyList[i].id+'">';
			                HTMLlist += '<input type="text" id="'+oBodyList[i].id+"9"+'" style="display: none;" name="picUrl" value="'+oBodyList[i].picUrl+'">';
							HTMLlist += '<input type="button" style="margin-left: -50px;" value="修改" id="'+oBodyList[i].id+"4"+'" class="zheding1" onclick="modifyComResources('+oBodyList[i].id+')" />'+
										'<input type="button"  style="margin-left: -50px;display: none" id="'+oBodyList[i].id+"5"+'" onclick="modifyComResourcesOk('+oBodyList[i].id+')" class="zheding1" value="确定" />'+
										'<input type="button" style="margin-left: -50px;" class="zheding1" onclick="removeComRes('+oBodyList[i].comId+','+oBodyList[i].id+')" value="删除" />';
							HTMLlist += '</div>';
							HTMLlist += '</div>';
							HTMLlist += '<div class="clear">';
							HTMLlist += '</div>';
	                    
					} 
				}
				
				//展示未审核
				for(var i=0;i<oBodyList.length;i++){
					var mess="无数据";
					var status=oBodyList[i].status;
					//alert("kkk:"+status);
					if(status=='0')
						mess="未审核";
					
					if(status=='0'){
						
						HTMLlist += '<div class="show-one">';
						HTMLlist += '<p><img style="width:60px;height:70px" id="'+oBodyList[i].id+"1"+'" src="'+oBodyList[i].picUrl+'" alt="商品图片" class="one-p"><input name="newIcon" style="display: none;margin-left: 50px;"   disabled="true" id="'+oBodyList[i].id+"2"+'"  type="file" onchange="changeImg('+oBodyList[i].id+')"/></p>';
						
	                    HTMLlist +='<p id="'+oBodyList[i].id+"11"+'" style="width:60px;margin-top: 40px;margin-left: 60px;" >'+ mess+'</p>';
	                    HTMLlist += '<textarea readOnly=true   name="picDescription"  id="'+oBodyList[i].id+"3"+'" style="width:550px;height:82px;display:block;float:left;margin:15px 0 0 50px;border:solid 1px #ABA9AA;">'+oBodyList[i].picDescription+'</textarea>';
	                    HTMLlist += '<div class="show-btn">';
	                    
	                    HTMLlist += '<input type="text" id="'+oBodyList[i].id+"7"+'" style="display: none;" name="status" value="0">';
	                    HTMLlist += '<input type="text" id="'+oBodyList[i].id+"8"+'" style="display: none;" name="id" value="'+oBodyList[i].id+'">';
	                    HTMLlist += '<input type="text" id="'+oBodyList[i].id+"9"+'" style="display: none;" name="picUrl" value="'+oBodyList[i].picUrl+'">';
	                    
	                    
	                    HTMLlist += '<input type="button" style="margin-left: -50px;" id="'+oBodyList[i].id+"4"+'" class="zheding1" value="修改" onclick="modifyComResources('+oBodyList[i].id+')">'+ 
	                    '<input type="button" style="margin-left: -50px; display: none" id="'+oBodyList[i].id+"5"+'" class="zheding1" value="确定" onclick="modifyComResourcesOk('+oBodyList[i].id+')">'+ 
	                    '<input type="button" style="margin-left: -50px;" class="zheding1" value="删除" onclick="removeComRes('+oBodyList[i].comId+','+oBodyList[i].id+')">';
	                    
	                    HTMLlist += '</div></div>';
	                    HTMLlist += '<div class="clear">';
	                    HTMLlist += '</div>';
					} 
				}
				HTMLlist += '</form>';
				
				//新增
				HTMLlist += '<div id="proImgDiv" class="show-one">';
				HTMLlist += '<form id="newResource" method="post" enctype="multipart/form-data">';
				
                HTMLlist += '<p><img src="" style="display:block;border: 0px ;width:60px ;height:70px ;" id="preview" alt="商品图片" class="one-p">'+
                			'<input style="margin-left: 50px;" id="f"  name="newIcon" type="file" onchange="change()"/></p>';
                
                HTMLlist += '<textarea id="picDescription" name="picDescription" style="width:550px;height:82px;display:block;float:left;margin:15px 0 0 170px;border:solid 1px #ABA9AA;"></textarea>';
                HTMLlist += '</form>';
                HTMLlist += '<div class="show-btn">';
                HTMLlist += '<button style="margin-left: -50px;" class="zheding" onclick="addComResources('+shopId+')">保存</button><button style="margin-left: -50px;" class="del" onclick="clearContent()">清空</button>';
                HTMLlist += '</div>';
                HTMLlist += '</div>';
                HTMLlist += '<div class="clear">';
                HTMLlist += '</div>';
				$('.shop-list').html(HTMLlist);
				//添加提交
				$('#newResource').form({
					url : '/supplier/addComResources',
					data:{'userid':shopId},
					onSubmit : function(param) {
						param.usrId = shopId;
						
						if(!$('#f').val()){
							ReminderArert("商品图片不能为空");
							return false;
						}if(!$('#picDescription').val()){
							ReminderArert("商品描述不能为空");
							return false;
						}
						return true;
					},
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
							ReminderArert( result.msg);
							ComResourcesList(shopId);
						}else{
							if(result.msg=='timeOut'){
								$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
							}else
							$.messager.alert('提示', result.msg, 'info');
						}
					}
				});
				//修改提交
				$('#modifyResource').form({
					url : '/supplier/modifyResource',
					data:{'id':shopId},
					onSubmit : function(param) {
						//param.usrId = id;
						return true;
					},
					success : function(result) {
						result = $.parseJSON(result);
						if (result.success) {
							//alert("id:"+comid);
							$('#'+comid+'2').hide();
							$('#'+comid+'11').text("未审核");
							$('#'+comid+'2').prop("disabled",true);
							$('#'+comid+'4').show();
							$('#'+comid+'5').hide();
							$('textarea').prop("readOnly",true);
							$('textarea').prop("disabled",false);
							$('#picDescription').prop("readOnly",false);
							
							ReminderArert( result.msg);
//							ReminderArert("商品信息添加成功");
							//ComResourcesList(shopId);
						}else{
							ReminderArert(result.msg);
						}
					}
				});
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}
//清空商品信息
function clearContent(){
	$("#newResource").form('clear');
	$("#preview").attr("src","");
}
//添加商品信息
function addComResources(shopId){
	$('#newResource').submit();
}
function modifyComResourcesOk(id){
	comid=id;//全局 商品id
	var mess=$('#'+id+'3').val();//新修改的 商品描述
	var url=$('#'+id+'2').val(); //新修改的 商品图片
	if(mess==comMess && ''==url){
		//alert("描述未修改");
		$('#'+id+'2').prop("disabled",true);
		$('#'+id+'2').hide();
		$('#'+id+'3').prop("readOnly",true);
		$('#'+id+'4').show();
		$('#'+id+'5').hide();
		
		
		return false;
	}
	
		
	//alert("ssssss~~");
	$('textarea').prop("disabled",true);
	$('input[name="id"]').prop("disabled",true);
	$('input[name="status"]').prop("disabled",true);
	$('input[name="picUrl"]').prop("disabled",true);
	$('#'+id+'3').prop("disabled",false);
	$('#'+id+'7').prop("disabled",false);
	$('#'+id+'8').prop("disabled",false);
	$('#'+id+'9').prop("disabled",false);
	//alert("id:"+id);
	$('#modifyResource').submit();
	//alert("ssss");
}
function modifyComResources(id){
	comMess=$('#'+id+'3').val();//全局 商品描述
	
	$('#'+id+'2').prop("disabled",false);
	$('#'+id+'2').show();
	$('#'+id+'3').prop("readOnly",false);
	//$('#'+id+'3').prop("disabled",false);
	
	
	if(!$('#'+id+'3').val()){
		ReminderArert("商品描述不能为空");
		return false;
	}
	$('#'+id+'5').show();
	$('#'+id+'4').hide();
	//alert("wwwww");
}


//顶置
function setResourceTop(shopId,id){  
	var reqUrl = '/supplier/setResourceTop';
	var reqData = {'usrId':shopId,'id':id};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				ComResourcesList(shopId);
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}


//删除
function removeComRes(shopId,id){  
	var reqUrl = '/supplier/deleteComResource';
	var reqData = {'usrId':shopId,'id':id};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				ComResourcesList(shopId);
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}

//订单信息
function orderList(shopId){  
	$('#yydataGrid').datagrid({
		url : '/supplier/getServiceOrderList?usrId='+shopId,
		striped : true,
		rownumbers : true,
		pagination : true,
		idField : 'id',
		singleSelect : true,
		fitColumns:false,
		sortName : 'actionDate',
		sortOrder : 'desc',
		pageSize : 50,
		pageList : [ 2, 3, 10, 40, 50, 100, 200, 300, 400, 500 ],
		columns : [ [ {
			width:'10',
			title : 'comName',
			field : 'comName',
			hidden : true
		},{
			width:'10',
			title : 'backInfo',
			field : 'backInfo',
			hidden : true
		},{
			width:'10',
			title : 'id',
			field : 'id',
			hidden : true
		}, {
			width:'110',
			title : '用户名',
			align:'center',
			field : 'userName'
		} , {
			width:'130',
			title : '手机号码',
			align:'center',
			field : 'userPhone'
		}/*, {
			width:'100',
			title : '社区',
			align:'center',
			field : 'communityName'
		}*/,  {
			width:'340',
			title : '地址',
			align:'center',
			field : 'roomName'
		}, {
			width:'200',
			title : '申请时间',
			align:'center',
			field : 'actionDate'
		}/*, {
			width:'250',
			title : '描述',
			align:'center',
			field : 'comment'
		}*/, {
			width:'120',
			title : '状态',
			align:'center',
			field : 'status',
			formatter : function(value, row, index) {
				switch (value) {
				case '1':
					return '<font color="#E1E100">待受理</font>';
				case '2':
					return '<font color="green">已受理</font>';	
				case '3':
					return '确认收货';	
				case '4':
					return '取消订单';	
				case '5':
					return '<font color="gray">拒绝受理</font>';
				}
			}
		}, {
			width:'80',
			field : 'action',
			title : '操作',
			align : 'center',
			formatter : function(value, row, index) {
					//str += '<a href="javascript:void(0)" class="zdyBtn" onclick="showFun('+row.userName+','+row.userPhone+','+row.communityName+','+row.roomName+','+row.comment+','+row.status+','+shopId+');" >查看</a>';
				var str='';
				if(row.status!='1'){
					str += '<a href="javascript:void(0)" class="zdyBtn" onclick="processFun(\''+row.id+'\',\''+row.comName+'\',\''+row.userName+'\',\''+row.userPhone+'\',\''+row.roomName+'\',\''+row.actionDate+'\',\''+row.status+'\',\''+row.provider+'\',\''+row.backInfo+'\','+shopId+');" >查看</a>';
				}else{
					str += '<a href="javascript:void(0)" class="zdyBtn" onclick="processFun(\''+row.id+'\',\''+row.comName+'\',\''+row.userName+'\',\''+row.userPhone+'\',\''+row.roomName+'\',\''+row.actionDate+'\',\''+row.status+'\',\''+row.provider+'\',\''+row.backInfo+'\','+shopId+');" >处理</a>';
				}
					/*str += '&nbsp;&nbsp;';	
					str += '<a href="javascript:void(0)" class="zdyBtn" onclick="receive('+row.id+','+shopId+',\''+row.comName+'\');" >受理</a>';
					str += '&nbsp;&nbsp;';	
					str += '<a href="javascript:void(0)" class="zdyBtn" onclick="reject('+row.id+','+shopId+');" >拒绝</a>';
					str += '&nbsp;&nbsp;';	*/
				return str;
			}
		}
		] ],
		onLoadSuccess:function(data){
			var total = data.total;
			if(data.total=='-1'){
				$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});			
			}
		}
	});
}


/*//订单信息
function orderList(shopId){  
	var reqUrl = '/supplier/getServiceOrderList';
	var reqData = {'usrId':shopId};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			var OrderList = '';
			if(data.success){
				//ComResourcesList(shopId);
				var oBodyList = data.obj;
				for(var i=0;i<oBodyList.length;i++){
					OrderList +='<tr>';
					OrderList += '<td style="display:none;">'+oBodyList[i].id+'</td>';
					OrderList += '<td>'+oBodyList[i].userName+'</td>';
					OrderList += '<td>'+oBodyList[i].userPhone+'</td>';
					OrderList += '<td>'+oBodyList[i].communityName+'</td>';
					OrderList += '<td>'+oBodyList[i].roomName+'</td>';
					OrderList += '<td>'+oBodyList[i].comment+'</td>';
					var status_ = oBodyList[i].status;
					switch (status_) {
					case '1':
						OrderList += '<td>待受理</td>';
						break;
					case '2':
						OrderList += '<td>已受理</td>';
						break;
					case '3':
						OrderList += '<td>确定收货</td>';
						break;
					case '4':
						OrderList += '<td>取消订单</td>';
						break;
					case '5':
						OrderList += '<td>拒绝受理</td>';
						break;
					}
					OrderList += '<td align="center"><a id="show" href="javascript:void(0)" onclick="showFun('+oBodyList[i].id+','+shopId+')" >查看</a>&nbsp;<a id="receive" href="javascript:void(0)" onclick="receive('+oBodyList[i].id+','+shopId+')" >受理</a>&nbsp;<a id="reject" href="javascript:void(0)" onclick="reject('+oBodyList[i].id+','+shopId+')"  >拒绝</a></td>';
					OrderList += '</tr>';
				}
				$('#yybody').html(OrderList);
				$('#receive').click(function(){  //受理
					var backInfo = '';
//					var id = this.parentNode.parentNode.getAttribute('attr');
					var id = this.parent().parent().children().eq(0).html();
					alsert(id);
					//orderProcess(shopId,id,'2',backInfo);
				});
				$('.reject').click(function(){  //拒绝
					var backInfo = '';
					var id = this.parentNode.parentNode.getAttribute('attr');
					var oMask = '<div id="oMask">'
				    	+'<div id="orderMask">'
				    	+'<h3>拒绝原因：</h3>'
				    	+'<textarea></textarea>'
				    	+'<p><a href="javascript:;" class="okMask">确定</a>'
				    	+'<a href="javascript:;" class="cancelMask">取消</a></p>'
				    	+'</div></div>';
						$('body').append(oMask);
					$('.okMask').click(function(){
						$(this).parent().parent().parent().remove();
						backInfo = $('textarer').text();
						orderProcess(shopId,id,'5',backInfo);
					});
					$('.cancelMask').click(function(){
						$(this).parent().parent().parent().remove();
					});
					
				});
			}else{
				ReminderArert(data.msg);
			}
		}
	});
}*/



//订单处理
/*function showFun(comment,backInfo,shopId) {
	$("#commId").html('');
	$("#backInfoId").html('');
	$("#commId").html('<h4 style="font-size: 15px">预约内容:</h4><div id="comment_td" style="word-wrap: break-word; margin-left: 8px"><textarea readonly="readonly" style="width:550px;height:100px;">'+comment+'</textarea></div>');
	if(backInfo!='undefined'){
		$("#backInfoId").html('<h4 style="font-size: 15px">反馈内容:</h4><div id="backInfo_td" style="word-wrap: break-word; margin-left: 8px"><textarea readonly="readonly" style="width:550px;height:100px;">'+backInfo+'</textarea></div>');
	}
	$("#yyDetail").show();
	$("#yyDetail").dialog({    
	    title: '查看',    
	    width: 600,    
	    height: 300,    
	    closed: false,    
	    cache: false,      
	    modal: true,
	    buttons:[{
			text:'返回',
			handler:function(){$("#yyDetail").dialog('close');}
		}]
	});   
}*/
function processFun(id,comName,userName,userPhone,roomName,actionDate,status,provider,backInfo,shopId){
	//清空上一次数据
	$("#proName").html('');
	$("#proPhone").html('');
	$("#proRoom").html('');
	$("#proDate").html('');
	$("#proStatus").html('');
	$("#orderContent").html('');
	$("#proServiceCom").html('');
	$("#processComTr").html('');
	$("#declineTr").html('');
	
	//数据初始化
	$.ajax({
		url : '/supplier/orderDetail', 
		dataType : 'json',
		data : "id="+id+"&usrId="+shopId,
		success : function(result){ 
			if(result.success){
				$("#proName").html(result.obj.userName);
				$("#proPhone").html(result.obj.userPhone);
				$("#proRoom").html(roomName);
				$("#proDate").html(result.obj.actionDate);
				$("#proServiceCom").html(result.obj.comName);
				$("#orderContent").html(result.obj.comment);
			}else{
				if(result.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else{
					
				}
			}
			
		} 
	});
	
	var status_='';
	var title='';
	switch(status){
	case '1':
	{
		status_='待受理';
		title='预约处理';
		$("#processComTr").html('<input id="processCom" style="width:550px;height:88%" value="'+comName+'">');
	}
	break;
	case '2':
	{
		status_='已受理';
		title='查看';
		$("#processComTr").html(provider);
	}
	break;
	case '5':
	{
		status_='拒绝受理';
		title='查看';
		$("#processComTr").html('');
		//$("#declineTr").html('<th>拒绝原因</th><td colspan="3"><textarea id="declineContent" style="width:550px;height:100px;"></textarea></td>');
		//$("#declineContent").html(backInfo);
	}
	break;
	}
	$("#proStatus").html(status_);
	if(status=='1'){
		$("#processOrder").show();
		$("#processOrder").dialog({    
			title: title,    
			width: 700,    
			height: 350,    
			closed: false,    
			cache: false,      
			modal: true,
			buttons:[{
				text:'受理',
				handler:function(){if(!$("#processCom").val()){ReminderArert("请填写处理机构");return;}orderProcess(shopId,id,'2',$("#processCom").val());}
			},{
				text:'拒绝',
				handler:function(){orderProcess(shopId,id,'5','');}
			},{
				text:'关闭',
				handler:function(){$("#processOrder").dialog('close');}
			}]
		}); 
	}else{
		$("#processOrder").show();
		$("#processOrder").dialog({    
			title: title,    
			width: 700,    
			height: 350,    
			closed: false,    
			cache: false,      
			modal: true,
			buttons:[{
				text:'关闭',
				handler:function(){$("#processOrder").dialog('close');}
			}]
		}); 
	}
}
function editpwd(){
	//alert("www");
	$("#pwd1").val("");
	$("#pwd2").val("");
	$("#pwd3").val("");
	
	$(".c1").hide();
	$(".c2").hide();
	$(".c3").hide();
	
	var status_='';
	var title='';
	if(status=='1'){}else{
		$("#JKDiv_4").show();
		$("#JKDiv_4").dialog({    
			title: title,    
			width: 730,    
			height: 500,    
			closed: false,    
			cache: false,      
			modal: true,
			
			buttons:[{
				text:'确定',
				handler:function(){passwordBtn();}
			},{
				text:'返回',
				handler:function(){$("#JKDiv_4").dialog('close');}
			}]
		}); 
	}
}


/*function receive(id,shopId,comName){
	$("#provider").val(comName);
	$("#yysl").show();
	$("#yysl").dialog({    
	    title: '预约受理',    
	    width: 400,    
	    height: 200,    
	    closed: false,    
	    cache: false,      
	    modal: true,
	    buttons:[{
			text:'受理',
			handler:function(){if(!$("#provider").val()){ReminderArert("服务提供商不能为空");return;}$("#yysl").dialog('close');orderProcess(shopId,id,'2',$("#provider").val());}
		}]
	});  
	//orderProcess(shopId,id,'2','');
}*/

/*function reject(id,shopId){
	$("#yyjj").show();
	$("#yyjj").dialog({    
	    title: '拒绝受理',    
	    width: 400,    
	    height: 200,    
	    closed: false,    
	    cache: false,      
	    modal: true,
	    buttons:[{
			text:'拒绝',
			handler:function(){$("#yyjj").dialog('close');orderProcess(shopId,id,'5',$("#feedBack").val());}
		}]
	});    
}*/
/*function reject(id,shopId){
	var oMask = '<div id="oMask"  class="easyui-dialog">'
    	+'<div id="orderMask">'
    	+'<h3>拒绝原因：</h3>'
    	+'<textarea></textarea>'
    	+'<p><a href="javascript:;" id="okMask">确定</a>'
    	+'<a href="javascript:;" id="cancelMask">取消</a></p>'
    	+'</div></div>';
	$('body').append(oMask);
	$('#okMask').click(function(){
		$(this).parent().parent().parent().remove();
		backInfo = $('textarer').text();
		orderProcess(shopId,id,'5',backInfo);
	});
	$('#cancelMask').click(function(){
		$(this).parent().parent().parent().remove();
	});
}*/

function orderProcess(shopId,id,status,backInfo){  
	var reqUrl = '/supplier/orderProcess';
	var reqData = {'usrId':shopId,'id':id,'status':status,'backInfo':backInfo};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				ReminderArert(data.msg,function(){$("#processOrder").dialog('close');orderList(shopId);});
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}

//获得商家评论
function getComDiscuss(shopId){  
	var reqUrl = '/supplier/getComDiscuss';
	var reqData = {'usrId':shopId,'id':shopId};
 
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				var oBodyList = data.obj;
				var strTitalHTML = '';
				var strHTML = '';
				var sum = '';
				
				//评论信息
				for(var i=0;i<oBodyList.length;i++){
					var j = oBodyList[i].serviceLevel;
					//统计星星总数，以便计算平均值
					sum =Number(sum)+Number(j);
				    var sy = 5-j;
					strHTML += '<div class=\"ul-box\" id=\"sjplList\" style=\"margin:0 0 20px 0;\">';
					strHTML += '<span class="" style="padding: 20px 59px 0 49px;margin-top: 10px;">';
					strHTML +=  oBodyList[i].userName+'</span>';
					strHTML +=  '<span style="padding: 10px 180px 0 0;">';
					strHTML +=  oBodyList[i].date+'</span>';
					strHTML += '<span class=\"star-wu\" style=\"\" >';
					
					for(var m=0;m<j;m++){
						strHTML += '<img src=\"/style/img/star-on.png\">';
					};
					for(var n=0;n<sy;n++){
						 strHTML += '<img src=\"/style/img/star-off.png\">';
					};
					
					strHTML += '</span> <br/><br/><br/>';
					strHTML += '<div style="height:auto !important;"><p style="padding: 0 0 0 49px;">';
					strHTML += oBodyList[i].content;
					strHTML += '</p></div></div>';
					strHTML += '<hr/><br/><br/><br/>';
				};
				
				//评论头信息
		    	strTitalHTML+='<div style=\"height: 41px;line-height:41px;background: #f5f5f5;margin:0 0 20px 0;\">';
		    	strTitalHTML+= '<p style="float: left;margin-left: 47px;">状态:';
		    	if("2"==state){
		    		strTitalHTML+= '启用</p>';
		    	}else{
		    		strTitalHTML+= '停用</p>';
		    	}
		    	strTitalHTML+= '<div class=\"star-wu\" style=\"float: right;padding: 0 56px 0 0;\">';
		        
		    	var ttpj1 = sum/oBodyList.length;
		    	var ttpj = Math.ceil(ttpj1);
		    	var ttsy = 5-ttpj;
		    	for(var k=0;k<ttpj;k++){
		    		strTitalHTML += '<img src=\"/style/img/star-on.png\">';
		    	}
		    	for(var q=0;q<ttsy;q++){
		    		strTitalHTML += '<img src=\"/style/img/star-off.png\">';
		    	}
		    	
		    	
		    	$('#commentTitalList').html(strTitalHTML);
				$('#commentList').html(strHTML);
				//showStarFn();
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}
//setInterval('fn()',2000);
function showStarFn(){
	var oUl = $('.commentGradeList');
	for(var i=0;i<oUl.length;i++){
		var arr = oUl[i].getAttribute('attr');
		var aLi = oUl[i].getElementsByTagName('li');
		for(var j=0;j<arr;j++){
			aLi[j].className = 'active';
			
		}
	}
}


function getStatus(shopId){  
	var reqUrl = '/supplier/getStatus';
	var reqData = {'usrId':shopId,'id':shopId};
	$.ajax({
		url:reqUrl,
		type : 'post',
		data:reqData,
		dataType : "json",
		success: function(data){  
			if(data.success){
				var oNum = data.obj.stars;
				var status = '';
				if(data.obj.status == '2'){
					status = '正常';
				}else if(data.obj.status == '3'){
					status = '禁用';
				}
				$('.gradel_title i').html(status);
				allStar(oNum);
			}else{
				if(data.msg=='timeOut'){
					$.messager.alert('提示', '您还没有登录或登录已超时，请重新登录，然后再刷新本功能！', 'error',function(){window.location.href='/admin/shopLogin';});
				}else
				$.messager.alert('提示', data.msg, 'info');
			}
		}
	});
}

function allStar(oNum){
	// var num = '2.1';
	 var aList = $('.gradeList li');
	 var str = '';
	 str =  oNum.split('.')[0];
	 var str2 =  oNum.split('.')[1];
	 if(str2>5){
		 str = parseInt(str) + 1;
		 str2 = 0;
	 }else if(str2>0 && str2<5){
		 str2 = 5;
	 }
	 oNum = str +'.'+str2;
	 
	 for(var i=0;i<oNum*2;i++){
		 aList[i].className = 'active';
	 }
}

//信息校验
function checkEmail(value){
	if(!value){
		$("#email_").html("请输入邮箱");
	}
	else{
		$("#email_").html("");
		checkExist("",value,"1");
	}
}
function checkName(value){
	if(!value){
		$("#name_").html("请输入商家名称");
	}else{
		$("#name_").html("");
	}
}
function checkAddress(value){
	if(!value){
		$("#address_").html("请输入地址");
	}else{
		$("#address_").html("");
	}
}
function checkLoginName(value){
	if(!value){
		$("#loginName_").html("请输入用户名");
	}
	else{
		var rex = /^(([0-9])|([a-z])|([A-Z])|(_)){6,30}$/;
		if(!rex.test(value)){
			$("#loginName_").html("用户名必须是6至30位的数字、字母或下划线");
		}else{
			$("#loginName_").html("");
			checkExist("",value,"2");
		}
	}
}
function checkPhone(value){
	if(!value){
		$("#phone_").html("请输入手机号码");
	}else{
		var rex=/^1[3-8]\d{9}$/;
		if(!rex.test(value)){
			$("#phone_").html("手机号码输入错误");
		}else{
			$("#phone_").html("");
			checkExist("",value,"3");
		}
	}
}
function checkTel(value){
	if(value){
		  var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;//(固定电话号码)
          var rex3=/^400\d{0,1}-\d{2,4}-\d{2,4}$/;//400电话：400-***-****
          var rex21=/^((0\d{2,3}))(\d{7,8})((\d{3,}))?$/;//(不带横杠的固定电话号码)
          var rex31=/^400\d{0,1}\d{2,4}\d{2,4}$/;//(不带横杠的400电话号码)
          if(rex2.test(value)||rex3.test(value)||rex21.test(value)||rex31.test(value)){
        	  $("#tel_").html("");
          }else{
        	  $("#tel_").html("座机号码输入错误");
          }
	}else{
		 $("#tel_").html("");
	}
}
function checkPassword(value){
	if(!value){
		$("#password_").html("请输入密码");
	}else{
		var rex=/^[^\u4e00-\u9fa5]{6,18}$/;
		if(!rex.test(value)){
			$("#password_").html("密码必须是6至18位的非中文字符");
		}else
		$("#password_").html("");
	}
}
function checkCertPassword(value){
	var pas = $("#password").val();
	if(!value){
		$("#certPassword_").html("请输入确认密码");
	}else if(pas!=value){
		$("#certPassword_").html("密码与确认密码不一致");
	}else{
		$("#certPassword_").html("");
	}
}
function checkCommentInfo(value){
	if(!value){
		$("#comment_").html("请输入商家简介");
	}else{
		$("#comment_").html("");
	}
}

function checkVerify(value){
	if(!value){
		$("#verify_").html("请输入验证码");
	}else{
		$("#verify_").html("");
	}
}

Date.prototype.format = function(format){ 
	var o = { 
	"M+" : this.getMonth()+1, //month 
	"d+" : this.getDate(), //day 
	"h+" : this.getHours(), //hour 
	"m+" : this.getMinutes(), //minute 
	"s+" : this.getSeconds(), //second 
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
	"S" : this.getMilliseconds() //millisecond 
	} 

	if(/(y+)/.test(format)) { 
	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
	if(new RegExp("("+ k +")").test(format)) { 
	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	} 
	} 
	return format; 
	}
