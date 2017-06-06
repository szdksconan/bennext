package com.unipay.benext.utils.upload;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface BaseExcelDao {
	/**
	 * 根据实体注解下载对应的数据，生成Excel文件
	 * clasz 映射的类
	 * filename 报表的名称
	 * isDown是否需要上传后进行下载
	 * dirName 目录名称
	 */
	public void builderExcelData(HttpServletResponse response,List<Class> clasz, String filename,int isDown,String dirName);
	/**
	 * 上传Excel文件
	 * upExcel 上传的file文件
	 * filename 上传的名字
	 * isRead是否读取数据0，不读取只上传，1读取并上传
	 */
	public Object[][] uploadOrRead(HttpServletRequest request,MultipartFile multipartFile, String filename, int isRead) throws IOException;

	/**
	 * 上传图片 并返回路径
	 * upExcel 上传的file文件
	 * filename 上传的名字
	 */
	public String uploadAndPath(HttpServletRequest request,MultipartFile multipartFile, String filename) throws IOException;


}
