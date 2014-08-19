package com.shoppingcart.android;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shoppingcart.android.adapters.CategoryAdapter;
import com.shoppingcart.android.dto.CategoryDto;
import com.shoppingcart.android.models.CategoryModel;
import com.shoppingcart.android.models.ShoppingCartModel;
import com.shoppingcart.android.slidingmenu.BaseActivity;
import com.shoppingcart.android.slidingmenu.MyCartListFragment;

public class HomeActivity extends BaseActivity implements
		SearchView.OnQueryTextListener {

	ArrayList<CategoryDto> categoryList = new ArrayList<CategoryDto>();
	ArrayList<CategoryDto> dummyCategoryList = new ArrayList<CategoryDto>();
	ArrayList<CategoryDto> categoryListOriginal = new ArrayList<CategoryDto>();
	CategoryAdapter categoryAdapter;
	GridView categoryGridView;
	String searchedText = "";
	SearchView mSearchView;

	public HomeActivity() {
		super(R.string.mycart);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		categoryList = new ArrayList<CategoryDto>();
		categoryAdapter = new CategoryAdapter(this, categoryList,
				HomeActivity.this);
		categoryGridView = (GridView) findViewById(R.id.gridView1);
		categoryGridView.setAdapter(categoryAdapter);
		categoryGridView.setEmptyView(findViewById(R.id.emptyView));
		categoryGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				System.out.println("Gridview on item click");
				Intent intent = new Intent(HomeActivity.this,
						ProductsActivity.class);
				intent.putExtra("categoryid", categoryList.get(myItemInt)
						.getCategoryid());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_up,
						R.anim.slide_in_up_exit);
			}
		});
		setTitle("View Cart");
		new getAllCategories()
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

	private class getAllCategories extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("Async Started");
			categoryList.clear();
			categoryListOriginal.clear();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... url) {

			categoryList.addAll(CategoryModel
					.getAllCategories(HomeActivity.this));
			categoryListOriginal.addAll(categoryList);
			return "";
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			categoryAdapter.notifyDataSetChanged();

			System.out.println("Async Destroyed!");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.search_products);
		mSearchView = (SearchView) searchItem.getActionView();
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setQueryHint(getResources().getString(
				R.string.search_categories));
		categoryGridView.setTextFilterEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			toggle();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		System.out.println(ShoppingCartModel.count(this)
				+ " <----Shopping cart size");
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		getSlidingMenu().setShadowDrawable(R.drawable.shadow);

		getSlidingMenu().setSecondaryMenu(R.layout.mycart_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.mycart_frame_two, new MyCartListFragment())
				.commit();

		super.onResume();
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
			categoryGridView.clearTextFilter();
			// productAdapter = new ProductAdapter();
			// // Assign adapter to ListView
			// productLV.setAdapter(productAdapter);
			// new loadAllProducts().execute();
			categoryList.clear();
			categoryList.addAll(categoryListOriginal);
			categoryAdapter.notifyDataSetChanged();
		} else {
			categoryList.clear();
			// if (productList.isEmpty()) {
			// System.out.println("productlist is empty");
			categoryList.addAll(categoryListOriginal);
			// }
			System.out.println("Pasok sa else");
			// productLV.setFilterText(arg0.toString());
			searchProduct(arg0);
		}
		return false;
	}

	public void searchProduct(String name) {
		dummyCategoryList = new ArrayList<CategoryDto>();
		for (CategoryDto categoryDto : categoryList) {

			if (categoryDto.getCategoryName().toUpperCase()
					.contains(name.toUpperCase())) {
				dummyCategoryList.add(categoryDto);
			}
		}
		categoryList.clear();
		categoryList.addAll(dummyCategoryList);
		// productAdapter = new ProductAdapter(dummyProductList);
		// Assign adapter to ListView
		// productLV.setAdapter(productAdapter);
		categoryAdapter.notifyDataSetChanged();

	}
}
