package com.panfeng.shining.entity;

import java.io.Serializable;

import tyu.common.net.TyuDefine;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.JsonData.JsonConverter;

public class TypeEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 111654465L;

	public String getTypeSmallImage() {
		return typeSmallImage;
	}

	public void setTypeSmallImage(String typeSmallImage) {
		this.typeSmallImage = typeSmallImage;
	}

	public String getTypeBigImage() {
		return typeBigImage;
	}

	public void setTypeBigImage(String typeBigImage) {
		this.typeBigImage = typeBigImage;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTypeContents() {
		return typeContents;
	}

	public void setTypeContents(String typeContents) {
		this.typeContents = typeContents;
	}

	private String typeSmallImage;
	private String typeBigImage;
	private String typeName;
	private String state;
	private String typeContents;

	public TypeEntity() {
		super();
	}

	@Override
	public String toString() {
		super.toString();
		StringBuffer sb = new StringBuffer();
		sb.append(this.typeSmallImage);
		sb.append("|");
		sb.append(this.typeBigImage);
		sb.append("|");
		sb.append(this.typeName);
		sb.append("|");
		sb.append(this.state);
		sb.append("|");
		sb.append(this.typeContents);

		return sb.toString();
	}

	// video_sort_image/1440838818614.jpg
	// {"ms_id":6,"ms_lueimageurl":"1440838829303.jpg","ms_changimageurl":"1440838818614.jpg","ms_name":"搞笑","ms_state":1,"ms_remark":"123"},
	// json转换器
	public static JsonConverter<TypeEntity> jsonConverter = new JsonConverter<TypeEntity>() {
		@Override
		public TypeEntity convert(JsonData item) {
			TypeEntity typeEntity = new TypeEntity();
			typeEntity.setTypeBigImage(item.optString("ms_changimageurl"));
			typeEntity.setTypeSmallImage(item.optString("ms_lueimageurl"));
			typeEntity.setTypeName(item.optString("ms_name"));
			typeEntity.setTypeContents(item.optString("ms_remark"));
			typeEntity.setState(item.optString("ms_state"));
			return typeEntity;
		}
	};

	public String getBigImageUrl() {
		if (this.typeBigImage != null && !this.typeBigImage.equals(""))
			return TyuDefine.HOST + "video_sort_image/" + typeBigImage;
		return "";
	}

	public String getSmallImageUrl() {
		if (this.typeSmallImage != null && !this.typeSmallImage.equals(""))
			return TyuDefine.HOST + "video_sort_image/" + typeSmallImage;
		return "";
	}

}
