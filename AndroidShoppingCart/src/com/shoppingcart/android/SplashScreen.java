package com.shoppingcart.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class SplashScreen extends Activity {

	private long del = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);

		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				Intent in = new Intent().setClass(SplashScreen.this,
						HomeActivity.class).addFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);

				finish();
			}

		};

		Timer timer = new Timer();
		timer.schedule(task, del);
		// loginDB.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

}
