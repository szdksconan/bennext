package com.unipay.benext.utils.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public class BaseExcelDaoImpl implements BaseExcelDao{
	/**
	 * 报表下载
	 * 
	 * @param clasz
	 *            传入的那个集合类
	 * @param filename
	 *            Excel的文件名称
	 * @param isDown
	 *            是否需要下载，0不下载，1需要下载
	 */
	@SuppressWarnings("unchecked")
	public void builderExcelData(HttpServletResponse response,List clasz, String filename, int isDown,String dirName) {
		String downpath = "";
		// 系统平台
		String platform = System.getProperty("os.name",
				"No Define System name!");
		// 判断平台
		if (platform.toLowerCase().indexOf("windows") != -1) {
			downpath = "d:\\yht\\" + dirName+"\\";
		} else {
			downpath = "/opt/yht/download/";
		}
		File dpath = new File(downpath);
		if (!dpath.exists())
			dpath.mkdirs();
		downpath = downpath + filename + ".xls";
		dpath = new File(downpath);
		// 写入Excel
		if (null != clasz && clasz.size() > 0) {
			try {
				ExcelAdapter excelAdapter = new ExcelAdapter();
				// 判断有没有这个地址，没有的话就创建一个
				excelAdapter.exportExcel(clasz, downpath);
				// -----------下载
				if (isDown != 0) {
					// 设置类型
					response.setCharacterEncoding("utf-8");
					response.setContentType("application/octet-stream");
					filename = new String(filename.getBytes("utf-8"),"ISO8859-1");
//					response.setHeader("Content-Disposition","attachment; filename=\""+ downpath.substring(downpath.lastIndexOf('/') + 1) + "\"");
					response.setHeader("Content-Disposition","attachment; filename=\""+ filename + ".xls\"");
					response.setContentType("application/msexcel;charset=UTF-8");
					int i = 0;
					byte[] b = new byte[1024];
					FileInputStream fis = null;
					OutputStream out1 = null;
					try {
						fis = new FileInputStream(dpath);
						out1 = response.getOutputStream();
						while ((i = fis.read(b)) > 0) {
							out1.write(b, 0, i);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						fis.close();
						out1.flush();
						out1.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/***
	 *            需要上次的文件
	 * @param filename
	 *            需要上传的文件名称
	 * @param isRead
	 *            设置是否可以在上传后进行读取，并返回数据，0只上传，不读取，其他，可以上传和读取数据
	 */
	public Object[][] uploadOrRead(HttpServletRequest request,MultipartFile multipartFile, String filename, int isRead){
		// --------------------文件上传
		int BUFFER_SIZE = 16 * 1024;// 设置缓冲大小
		InputStream in = null;
		OutputStream out = null;
		try {
			String path = "";
			// 系统平台
			String platform = System.getProperty("os.name",
					"No Define System name!");
			// 判断平台
			if (platform.toLowerCase().indexOf("windows") != -1) {
				// path =
				// ServletActionContext.getServletContext().getRealPath("/temp")+
				// "\\";
				String itempath = request.getContextPath();
				itempath = itempath.substring(itempath.lastIndexOf("/") + 1,
						itempath.length()) + "\\";
				path = "d:\\yht\\" + itempath;
			} else {
				path = "/opt/yht/download/";
			}
			File excelFileFile = new File(path);
			// 如果不存在这个路径，就创建一个
			if (!excelFileFile.exists())
				excelFileFile.mkdirs();
			path = path + filename;
			excelFileFile = new File(path);
			// 获取流
			in = new BufferedInputStream(multipartFile.getInputStream(),
					BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(excelFileFile),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			// 写入数据
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ------------------------文件读取
		Object[][] data = null;
		if (isRead != 0) {
			try {
				// 获取数据流
				ExcelAdapter excelAdapter = new ExcelAdapter(multipartFile.getInputStream());
				int cols = excelAdapter.getCols();
				data = excelAdapter.reader(0, 0, cols);
				excelAdapter.close();
				multipartFile.getInputStream().close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/**
	 * 下载指定名称的文件
	 * @param response
	 * @param request
	 * @param filename 需要下载的文件名称（包含了后缀名称）
	 * @return
	 */
	public int downByName(HttpServletResponse response,HttpServletRequest request,String filename) {
		int issussce=0;
		try {
			if (filename != null) {
				String downpath = "";
				// 系统平台
				String platform = System.getProperty("os.name",
						"No Define System name!");
				// 判断平台
				if (platform.toLowerCase().indexOf("windows") != -1) {
					// downpath =
					// ServletActionContext.getServletContext().getRealPath("/temp")+
					// "\\";
					String itempath = request.getContextPath();
					itempath = itempath.substring(
							itempath.lastIndexOf("/") + 1, itempath.length())
							+ "\\";
					downpath = "d:\\yht\\" + itempath;
				} else {
					downpath = "/opt/yht/download/";
				}
				File dpath = new File(downpath);
				if (!dpath.exists())
					dpath.mkdirs();
				downpath = downpath + filename ;//+ ".xls";
				dpath = new File(downpath);

				// 设置类型
//				response.setContentType("application/octet-stream");
//				response.setHeader("Content-Disposition","attachment; filename=\"" + downpath.substring(downpath
//												.lastIndexOf('/') + 1) + "\"");
				long fileLength = dpath.length();
				response.setContentType("application/x-msdownload;");   
	            response.setHeader("Content-disposition", "attachment; filename="  
	                    + new String(filename.getBytes("utf-8"), "ISO8859-1"));   
	            response.setHeader("Content-Length", String.valueOf(fileLength));
				int i = 0;
				byte[] b = new byte[1024];
				FileInputStream fis = null;
				OutputStream out1 = null;
				try {
					fis = new FileInputStream(dpath);
					out1 = response.getOutputStream();
					while ((i = fis.read(b)) > 0) {
						out1.write(b, 0, i);
					}
					issussce=1;
				} catch (Exception e) {
					issussce=0;
					e.printStackTrace();
				} finally {
					fis.close();
					out1.flush();
					out1.close();
				}
			}
		} catch (Exception e) {
			issussce=0;
			e.printStackTrace();
		}
		return issussce;
	}
	
	/**
	 * 将指定文件名的图片展示在页面上
	 * @param request
	 * @param response
	 * @param fileName  要展示的图片名称（包含后缀名）
	 */
	public void showImg(HttpServletRequest request,HttpServletResponse response,String fileName) throws IOException {
		OutputStream toClient = null;
		FileInputStream hFile = null;
		try {
			if(fileName!=null&&!fileName.equals("")){
				String path = "";
				// 系统平台
				String platform = System.getProperty("os.name","No Define System name!");
				// 判断平台
				if (platform.toLowerCase().indexOf("windows") != -1) {
					String itempath = request.getContextPath();
					itempath = itempath.substring(itempath.lastIndexOf("/") + 1, itempath.length())+ "\\";
					path = "d:\\yht\\" + itempath;
				} else {
					path = "/opt/yht/download/";
				}
				hFile = new FileInputStream(path+fileName); // 以byte流的方式打开文件
				int i=hFile.available(); //得到文件大小   
				byte data[]=new byte[i];   
				hFile.read(data);  //读数据   
				response.setContentType("image/*"); //设置返回的文件类型   
				toClient=response.getOutputStream(); //得到向客户端输出二进制数据的对象
				toClient.write(data);  //输出数据   
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			toClient.flush();
			toClient.close();
			hFile.close();
		}
	}

	@Override
	public String uploadAndPath(HttpServletRequest request,MultipartFile multipartFile, String filename) {
		// --------------------文件上传
		int BUFFER_SIZE = 16 * 1024;// 设置缓冲大小
		InputStream in = null;
		OutputStream out = null;
		try {
			String path = "";
			// 系统平台
			String platform = System.getProperty("os.name",
					"No Define System name!");
			// 判断平台
			if (platform.toLowerCase().indexOf("windows") != -1) {
				// path =
				// ServletActionContext.getServletContext().getRealPath("/temp")+
				// "\\";
				String itempath = request.getContextPath();
				itempath = itempath.substring(itempath.lastIndexOf("/") + 1,
						itempath.length()) + "\\";
				path = "d:\\yht\\" + itempath;
			} else {
				path = "/opt/yht/download/";
			}
			File excelFileFile = new File(path);
			// 如果不存在这个路径，就创建一个
			if (!excelFileFile.exists())
				excelFileFile.mkdirs();
			path = path + filename;
			excelFileFile = new File(path);
			// 获取流
			in = new BufferedInputStream(multipartFile.getInputStream(),
					BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(excelFileFile),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			// 写入数据
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
}
