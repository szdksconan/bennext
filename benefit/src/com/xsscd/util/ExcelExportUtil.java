package com.xsscd.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.xsscd.exception.ExportExcelException;

public class ExcelExportUtil {
	// 定制日期格式
	private static String DATE_FORMAT = "m/d/yy";
	// 定制浮点数格式
	private static String NUMBER_FORMAT = " #,##0.00 ";
	private String xlsFileName;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private HSSFRow row;

	/**
	 * 初始化Excel
	 * 
	 * @param fileName
	 *            导出文件名
	 */
	public ExcelExportUtil(String fileName) {
		this.xlsFileName = fileName;
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet();
	}

	public ExcelExportUtil() {
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet();
		// 自适应列宽度
		// this.sheet.autoSizeColumn(1, true);
		// 手动设置宽度
		// sheet.setColumnWidth(m, “列名”.getBytes().length*2*256);
	}

	/**
	 * 导出Excel文件
	 * 
	 * @throws ExportExcelException
	 */
	public void exportXLS() throws ExportExcelException {
		try {
			FileOutputStream fOut = new FileOutputStream(xlsFileName);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new ExportExcelException(" 生成导出Excel文件出错! ");
		} catch (IOException e) {
			throw new ExportExcelException(" 写入Excel文件出错! ");
		}
	}

