package com.addBusiness.addbiz;

import java.util.Hashtable;

import org.json.JSONObject;

import android.R.string;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddBizDashboardActivity extends Activity {
	public static final String SENDER_ID = "905757219933";// This is the project
															// number as issued
															// by google console
	AccountManager acm = null;
	String USER_GOOGLE_MAIL = "";
	private AddBizDashboardActivity _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_biz_dashboard);

		// create variable for all the dashboard buttons
		Button btn_add_places = (Button) findViewById(R.id.btn_add_places);
		Button btn_add_biz = (Button) findViewById(R.id.btn_add_biz);
		Button btn_search_places = (Button) findViewById(R.id.btn_search_places);
		Button btn_search_biz = (Button) findViewById(R.id.btn_search_biz);
		Button btn_about = (Button) findViewById(R.id.btn_about);

		/**
		 * Handling all button click events i.e. setting click listeners to the
		 * buttons
		 * */

		btn_add_places.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Log.e("Registration",checkAccountManager ());
				if (isOnline()) {

					if (checkPreference()) {// device registered to receive from
											// GCM
						// servers
						SharedPreferences sp = getSharedPreferences(
								Utilities.REGISTRATION_ID_PREFERENCE,
								0);
						send_deviceID_to_application_server(sp.getString("id", "defValue"),Utilities.url_send_devideID_to_applicatio_server);
						//do other stuff i.e call a new intent to do work
						Intent i = new Intent(_this, RegisterBizData.class);
						_this.startActivity(i);
						

					} else {
						// proceed to checking accounts
						// which boils down to registering device to communicate
						// with GCM servers
						Toast.makeText(_this, "going to register ",
								Toast.LENGTH_LONG).show();
						checkAccountManager();
					
					}

				} else {
					
					String dialogTitle = getResources().getString(
							R.string.no_internet_connection);
					String dialogBody = getResources().getString(
							R.string.enable_data_or_leave_airplane_mode);
					String negativeBtnLabel = getResources().getString(R.string.cancel);
					String positiveBtnLabel = getResources().getString(
							R.string.settings);
					final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
					promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
							negativeBtnLabel, positiveBtnLabel, settingArea);

				}

			}
		});

		btn_add_biz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//checkAccountManager();
				if (isOnline()) {

					if (checkPreference()) {// device registered to receive from
											// GCM
						// servers
						SharedPreferences sp = getSharedPreferences(
								Utilities.REGISTRATION_ID_PREFERENCE,
								0);
						send_deviceID_to_application_server(sp.getString("id", "defValue"),Utilities.url_send_devideID_to_applicatio_server);
						//do other stuff i.e call a new intent to do work
						Intent i = new Intent(_this, Login.class);
						_this.startActivity(i);
						

					} else {
						// proceed to checking accounts
						// which boils down to registering device to communicate
						// with GCM servers
						//Toast.makeText(_this, "going to register ",Toast.LENGTH_LONG).show();
						checkAccountManager();
					
					}

				} else {
					
					String dialogTitle = getResources().getString(
							R.string.no_internet_connection);
					String dialogBody = getResources().getString(
							R.string.enable_data_or_leave_airplane_mode);
					String negativeBtnLabel = getResources().getString(R.string.cancel);
					String positiveBtnLabel = getResources().getString(
							R.string.settings);
					final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
					promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
							negativeBtnLabel, positiveBtnLabel, settingArea);

				}

				
			}
		});

		btn_search_places.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isOnline()) {

					if (checkPreference()) {// device registered to receive from
											// GCM
						// servers
						SharedPreferences sp = getSharedPreferences(
								Utilities.REGISTRATION_ID_PREFERENCE,
								0);
						send_deviceID_to_application_server(sp.getString("id", "defValue"),Utilities.url_send_devideID_to_applicatio_server);
						//do other stuff i.e call a new intent to do work
						Intent i = new Intent(_this, AroundMe.class);
						_this.startActivity(i);
						

					} else {
						// proceed to checking accounts
						// which boils down to registering device to communicate
						// with GCM servers
						//Toast.makeText(_this, "going to register ",Toast.LENGTH_LONG).show();
						checkAccountManager();
					
					}

				} else {
					
					String dialogTitle = getResources().getString(
							R.string.no_internet_connection);
					String dialogBody = getResources().getString(
							R.string.enable_data_or_leave_airplane_mode);
					String negativeBtnLabel = getResources().getString(R.string.cancel);
					String positiveBtnLabel = getResources().getString(
							R.string.settings);
					final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
					promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
							negativeBtnLabel, positiveBtnLabel, settingArea);

				}

			}
		});

		btn_search_biz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkAccountManager();

			}
		});

		btn_about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// // TODO Auto-generated method stub
				Intent intent = new Intent(_this, Help_options.class);
				_this.startActivity(intent);

			}
		});

	}
	
	public void send_deviceID_to_application_server(String registrationID, String url){
		/*we need deviceID
		 * device email address
		 */
		 Log.e("Registration", "has gone to register to application server");
		
		
		
		Hashtable<String, String> hTable = new Hashtable<String, String>();
		hTable.put("email", firstGoogleMailAccount());
		hTable.put("regID", registrationID);
		
		CommonConnectionClass conObject = new CommonConnectionClass(AddBizDashboardActivity.this);
		
		conObject.commonConnectionMethod(hTable, url);
		
	}
	public boolean checkPreference() {
		Log.e("Registration", "preference checking starting..........");

		SharedPreferences sp = getSharedPreferences(Utilities.REGISTRATION_ID_PREFERENCE, 0);
		String stored_id = sp.getString("id", "noValue");

		if (stored_id.equals("noValue")) {
			return false;
		} else {

			return true;

		}

	}

	/**
	 * This is the method that registers a device if it is not registered it is
	 * called when there is atleast one user (gool mail) account to be used to
	 * identify the user
	 * 
	 * @param no
	 *            parameter
	 * @return void
	 */
	private void registerWithGCM() {
		// promptUserToCreateGoogleAccount("going to gcm","nor registers"
		// ,"fdsfgd","ssdf");
		Toast.makeText(_this, "going to register ",Toast.LENGTH_LONG).show();

		Intent registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		// // sets the app name in the intent
		registrationIntent.putExtra("app",
				PendingIntent.getBroadcast(_this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", SENDER_ID);
		_this.startService(registrationIntent);
		Log.e("Registration", "reaching gcm");

	}

	/**
	 * Determines whether a user has atleast one google mail account if not He
	 * is prompted to create one by directing the user to user settings this
	 * method is fired if internet is present
	 * 
	 * @param no
	 * @return void
	 */

	private void checkAccountManager() {
		// Log.v("account", "There is no google account");

		if (isOnline()) {
			try {

				acm = AccountManager.get(_this);

				Account[] accounts = acm.getAccountsByType("com.google");

				// if no account has been registered, prompt the user to do so
				if (accounts.length == 0) {
					// propt user to do so by asking him to create one
					// Log.e("account", "There is no google account");
					String dialogTitle = getResources().getString(
							R.string.no_gmail_account_found);
					String dialogBody = getResources().getString(
							R.string.create_gmail_account);
					String negativeBtnLabel = getResources().getString(
							R.string.cancel);
					String positiveBtnLabel = getResources().getString(
							R.string.settings);
					final String settingArea = Settings.ACTION_ADD_ACCOUNT;

					promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
							negativeBtnLabel, positiveBtnLabel, settingArea);
				} else {
					registerWithGCM();
//					read preference
//					regisgter device 
//					move to next screen
					
				}

			} catch (Exception e) {
				Log.e("error", e.toString());
			}

		} else {// Prompt the user to enable data, or leave airplane mode

			String dialogTitle = getResources().getString(
					R.string.no_internet_connection);
			String dialogBody = getResources().getString(
					R.string.enable_data_or_leave_airplane_mode);
			String negativeBtnLabel = getResources().getString(R.string.cancel);
			String positiveBtnLabel = getResources().getString(
					R.string.settings);
			final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
			promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
					negativeBtnLabel, positiveBtnLabel, settingArea);

		}

	}

	/**
	 * Returns the first google mail account in that device
	 * 
	 * @param void
	 * @return USER_GOOGLE_MAIL
	 */

	public String firstGoogleMailAccount() {

		acm = AccountManager.get(_this);

		Account[] accounts = acm.getAccountsByType("com.google");

		if (accounts.length > 0) {

			USER_GOOGLE_MAIL = accounts[0].name.toString();
		}

		return USER_GOOGLE_MAIL;

	}

	/**
	 * Find out whether your device is data enable
	 * 
	 * @param no
	 * @return boolean
	 */

	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;
		}
		return false;
	}

	/**
	 * Prompts the user to create a google account by asking to navigate to
	 * account settings
	 * 
	 * @param AlertDialog
	 *            title
	 * 
	 * @param AlertDialog
	 *            Body
	 * @param negative
	 *            button label
	 * @param positive
	 *            button label
	 * @return void
	 */

	private void promptUserToCreateGoogleAccount(String title, String body,
			String negativeButtonLabel, String positiveButtonLabbel,
			final String settingsArea) {
		// setResult(account);
		AlertDialog.Builder settingsAlert = new AlertDialog.Builder(_this);

		// Setting Dialog Title
		settingsAlert.setTitle(title);
		// Setting Dialog Message
		settingsAlert.setMessage(body);
		// On pressing Settings button
		settingsAlert.setPositiveButton(positiveButtonLabbel,
				new DialogInterface.OnClickListener() {
					// On pressing Settings button
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(settingsArea);

						_this.startActivity(intent);

					}
				});

		// on pressing cancel button
		settingsAlert.setNegativeButton(negativeButtonLabel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		settingsAlert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_biz_dashboard, menu);
		return true;
	}

}
