/*jQuery(function(){
    //隐藏所有dd
    $('.sfq dd').hide().first().show();

    //点击dt,显示当前dt下的dd
    $('.sfq dt').click(function(){
        $(this).addClass('high');
        $(this).next().slideDown(200);
        $(this).parent().siblings().children('dd').slideUp(200);
        $(this).parent().siblings().children('dt').removeClass('high');

    });

});*/
jQuery(function(){
    //隐藏所有dd
    $('.sfq dd').hide().first().show();

    //点击dt,显示当前dt下的dd
    $('.sfq dt').click(function(){
        $(this).addClass('high');
        $(this).next().slideDown(200);
        $(this).parent().siblings().children('dd').slideUp(200);
        $(this).parent().siblings().children('dt').removeClass('high');

    });
    $('.sfq ol li').click(function(){
        $(this).addClass('high-bg');
        $(this).parent().siblings().children('li').removeClass('high-bg');
        $(this).parents("dl").siblings().find("li").removeClass('high-bg');

    });
});