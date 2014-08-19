package com.shoppingcart.android.dto;

public class ProductDto {
	long _id;
	long productid;
	String productName;
	String description;
	double price;
	long categoryid;
	String image;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getProductid() {
		return productid;
	}

	public void setProductid(long productid) {
		this.productid = productid;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

}
