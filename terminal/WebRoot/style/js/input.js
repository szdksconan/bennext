function addListener(element,e,fn){
	if(element!=null){
		if(element.addEventListener){
	        element.addEventListener(e,fn,false);
	    } else {
	        element.attachEvent("on" + e,fn);
	    }
	}
}

var myinput = document.getElementById("username");
addListener(myinput,"click",function(){
    myinput.value = "";
});

var myinput1 = document.getElementById("password");
addListener(myinput1,"click",function(){
    myinput1.value = "";
});

var myinput2 = document.getElementById("yanzheng");
addListener(myinput2,"click",function(){
    myinput2.value = "";
});
var myinput3 = document.getElementById("userName");
addListener(myinput3,"click",function(){
    myinput3.value = "";
});
addListener(myinput3,"blur",function(){
    myinput3.value = "dfasdfasdf";
});
var myinput4 = document.getElementById("phone");
addListener(myinput4,"click",function(){
    myinput4.value = "";
});

