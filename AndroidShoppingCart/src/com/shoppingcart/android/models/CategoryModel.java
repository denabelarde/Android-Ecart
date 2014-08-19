package com.shoppingcart.android.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.shoppingcart.android.dto.CategoryDto;
import com.shoppingcart.android.helpers.DatabaseHelper;

public class CategoryModel {
	public static int count(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "count(_id)" };
		int reportcount = 0;

		Cursor report = dbhelper.query("category", columns, null, null, null,
				null, null);

		if (report.moveToFirst()) {
			reportcount = report.getInt(0);
		}
		report.close();
		dbhelper.close();

		return reportcount;
	}

	public static ArrayList<CategoryDto> getAllCategories(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		Cursor category = dbhelper.query("category", null, null, null, null,
				null, "categoryname ASC");

		ArrayList<CategoryDto> categoryList = new ArrayList<CategoryDto>();

		if (category.moveToFirst()) {
			do {

				CategoryDto categoryDto = new CategoryDto();
				categoryDto.set_id(category.getLong(0));
				categoryDto.setCategoryName(category.getString(1));
				categoryDto.setCategoryid(category.getLong(2));
				categoryDto.setImage(category.getString(3));
				categoryDto.setCategoryColor(category.getString(4));
				categoryList.add(categoryDto);

			} while (category.moveToNext());

		}

		category.close();
		dbhelper.close();
		return categoryList;
	}

}