	/**
	 * 导出Excel文件
	 * 
	 * @throws ExportExportExcelException
	 */
	public void exportXLS(OutputStream out) throws ExportExcelException {
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			throw new ExportExcelException(" 生成导出Excel文件出错! ");
		} catch (IOException e) {
			throw new ExportExcelException(" 写入Excel文件出错! ");
		}

	}

	/**
	 * 创建表头行
	 * 
	 * @param param
	 */
	public void addTableHead(String... param) {
		createRow(0);// 创建第一行
		for (int i = 0; i < param.length; i++) {
			setCell(i, param[i]);
		}
	}

	/**
	 * 只针对导出检验项目时的创建表头行
	 * 
	 * @param aHearderNames
	 *            一级表头
	 * @param bHearderNames
	 *            二级
	 * @param cHearderNames
	 *            三级
	 */
	public void addTableHeader(List<String> aHeadNames, List<String> bHeadNames, List<String> cHeadNames) {
		createRow(0);// 创建第一行
		HSSFRow row2 = this.sheet.createRow(1);// 创建第三行
		HSSFRow row3 = this.sheet.createRow(2);// 创建第三行
		for (int i = 0; i < aHeadNames.size(); i++) {
			if (i == aHeadNames.size() - 1) {// 最后一列
				int blength = bHeadNames.size();
				int clength = cHeadNames.size();
				for (int j = 0; j < blength; j++) {
					for (int k = 0; k < clength; k++) {
						HSSFCell cell3 = row3.createCell(i + j * clength + k);
						cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell3.setCellValue(cHeadNames.get(k));
					}
					HSSFCell cell2 = row2.createCell(i + j * clength);
					cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell2.setCellValue(bHeadNames.get(j));
					// 合并列
					this.sheet
							.addMergedRegion(new CellRangeAddress(1, 1, i + j * clength, (i + (j + 1) * clength) - 1));
				}
				setCell(i, aHeadNames.get(i));
				// 合并列
				this.sheet.addMergedRegion(new CellRangeAddress(0, 0, i, (i + blength * clength) - 1));
			} else {
				setCell(i, aHeadNames.get(i));
				// 合并行
				this.sheet.addMergedRegion(new CellRangeAddress(0, 2, i, i));
			}
		}
	}

	/**
	 * 创建多行数据
	 * 
	 * @param list
	 *            数据库读出的数据
	 * @param headRowNum
	 *            表头所占行数
	 */
	public void addTableInfoObj(List<Object> list, int headRowNum, String... noFieldsName) throws Exception {
		int count = headRowNum;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);// 获取list内的对象 即一行记录
			Class c = obj.getClass();// 获取映射对象
			createRow(count++);// 创建第一行
			setCell(0, (i + 1));// 插入序号行号
			Field[] fieldAll = c.getDeclaredFields();// 获取对象所有属性
			Field[] fieldArray = null;
			if (noFieldsName != null) {
				List<Field> listField = new ArrayList<Field>();
				for (Field field : fieldAll) {
					boolean need = true;
					for (String fieldName : noFieldsName) {
						if (field.getName().equals(fieldName)) {
							need = false;
							break;
						}
					}
					if (need) {
						listField.add(field);
					}
				}
				fieldArray = listField.toArray(new Field[listField.size()]);
			} else {
				fieldArray = fieldAll;
			}
			for (int j = 0; j < fieldArray.length; j++) {// 遍历属性对象
				String getter = GetNameUtil.getGetter(fieldArray[j]);// 获取访问器名称
				Method m = c.getDeclaredMethod(getter);
				Object value = m.invoke(obj);
				if (value != null) {
					if (value.getClass() == String.class) {
						setCell(j + 1, (String) value);
					} else if (value.getClass() == int.class || value.getClass() == Integer.class) {
						setCell(j + 1, (Integer) value);
					} else if (value.getClass() == long.class || value.getClass() == Long.class) {
						setCell(j + 1, (Long) value);
					} else if (value.getClass() == float.class || value.getClass() == Float.class) {
						setCell(j + 1, (Float) value);
					} else if (value.getClass() == double.class || value.getClass() == Double.class) {
						setCell(j + 1, (Double) value);
					} else if (value.getClass() == Date.class) {// 日期类型的处理
						Calendar calendar = new GregorianCalendar();
						calendar.setTime((Date) value);
						setCell(j + 1, calendar);
					} else if (value.getClass() == java.util.Date.class) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime((java.util.Date) value);
						setCell(j + 1, calendar);
					} else if (value.getClass() == Timestamp.class) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime((Timestamp) value);
						setCell(j + 1, calendar);
					} else if (value.getClass() == ArrayList.class) {
						// 导出检验项目时需要
						List valueList = (ArrayList) value;
						int k = j;// 列号
						for (int n = 0; n < valueList.size(); n++) {
							Object o = valueList.get(n);// 获取list内的对象 即一行记录
							Class cl = o.getClass();
							Field[] fields = cl.getDeclaredFields();
							for (int l = 0; l < fields.length; l++) {
								String geter = GetNameUtil.getGetter(fields[l]);
								Method me = cl.getDeclaredMethod(geter);
								Object val = me.invoke(o);
								if (val != null) {
									if (val.getClass() == String.class) {
										setCell(k + 1, (String) val);
									} else if (val.getClass() == int.class || val.getClass() == Integer.class) {
										setCell(k + 1, (Integer) val);
									} else if (val.getClass() == double.class || val.getClass() == Double.class) {
										setCell(k + 1, (Double) val);
									} else if (val.getClass() == Date.class) {
										Calendar calendar = new GregorianCalendar();
										calendar.setTime((Date) val);
										setCell(k + 1, calendar);
									} else if (val.getClass() == java.util.Date.class) {
										Calendar calendar = new GregorianCalendar();
										calendar.setTime((java.util.Date) val);
										setCell(k + 1, calendar);
									} else if (val.getClass() == Timestamp.class) {
										Calendar calendar = new GregorianCalendar();
										calendar.setTime((Timestamp) val);
										setCell(k + 1, calendar);
									}
								}
								k++;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 创建多行数据
	 * 
	 * @param customPaixu
	 * 
	 * @param list
	 *            数据库读出的数据
	 * @param headRowNum
	 *            表头所占行数
	 */
	public void addTableInfoMap(boolean customPaixu, List<Map<String, Object>> list, int headRowNum,
			String... noFieldsName) throws Exception {
		int count = headRowNum;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);// 获取list内的对象 即一行记录
			createRow(count++);// 创建第一行
			setCell(0, (i + 1));// 插入序号行号
			Set<String> keyAll = map.keySet();
			List<String> keyNeed = new ArrayList<String>();
			if (noFieldsName != null) {
				for (String key : keyAll) {
					boolean need = true;
					for (String fieldName : noFieldsName) {
						if (key.equals(fieldName)) {
							need = false;
							break;
						}
					}
					if (need) {
						keyNeed.add(key);
					}
				}
			}

			for (int j = 0; j < keyNeed.size(); j++) {// 遍历属性对象
				String key = keyNeed.get(j);

				Integer location = j + 1;
				if (customPaixu) {
					String reg = "^\\d{2}.*$";
					if (!key.matches(reg)) {
						continue;
					}
					location = Integer.valueOf(key.substring(0, 2));
				}
				location--;
				Object value = map.get(key);
				if (value != null) {
					if (value.getClass() == String.class) {
						setCell(location, (String) value);
					} else if (value.getClass() == int.class || value.getClass() == Integer.class) {
						setCell(location, (Integer) value);
					} else if (value.getClass() == long.class || value.getClass() == Long.class) {
						setCell(location, (Long) value);
					} else if (value.getClass() == float.class || value.getClass() == Float.class) {
						setCell(location, (Float) value);
					} else if (value.getClass() == double.class || value.getClass() == Double.class) {
						setCell(location, (Double) value);
					} else if (value.getClass() == Date.class) {// 日期类型的处理
						Calendar calendar = new GregorianCalendar();
						calendar.setTime((Date) value);
						setCell(location, calendar);
					} else if (value.getClass() == java.util.Date.class) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime((java.util.Date) value);
						setCell(location, calendar);
					} else if (value.getClass() == Timestamp.class) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime((Timestamp) value);
						setCell(location, calendar);
					}
				}
			}
		}
	}

	/**
	 * 增加一行
	 * 
	 * @param index
	 *            行号
	 */
	public void createRow(int index) {
		this.row = this.sheet.createRow(index);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, String value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, Calendar value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellValue(value == null ? null : value.getTime());
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT)); // 设置cell样式为定制的日期格式
		cell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
		// sheet.setColumnWidth(index, 10000);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, int value) {

		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, double value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}

	public static String getDATE_FORMAT() {
		return DATE_FORMAT;
	}

	public static void setDATE_FORMAT(String dATEFORMAT) {
		DATE_FORMAT = dATEFORMAT;
	}

	public static String getNUMBER_FORMAT() {
		return NUMBER_FORMAT;
	}

	public static void setNUMBER_FORMAT(String nUMBERFORMAT) {
		NUMBER_FORMAT = nUMBERFORMAT;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public HSSFRow getRow() {
		return row;
	}

	public void setRow(HSSFRow row) {
		this.row = row;
	}

}
