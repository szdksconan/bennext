function ChangeDiv(divId,divName,zDivCount,type,params)//type 菜单类型    params  请求参数
{
    for(i=0;i<=zDivCount;i++)
    {
        document.getElementById(divName+i).style.display="none";
    }
    document.getElementById(divName+divId).style.display="block";
    
    
    //获取商家评论
    if('sjpl'==type){
     	this.getComDiscuss(shopId);
    }
    //获取预约信息
    if('yyfk'==type){
    	this.orderList(params);
    }
    //获取商品信息
    if('spxx'==type){
    	this.ComResourcesList(params);
    }
    
}

$("#tablide  li").click(function(){	
    $(this).addClass('high').siblings().removeClass('high');
});