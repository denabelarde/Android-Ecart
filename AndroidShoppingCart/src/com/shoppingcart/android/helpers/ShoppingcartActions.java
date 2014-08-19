package com.shoppingcart.android.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppingcart.android.ProductsActivity;
import com.shoppingcart.android.R;
import com.shoppingcart.android.dto.ProductDto;
import com.shoppingcart.android.dto.ShoppingCartDto;
import com.shoppingcart.android.models.ShoppingCartModel;

public class ShoppingcartActions {
	int productQuantity = 0;
	double totalPrice = 0;
	Dialog addToCartDialog;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			java.util.Locale.getDefault());

	public void openAddToCartDialog(final Context context,
			final ProductDto productDto) {
		LayoutInflater inflater = (LayoutInflater) ((Activity) context)
				.getLayoutInflater();

		View customView = inflater.inflate(R.layout.addtocart_dialog, null);
		TextView dialog_product_name = (TextView) customView
				.findViewById(R.id.dialog_product_name);
		TextView dialog_product_price = (TextView) customView
				.findViewById(R.id.dialog_product_price);
		final TextView dialog_total_price = (TextView) customView
				.findViewById(R.id.dialog_total_price);
		TextView quantity_add = (TextView) customView
				.findViewById(R.id.quantity_add);
		TextView quantity_subtract = (TextView) customView
				.findViewById(R.id.quantity_subtract);
		final TextView quantity_text = (TextView) customView
				.findViewById(R.id.quantity_text);

		Button dialog_selectproduct = (Button) customView
				.findViewById(R.id.dialog_selectproduct);

		dialog_product_name.setText(productDto.getProductName());
		dialog_product_price
				.setText("Price: " + productDto.getPrice() + " Php");
		dialog_total_price.setText("Total Price: " + totalPrice + " Php");

		quantity_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				productQuantity++;

				quantity_text.setText(productQuantity < 10 ? "0"
						+ productQuantity : productQuantity + "");
				totalPrice += productDto.getPrice();
				dialog_total_price.setText("Total Price: " + totalPrice
						+ " Php");
			}
		});

		quantity_subtract.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (productQuantity > 0) {
					productQuantity--;
					quantity_text.setText(productQuantity < 10 ? "0"
							+ productQuantity : productQuantity + "");
					totalPrice -= productDto.getPrice();
					dialog_total_price.setText("Total Price: " + totalPrice
							+ " Php");
				}
			}
		});

		dialog_selectproduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (productQuantity == 0) {
					Toast.makeText(context, "Atleast 1 product is required!",
							Toast.LENGTH_SHORT).show();
				} else {
					ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
					shoppingCartDto.setProductid(productDto.getProductid());
					shoppingCartDto.setQuantity(productQuantity);
					shoppingCartDto.setPrice(productDto.getPrice());
					shoppingCartDto.setDatecreated(sdf.format(new Date()));
					ShoppingCartModel.addToCart(context, shoppingCartDto);
					Toast.makeText(context, "Item Added to Cart!",
							Toast.LENGTH_SHORT).show();
					addToCartDialog.dismiss();
				}
			}
		});
		// Build the dialog
		addToCartDialog = new Dialog(context);
		// addToCartDialog.getWindow().getAttributes().windowAnimations =
		// R.style.DialogSlideAnim;
		// addToCartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		addToCartDialog.setContentView(customView);
		// addToCartDialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		addToCartDialog.setTitle("Add to Cart");

		addToCartDialog.show();

		addToCartDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				totalPrice = 0;
				productQuantity = 0;
			}
		});
	}

}
