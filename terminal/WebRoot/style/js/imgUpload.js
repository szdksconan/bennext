(function($){
    function _init(ele){
        var options = $(ele).data("imgUploadOptions").options;
        //var mask = $("<div class='imgupload-mask'></div>");
        var form = $("<div style='display:block;visibility: hidden'>"+
        "<form id='imgupload_form' method='post' enctype='multipart/form-data'  target='uploadframe' >"+
        "<input type='file' id='uploadImg'  name='uploadImg' accept='image/png, image/jpeg'></form></div>");
       // var imgSelectBtn = $("<div class='contentEdit-imgBtn-active'>选择图片</div>");
        //var imgDesc = $("<div class='imgDesc'></div>");
        //if(options.imgDesc){
       //     imgDesc.text(options.imgDesc);
      //  }
       //var panel = $("<div class='imgupload-panel'></div>");
      //  panel.append(form)//.append(imgDesc);
        $(document.body).append(form)//.append(panel);
      //  $(".imgupload-mask").css({display:"block",width:"100%",zIndex:10000,height:$(window).height()}).appendTo("body");
      //  $(".imgupload-panel").css({display:"block",width:"400px",height:"200px",zIndex:10001,left:($(document.body).outerWidth(true) - 400) / 2,top:($(window).height() - 200) / 2});
        //$(window).resize(function(){
        //    $(".imgupload-mask").css({display:"block",width:"100%",zIndex:10000,height:$(window).height()}).appendTo("body");
        //    $(".imgupload-panel").css({display:"block",width:"400px",height:"200px",zIndex:10001,left:($(document.body).outerWidth(true) - 400) / 2,top:($(window).height() - 200) / 2});
       // });
       // $(".contentEdit-imgBtn-active").click(function(){
            $("#uploadImg").click();
       // });
        $("#uploadImg").change({oraEle:ele},_uploadImgHandle);
       // $(".imgupload-mask").click(_close);
    }
    function _validateImgType(options){
        var file=$(options.fileInputEleSelector)[0],
            array = options.validArr,
            isExists = false,
            isOutOfSize = false;
        if (file.value == '') {
            alert("请选择要上传的图片!");
            return false;
        } else {
            var fileContentType = file.value.match(/^(.*)(\.)(.{1,8})$/)[3];
            // 循环判断图片的格式是否正确
            for ( var i in array) {
                if (fileContentType.toLowerCase() == array[i].toLowerCase()) {
                    isExists = true;
                }
            }
            if(!isExists){
                alert("图片类型选择有误，请重新选择");
            }
            return isExists;
        }
    }
    function _uploadImgHandle(event){
        _uploadImg(event.data.oraEle);
    }
    function _uploadImg(target) {
        var options = $(target).data("imgUploadOptions").options;
        if(_validateImgType(options)){
            $('.contentEdit-imgBtn-active').addClass('contentEdit-imgBtn-disable').unbind('click').text('上传中。。。');
            $('#imgupload_form').form('submit', {
                url: options.uploadUrl,
                dataType: 'json',
                success: function (data) {
                    $('.contentEdit-imgBtn-active').removeClass('contentEdit-imgBtn-disable').text("上传成功");
                    options.onSuccess.call(target,data);
                    _close();
                }
            });
        }
    }
    function _close(){
        $(".imgupload-mask").remove();
        $(".imgupload-panel").remove();
    }
    $.fn.imgUpload = function(options){
        options = options || {};
        return this.each(function() {
            var state = $(this).data("imgUploadOptions");
            if (!state) {
                $(this).data("imgUploadOptions", {options: $.extend({},$.fn.imgUpload.defaults,options)});
            }
            _init(this);
        });
    };
    $.fn.imgUpload.methods = {
        close:function(){
            _close();
        }
    };
    $.fn.imgUpload.defaults = {
        uploadUrl:"uri.json",
        imgDesc:"请选择合适尺寸的图片。",
        fileInputEleSelector:"#uploadImg",
        validArr:['jpeg','jpg','png'],
        onSuccess:function(data){
        }
    };
})(jQuery);