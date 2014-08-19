package com.shoppingcart.android.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppingcart.android.R;
import com.shoppingcart.android.dto.CategoryDto;

public class CategoryAdapter extends ArrayAdapter<CategoryDto> {
	private final Context context;
	TextView tv1, tv2, tv3, tv4, tv5, tv6;
	ArrayList<CategoryDto> categoryList;
	Activity activity;

	public CategoryAdapter(Context context, ArrayList<CategoryDto> categoryList,Activity activity) {
		super(context, R.layout.category_item, categoryList);
		this.context = context;
		this.categoryList = categoryList;
		this.activity=activity;
		// TODO Auto-generated constructor stub
		// userdto = UserModel.getCurrentUser(context);
	}

	public class ViewHolder {
		TextView categoryName;
		RelativeLayout relativeLayout;
		ImageView categoryImage;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.category_item, parent, false);
			holder = new ViewHolder();
			holder.categoryName = (TextView) view
					.findViewById(R.id.reports_name);
			// holder.date1Text = (TextView) view
			// .findViewById(R.id.redeemed_date);
			holder.categoryImage = (ImageView) view
					.findViewById(R.id.reports_item_imageView);
			holder.relativeLayout = (RelativeLayout) view
					.findViewById(R.id.reportsBtn);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CategoryDto categoryDto = categoryList.get(position);

		holder.categoryName.setText(categoryDto.getCategoryName());
		String uri = "@drawable/";
		if (categoryDto.getImage().isEmpty()) {
			uri += "ic_error.png";
		} else {
			uri += categoryDto.getImage();
		}
		int imageResource = activity.getResources().getIdentifier(uri, null,
				activity.getPackageName());
		Drawable res = activity.getResources().getDrawable(imageResource);
		holder.categoryImage.setImageDrawable(res);
		Display display = activity.getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		holder.categoryImage.getLayoutParams().height = (height / 2) / 2;
		holder.relativeLayout.setBackgroundColor(Color.parseColor(categoryDto
				.getCategoryColor()));
		return view;

	}

}
