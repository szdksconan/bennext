package com.unipay.benext.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 *
 */
public class ExeclUtil {
	private static void deletePath(String filepath) throws Exception {
		File f = new File(filepath);
		if(f.exists() && f.isDirectory()){
			File delFile[]=f.listFiles();
			int i =f.listFiles().length;
			for(int j=0;j<i;j++){
				if(delFile[j].isDirectory()){
					deletePath(delFile[j].getAbsolutePath());
				}
				delFile[j].delete();
			}
		}
	}

	private static boolean isNull(String str) {
		if (null == str || str.equals("")) {
			return true;
		}
		return false;
	}

	private static void mkDirs(String filePath) {
		File directory = new File(filePath.toString());
		if (directory.exists() && directory.isDirectory()) {

		}
		else {
			directory.mkdirs();
		}
	}

	private static void zip(String sourceFile,String targetZip){
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(new File(targetZip));
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(new File(sourceFile));
		zip.addFileset(fileSet);
		zip.execute();
	}

	/**
	 *
	 * @param colTitleAry 表头
	 * @param colWidthAry 宽度
	 * @param convStr 内容
	 * @param numColAry
	 * @param response
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public synchronized void writeExecl(String[] colTitleAry, short[] colWidthAry, Object[][] convStr, int[][] numColAry, HttpServletResponse response, String fileName) throws Exception {
		String filePath = File.separator+"mnt"+File.separator+"downLoad"+File.separator+fileName;
		deletePath(filePath);
		File file = new File(filePath);
		file.delete();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
        int rowCount=0;
		HSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);

		HSSFCellStyle colTitleStyle=createColTitleStyle(wb,font);
		createColTitleRow(sheet,colTitleStyle,rowCount,colTitleAry,colWidthAry);

		this.writeExcel(wb,font,sheet,convStr,numColAry,rowCount,fileName,filePath,response);
	}

	private void writeExcel(HSSFWorkbook wb,HSSFFont font,HSSFSheet sheet,Object[][] convStr, int[][] numColAry,int rowCount,String fileName,String filePath, HttpServletResponse response) throws IOException {
		HSSFCellStyle contentStyle=createDefaultStyle(wb,font);
		writeContent(sheet,contentStyle,convStr,numColAry,rowCount+1);
		if (isNull(fileName)) {
			fileName = "my_excel";
		}
		mkDirs(filePath);
		String newfile = filePath + File.separator + fileName + ".xls";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(newfile);
			wb.write(fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String zipFile = filePath + File.separator + fileName + ".zip";
		zip(filePath, zipFile);
		InputStream is = null;
		OutputStream os = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			response.setCharacterEncoding("utf-8");
			response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName,"utf-8") + ".zip");
			is = new FileInputStream(zipFile);
			bis = new BufferedInputStream(is);
			os = response.getOutputStream();
			bos = new BufferedOutputStream(os);

			int read = 0;
			byte[] bytes = new byte[8072];
			while((read = bis.read(bytes, 0, bytes.length)) != -1){
				bos.write(bytes, 0, read);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bos.close();
			os.close();
			bis.close();
			is.close();
		}
	}

	private void writeContent(HSSFSheet sheet,HSSFCellStyle style,Object[][] convStr, int[][] numColAry, int rowCount) {
		for (int i = 0; i < convStr.length; i++) {
			HSSFRow row = sheet.createRow(rowCount);
			for (int j = 0; j < convStr[i].length; j++) {
				HSSFCell cell = row.createCell(j);
				Object obj = convStr[i][j] == null ? "" : convStr[i][j];
				try {
					if (obj.getClass()== BigDecimal.class){
						obj = "".equals(obj)?"0":obj;
						obj = ((BigDecimal)obj).doubleValue();
					}
					if (obj.getClass()==Double.class){
						obj = "".equals(obj)?"0":obj;
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue((Double)obj);
					}else if (obj.getClass()==Integer.class){
						obj = "".equals(obj)?"0":obj;
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue((Integer)obj);
					}else {
						cell.setCellValue((String)obj);
					}
				}catch (Exception e){
					cell.setCellValue((String)obj);
				}
				cell.setCellStyle(style);
			}
			rowCount ++;
		}
	}

	private static HSSFCellStyle createColTitleStyle(HSSFWorkbook wb,HSSFFont font){
		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		return titleStyle;
	}

	private static HSSFCellStyle createDefaultStyle(HSSFWorkbook wk,HSSFFont font){
		HSSFCellStyle style = wk.createCellStyle();
		style.setBorderTop((short)1);
		style.setBorderLeft((short)1);
		style.setBorderBottom((short)1);
		style.setBorderRight((short)1);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
	}

	private static void createColTitleRow(HSSFSheet sheet,HSSFCellStyle style,int rowNum,String[] colTitleAry,short[] colWidthAry){
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short)(30*16));
		for( int i = 0 ; i < colTitleAry.length ; i ++ ){
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(colTitleAry[i]);
			sheet.setColumnWidth(i, (36*colWidthAry[i]));
		}
	}
}
