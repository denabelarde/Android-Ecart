package com.shoppingcart.android.models;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.shoppingcart.android.dto.ProductDto;
import com.shoppingcart.android.helpers.DatabaseHelper;

public class ProductModel {
	public static int count(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "count(_id)" };
		int reportcount = 0;

		Cursor report = dbhelper.query("product", columns, null, null, null,
				null, null);

		if (report.moveToFirst()) {
			reportcount = report.getInt(0);
		}
		report.close();
		dbhelper.close();

		return reportcount;
	}

	public static ArrayList<ProductDto> getProductsByCategory(Context context,
			long categoryid) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		Cursor product = dbhelper
				.query("product", null, "categoryid=?",
						new String[] { categoryid + "" }, null, null,
						"productname ASC");

		ArrayList<ProductDto> productList = new ArrayList<ProductDto>();

		if (product.moveToFirst()) {
			do {

				ProductDto productDto = new ProductDto();
				productDto.set_id(product.getLong(0));
				productDto.setProductid(product.getLong(1));
				productDto.setProductName(product.getString(2));
				productDto.setDescription(product.getString(3));
				productDto.setPrice(product.getDouble(4));
				productDto.setCategoryid(product.getLong(5));
				productDto.setImage(product.getString(6));
				productList.add(productDto);
				System.out.println(productDto.getImage());
			} while (product.moveToNext());

		}

		System.out.println("loading finished!");
		product.close();
		dbhelper.close();
		return productList;
	}

	public static ProductDto getProductByProductID(Context context,
			long productid) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		Cursor product = dbhelper.query("product", null, "productid=?",
				new String[] { productid + "" }, null, null, null);

		ProductDto productDto = new ProductDto();

		if (product.moveToFirst()) {
			do {

				productDto.set_id(product.getLong(0));
				productDto.setProductid(product.getLong(1));
				productDto.setProductName(product.getString(2));
				productDto.setDescription(product.getString(3));
				productDto.setPrice(product.getDouble(4));
				productDto.setCategoryid(product.getLong(5));
				productDto.setImage(product.getString(6));

				System.out.println(productDto.getImage());
			} while (product.moveToNext());

		}

		System.out.println("loading finished!");
		product.close();
		dbhelper.close();
		return productDto;
	}
}
