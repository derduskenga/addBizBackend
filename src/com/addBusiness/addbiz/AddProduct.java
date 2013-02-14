package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends Activity {
	AddProduct _this;
	private EditText txtProductName;
	private EditText txtProductDescription;
	private Button btnSubmitProduct;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		addListenerToButton();

	}

	public void addListenerToButton() {
		
		txtProductDescription = (EditText) findViewById(R.id.txtProductDescription);
		txtProductName = (EditText) findViewById(R.id.txtProductName);
		btnSubmitProduct = (Button) findViewById(R.id.btnAddProduct);

		btnSubmitProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final String name = encodeURL(txtProductName.getText().toString().trim());
				final String pDescription = encodeURL(txtProductDescription.getText().toString().trim());
				final String email = firstGoogleMailAccount();
				
				if (name.equals("") || pDescription.equals("")) {

					Toast.makeText(
							_this,
							getResources().getString(
									R.string.all_fields_are_required),
							Toast.LENGTH_LONG).show();

				} else {

					if (isOnline()) {
						
						// servercode comes here
						Thread trd = new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Looper.prepare();
								String url = encodeURL(Utilities.url_registerBiz_to_addProduct);

								Log.e("Registration",
										"gone to connectin class with url "
												+ url);

								HttpResponse response = null;
								// code to do the HTTP request
								InputStream is = null;
								String result;
								Log.d("res",
										"in addListenerToButton func 5");
								// Creating HTTP client
								final HttpParams httpParams = new BasicHttpParams();
								HttpConnectionParams.setConnectionTimeout(
										httpParams, 10000);
								Log.d("res",
										"in addListenerToButton func 6");

								HttpClient httpClient = new DefaultHttpClient(
										httpParams);
								Log.d("res",
										"in addListenerToButton func 7");

								// Creating HTTP Post
								HttpPost httpPost = new HttpPost(url);
					
								Log.d("res",
										"in addListenerToButton func 8");
								List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
								Log.d("res",
										"in addListenerToButton func 7");
								nameValuePair.add(new BasicNameValuePair("email", email));
								nameValuePair.add(new BasicNameValuePair("name", name));
								nameValuePair.add(new BasicNameValuePair("pDescription", pDescription));
								Log.d("res",
										"in addListenerToButton func 9");
								try {
									httpPost.setEntity(new UrlEncodedFormEntity(
											nameValuePair));
								} catch (UnsupportedEncodingException e) {
									// writing error to Log
									e.printStackTrace();
								}

								try {
									response = httpClient.execute(httpPost);

									HttpEntity entity = response
											.getEntity();

									is = entity.getContent();

									// writing response to log
									Log.d("res", "1 " + response.toString());

								} catch (ConnectTimeoutException ctEx) {

									Toast.makeText(getApplicationContext(),
											"Server not responding",
											Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// writing exception to log
									Log.d("res", "2 " + e.toString());
									// e.printStackTrace();
								}

								try {
									BufferedReader reader = new BufferedReader(
											new InputStreamReader(is,
													"iso-8859-1"), 8);
									StringBuilder sb = new StringBuilder();
									String line = null;
									while ((line = reader.readLine()) != null) {
										sb.append(line + "\n");
									}
									is.close();
									
									result = sb.toString();

									pDialog.dismiss();
									
									Toast.makeText(_this, result,
											Toast.LENGTH_LONG).show();
									
									Log.d("res", "4" + result);
									// call the method that will return a
									// value

								} catch (Exception e) {
									Log.e("log_tag",
											"Error converting result "
													+ e.toString());
								}
								Looper.loop();

							}

						});
						
						trd.start();
						openDialog();

					} else {
						String dialogTitle = getResources().getString(
								R.string.no_internet_connection);
						String dialogBody = getResources().getString(
								R.string.enable_data_or_leave_airplane_mode);
						String negativeBtnLabel = getResources().getString(
								R.string.cancel);
						String positiveBtnLabel = getResources().getString(
								R.string.settings);
						final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
						promptUserToCreateGoogleAccount(dialogTitle,
								dialogBody, negativeBtnLabel, positiveBtnLabel,
								settingArea);
					}

				}

			}
		});

	}
	
	
	private Handler closeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (pDialog != null)
				pDialog.dismiss();

		}
	};

	public void openDialog() {
		// Open the dialog
		pDialog = new ProgressDialog(AddProduct.this);
		pDialog.setMessage(Html
				.fromHtml("<b>Please wait...</b><br/>Adding product"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		closeHandler.sendEmptyMessageDelayed(0, 12000);
		// show toast

	}

	public String firstGoogleMailAccount() {
		String USER_GOOGLE_MAIL = "";
		AccountManager acm = AccountManager.get(_this);

		Account[] accounts = acm.getAccountsByType("com.google");

		if (accounts.length > 0) {

			USER_GOOGLE_MAIL = accounts[0].name.toString();
		}

		return USER_GOOGLE_MAIL;

	}

	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;
		}
		return false;
	}

	public String encodeURL(String url) {
		url = replace(url, 'à', "%E0");
		url = replace(url, 'è', "%E8");
		url = replace(url, 'é', "%E9");
		url = replace(url, 'ì', "%EC");
		url = replace(url, 'ò', "%F2");
		url = replace(url, 'ù', "%F9");
		url = replace(url, '$', "%24");
		url = replace(url, '#', "%23");
		url = replace(url, '£', "%A3");
		url = replace(url, '@', "%40");
		url = replace(url, '\'', "%27");
		url = replace(url, ' ', "%20");

		return url;
	}

	private String replace(String source, char oldChar, String dest) {
		String str = "";
		for (int i = 0; i < source.length(); i++) {
			if (source.charAt(i) != oldChar) {
				str += source.charAt(i);
			} else {
				str += dest;
			}
		}
		return str;
	}

	public void promptUserToCreateGoogleAccount(String dialogTitle,
			String dialogBody, String negativeBtnLabel,
			String positiveBtnLabel, final String settingArea) {
		// TODO Auto-generated method stub
		// setResult(account);
		AlertDialog.Builder settingsAlert = new AlertDialog.Builder(_this);

		// Setting Dialog Title
		settingsAlert.setTitle(dialogTitle);
		// Setting Dialog Message
		settingsAlert.setMessage(dialogBody);
		// On pressing Settings button
		settingsAlert.setPositiveButton(positiveBtnLabel,
				new DialogInterface.OnClickListener() {
					// On pressing Settings button
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(settingArea);

						_this.startActivity(intent);

					}
				});

		// on pressing cancel button
		settingsAlert.setNegativeButton(negativeBtnLabel,
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
		getMenuInflater().inflate(R.menu.activity_add_product, menu);
		return true;
	}

}
