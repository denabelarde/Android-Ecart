package com.shoppingcart.android.dto;

public class CategoryDto {
	long _id;
	String categoryName;
	long categoryid;
	String image;
	String categoryColor;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategoryColor() {
		return categoryColor;
	}

	public void setCategoryColor(String categoryColor) {
		this.categoryColor = categoryColor;
	}

}
