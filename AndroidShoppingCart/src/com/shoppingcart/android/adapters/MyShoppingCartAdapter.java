package com.shoppingcart.android.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shoppingcart.android.R;
import com.shoppingcart.android.dto.ShoppingCartDto;
import com.shoppingcart.android.models.ProductModel;

public class MyShoppingCartAdapter extends ArrayAdapter<ShoppingCartDto> {
	private final Context context;
	TextView tv1, tv2, tv3, tv4, tv5, tv6;
	ArrayList<ShoppingCartDto> shoppingCartList;

	public MyShoppingCartAdapter(Context context,
			ArrayList<ShoppingCartDto> shoppingCartList) {
		super(context, R.layout.cart_item, shoppingCartList);
		this.context = context;
		this.shoppingCartList = shoppingCartList;
		// TODO Auto-generated constructor stub
		// userdto = UserModel.getCurrentUser(context);
	}

	public class ViewHolder {
		TextView productname;
		TextView productdetails;
		TextView productprice;
		// LoadImage loadImg;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater.inflate(R.layout.cart_item, parent, false);
			holder = new ViewHolder();
			holder.productname = (TextView) view.findViewById(R.id.productname);
			holder.productdetails = (TextView) view
					.findViewById(R.id.productdetails);
			holder.productprice = (TextView) view
					.findViewById(R.id.productprice);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ShoppingCartDto shoppingCartDto = shoppingCartList.get(position);

		holder.productname.setText(ProductModel.getProductByProductID(context,
				shoppingCartDto.getProductid()).getProductName());
		holder.productdetails.setText("Quantity: "
				+ shoppingCartDto.getQuantity());
		double totalPrice = shoppingCartDto.getPrice()
				* shoppingCartDto.getQuantity();
		holder.productprice.setText(totalPrice + "");

		return view;

	}
}
