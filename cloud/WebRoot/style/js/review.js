/**
 * Created by Vincent on 2015/4/3.
 */
function show(inputId,reviewId,containerId,oW,callback){
    var file=document.getElementById(inputId);
    var review=document.getElementById(reviewId);
    var filePath = file.value;
    var fileSplit = filePath.split(".");
    var fileType = fileSplit[fileSplit.length-1];
	var tempImg=new Image();
    /*if(fileType!='png'){
        alert('由于苹果官方限制,请上传.png格式的图片');
        return false;
    }*/
    setTimeout(function(){
        if(file.files && file.files[0]){
        	if(!oW){
        		oW = 100;
        	}
            review.style.width = oW + 'px';
            review.style.height = '50px';
            review.src = window.URL.createObjectURL(file.files[0]);
            if(callback){
            	callback();
            }
        	
        }else{
            file.select();
            document.getElementById(containerId).innerHTML="";
            document.getElementById(containerId).style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + filePath + "\")";
        }
    },200);
    return true;
}