package com.xsscd.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.FileInfoService;

/**
 * 文件相关操作
 * @author zengcy
 *
 */
@ControllerBind(controllerKey="/back/fileInfo")
public class FileInfoController extends Controller{
	
	private  List<String> delFileList;
	
	public List<String> getDelFileList() {
		if(this.delFileList==null){
			this.delFileList=new ArrayList<String>();		
		}
		return this.delFileList;
	}
	public void setDelFileList(List<String> delFileList) {
		this.delFileList = delFileList;
	}
	/**
	 * 上传文件
	 */
	public void uploadFile(){
		FileInfoService.service.operateProxy(this, "上传文件", "uploadFile");
	}
	/**
	 * 删除文件
	 */
	public void deleteFile(){
		FileInfoService.service.deleteBatchProxy(this);
	}
}
