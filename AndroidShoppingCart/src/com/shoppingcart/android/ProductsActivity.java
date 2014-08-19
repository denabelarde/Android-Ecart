package com.shoppingcart.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppingcart.android.adapters.ProductAdapter;
import com.shoppingcart.android.dto.ProductDto;
import com.shoppingcart.android.dto.ShoppingCartDto;
import com.shoppingcart.android.helpers.ShoppingcartActions;
import com.shoppingcart.android.models.ProductModel;
import com.shoppingcart.android.models.ShoppingCartModel;

public class ProductsActivity extends Activity implements
		SearchView.OnQueryTextListener {
	ListView product_lv;
	ArrayList<ProductDto> productList = new ArrayList<ProductDto>();
	ArrayList<ProductDto> dummyProductList = new ArrayList<ProductDto>();
	ArrayList<ProductDto> originalProductList = new ArrayList<ProductDto>();
	ProductAdapter productAdapter;
	long categoryid;
	Dialog itemClickedDialog, addToCartDialog;
	final static int maxExcerptCharacters = 200;
	int productQuantity = 0;
	double totalPrice = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			java.util.Locale.getDefault());
	String searchedText = "";
	SearchView mSearchView;
	ShoppingcartActions cartActions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		cartActions = new ShoppingcartActions();
		product_lv = (ListView) findViewById(R.id.product_lv);
		productAdapter = new ProductAdapter(ProductsActivity.this, productList);
		product_lv.setAdapter(productAdapter);
		product_lv.setEmptyView(findViewById(R.id.emptyView));
		product_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				if (productList.get(myItemInt).getDescription().length() > maxExcerptCharacters) {
					openDialog(true, productList.get(myItemInt));
				} else {
					openDialog(false, productList.get(myItemInt));
				}
			}
		});
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			categoryid = extras.getLong("categoryid");
			System.out.println("categoryid: " + categoryid);
			new getAllProducts()
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

	}

	class getAllProducts extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("Async Started");
			productList.clear();
			originalProductList.clear();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			productList.addAll(ProductModel.getProductsByCategory(
					ProductsActivity.this, categoryid));
			originalProductList.addAll(productList);
			System.out.println("tpos na loding dpota ka");
			System.out.println(productList.size()
					+ " tpos na loding dpota ka man");
			return "";
		}

		// @Override
		// protected String doInBackground(String... params) {
		// // TODO Auto-generated method sstub
		// System.out.print( " <---product list size");
		// productList = ProductModel.getProductsByCategory(
		// ProductsActivity.this, categoryid);
		//
		// return null;
		// }

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			productAdapter.notifyDataSetChanged();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products, menu);
		MenuItem searchItem = menu.findItem(R.id.search_products);
		mSearchView = (SearchView) searchItem.getActionView();
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setQueryHint(getResources().getString(
				R.string.search_products));
		product_lv.setTextFilterEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

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

	public void openDialog(boolean hasDetails, final ProductDto productDto) {
		LayoutInflater inflater = (LayoutInflater) getLayoutInflater();

		View customView = inflater.inflate(R.layout.product_clicked_dialog,
				null);

		TextView textBtn1, textBtn2, textBtn3, txtBtnCancel;
		textBtn1 = (TextView) customView.findViewById(R.id.dialogbtn1);
		textBtn2 = (TextView) customView.findViewById(R.id.dialogbtn2);
		textBtn3 = (TextView) customView.findViewById(R.id.dialogbtn3);
		txtBtnCancel = (TextView) customView
				.findViewById(R.id.dialogbtn_cancel);
		LinearLayout btngroup1, btngroup2;
		btngroup1 = (LinearLayout) customView.findViewById(R.id.btngroup1);
		btngroup2 = (LinearLayout) customView.findViewById(R.id.btngroup2);

		if (hasDetails == true) {
			btngroup1.setVisibility(View.VISIBLE);
			btngroup2.setVisibility(View.GONE);
		} else {
			btngroup1.setVisibility(View.GONE);
			btngroup2.setVisibility(View.VISIBLE);
		}

		textBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemClickedDialog.dismiss();
				cartActions.openAddToCartDialog(ProductsActivity.this, productDto);

			}
		});

		textBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemClickedDialog.dismiss();

				Intent intent = new Intent(ProductsActivity.this,
						ProductDetails.class);
				intent.putExtra("productid", productDto.getProductid());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_up,
						R.anim.slide_in_up_exit);
			}
		});

		textBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemClickedDialog.dismiss();
				cartActions.openAddToCartDialog(ProductsActivity.this, productDto);
			}
		});
		txtBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemClickedDialog.dismiss();

			}
		});

		// Build the dialog
		itemClickedDialog = new Dialog(this, R.style.DialogSlideAnim);
		itemClickedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		itemClickedDialog.setContentView(customView);
		itemClickedDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = itemClickedDialog.getWindow()
				.getAttributes();

		wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

		itemClickedDialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		// LayoutParams params = getActivity().getWindow().getAttributes();
		// params.height = LayoutParams.FILL_PARENT;
		// getActivity().getWindow().setAttributes(
		// (android.view.WindowManager.LayoutParams) params);

		// itemClickedDialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		itemClickedDialog.setTitle("Actions");

		itemClickedDialog.show();
	}

	public void openAddToCartDialog2(final ProductDto productDto) {
		LayoutInflater inflater = (LayoutInflater) getLayoutInflater();

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
					Toast.makeText(ProductsActivity.this,
							"Atleast 1 product is required!", Toast.LENGTH_SHORT)
							.show();
				} else {
					ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
					shoppingCartDto.setProductid(productDto.getProductid());
					shoppingCartDto.setQuantity(productQuantity);
					shoppingCartDto.setPrice(productDto.getPrice());
					shoppingCartDto.setDatecreated(sdf.format(new Date()));
					ShoppingCartModel.addToCart(ProductsActivity.this,
							shoppingCartDto);
					Toast.makeText(ProductsActivity.this,
							"Item Added to Cart!", Toast.LENGTH_SHORT).show();
					addToCartDialog.dismiss();
				}
			}
		});
		// Build the dialog
		addToCartDialog = new Dialog(this);
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

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("onqueryTextchange");
		System.out.println(arg0 + " <---ARG");
		searchedText = arg0.toUpperCase();
		if (TextUtils.isEmpty(arg0)) {
			product_lv.clearTextFilter();
			// productAdapter = new ProductAdapter();
			// // Assign adapter to ListView
			// productLV.setAdapter(productAdapter);
			// new loadAllProducts().execute();
			productList.clear();
			productList.addAll(originalProductList);
			productAdapter.notifyDataSetChanged();
		} else {
			productList.clear();
			// if (productList.isEmpty()) {
			// System.out.println("productlist is empty");
			productList.addAll(originalProductList);
			// }
			System.out.println("Pasok sa else");
			// productLV.setFilterText(arg0.toString());
			searchProduct(arg0);
		}
		return false;
	}

	public void searchProduct(String name) {
		dummyProductList = new ArrayList<ProductDto>();
		for (ProductDto productDto : productList) {

			if (productDto.getProductName().toUpperCase()
					.contains(name.toUpperCase())) {
				dummyProductList.add(productDto);
			}
		}
		productList.clear();
		productList.addAll(dummyProductList);
		// productAdapter = new ProductAdapter(dummyProductList);
		// Assign adapter to ListView
		// productLV.setAdapter(productAdapter);
		productAdapter.notifyDataSetChanged();

	}

}
