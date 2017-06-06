;var currentuser;


function loadjscssfile(filename, filetype){
if (filetype=="js"){
var fileref=document.createElement('script');
fileref.setAttribute("type","text/javascript");
fileref.setAttribute("src", filename);
}
else if (filetype=="css"){
var fileref=document.createElement("link");
fileref.setAttribute("rel", "stylesheet");
fileref.setAttribute("type", "text/css");
fileref.setAttribute("href", filename);
}
if (typeof fileref!="undefined")
document.getElementsByTagName("body")[0].appendChild(fileref);
}

function removejscssfile(filename, filetype){
var targetelement=(filetype=="js")? "script" : (filetype=="css")? "link" : "none";
var targetattr=(filetype=="js")? "src" : (filetype=="css")? "href" : "none";
var allsuspects=document.getElementsByTagName(targetelement);
for (var i=allsuspects.length; i>=0; i--){
if (allsuspects[i] && allsuspects[i].getAttribute(targetattr)!=null && allsuspects[i].getAttribute(targetattr).indexOf(filename)!=-1)
   allsuspects[i].parentNode.removeChild(allsuspects[i]);
}
}



function notigreen(title,content){
 $(".notigreen h1").text(title);
 $(".notigreen p").text(content);
 $(".notigreen").fadeIn(300,function(){
  setTimeout('$(".notigreen").fadeOut(300)',1000);
 });
}
function notiblue(title,content){
 $(".notiblue h1").text(title);
 $(".notiblue p").text(content);
 $(".notiblue").fadeIn(300,function(){
  setTimeout('$(".notiblue").fadeOut(300)',1500);
 });
}
function notired(title,content){
 $(".notired h1").text(title);
 $(".notired p").text(content);
 $(".notired").fadeIn(300,function(){
  $(".notired").unbind().click(function(){
    $(this).hide();
  });//---click
 });//----fadein
}


function kongge(m){
    while((m.length>0)&&(m.charAt(0)==' '))  
    m = m.substring(1,m.length);
    while((m.length>0)&&(m.charAt(m.length-1)==' '))  
    m = m.substring(0, m.length-1);
    return m;  
}
function numonly(){
$("input[name='numonly']").unbind("keyup").keyup(function(){  
            $(this).val($(this).val().replace(/[^0-9.]/g,''));
        }).bind("paste",function(){
            $(this).val($(this).val().replace(/[^0-9.]/g,''));   
        }).css("ime-mode", "disabled"); 
}

function select(sId,text){  
    var s = document.getElementById(sId);  
    var ops = s.options;  
    for(var i=0;i<ops.length; i++){  
        //var tempValue = ops[i].value;
        var tempText = ops[i].text;
        if(tempText == text) 
        {  
            ops[i].selected = true;
            break; 
        }  
    }  
}

function checkimg(tutu){

 var str=document.getElementById(tutu).value;
 var pos = str.lastIndexOf(".");
 var lastname = str.substring(pos,str.length)
 if (!(lastname.toLowerCase()==".jpg" || lastname.toLowerCase()==".png" || lastname.toLowerCase()==".jpeg" || lastname.toLowerCase()==".bmp"))
 {
     //alert("您上传的文件类型为"+lastname+"，文件必须为静态图片格式");
     return false;
 }
  return true;

}
// function check(sId,text){  
//     var s = document.getElementById(sId);  
//     var ops = s.options;  
//     for(var i=0;i<ops.length; i++){  
//         //var tempValue = ops[i].value;
//         var tempText = ops[i].text;
//         if(tempText == text) 
//         {  
//             ops[i].selected = true;
//             break; 
//         }  
//     }  
// }

function loading(){
    $(".lzloader").stop().fadeIn(0);
}
function noading(){
    $(".lzloader").fadeOut(500);
    if(typeof(bangding) != "undefined"){
        bangding();
    }
    numonly();
}




$("#nav li a,#bodynav a").click(function(){
    loading();
})













