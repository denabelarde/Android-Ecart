package com.shoppingcart.android.dto;

public class ShoppingCartDto {

	long _id;
	long productid;
	int quantity;
	double price;
	String datecreated;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(String datecreated) {
		this.datecreated = datecreated;
	}

}
