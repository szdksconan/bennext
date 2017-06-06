package com.xsscd.util;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {
	private String path;//从项目根目录下起的配置文件路径
	private Element root = null;//作为起始节点

	public XmlUtil(String path) {
		super();
		this.path = path;
		this.init(path);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	//初始化
	private void init(String path) {
		try {
			URI u=new URI(path);
			File file = new File(u);
			SAXReader reader =new SAXReader();
			Document doc =  reader.read(file);
			root = doc.getRootElement();
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("配置文件未找到！");
		}
	}

	//通过节点和属性名称找到对应的临界值
	public double getCriticalvalueByNames(String itemname,String markname){
		String value = getValue("name", "markName", "value", itemname, markname);
		if(value == ""){
			value = "0";
		}
//		System.out.println(value);
		return Double.parseDouble(value);
	}
	
	//通过节点和属性名称找到对应临界值的单位
	public String getCriticalUnitByNames(String itemname,String markname){
		String value = getValue("name", "markName", "unit", itemname, markname);
//		System.out.println(value);
		return value;
	}
	
	//通过节点和数据类型找到对应临界值
	public int getCriticalvalueByType(String itemname,int type){
		
		String value = getValue("name", "type", "value", itemname, String.valueOf(type));
		if(value == ""){
			value = "0";
		}
//		System.out.println(value);
		return Integer.parseInt(value);
	}
	
	//通过itemname markname detailType或者标记的详细值
	public double getDetailValue(String itemname,String markname,String detailType){
		String value = getValue("name", "markName", "value", itemname, markname, detailType);
		if(value == ""){
			value = "0";
		}
		return Double.parseDouble(value);
	}
	/**
	 * @method getValue 获取critical value节点的value或者unit
	 * @param attrName1 item节点的属性 条件之一
	 * @param attrName2 criticalValue节点的属性 与attrName1一起作为遍历条件
	 * @param attrName3 criticalValue节点的属性 需要获取的值
	 * @param para1 attrName1的参数值
	 * @param para2 attrName2的参数值
	 * @return 找到了值就返回该值 未找到就返回“”
	 */
	@SuppressWarnings("unchecked")
	private String getValue(String attrName1,String attrName2,String keyName,String para1,String para2){
		String value = "";
		List<Element> l=root.elements();
		for(Element e:l){
			if(e.attribute(attrName1).getValue().equals(para1)){
				List<Element> as=e.elements();
				for(Element a:as){
					if(a.attribute(attrName2)==null){
						return value;
					}else if(a.attribute(attrName2).getValue().equals(para2)){
						if(a.attribute(keyName)!=null){
							value=a.attribute(keyName).getValue();
						}
					}
				}
			}
		}
		return value;
	}
	/**
	 * @method getDetailValue 获取critical value节点下 detail节点的value或者unit
	 * @param attrName1 item节点的属性 条件之一
	 * @param attrName2 criticalValue节点的属性 与attrName1一起作为遍历条件
	 * @param detailType detail节点
	 * @param attrName3 criticalValue节点的属性 需要获取的值value或者unit
	 * @param para1 attrName1的参数值
	 * @param para2 attrName2的参数值
	 * @return 找到了值就返回该值 未找到就返回“”
	 */
	@SuppressWarnings("unchecked")
	private String getValue(String attrName1,String attrName2,String keyName,String para1,String para2,String para3){
		String value = "";
		List<Element> l=root.elements();
		for(Element e:l){
			if(e.attribute(attrName1).getValue().equals(para1)){
				List<Element> as=e.elements();
				for(Element a:as){
					if(a.attribute(attrName2)==null){
						return value;
					}else if(a.attribute(attrName2).getValue().equals(para2)){
						List<Element> ds = a.elements();
						for(Element d:ds){
							if(d.attribute("type").getValue().equals(para3)){
								if(d.attribute(keyName)!=null){
									value=d.attribute(keyName).getValue();
								}
							}
						}
					}
				}
			}
		}
		return value;
	}
}