function lzl(){
$.fn.getHexBackgroundColor = function() { var rgb = $(this).css('background-color'); if(!$.browser.msie){ rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/); function hex(x) {return ("0" + parseInt(x).toString(16)).slice(-2);} rgb= "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]); } return rgb; }
// $(".ziye-checkbox").click(function(){alert();
//     var thiss = $(this);
//     var thischeck = thiss.children("span");
//     var checkcolor = thischeck.getHexBackgroundColor();alert();

//     if(checkcolor=="#c0c0c0"){thischeck.addClass("buttonRed");alert("11");}
//     else{thischeck.removeClass("buttonRed");alert("22");}

//     //var checkcolor = thischeck.css('background-color');alert(checkcolor);
//     //$(this).children("span").css("background","#ec693f");
// })
}




  function lzltan() {

    //var overlay = document.querySelector( '.md-overlay' );
    var overlay = $(".md-overlay");

    [].slice.call( document.querySelectorAll( '.md-trigger' ) ).forEach( function( el, i ) {

      var modal = document.querySelector( '#' + el.getAttribute( 'data-modal' ) ),
        close = modal.querySelector( '.md-close' );

      function removeModal( hasPerspective ) {
        classie.remove( modal, 'md-show' );

        if( hasPerspective ) {
          classie.remove( document.documentElement, 'md-perspective' );
        }
      }

      function removeModalHandler() {
        removeModal( classie.has( el, 'md-setperspective' ) ); 
      }

      el.addEventListener( 'click', function( ev ) {
        classie.add( modal, 'md-show' );
        overlay.addClass('md-overlayshow');
        overlay.removeEventListener( 'click', removeModalHandler );
        overlay.addEventListener( 'click', removeModalHandler );

        if( classie.has( el, 'md-setperspective' ) ) {
          setTimeout( function() {
            classie.add( document.documentElement, 'md-perspective' );
          }, 25 );
        }
      });

      close.addEventListener( 'click', function( ev ) {
        ev.stopPropagation();
        removeModalHandler();
        overlay.removeClass('md-overlayshow');
      });

    } );

  }




//*************************mail start ***********************//
(function($){
    $.fn.autoMail = function(options){
        var opts = $.extend({}, $.fn.autoMail.defaults, options);
        return this.each(function(){
            var $this = $(this);
            var o = $.meta ? $.extend({}, opts, $this.data()) : opts;
            
            var top = $this.offset().top;
            var left = $this.offset().left;
            //var $mailBox = $('<div id="mailBox" style="top:'+top+'px;left:'+left+'px;width:'+$this.width()+'px"></div>');
            var $mailBox = $('<div id="mailBox"></div>');
            $('.mailyz').append($mailBox);
            
            //设置高亮li
            function setEmailLi(index){
                $('#mailBox li').removeClass('cmail').eq(index).addClass('cmail');
            }
            //初始化邮箱列表
            var emails = o.emails;
            var init = function(input){
                //取消浏览器自动提示
                input.attr('autocomplete','off');
                //添加提示邮箱列表
                if(input.val()!=""){
                    var emailList = '<p>请选择邮箱类型</p><ul>';
                    for(var i = 0; i < emails.length; i++) {
                        emailList += '<li>'+input.val()+'@'+emails[i]+'</li>';
                    }
                    emailList += '</ul>';
                    $mailBox.html(emailList).show(0);
                }else{
                    $mailBox.hide(0);
                }
                //添加鼠标事件
                $('#mailBox li').hover(function(){
                    $('#mailBox li').removeClass('cmail');
                    $(this).addClass('cmail');
                },function(){
                    $(this).removeClass('cmail');
                }).click(function(){
                    input.val($(this).html());
                    $mailBox.hide(0);
                });
            }
            //当前高亮下标
            var eindex = -1;
            //监听事件
            $this.focus(function(){
                if($this.val().indexOf('@') == -1){
                    init($this);
                }else{
                    $mailBox.hide(0);
                }
            }).blur(function(){
                setTimeout(function(){
                    $mailBox.hide(0);
                },1000);//
            }).keyup(function(event){
                if($this.val().indexOf('@') == -1){
                    //上键
                    if(event.keyCode == 40){
                        eindex ++;
                        if(eindex >= $('#mailBox li').length){
                            eindex = 0;
                        }
                        setEmailLi(eindex);
                    //下键
                    }else if(event.keyCode == 38){
                        eindex --;
                        if(eindex < 0){
                            eindex = $('#mailBox li').length-1;
                        }
                        setEmailLi(eindex);
                    //回车键
                    }else if(event.keyCode == 13){
                        if(eindex >= 0){
                            $this.val($('#mailBox li').eq(eindex).html());
                            $mailBox.hide(0);
                        }
                    }else{
                        eindex = -1;
                        init($this);
                    }
                }else{
                    $mailBox.hide(0);
                }
            //如果在表单中，防止回车提交
            }).keydown(function(event){
                if(event.keyCode == 13){
                    return false;
                }
            });
        });
    }
    $.fn.autoMail.defaults = {
        emails:[]
    }
})(jQuery);

//*************************mail end ***********************//











$(document).ready(function(){
    $(".widget-content table th:eq(0):has(input)").css("width","60px");
    //$(".fugouxuan").css({"":""});
    $(".sidebar-zi a").removeAttr("href");
});//排版








/**
 * 分页查询
 */
//搜索数据
function searchData(reqType,url,fn){
//	var jsonStr= JSON.stringify(qp);
 	var name= $.cookieHelper('UName')==null?'':$.cookieHelper('UName');
	var data={};
	for(var key in searchParams ){
		data[key]=searchParams[key];
	}
	for(var key in pageParams ){
		data[key]=pageParams[key];
	}
    //get方式时对值进行编码，防止中文乱码
    if(reqType=='GET'){
        if(typeof data=='object'){
         for (var key in data) {
             var enValue=encodeURIComponent(data[key]);
             data[key]=enValue;
         }
       }
     }
	var head={UName:name};
    $.ajax({
        type: reqType,
        url: url,
        data: data,
        headers:head,
        contentType:'application/json; charset=utf-8',
        dataType: "json",
        beforeSend:function(){
            loading();
        },
        complete:function(XMLHttpRequest, textStatus){
                
            eval("var data="+XMLHttpRequest.responseText);
            if(data.isSuccess==false){
            	if(data.message=='没有登录'){
            		$.cookieHelper('UName',null);
            	window.location.href="login.html";
            	}else{
            		if(data.message.indexOf('没有权限')!=-1){
		            	notired("温馨提示：",data.message);
		            	return;
		            }
	                notiblue("温馨提示",data.message);
            	}
            }
            else{
                //正常数据处理
                fn.call(this,data.itemList,data.count);
            }
        }
    });
}
//上一页
function qianPage(page,limit,reqType, url){
    var page=currentPage-1;
    tiaozhuanPage(page,limit,reqType, url);
}
//下一页
function houPage(page,limit,reqType, url){
    var page=currentPage+1;
    tiaozhuanPage(page,limit,reqType, url);
}
//pageParams start limit ，searchParams 查询条件 currentPage 当前页面
var pageParams={},searchParams={},currentPage;
//跳转指定页
function tiaozhuanPage(page,limit,reqType, url){
    pageParams.pageNum=page;
    pageParams.limit=limit;
    
    searchData(reqType, url, function(list,count){
        //1 删除原表格数据
        $("#tableData").html('');
        //2调整 分页栏（上一页下一页 总页数）       
        currentPage=parseInt(page);
        $('#currentPage').html("第"+currentPage+"页");
        var countPage=Math.ceil(count/limit);
        $('#countPage').html("共"+countPage+"页 "+count+"条数据");
        //3更新新数据
        updateData(list);
        //如果 是第一个和最后一页
        if(currentPage==1 && currentPage==countPage){
            $('#qianBtn').unbind().removeAttr("href").parent().addClass("disabled");
            $('#houBtn').unbind().removeAttr("href").parent().addClass("disabled");
        }else if(currentPage!==1 && currentPage==countPage){
            $('#qianBtn').unbind().attr("href","").parent().removeClass("disabled");
            $('#houBtn').unbind().removeAttr("href").parent().addClass("disabled");

            $('#qianBtn').unbind().bind('click',function(){
                qianPage(page,limit,reqType, url);
                return false;
            });
        }else if(currentPage==1 && currentPage!==countPage){
            $('#qianBtn').unbind().removeAttr("href").parent().addClass("disabled");
            $('#houBtn').unbind().attr("href","").parent().removeClass("disabled");
            
            $('#houBtn').unbind().bind('click',function(){
                houPage(page,limit,reqType, url);
                return false;
            });
        }else{
            $('#qianBtn').unbind().attr("href","").parent().removeClass("disabled");
            $('#houBtn').unbind().attr("href","").parent().removeClass("disabled");

            $('#qianBtn').unbind().bind('click',function(){
                qianPage(page,limit,reqType, url);
                return false;
            });

            $('#houBtn').unbind().bind('click',function(){
                houPage(page,limit,reqType, url);
                return false;
            });
        }
        //设置监听
        $('#tiaozhuanBtn').unbind().bind('click',function(){
            var page= $('#tiaozhuanPage').val();
            if(page>=1&&page<=countPage&&page!=currentPage){
                tiaozhuanPage(page,limit,reqType, url); 
            }
        });
    });
    noading();
}

/**
 * 添加 修改 删除
 */ 
 function generalAjax(params,reqType,url,fn,fn2,headers){
 	var head={};
 	if(headers){
 		head=headers;
 	}else{
	 	var name= $.cookieHelper('UName')==null?'':$.cookieHelper('UName');
		var head={UName:name};
 	}
    //get方式时对值进行编码，防止中文乱码
    if(reqType=='GET'){
        if(typeof params=='object'){
         for (var key in params) {
             var enValue=encodeURIComponent(params[key]);
             params[key]=enValue;
         }
       }
     }
// 	var jsonStr= JSON.stringify(params);
        $.ajax({
            type: reqType,
            url: url,
            data: params,
            async:false,
//            contentType:'application/json; charset=utf-8',
            headers:head,
            dataType: "json",
            beforeSend:function(){
            },
            complete:function(XMLHttpRequest, textStatus){
                
                eval("var data="+XMLHttpRequest.responseText);
                if(data.isSuccess==false){
                	if(data.message=='没有登录'){
            			$.cookieHelper('UName',null);
            			window.location.href="login.html";
            		}else{
	                	 if(data.message.indexOf('没有权限')!=-1){
			            	notired("温馨提示：",data.message);
			            	return;
			            }
	                	if(fn2){
	                		 fn2.call(this,data.message);
	                	}else{
		                    notired("温馨提示：",data.message);
	                	}
            		}
                }
                else{
                    
                    //请求成功处理
                    fn.call(this,data);
                }
            }
        });
}
/**
 * 上传文件
 */
function uploadAjax(params,url,fileId,fn,fn2,headers){
 	var head={};
 	if(headers){
 		head=headers;
 	}else{
	 	var name= $.cookieHelper('UName')==null?'':$.cookieHelper('UName');
		var head={UName:name};
 	}
    /**
	 * 上传文件
	 */
	 $.ajaxFileUpload({
	     url: url, //用于文件上传的服务器端请求地址
	     secureuri: false, //是否需要安全协议，一般设置为false
	     fileElementId: fileId, //文件上传域的ID
	     data:params,
	     headers:head,
	     dataType: 'json', //返回值类型 一般设置为json
	     success:function(data, textStatus){
		  
	     	if(data.IsSuccess==false){
                	if(data.Message=='没有登录'){
            			$.cookieHelper('UName',null);
            			window.location.href="login.html";
            		}else{
            			if(data.message.indexOf('没有权限')!=-1){
			            	notired("温馨提示：",data.message);
			            	return;
			            }
	                	if(fn2){
	                		 fn2.call(this,data.Message);
	                	}else{
		                    notired("温馨提示：",data.Message);
	                	}
            		}
            }
            else{
                
                //请求成功处理
                fn.call(this,data);
            }
		 },
	     error: function (data, status, e){//服务器响应失败处理函数
	        notired("添加失败","连接遇到错误");
	     }
	 });
}
/**
 * 界面调整
 */
//隐藏增加和修改窗口
 $("#addWin").hide();
 $("#editWin").hide();
//设置显示或隐藏窗口
$("#addWinBtn").unbind().click(function(){
	showOrHideWin("addWin");
    $("#editWin").hide();
});
//显示或消失
function showOrHideWin(id){
	var $wcontent = $("#"+id);
	
	if($wcontent.is(':visible')) 
	{
	  $(this).children('i').removeClass('icon-chevron-up');
	  $(this).children('i').addClass('icon-chevron-down');
	}
	else 
	{
	  $(this).children('i').removeClass('icon-chevron-down');
	  $(this).children('i').addClass('icon-chevron-up');
	}            
	$wcontent.toggle(500);
}
//显示
function showWin(id){
	var $wcontent = $("#"+id);
	
//	if($wcontent.is(':visible')) 
//	{
//	  $(this).children('i').removeClass('icon-chevron-up');
//	  $(this).children('i').addClass('icon-chevron-down');
//	}
//	else 
//	{
	  $(this).children('i').removeClass('icon-chevron-down');
	  $(this).children('i').addClass('icon-chevron-up');
//	}            
	$wcontent.toggle(500);
}
/**
 * 加载select数据列表
 * type ->project reference
 */
 function loadSelectList(selectId,url,param,reqType,idName,isTishi){
 	var type="GET";
 	if(reqType){
 		type=reqType;
 	}
 	var name= $.cookieHelper('UName')==null?'':$.cookieHelper('UName');
	var head={UName:name};
// 	var jsonStr= JSON.stringify(param);
    //$("#"+selectId).html("");//20150105添加
	  $.ajax({
	    type: type,
	    url: url,
	    async:false,
	    data: param,
	    headers:head,
	     contentType:'application/json; charset=utf-8',
	    dataType: "json",
	    beforeSend:function(){
	    },
	    complete:function(XMLHttpRequest, textStatus){
	      
	        eval("var data="+XMLHttpRequest.responseText);
	        if(data.message=='没有登录'){
            	$.cookieHelper('UName',null);
            	window.location.href="login.html";
            	return;
            }
            if(data.message.indexOf('没有权限')!=-1){
            	notired("温馨提示：",data.message);
            	return;
            }
	        var fir= $("#"+selectId).children().get(0);
	        $("#"+selectId).html('');
	        if(isTishi){
	        	$("#"+selectId).append(fir);
		    }
	        //map和数组的情况
	        var list=data.itemList? data.itemList:data;
	        for(var idx in list){
	          var isIdName= idName==undefined;
	          var idkey=isIdName?'id':idName.id,nameKey=isIdName?'name':idName.name;
	          var val= list[idx][idkey],name=list[idx][nameKey];
	          $("#"+selectId).append("<option value="+val+">"+name+"</option>");
	        }
	      }});
 }
/**
 * 加载联动的select
 * selectParamArr : [{selectId:'',url:'', idName:{id:'',name:''}},{},{}]
 */
function loadLinkageSelectList(selectParamArr){
	var url0=selectParamArr[0].url;
	var selectId0=selectParamArr[0].selectId;
	var idName0=selectParamArr[0].idName;
	//加载第一个select的数据
	generalAjax('',"GET",url0,function(data){
	       $("#"+selectId0).html('');
	       //添加option数据
	       var list=data.itemList? data.itemList:data;
	       var idkey=idName0.id,nameKey=idName0.name;
	       for(var idx in list){
	          var val= list[idx][idkey],name=list[idx][nameKey];
	          $("#"+selectId0).append("<option value="+val+">"+name+"</option>");
	       }
	       //设置第一个至倒数第二个select的click监听
	       for (var i = 0; i< selectParamArr.length-1; i++) {
	       	var url=selectParamArr[i+1].url;
			var pSelectId=selectParamArr[i].selectId;
			var selectId=selectParamArr[i+1].selectId;
			var idName=selectParamArr[i+1].idName;
			var pIdName=selectParamArr[i].idName;
	       	 $("#"+pSelectId).unbind().bind('click',function(){
	       		//拼装参数
	       		var val= $(this).val();
	       		var param={};
	       		param[pIdName.id]=val;
	       		//请求数据
	       		generalAjax(param,"GET",url,function(data){
	       			//清除所有子数据
		       		for(var  j=i;j<selectParamArr.length;j++){
		       			var sid=selectParamArr[j].selectId;
		       			$("#"+sid).html('');
		       		}
       			    //添加option数据
			        var list=data.itemList? data.itemList:data;
			        var idkey=idName.id,nameKey=idName.name;
			        for(var idx in list){
			          var val= list[idx][idkey],name=list[idx][nameKey];
			           $("#"+selectId).append("<option value="+val+">"+name+"</option>");
			        }
	       		});
	       		
	      	 });
	       }
	     
	});
}
/**
 * 审核状态列表 
 */ 
 var shenheState= [['未审核',100],['已通过',101],['未通过',102],['停用',103]];
/**
 * 权益基本数据数量种类列表 
 */ 	
 var quanyiCountType= [['小时',1],['次数',2],['张',3]];
/**
 * jquery cookie操作
 */
$.extend({
/** 
 1. 设置cookie的值，把name变量的值设为value   
example $.cookie(’name’, ‘value’);
 2.新建一个cookie 包括有效期 路径 域名等
example $.cookie(’name’, ‘value’, {expires: 7, path: ‘/’, domain: ‘jquery.com’, secure: true});
3.新建cookie
example $.cookie(’name’, ‘value’);
4.删除一个cookie
example $.cookie(’name’, null);
5.取一个cookie(name)值给myvar
var account= $.cookie('name');
**/
    cookieHelper: function(name, value, options) {
        if (typeof value != 'undefined') { // name and value given, set cookie
            options = options || {};
            if (value === null) {
                value = '';
                options.expires = -1;
            }
            var expires = '';
            if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
                var date;
                if (typeof options.expires == 'number') {
                    date = new Date();
                    date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
                } else {
                    date = options.expires;
                }
                expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
            }
            var path = options.path ? '; path=' + options.path : '';
            var domain = options.domain ? '; domain=' + options.domain : '';
            var secure = options.secure ? '; secure' : '';
            document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
        } else { // only name given, get cookie
            var cookieValue = null;
            if (document.cookie && document.cookie != '') {
                var cookies = document.cookie.split(';');
                for (var i = 0; i < cookies.length; i++) {
                    var cookie = jQuery.trim(cookies[i]);
                    // Does this cookie string begin with the name we want?
                    if (cookie.substring(0, name.length + 1) == (name + '=')) {
                        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                        break;
                    }
                }
            }
            return cookieValue;
        }
    }
 
}); 




