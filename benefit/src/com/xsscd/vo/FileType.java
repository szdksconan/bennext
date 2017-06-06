package com.xsscd.vo;

public enum FileType {
	image("图片"),
	video("视频"),
	document("文档"),
	other("其他");
	private String value;
	
	private FileType(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return this.value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
