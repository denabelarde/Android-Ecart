package com.shoppingcart.android.slidingmenu;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shoppingcart.android.R;

public class BaseActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected ListFragment mFrag;

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.mycart_frame);
		System.out.println("Base Activity Oncreate");
	
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		//
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

}