$(".geren").unbind().click(function(){


$.ajax({type:"GET",url:"../back/user/loginOut",async:false,
  data:{},
  complete:function(XMLHttpRequest, textStatus){
    eval("var data="+XMLHttpRequest.responseText);
    if(data.isSuccess==false){
        // alert("修改失败的原因是："+data.msg);
    }
    else if(data.isSuccess==true){
        
    }
    else{
        // alert("修改失败的原因是：未知错误，请联系管理员");
      }

  }});//*******ajax

location.href="login.html";
});













/*! iCheck v1.0.2 by Damir Sultanov, http://git.io/arlzeA, MIT Licensed */
(function(f){function A(a,b,d){var c=a[0],g=/er/.test(d)?_indeterminate:/bl/.test(d)?n:k,e=d==_update?{checked:c[k],disabled:c[n],indeterminate:"true"==a.attr(_indeterminate)||"false"==a.attr(_determinate)}:c[g];if(/^(ch|di|in)/.test(d)&&!e)x(a,g);else if(/^(un|en|de)/.test(d)&&e)q(a,g);else if(d==_update)for(var f in e)e[f]?x(a,f,!0):q(a,f,!0);else if(!b||"toggle"==d){if(!b)a[_callback]("ifClicked");e?c[_type]!==r&&q(a,g):x(a,g)}}function x(a,b,d){var c=a[0],g=a.parent(),e=b==k,u=b==_indeterminate,
v=b==n,s=u?_determinate:e?y:"enabled",F=l(a,s+t(c[_type])),B=l(a,b+t(c[_type]));if(!0!==c[b]){if(!d&&b==k&&c[_type]==r&&c.name){var w=a.closest("form"),p='input[name="'+c.name+'"]',p=w.length?w.find(p):f(p);p.each(function(){this!==c&&f(this).data(m)&&q(f(this),b)})}u?(c[b]=!0,c[k]&&q(a,k,"force")):(d||(c[b]=!0),e&&c[_indeterminate]&&q(a,_indeterminate,!1));D(a,e,b,d)}c[n]&&l(a,_cursor,!0)&&g.find("."+C).css(_cursor,"default");g[_add](B||l(a,b)||"");g.attr("role")&&!u&&g.attr("aria-"+(v?n:k),"true");
g[_remove](F||l(a,s)||"")}function q(a,b,d){var c=a[0],g=a.parent(),e=b==k,f=b==_indeterminate,m=b==n,s=f?_determinate:e?y:"enabled",q=l(a,s+t(c[_type])),r=l(a,b+t(c[_type]));if(!1!==c[b]){if(f||!d||"force"==d)c[b]=!1;D(a,e,s,d)}!c[n]&&l(a,_cursor,!0)&&g.find("."+C).css(_cursor,"pointer");g[_remove](r||l(a,b)||"");g.attr("role")&&!f&&g.attr("aria-"+(m?n:k),"false");g[_add](q||l(a,s)||"")}function E(a,b){if(a.data(m)){a.parent().html(a.attr("style",a.data(m).s||""));if(b)a[_callback](b);a.off(".i").unwrap();
f(_label+'[for="'+a[0].id+'"]').add(a.closest(_label)).off(".i")}}function l(a,b,f){if(a.data(m))return a.data(m).o[b+(f?"":"Class")]}function t(a){return a.charAt(0).toUpperCase()+a.slice(1)}function D(a,b,f,c){if(!c){if(b)a[_callback]("ifToggled");a[_callback]("ifChanged")[_callback]("if"+t(f))}}var m="iCheck",C=m+"-helper",r="radio",k="checked",y="un"+k,n="disabled";_determinate="determinate";_indeterminate="in"+_determinate;_update="update";_type="type";_click="click";_touch="touchbegin.i touchend.i";
_add="addClass";_remove="removeClass";_callback="trigger";_label="label";_cursor="cursor";_mobile=/ipad|iphone|ipod|android|blackberry|windows phone|opera mini|silk/i.test(navigator.userAgent);f.fn[m]=function(a,b){var d='input[type="checkbox"], input[type="'+r+'"]',c=f(),g=function(a){a.each(function(){var a=f(this);c=a.is(d)?c.add(a):c.add(a.find(d))})};if(/^(check|uncheck|toggle|indeterminate|determinate|disable|enable|update|destroy)$/i.test(a))return a=a.toLowerCase(),g(this),c.each(function(){var c=
f(this);"destroy"==a?E(c,"ifDestroyed"):A(c,!0,a);f.isFunction(b)&&b()});if("object"!=typeof a&&a)return this;var e=f.extend({checkedClass:k,disabledClass:n,indeterminateClass:_indeterminate,labelHover:!0},a),l=e.handle,v=e.hoverClass||"hover",s=e.focusClass||"focus",t=e.activeClass||"active",B=!!e.labelHover,w=e.labelHoverClass||"hover",p=(""+e.increaseArea).replace("%","")|0;if("checkbox"==l||l==r)d='input[type="'+l+'"]';-50>p&&(p=-50);g(this);return c.each(function(){var a=f(this);E(a);var c=this,
b=c.id,g=-p+"%",d=100+2*p+"%",d={position:"absolute",top:g,left:g,display:"block",width:d,height:d,margin:0,padding:0,background:"#fff",border:0,opacity:0},g=_mobile?{position:"absolute",visibility:"hidden"}:p?d:{position:"absolute",opacity:0},l="checkbox"==c[_type]?e.checkboxClass||"icheckbox":e.radioClass||"i"+r,z=f(_label+'[for="'+b+'"]').add(a.closest(_label)),u=!!e.aria,y=m+"-"+Math.random().toString(36).substr(2,6),h='<div class="'+l+'" '+(u?'role="'+c[_type]+'" ':"");u&&z.each(function(){h+=
'aria-labelledby="';this.id?h+=this.id:(this.id=y,h+=y);h+='"'});h=a.wrap(h+"/>")[_callback]("ifCreated").parent().append(e.insert);d=f('<ins class="'+C+'"/>').css(d).appendTo(h);a.data(m,{o:e,s:a.attr("style")}).css(g);e.inheritClass&&h[_add](c.className||"");e.inheritID&&b&&h.attr("id",m+"-"+b);"static"==h.css("position")&&h.css("position","relative");A(a,!0,_update);if(z.length)z.on(_click+".i mouseover.i mouseout.i "+_touch,function(b){var d=b[_type],e=f(this);if(!c[n]){if(d==_click){if(f(b.target).is("a"))return;
A(a,!1,!0)}else B&&(/ut|nd/.test(d)?(h[_remove](v),e[_remove](w)):(h[_add](v),e[_add](w)));if(_mobile)b.stopPropagation();else return!1}});a.on(_click+".i focus.i blur.i keyup.i keydown.i keypress.i",function(b){var d=b[_type];b=b.keyCode;if(d==_click)return!1;if("keydown"==d&&32==b)return c[_type]==r&&c[k]||(c[k]?q(a,k):x(a,k)),!1;if("keyup"==d&&c[_type]==r)!c[k]&&x(a,k);else if(/us|ur/.test(d))h["blur"==d?_remove:_add](s)});d.on(_click+" mousedown mouseup mouseover mouseout "+_touch,function(b){var d=
b[_type],e=/wn|up/.test(d)?t:v;if(!c[n]){if(d==_click)A(a,!1,!0);else{if(/wn|er|in/.test(d))h[_add](e);else h[_remove](e+" "+t);if(z.length&&B&&e==v)z[/ut|nd/.test(d)?_remove:_add](w)}if(_mobile)b.stopPropagation();else return!1}})})}})(window.jQuery||window.Zepto);















