package com.shoppingcart.android.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.shoppingcart.android.dto.ShoppingCartDto;
import com.shoppingcart.android.helpers.DatabaseHelper;

public class ShoppingCartModel {
	public static int count(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "count(_id)" };
		int itemcount = 0;

		Cursor shoppingcart = dbhelper.query("shoppingcart", columns, null,
				null, null, null, null);

		if (shoppingcart.moveToFirst()) {
			itemcount = shoppingcart.getInt(0);
		}
		shoppingcart.close();
		dbhelper.close();

		return itemcount;
	}

	public static int getTotalItems(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "quantity" };

		Cursor shoppingcart = dbhelper.query("shoppingcart", columns, null,
				null, null, null, null);
		int totalitems = 0;

		if (shoppingcart.moveToFirst()) {
			do {
				try {
					totalitems += shoppingcart.getInt(0);
				} catch (Exception e) {

				}

			} while (shoppingcart.moveToNext());

		}
		shoppingcart.close();
		dbhelper.close();

		return totalitems;
	}

	public static double getTotalPayment(Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "quantity * price" };

		Cursor shoppingcart = dbhelper.query("shoppingcart", columns, null,
				null, null, null, null);
		double totalpayment = 0;

		if (shoppingcart.moveToFirst()) {
			do {

				totalpayment += shoppingcart.getInt(0);

			} while (shoppingcart.moveToNext());

		}
		shoppingcart.close();
		dbhelper.close();

		return totalpayment;
	}

	public static ArrayList<ShoppingCartDto> getAllShoppingCartIems(
			Context context) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		Cursor shoppingcart = dbhelper.query("shoppingcart", null, null, null,
				null, null, "datecreated DESC");

		ArrayList<ShoppingCartDto> shopCartList = new ArrayList<ShoppingCartDto>();

		if (shoppingcart.moveToFirst()) {
			do {

				ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
				shoppingCartDto.set_id(shoppingcart.getLong(0));
				shoppingCartDto.setProductid(shoppingcart.getLong(1));
				shoppingCartDto.setQuantity(shoppingcart.getInt(2));
				shoppingCartDto.setPrice(shoppingcart.getDouble(3));
				shoppingCartDto.setDatecreated(shoppingcart.getString(4));
				shopCartList.add(shoppingCartDto);

			} while (shoppingcart.moveToNext());

		}

		shoppingcart.close();
		dbhelper.close();
		return shopCartList;
	}

	public static ShoppingCartDto getSingleCartItem(Context context,
			long productid) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		Cursor shoppingcart = dbhelper.query("shoppingcart", null,
				"productid=?", new String[] { productid + "" }, null, null,
				null);
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

		if (shoppingcart.moveToFirst()) {
			do {

				shoppingCartDto.set_id(shoppingcart.getLong(0));
				shoppingCartDto.setProductid(shoppingcart.getLong(1));
				shoppingCartDto.setQuantity(shoppingcart.getInt(2));
				shoppingCartDto.setPrice(shoppingcart.getDouble(3));
				shoppingCartDto.setDatecreated(shoppingcart.getString(4));

			} while (shoppingcart.moveToNext());

		}

		shoppingcart.close();
		dbhelper.close();
		return shoppingCartDto;
	}

	public static long addToCart(Context context,
			ShoppingCartDto shoppingCartDto) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		if (checkItemAvailability(context, shoppingCartDto.getProductid()) == true) {
			addQuantityToExistingItem(context, shoppingCartDto);
			return 0;
		} else {

			ContentValues values = new ContentValues();

			values.put("productid", shoppingCartDto.getProductid());
			values.put("quantity", shoppingCartDto.getQuantity());
			values.put("price", shoppingCartDto.getPrice());
			values.put("datecreated", shoppingCartDto.getDatecreated());
			return dbhelper.insert("shoppingcart", values);
		}

	}

	public static boolean checkItemAvailability(Context context, long productID) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);
		String[] columns = { "count(_id)" };
		int reportcount = 0;

		Cursor report = dbhelper.query("shoppingcart", columns, "productid=?",
				new String[] { productID + "" }, null, null, null);

		if (report.moveToFirst()) {
			reportcount = report.getInt(0);
		}
		report.close();
		dbhelper.close();

		if (reportcount > 0) {
			return true;
		} else {
			return false;
		}

	}

	public static void addQuantityToExistingItem(Context context,
			ShoppingCartDto shoppingCartDto) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		ContentValues values = new ContentValues();

		values.put("quantity",
				getSingleCartItem(context, shoppingCartDto.getProductid())
						.getQuantity() + shoppingCartDto.getQuantity());

		try {
			dbhelper.update("shoppingcart", values, "productid=?",
					new String[] { shoppingCartDto.getProductid() + "" });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateItemQuantity(Context context,
			ShoppingCartDto shoppingCartDto) {
		DatabaseHelper dbhelper = new DatabaseHelper(context);

		ContentValues values = new ContentValues();

		values.put("quantity", shoppingCartDto.getQuantity());

		try {
			dbhelper.update("shoppingcart", values, "productid=?",
					new String[] { shoppingCartDto.getProductid() + "" });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void deleteAllItems(Context context) {

		DatabaseHelper dbhelper = new DatabaseHelper(context);
		System.out.println(dbhelper.delete("shoppingcart", null, null)
				+ " <--- item Deleted!");

	}

	public static void deleteSingleItem(Context context, long productid) {

		DatabaseHelper dbhelper = new DatabaseHelper(context);
		System.out.println(dbhelper.delete("shoppingcart", "productid=?",
				new String[] { productid + "" }) + " <--- item Deleted!");

	}
}
