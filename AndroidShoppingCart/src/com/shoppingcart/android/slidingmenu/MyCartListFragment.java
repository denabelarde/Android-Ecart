package com.shoppingcart.android.slidingmenu;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.shoppingcart.android.R;
import com.shoppingcart.android.adapters.MyShoppingCartAdapter;
import com.shoppingcart.android.dto.ProductDto;
import com.shoppingcart.android.dto.ShoppingCartDto;
import com.shoppingcart.android.models.ProductModel;
import com.shoppingcart.android.models.ShoppingCartModel;

public class MyCartListFragment extends Fragment {
	View myFragmentView;
	MyShoppingCartAdapter shoppingCartAdapter;
	ArrayList<ShoppingCartDto> shoppingCartList;
	ListView shoppingcart_lv;
	TextView cart_item_count, total_payment;
	Dialog itemClickedDialog, changeQuantityDialog;
	int productQuantity = 0;
	double totalPrice = 0;
	Button checkout_btn;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "credential from developer.paypal.com";

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Hipster Store")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.my_shopping_cart, container,
				false);
		shoppingCartList = new ArrayList<ShoppingCartDto>();
		shoppingCartAdapter = new MyShoppingCartAdapter(getActivity(),
				shoppingCartList);
		shoppingcart_lv = (ListView) myFragmentView
				.findViewById(R.id.shoppingcart_lv);
		cart_item_count = (TextView) myFragmentView
				.findViewById(R.id.cart_item_count);
		total_payment = (TextView) myFragmentView
				.findViewById(R.id.totalpayment);
		checkout_btn = (Button) myFragmentView.findViewById(R.id.checkout_btn);

		shoppingcart_lv.setAdapter(shoppingCartAdapter);
		shoppingcart_lv.setEmptyView(myFragmentView
				.findViewById(R.id.emptyView));

		checkout_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!shoppingCartList.isEmpty()) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							getActivity());
					adb.setTitle("Check out confirmation");
					adb.setIcon(R.drawable.warning);
					adb.setMessage("Are you sure you want to check out?");
					adb.setNegativeButton(
							getResources().getString(R.string.button_no), null);
					adb.setPositiveButton(
							getResources().getString(R.string.button_yes),
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

									PayPalPayment thingToBuy = getStuffToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

									Intent intent = new Intent(getActivity(),
											PaymentActivity.class);

									intent.putExtra(
											PaymentActivity.EXTRA_PAYMENT,
											thingToBuy);

									startActivityForResult(intent,
											REQUEST_CODE_PAYMENT);
								}

							});

					adb.show();
				}

			}
		});
		shoppingcart_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				openDialog(shoppingCartList.get(myItemInt));
			}
		});

		new getAllCartList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		Intent intent = new Intent(getActivity(), PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		getActivity().startService(intent);
		return myFragmentView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("Favoritelist onresume");

		super.onResume();
	}

	private class getAllCartList extends AsyncTask<String, Void, String> {
		int totalItems = 0;
		double totalpayment = 0;

		// Downloading data in non-ui thread
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("Async Started");
			shoppingCartList.clear();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... url) {

			shoppingCartList.addAll(ShoppingCartModel
					.getAllShoppingCartIems(getActivity()));
			totalItems = ShoppingCartModel.getTotalItems(getActivity());
			totalpayment = ShoppingCartModel.getTotalPayment(getActivity());
			return "";
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			shoppingCartAdapter.notifyDataSetChanged();
			cart_item_count.setText("Total Items: " + totalItems);
			total_payment.setText(totalpayment
					+ " "
					+ getActivity().getResources().getString(
							R.string.currency_string));
			System.out.println("Async Destroyed!");
		}
	}

	public void openDialog(final ShoppingCartDto shoppingCartDto) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();

		View customView = inflater.inflate(R.layout.cart_clicked_dialog, null);

		TextView textBtn1, textBtn2, txtBtnCancel;
		textBtn1 = (TextView) customView.findViewById(R.id.dialogbtn1);
		textBtn2 = (TextView) customView.findViewById(R.id.dialogbtn2);

		txtBtnCancel = (TextView) customView
				.findViewById(R.id.dialogbtn_cancel);
		LinearLayout btngroup1, btngroup2;
		btngroup1 = (LinearLayout) customView.findViewById(R.id.btngroup1);
		btngroup2 = (LinearLayout) customView.findViewById(R.id.btngroup2);

		textBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemClickedDialog.dismiss();
				openChangeQuantity(shoppingCartDto);
			}
		});

		textBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShoppingCartModel.deleteSingleItem(getActivity(),
						shoppingCartDto.getProductid());
				new getAllCartList()
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				itemClickedDialog.dismiss();
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
		itemClickedDialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
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

	public void openChangeQuantity(final ShoppingCartDto shoppingCartDto) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		productQuantity = shoppingCartDto.getQuantity();
		totalPrice = productQuantity * shoppingCartDto.getPrice();
		View customView = inflater.inflate(R.layout.change_quantity_dialog,
				null);
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
		final ProductDto productDto = ProductModel.getProductByProductID(
				getActivity(), shoppingCartDto.getProductid());
		dialog_product_name.setText(productDto.getProductName());
		dialog_product_price
				.setText("Price: " + productDto.getPrice() + " Php");
		dialog_total_price.setText("Total Price: " + totalPrice + " Php");
		quantity_text.setText(productQuantity < 10 ? "0" + productQuantity
				: productQuantity + "");
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
					ShoppingCartModel.deleteSingleItem(getActivity(),
							productDto.getProductid());
					Toast.makeText(getActivity(),
							"Item has been removed from cart!",
							Toast.LENGTH_LONG).show();
				} else {
					shoppingCartDto.setQuantity(productQuantity);
					shoppingCartDto.setPrice(totalPrice);
					ShoppingCartModel.updateItemQuantity(getActivity(),
							shoppingCartDto);
					Toast.makeText(getActivity(), "Item updated!",
							Toast.LENGTH_LONG).show();

				}
				changeQuantityDialog.dismiss();
				new getAllCartList()
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		// Build the dialog
		changeQuantityDialog = new Dialog(getActivity());
		// addToCartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		changeQuantityDialog.setContentView(customView);
		// addToCartDialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		changeQuantityDialog.setTitle("Add to Cart");

		changeQuantityDialog.show();

		changeQuantityDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				totalPrice = 0;
				productQuantity = 0;
			}
		});
	}

	public PayPalPayment getStuffToBuy(String paymentIntent) {

		PayPalItem[] items = new PayPalItem[shoppingCartList.size()];
		String itemNames = "";
		for (int x = 0; x < shoppingCartList.size(); x++) {
			ShoppingCartDto shoppingcartDto = shoppingCartList.get(x);
			items[x] = new PayPalItem(ProductModel.getProductByProductID(
					getActivity(), shoppingcartDto.getProductid())
					.getProductName(), shoppingcartDto.getQuantity(),
					new BigDecimal(shoppingcartDto.getPrice() + ""),
					getActivity().getResources().getString(
							R.string.currency_string),
					shoppingcartDto.getProductid() + "");

			itemNames += ProductModel.getProductByProductID(getActivity(),
					shoppingcartDto.getProductid()).getProductName()
					+ "\n ";
		}

		//
		// PayPalItem[] items = {
		//
		// new PayPalItem("old jeans with holes", 2, new BigDecimal(
		// "87.50"), "USD", "sku-12345678"),
		// new PayPalItem("free rainbow patch", 1, new BigDecimal("0.00"),
		// "USD", "sku-zero-price"),
		// new PayPalItem(
		// "long sleeve plaid shirt (no mustache included)", 6,
		// new BigDecimal("37.99"), "USD", "sku-33333") };

		BigDecimal subtotal = PayPalItem.getItemTotal(items);
		BigDecimal shipping = new BigDecimal("0");
		BigDecimal tax = new BigDecimal("0");
		PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
				shipping, subtotal, tax);
		BigDecimal amount = subtotal.add(shipping).add(tax);
		PayPalPayment payment = new PayPalPayment(amount, getActivity()
				.getResources().getString(R.string.currency_string),
				"Android Shopping App", paymentIntent);
		return payment.items(items).paymentDetails(paymentDetails);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					// Log.i(TAG, confirm.toJSONObject().toString(4));
					// Log.i(TAG,
					// confirm.getPayment().toJSONObject().toString(4));
					/**
					 * TODO: send 'confirm' (and possibly confirm.getPayment()
					 * to your server for verification or consent completion.
					 * See https://developer.paypal.com
					 * /webapps/developer/docs/integration
					 * /mobile/verify-mobile-payment/ for more details.
					 * 
					 * For sample mobile backend interactions, see
					 * https://github
					 * .com/paypal/rest-api-sdk-python/tree/master
					 * /samples/mobile_backend
					 */
					ShoppingCartModel.deleteAllItems(getActivity());
//					new getAllCartList()
//					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					Toast.makeText(getActivity(),
							"Thank you for shopping...",
							Toast.LENGTH_LONG).show();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				// Log.i(
				// TAG,
				// "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		}
	}

}