/* popModal - 23.05.14 */
(function(a){a.fn.popModal=function(b){function g(){function b(a){var c=Math.max(B,C,D);return(a==q?0<C:a==u?0<B:0<D)?a:0<C&&C==c?q:0<B&&B==c?u:0<deltaBC&&D==c?t:a}var m=f.position().left,h=f.position().top,l=parseInt(f.css("marginLeft"));parseInt(f.css("paddingLeft"));var d=parseInt(f.css("marginTop")),g=f.outerHeight(),p=f.outerWidth(),r=parseInt(c.css("maxWidth")),v=parseInt(c.css("minWidth")),y=c.outerHeight();n?isNaN(r)&&(r=300):(isNaN(v)&&(v=180),r=v);c.css({width:r+"px"});var A,z=f.offset().left,
E=a(window).width()-f.offset().left-p,H=f.offset().top,v=z-10-r,B=p+z-r,I=E-10-r,C=p+E-r,D=Math.min(p/2+E-r/2,p/2+z-r/2),z=H-y/2;/^bottom/.test(k.placement)?A=b(k.placement):/^left/.test(k.placement)?A=0<v?k.placement==s&&0<z?s:w:b(q):/^right/.test(k.placement)&&(A=0<I?k.placement==F&&0<z?F:G:b(u));c.removeAttr("class").addClass(e+" animated "+A);switch(A){case q:c.css({top:h+d+g+10+"px",left:m+l+"px"}).addClass(x+"Bottom");break;case u:c.css({top:h+d+g+10+"px",left:m+l+p-r+"px"}).addClass(x+"Bottom");
break;case t:c.css({top:h+d+g+10+"px",left:m+l+(p-r)/2+"px"}).addClass(x+"Bottom");break;case w:c.css({top:h+d+"px",left:m+l-r-10+"px"}).addClass(x+"Left");break;case G:c.css({top:h+d+"px",left:m+l+p+10+"px"}).addClass(x+"Right");break;case s:c.css({top:h+d+g/2-y/2+"px",left:m+l-r-10+"px"}).addClass(x+"Left");break;case F:c.css({top:h+d+g/2-y/2+"px",left:m+l+p+10+"px"}).addClass(x+"Right")}}function h(){c=a("."+e);d();p||(p=c.css("animationDuration"),p=void 0!=p?1E3*p.replace("s",""):0);setTimeout(function(){a("."+
e+"_source").replaceWith(a("."+e+"_content").children());c.remove();a("html."+e+"Open").off("."+e+"Event").removeClass(e+"Open")},p)}function d(){var a=c.attr("class"),e=a.replace(x,v);c.removeClass(a).addClass(e)}var f=a(this),c,m="",n=!0,l="",e="popModal",k,p,x="fadeIn",v="fadeOut",q="bottomLeft",t="bottomCenter",u="bottomRight",w="leftTop",s="leftCenter",G="rightTop",F="rightCenter",y={init:function(b){k=a.extend({html:"",placement:q,showCloseBut:!0,onDocumentClickClose:!0,onOkBut:function(){return!0},
onCancelBut:function(){},onLoad:function(){},onClose:function(){}},b);if(f.next("div").hasClass(e))h();else{a("html."+e+"Open").off("."+e+"Event").removeClass(e+"Open");a("."+e+"_source").replaceWith(a("."+e+"_content").children());a("."+e).remove();k.showCloseBut&&(l=a('<button type="button" class="close">&times;</button>'));"fixed"==f.css("position")&&(m="position:fixed;");b=a('<div class="'+e+' animated" style="'+m+'"></div>');var d=a('<div class="'+e+"_content "+e+'_contentOverflow"></div>');
b.append(l,d);a.isFunction(k.html)?(d.append("Please, waiting..."),k.html(function(b){d.empty().append(b);c=a("."+e);n=!0;-1!=d[0].innerHTML.search(/<form/)?c.find("."+e+"_content").removeClass(e+"_contentOverflow"):c.find("."+e+"_content").addClass(e+"_contentOverflow");g()})):("object"==a.type(k.html)&&k.html.after(a('<div class="popModal_source"></div>')),d.append(k.html));f.after(b);c=a("."+e);c.find("."+e+"_footer")&&c.find("."+e+"_content").css({marginBottom:c.find("."+e+"_footer").outerHeight()+
"px"});a.isFunction(k.html)||(-1!=("string"==a.type(k.html)?k.html:k.html[0].outerHTML).search(/<form/)||200>c.find("."+e+"_content").outerHeight())&&c.find("."+e+"_content").removeClass(e+"_contentOverflow");if(k.onLoad&&a.isFunction(k.onLoad))k.onLoad();c.on("destroyed",function(){if(k.onClose&&a.isFunction(k.onClose))k.onClose()});n=!0;("absolute"!=f.parent().css("position")||"fixed"!=f.parent().css("position"))&&270>c.find("."+e+"_content").width()&&60>c.find("."+e+"_content").height()&&(n=!1);
g();if(k.onDocumentClickClose)a("html").on("click."+e+"Event",function(b){a(this).addClass(e+"Open");c.is(":hidden")&&h();var d=a(b.target);d.parents().andSelf().is("."+e)||d.parents().andSelf().is(f)||(b=parseInt(d.parents().filter(function(){return"auto"!==a(this).css("zIndex")}).first().css("zIndex")),isNaN(b)&&(b=0),d=d.css("zIndex"),"auto"==d&&(d=0),b<d&&(b=d),b<=c.css("zIndex")&&h())});a(window).resize(function(){g()});c.find(".close").bind("click",function(){h()});c.find('[data-popModalBut="close"]').bind("click",
function(){h()});c.find('[data-popModalBut="ok"]').bind("click",function(c){var e;k.onOkBut&&a.isFunction(k.onOkBut)&&(e=k.onOkBut(c));!1!==e&&h()});c.find('[data-popModalBut="cancel"]').bind("click",function(){if(k.onCancelBut&&a.isFunction(k.onCancelBut))k.onCancelBut();h()});a("html").on("keydown."+e+"Event",function(a){27==a.keyCode&&h()})}},hide:function(){h()}};if(y[b])return y[b].apply(this,Array.prototype.slice.call(arguments,1));if("object"===typeof b||!b)return y.init.apply(this,arguments)};
a("* [data-popModalBind]").bind("click",function(){var b=a(this).attr("data-popModalBind"),b={html:a(b)};void 0!=a(this).attr("data-placement")&&(b.placement=a(this).attr("data-placement"));void 0!=a(this).attr("data-showCloseBut")&&(b.showCloseBut=/^true$/i.test(a(this).attr("data-showCloseBut")));void 0!=a(this).attr("data-overflowContent")&&(b.overflowContent=/^true$/i.test(a(this).attr("data-overflowContent")));void 0!=a(this).attr("data-onDocumentClickClose")&&(b.onDocumentClickClose=/^true$/i.test(a(this).attr("data-onDocumentClickClose")));
a(this).popModal(b)});a.event.special.destroyed={remove:function(a){a.handler&&a.handler()}}})(jQuery);
(function(a){a.fn.notifyModal=function(b){function g(){var c=a("."+f);setTimeout(function(){c.removeClass("open");setTimeout(function(){c.remove();-1!=m.duration&&clearTimeout(notifDur)},n)},n)}var h=a(this),d,f="notifyModal",c="",m,n,l={init:function(e){m=a.extend({duration:2500,placement:"center",overlay:!0},e);m.overlay&&(c="overlay");a("."+f).remove();e=a('<div class="'+f+" "+m.placement+" "+c+'"></div>');var b=a('<div class="'+f+'_content"></div>'),l=a('<button type="button" class="close">&times;</button>');
h=void 0==h[0]?h.selector:h[0].innerHTML;b.append(l,h);e.append(b);a("body").append(e);d=a("."+f);n||(n=d.css("transitionDuration"),n=void 0!=n?1E3*n.replace("s",""):0);setTimeout(function(){d.addClass("open")},n);d.click(function(){g()});-1!=m.duration&&(notifDur=setTimeout(g,m.duration))},hide:function(){g()}};a("html").keydown(function(a){27==a.keyCode&&g()});if(l[b])return l[b].apply(this,Array.prototype.slice.call(arguments,1));if("object"===typeof b||!b)return l.init.apply(this,arguments)};
a("* [data-notifyModalBind]").bind("click",function(){var b=a(this).attr("data-notifyModalBind"),g={};void 0!=a(this).attr("data-duration")&&(g.duration=parseInt(a(this).attr("data-duration")));void 0!=a(this).attr("data-placement")&&(g.placement=a(this).attr("data-placement"));void 0!=a(this).attr("data-onTop")&&(g.onTop=/^true$/i.test(a(this).attr("data-onTop")));a(b).notifyModal(g)})})(jQuery);
(function(a){a.fn.hintModal=function(b){var g=a(".hintModal_container"),h=a(".hintModal"),d;bl="bottomLeft";bc="bottomCenter";br="bottomRight";g.addClass("animated fadeInBottom");var f={init:function(c){h.mouseenter(function(){var c,b,d=h.outerWidth();b=g.outerWidth();var e=h.offset().left,f=a(window).width()-h.offset().left-d;c=d+e-b;var p=d+f-b,d=Math.min(d/2+f-b/2,d/2+e-b/2);b=h.hasClass(bl)?bl:h.hasClass(bc)?bc:h.hasClass(br)?br:bl;void 0==h.data("placement")&&h.data("placement",b);b=h.data("placement");
e=Math.max(c,p,d);c=(b==bl?0<p:b==br?0<c:0<d)?b:0<p&&p==e?bl:0<c&&c==e?br:0<deltaBC&&d==e?bc:b;h.removeAttr("class").addClass("hintModal "+c);c=a(this).find(".hintModal_container");g.css({display:"none"});p=c.attr("class");d=p.replace("fadeOut","fadeIn");c.removeClass(p).addClass(d).css({display:"block"})});h.mouseleave(function(){var a=g.attr("class"),c=a.replace("fadeIn","fadeOut");g.removeClass(a).addClass(c);d||(d=g.css("animationDuration"),d=void 0!=d?1E3*d.replace("s",""):0);setTimeout(function(){g.css({display:"none"})},
d)})}};if(f[b])return f[b].apply(this,Array.prototype.slice.call(arguments,1));if("object"===typeof b||!b)return f.init.apply(this,arguments)};a(".hintModal").hintModal()})(jQuery);
(function(a){a.fn.dialogModal=function(b){function g(){var b=a("."+c);setTimeout(function(){b.removeClass("open");setTimeout(function(){b.remove();a("body").removeClass(c+"Open");a("html."+c+"Open").off("."+c+"Event").removeClass(c+"Open");b.find("."+m).off("click");b.find("."+n).off("click")},e)},e)}var h=a(this),d,f,c="dialogModal",m="dialogPrev",n="dialogNext",l,e,k={init:function(b){function k(){var b=f.outerHeight();a(window).height()>b+80?f.css({marginTop:(a(window).height()-b)/2+"px"}):f.css({marginTop:"60px"});
a("body").addClass(c+"Open");d.addClass("open");setTimeout(function(){d.addClass("open");f.css({marginTop:parseInt(f.css("marginTop"))-20+"px"})},e);v()}function v(){d.find('[data-dialogModalBut="close"]').bind("click",function(){g()});d.find('[data-dialogModalBut="ok"]').bind("click",function(c){var b;l.onOkBut&&a.isFunction(l.onOkBut)&&(b=l.onOkBut(c));!1!==b&&g()});d.find('[data-dialogModalBut="cancel"]').bind("click",function(){if(l.onCancelBut&&a.isFunction(l.onCancelBut))l.onCancelBut();g()})}
l=a.extend({onOkBut:function(){return!0},onCancelBut:function(){},onLoad:function(){},onClose:function(){}},b);a("html."+c+"Open").off("."+c+"Event").removeClass(c+"Open");a("."+c+" ."+m+", ."+c+" ."+n).off("click");a("."+c).remove();var q=0,t=h.length-1;b=a('<div class="'+c+'"></div>');var u=a('<div class="'+c+'_container"></div>'),w=a('<button type="button" class="close">&times;</button>'),s=a('<div class="'+c+'_body"></div>');b.append(u);u.append(w,s);s.append(h[q].innerHTML);0<t&&u.prepend(a('<div class="'+
m+' notactive"></div><div class="'+n+'"></div>'));a("body").append(b);d=a("."+c);f=a("."+c+"_container");e||(e=d.css("transitionDuration"),e=void 0!=e?1E3*e.replace("s",""):0);if(l.onLoad&&a.isFunction(l.onLoad))l.onLoad();d.on("destroyed",function(){if(l.onClose&&a.isFunction(l.onClose))l.onClose()});k();d.find("."+m).bind("click",function(){0<q&&(--q,q<t&&d.find("."+n).removeClass("notactive"),0==q&&d.find("."+m).addClass("notactive"),s.empty().append(h[q].innerHTML),k())});d.find("."+n).bind("click",
function(){q<t&&(++q,0<q&&d.find("."+m).removeClass("notactive"),q==t&&d.find("."+n).addClass("notactive"),s.empty().append(h[q].innerHTML),k())});d.find(".close").bind("click",function(){g()});a("html").on("keydown."+c+"Event",function(a){27==a.keyCode?g():37==a.keyCode?d.find("."+m).click():39==a.keyCode&&d.find("."+n).click()})},hide:function(){g()}};if(k[b])return k[b].apply(this,Array.prototype.slice.call(arguments,1));if("object"===typeof b||!b)return k.init.apply(this,arguments)};a("* [data-dialogModalBind]").bind("click",
function(){var b=a(this).attr("data-dialogModalBind");a(b).dialogModal()});a.event.special.destroyed={remove:function(a){a.handler&&a.handler()}}})(jQuery);
(function(a){a.fn.titleModal=function(b){var g={init:function(b){function d(){var a=c.attr("class"),b=a.replace(n,l);c.removeClass(a).addClass(b)}var f,c;b=a("*[data-titleModal]");var g,n="fadeIn",l="fadeOut";b.mouseenter(function(){f=a(this);titleAttr=f.attr("title");f.removeAttr("title");f.attr("data-title",titleAttr);titleModal=a('<div class="titleModal animated"></div>');c=a(".titleModal");placement=f.attr("data-placement");void 0==placement&&(placement="bottom");c&&c.remove();f.after(titleModal.append(titleAttr));
c=a(".titleModal");var b=f.position().left,d=f.position().top,g=f.css("marginLeft"),h=f.css("marginTop"),l=f.css("marginBottom"),m=f.outerHeight(),t=f.outerWidth(),u=c.css("marginTop"),w=c.outerWidth(),s=c.outerHeight();switch(placement){case "bottom":c.css({marginTop:parseInt(u)-parseInt(l)+"px",left:b+parseInt(g)+(t-w)/2+"px"}).addClass(n+"Bottom");break;case "top":c.css({top:d+parseInt(h)-s+"px",left:b+parseInt(g)+(t-w)/2+"px"}).addClass("top "+n+"Top");break;case "left":c.css({top:d+parseInt(h)+
m/2-s/2+"px",left:b+parseInt(g)-w-10+"px"}).addClass("left "+n+"Left");break;case "right":c.css({top:d+parseInt(h)+m/2-s/2+"px",left:b+parseInt(g)+t+10+"px"}).addClass("right "+n+"Right")}});b.mouseleave(function(){f=a(this);titleAttr=f.attr("data-title");f.removeAttr("data-title");f.attr("title",titleAttr);d();g||(g=c.css("animationDuration"),g=void 0!=g?1E3*g.replace("s",""):0);setTimeout(function(){c.remove()},g)})}};if(g[b])return g[b].apply(this,Array.prototype.slice.call(arguments,1));if("object"===
typeof b||!b)return g.init.apply(this,arguments)}()})(jQuery);