package com.shoppingcart.android.adapters;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppingcart.android.R;
import com.shoppingcart.android.dto.ProductDto;
import com.shoppingcart.android.helpers.TouchImageView;

public class ProductAdapter extends ArrayAdapter<ProductDto> {
	private Context context;
	ArrayList<ProductDto> productList;
	Dialog imageDialog;

	public ProductAdapter(Context context, ArrayList<ProductDto> productList) {
		super(context, R.layout.product_item, productList);
		this.context = context;
		this.productList = productList;
		// TODO Auto-generated constructor stub
		// userdto = UserModel.getCurrentUser(context);
	}

	public class ViewHolder {
		TextView productName;
		TextView productDescription;
		ImageView productImage;
		TextView product_price;
		// LoadImage loadImg;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater.inflate(R.layout.product_item, parent, false);
			holder = new ViewHolder();
			holder.productName = (TextView) view
					.findViewById(R.id.product_name);
			holder.productDescription = (TextView) view
					.findViewById(R.id.product_description);
			holder.productImage = (ImageView) view
					.findViewById(R.id.product_image);
			holder.product_price = (TextView) view
					.findViewById(R.id.product_price);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final ProductDto productDto = productList.get(position);

		// Temporarily load images from drawable because we dont have a
		// webserver yet. Just for demo purposes
		holder.productName.setText(productDto.getProductName());
		holder.product_price.setText(context.getResources().getString(
				R.string.price_string)
				+ " "
				+ productDto.getPrice()
				+ " "
				+ context.getResources().getString(R.string.currency_string));
		String uri = "@drawable/";
		if (productDto.getImage().isEmpty()) {
			uri += "ic_error";
		} else {
			uri += productDto.getImage();
		}
		int imageResource = context.getResources().getIdentifier(uri, null,
				context.getPackageName());
		final Drawable res = context.getResources().getDrawable(imageResource);
		
		holder.productImage.setImageDrawable(res);
		holder.productImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openImageDialog(res);
			}
		});

		if (productDto.getDescription().length() > 200) {
			holder.productDescription.setText(productDto.getDescription()
					.substring(0, 200) + "...");
		} else {
			holder.productDescription.setText(productDto.getDescription());
		}

		return view;

	}

	public void openImageDialog(Drawable res) {
		LayoutInflater inflater = (LayoutInflater) ((Activity) context)
				.getLayoutInflater();

		View customView = inflater.inflate(R.layout.image_dialog, null);
		TouchImageView product_image = (TouchImageView) customView
				.findViewById(R.id.product_image);

		product_image.setImageDrawable(res);
		// Build the dialog
		// imageDialog = new Dialog(context, R.style.DialogSlideAnim);
		// imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// imageDialog.setContentView(customView);
		// imageDialog.getWindow().setBackgroundDrawable(
		// new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//

		//
		// imageDialog.setTitle(productname);
		//
		// imageDialog.show();

		imageDialog = new Dialog(context);
		// addToCartDialog.getWindow().getAttributes().windowAnimations =
		// R.style.DialogSlideAnim;
		// addToCartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		imageDialog.setContentView(customView);
		// addToCartDialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		imageDialog.setTitle("Pinch to zoom");
		imageDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		imageDialog.show();

	}
}
