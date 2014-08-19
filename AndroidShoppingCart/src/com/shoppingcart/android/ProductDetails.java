package com.shoppingcart.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shoppingcart.android.dto.ProductDto;
import com.shoppingcart.android.helpers.ShoppingcartActions;
import com.shoppingcart.android.helpers.TouchImageView;
import com.shoppingcart.android.models.ProductModel;

public class ProductDetails extends Activity {

	ProductDto productDto;
	TouchImageView contentpage_imageView;
	TextView contentpage_title, contentpage_details, contentpage_price;
	ShoppingcartActions cartActions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			cartActions = new ShoppingcartActions();
			productDto = ProductModel.getProductByProductID(this,
					extras.getLong("productid"));
			contentpage_imageView = (TouchImageView) findViewById(R.id.contentpage_imageView);
			contentpage_title = (TextView) findViewById(R.id.contentpage_title);
			contentpage_details = (TextView) findViewById(R.id.contentpage_details);
			contentpage_price = (TextView) findViewById(R.id.contentpage_price);

			contentpage_title.setText(productDto.getProductName());
			contentpage_details.setText(productDto.getDescription());
			contentpage_price.setText(productDto.getPrice() + "");
			String uri = "@drawable/";
			if (productDto.getImage().isEmpty()) {
				uri += "ic_error";
			} else {
				uri += productDto.getImage();
			}
			int imageResource = getResources().getIdentifier(uri, null,
					getPackageName());
			final Drawable res = getResources().getDrawable(imageResource);

			contentpage_imageView.setImageDrawable(res);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.addtocart_action:
			cartActions.openAddToCartDialog(ProductDetails.this, productDto);
			break;
		default:
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();

		overridePendingTransition(0, R.anim.slide_out_down);
	}

}
