package com.addBusiness.addbiz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Add_biz_splash extends Activity {
	
	private static String TAG = Add_biz_splash.class.getName();
	private static long SLEEP_TIME = 3;    // Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(checkPreference()){
			//do nothing
			Log.e("status", "shared preference already exist");
			
		}else{
			
			Log.e("status", "creating a false shared preference");
			
			SharedPreferences sp = getSharedPreferences(Utilities.LOGIN_STATUS, 1);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("status", "false");
			editor.commit();
			Log.e("status", "has commited the shared preference");
		}
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
	     this.getWindow().setFlags(WindowManager.
	    LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// Removes notification bar
		
		setContentView(R.layout.activity_add_biz_splash);
		
		// Start timer and launch main activity
	      IntentLauncher launcher = new IntentLauncher();
	      launcher.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_biz_splash, menu);
		return true;
	}
	
	
	private class IntentLauncher extends Thread {
	      @Override
	      /**
	       * Sleep for some time and than start new activity.
	       */
	      public void run() {
	         try {
	            // Sleeping
	            Thread.sleep(SLEEP_TIME*1000);
	         } catch (Exception e) {
	            Log.e(TAG, e.getMessage());
	         }
	 
	         // Start main activity
	         Intent intent = new Intent(Add_biz_splash.this, AddBizDashboardActivity.class);
	         Add_biz_splash.this.startActivity(intent);
	         Add_biz_splash.this.finish();
	      }
	   }
	
	public boolean checkPreference() {
		Log.e("Registration", "preference checking starting........1..");

		SharedPreferences sp = getSharedPreferences(Utilities.LOGIN_STATUS, 1);
		Log.e("Registration", "preference checking starting....2......");
		
		String status = sp.getString("status", "noValue");
		
		Log.e("Registration", "preference checking starting....3......");
		
		if (status.equals("noValue")) {
			Log.e("Registration", "preference checking starting.4.........");
			//preference exist
			return false;
			
		} else {
			//preference do not exist
			return true;

		}

	}

}
