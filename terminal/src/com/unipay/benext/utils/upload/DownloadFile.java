package com.unipay.benext.utils.upload;

import java.io.File;
import java.io.Serializable;

public class DownloadFile implements Serializable {
	
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	
	private File upExcel;// 上传的文件
	private static final int BUFFER_SIZE = 16 * 1024;// 设置缓冲大小
	private String upExcelFileName;// 文件名字
	private String upExcelContentType;// 文件类型

	public File getUpExcel() {
		return upExcel;
	}


	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}


	public String getUpExcelFileName() {
		return upExcelFileName;
	}


	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}


	public String getUpExcelContentType() {
		return upExcelContentType;
	}


	public void setUpExcelContentType(String upExcelContentType) {
		this.upExcelContentType = upExcelContentType;
	}

	
	
	
}
