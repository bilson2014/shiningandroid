package com.panfeng.shining.slw.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 保存应用配置信息
 * @author Administrator
 * 
 */
@Table(name = "Defined_Constant_Table")
public class DefinedConstantTable extends EntityBase {
	// 定义常量名称key
	@Column(column = "Constant_name")
	private String Constant_name;
	// 定义的常量的值value
	@Column(column = "Constant_value")
	private String Constant_value;
	// 备注/补充
	@Column(column = "Constant_comment")
	private String Constant_comment;

	public String getConstant_name() {
		return Constant_name;
	}

	public void setConstant_name(String constant_name) {
		Constant_name = constant_name;
	}

	public String getConstant_value() {
		return Constant_value;
	}

	public void setConstant_value(String constant_value) {
		Constant_value = constant_value;
	}

	public String getConstant_comment() {
		return Constant_comment;
	}

	public void setConstant_comment(String constant_comment) {
		Constant_comment = constant_comment;
	}

	public DefinedConstantTable() {
		super();
	}

	public DefinedConstantTable(String constant_name, String constant_value,
			String constant_comment) {
		super();
		Constant_name = constant_name;
		Constant_value = constant_value;
		Constant_comment = constant_comment;
	}

}
