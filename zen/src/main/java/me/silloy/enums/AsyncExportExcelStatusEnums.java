package me.silloy.enums;


public enum AsyncExportExcelStatusEnums {
	NEW(1, "文件生成中"),
	RUNNING(2, "文件生成中"),
	SUCCESS(3, "生成成功"),
	FAIL(4, "文件生成失败");

	private Integer code;
	private String name;

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	AsyncExportExcelStatusEnums(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getName(int code){
		AsyncExportExcelStatusEnums[] values = AsyncExportExcelStatusEnums.values();
		for(AsyncExportExcelStatusEnums v : values){
			if(v.getCode().equals(code)){
				return v.getName();
			}
		}
		return "";
	}

}
