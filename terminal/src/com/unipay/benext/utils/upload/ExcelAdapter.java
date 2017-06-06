package com.unipay.benext.utils.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelAdapter {	
	
	//工作表
	private Workbook workbook;
	//导出数据
	@SuppressWarnings("rawtypes")
	private Map<Integer, List<ExportColumn>> data;
	
	public ExcelAdapter() {
		
	}
	
	/**
	 * 加载指定路径的Excel文件 
	 * @param path
	 */
	public ExcelAdapter(String path) {		
		InputStream is = ExcelAdapter.class.getResourceAsStream(path);
		try {
			workbook = Workbook.getWorkbook(is);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过流加载Excel文件，B/S可以使用这个方法
	 * @param is
	 */
	public ExcelAdapter(InputStream is) {
		try {
			workbook = Workbook.getWorkbook(is);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将Excel表转换成一个二维数组 
	 * @param sheetId
	 * @param startNo
	 * @param cols
	 * @param path
	 * @return Object[][]
	 */
	@SuppressWarnings("unused")
	private Object[][] getData(Object sheetId, int startNo, int cols) {
		Object[][] data = null;
		Sheet sheet = null;
		//获取Excel文件中指定表
		if (sheetId instanceof Integer) {
			//以表编号获取
			sheet = workbook.getSheet(((Integer) sheetId).intValue());
		} else {
			//以表名称获取
			sheet = workbook.getSheet(sheetId.toString());
		}
		//封闭数据到二维数组中
		int rows = sheet.getRows();
		//判断是否还有可读数据
		if ((rows - startNo) > 0) {
			int x = (rows - startNo);
			data = new Object[rows - startNo][cols];
			//数组行坐标
			int index = 0;
			for (int i = startNo; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (sheet.getCell(j, i).getType()==CellType.DATE){
						DateCell dateCell = (DateCell) sheet.getCell(j, i);
						Date date = dateCell.getDate();
						String year = new SimpleDateFormat("yyyy-MM-dd").format(date);
						data[index][j] = parseTimestamp(year);
					}else {
						data[index][j] = sheet.getCell(j, i).getContents();
					}
				}
				index++;
			}
		}
		return data;
	}
	
	public Object parseTimestamp(String date) {
        String d = date;
        if (date.indexOf("/") > 0) {
            d = date.replace("/", "-");
        }
        if (date.length()>10){
        	 String temp = d + " 00:00:00";
        	try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = df.parse(temp);
                return new Timestamp(time.getTime());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else {
        	return d;
        }
    }
		
	/**
	 * 读取Excel文件中的一个表
	 * @param sheetIndex 表的编号
	 * @param startNo 从第几行数据开始读
	 * @param cols 每行读取多少列
	 * @param path Excel文件路径
	 * @return Object[][]
	 */
	public Object[][] reader(int sheetIndex, int startNo, int cols) {
		Integer no = new Integer(sheetIndex);
		return getData(no, startNo, cols);
	}
	
	/**
	 * 读取Excel文件中的一个表
	 * @param sheetIndex 表的名称
	 * @param startNo 从第几行数据开始读
	 * @param cols 每行读取多少列
	 * @param path Excel文件路径
	 * @return Object[][]
	 */
	public Object[][] reader(String sheetName, int startNo, int cols) {
		return getData(sheetName, startNo, cols);
	}
	
	/**
	 * 导出Excel文件 
	 * @param <T>
	 * @param list
	 * @throws WriteException 
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> void exportExcel(List<T> list, File file) throws WriteException {
		//工作薄		
		WritableWorkbook wwk = null;
		//表
		WritableSheet ws = null;
		Class clzz = null;
		try {
			//创建一个工作薄
			wwk = Workbook.createWorkbook(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (list != null && list.size() > 0) {
			data = this.getExportData(list.get(0).getClass(), list);
		}
		if (list != null && list.size() > 0) {
			clzz = list.get(0).getClass();
			if (clzz.isAnnotationPresent(ExcelSheetMapping.class)) {
				ExcelSheetMapping esm = (ExcelSheetMapping) clzz.getAnnotation(ExcelSheetMapping.class);
				//创建一张表wwk.createSheet(表名, 表编号)
				ws = wwk.createSheet(esm.sheetName(), 0);	
				try {
					this.writeSheetHeader(clzz, ws);
					for (int i = 0; i < data.size(); i++) {
						List<ExportColumn> columns = data.get(i);
						for (int j = 0; j < columns.size(); j++) {
							ws.addCell(new Label(j, (i + 1), columns.get(j).getContent()));
						}
					}
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				} finally {
					try {
						wwk.write();
						wwk.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 导出Excel文件 
	 * @param <T> 泛型
	 * @param list 数据源
	 * @param path 导出路径
	 */
	public <T> void exportExcel(List<T> list, String path) {
		try {
			this.exportExcel(list, new File(path));
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出Excel文件  
	 * @param <T> 泛型
	 * @param list 数据源
	 * @param uri 导出路径
	 */
	public <T> void exportExcel(List<T> list, URI uri) {
		try {
			this.exportExcel(list, new File(uri));
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取导出数据及相关信息
	 * @param <T>
	 * @param clzz
	 * @param list
	 * @return Map<Integer, List<ExportColumn>>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> Map<Integer, List<ExportColumn>> getExportData(Class clzz, List<T> list) {
		Map<Integer, List<ExportColumn>> map = new HashMap<Integer, List<ExportColumn>>();
		List<Field> fields = this.getWirteField(clzz);
		Field field = null;
		for (int i = 0; i < list.size(); i++) {
			List<ExportColumn> ecList = new ArrayList<ExportColumn>();
			for (int j = 0; j < fields.size(); j++) {
				field = fields.get(j);
				ExportColumn ec = new ExportColumn(list.get(i), field);
				ecList.add(ec);
			}
			map.put(new Integer(i), ecList);
		}		
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Field> getWirteField(Class clzz) {
		Field[] fields = clzz.getDeclaredFields();
		List<Field> fieldWrite = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ExcelColumnMapping.class)) {
				fieldWrite.add(field);
			}
		}
		return fieldWrite;
	}
	
	/**
	 * 创建表头
	 * @param clzz
	 * @param ws
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	@SuppressWarnings("rawtypes")
	private void writeSheetHeader(Class clzz, WritableSheet ws) throws RowsExceededException, WriteException {
		List<Field> fields = getWirteField(clzz);
		ExcelColumnMapping ecm = null;
		for (int i = 0; (fields != null && i < fields.size()); i++) {
			ecm = fields.get(i).getAnnotation(ExcelColumnMapping.class);
			ws.addCell(new Label(i, 0, ecm.columnName()));
		}
	}
	
	/**
	 * 关闭委托对象
	 */
	public void close() {
		workbook.close();
	}
	/**
	 * 获取Excel的行数
	 * @return
	 */
	public int getCols(){
		int cols=0;
		try {
			Sheet sheet=workbook.getSheet(0);
			cols = sheet.getColumns();
		} catch (IndexOutOfBoundsException e) {
			cols=100;
			e.printStackTrace();
		}
		return cols;
	}
}