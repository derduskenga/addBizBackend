package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONObject;

import com.addBusiness.addbiz.R.string;

import android.media.audiofx.Visualizer;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBusiness extends Activity {
	Button btnAddBusiness;
	EditText txtBusinessName;
	EditText txtBusinessType;
	AddBusiness _this;
	ProgressDialog pDialog;
	GetGPSLocation gps;
	String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_business);
		addListenerToButton();

	}

	public void addListenerToButton() {
		Log.d("res", "in addListenerToButton func 1");

		btnAddBusiness = (Button) findViewById(R.id.btnAddBusiness);
		txtBusinessName = (EditText) findViewById(R.id.txtBusinessName);
		txtBusinessType = (EditText) findViewById(R.id.txtBusinessType);
		Log.d("res", "in addListenerToButton func 2");
		btnAddBusiness.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				gps = new GetGPSLocation(AddBusiness.this);

				if (gps.canGetLocation()) {
					final double lat = gps.getLatitude();
					final double lng = gps.getLongitude();

					fetchBusinessVicinity(lat, lng);
					try{
						wait(2000);
					}catch(Exception e){
						e.printStackTrace();
					}
					
					SharedPreferences sp = getSharedPreferences("vicinity", 3);
					final String vic = sp.getString("vic", "defValue");
					
					Log.d("res", "in addListenerToButton func 3");
					// TODO Auto-generated method stub
					final String name = txtBusinessName.getText().toString()
							.trim();
					final String typeOrDescription = txtBusinessType.getText()
							.toString().trim();
					final String email = firstGoogleMailAccount();
					// connect to addBiz servers and upload the data
					Log.d("res", "in addListenerToButton func 4");

					if (name.equals("") || typeOrDescription.equals("")) {
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
									String url = encodeURL(Utilities.url_registerBiz_to_addBizz);

									Log.e("Registration",
											"gone to connectin class with url "
													+ url);

									HttpResponse response = null;
									// code to do the HTTP request
									InputStream is = null;
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
									Log.e("Registration", "go on..........with vic as. " + vic);
									List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
											6);

									nameValuePair.add(new BasicNameValuePair(
											"email", email));
									nameValuePair.add(new BasicNameValuePair(
											"name", encodeURL(name)));
									nameValuePair.add(new BasicNameValuePair(
											"type", encodeURL(typeOrDescription)));
									nameValuePair.add(new BasicNameValuePair(
											"lat", String.valueOf(lat)));
									nameValuePair.add(new BasicNameValuePair(
											"lng", String.valueOf(lng)));
									nameValuePair.add(new BasicNameValuePair(
											"vic", vic));

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

						} else {
							String dialogTitle = getResources().getString(
									R.string.no_internet_connection);
							String dialogBody = getResources()
									.getString(
											R.string.enable_data_or_leave_airplane_mode);
							String negativeBtnLabel = getResources().getString(
									R.string.cancel);
							String positiveBtnLabel = getResources().getString(
									R.string.settings);
							final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
							promptUserToCreateGoogleAccount(dialogTitle,
									dialogBody, negativeBtnLabel,
									positiveBtnLabel, settingArea);
						}

					}

				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

			}

		});

	}

	public void fetchBusinessVicinity(Double lat, Double lng) {

		final double lati = -1.3123622;
		final double lngi = 36.8129811;

		final String url = encodeURL(Utilities.FETCH_LOCATION_VICINITY + "location="
				+ lat + "," + lng + "&radius=100&sensor=false&key="
				+ Utilities.API_KEY);

		Thread trd = new Thread(new Runnable() {
			public String vicinity = "";

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				Log.e("Registration", "gone to connectin class with url " + url);
				HttpResponse response = null;
				// code to do the HTTP request
				InputStream is = null;
				String result = "";
				JSONObject jsonObj = null;

				// Creating HTTP client
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 30000);

				HttpClient httpClient = new DefaultHttpClient(httpParams);

				// Creating HTTP Post
				HttpPost httpPost = new HttpPost(url);
				Log.e("Registration", "go on...........");

				try {
					response = httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					// writing response to log
					Log.d("res", "1 vicinity " + response.toString());

				} catch (ConnectTimeoutException ctEx) {

					Toast.makeText(getApplicationContext(),
							"Server not responding", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// writing exception to log
					Log.d("res", "2 " + e.toString());
					// e.printStackTrace();
				}

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();

					result = sb.toString();

					pDialog.setMessage(Html
							.fromHtml("<b>Please wait...</b><br/>Now submitting your location"));
					// call the method that will return a value

					jsonObj = new JSONObject(result);
					Log.d("res", "" + result);
					String status = jsonObj.getString("status");

					if (status.equals("OK")) {

						JSONArray resultJson = jsonObj.getJSONArray("results");
						JSONObject r = resultJson.getJSONObject(1);

						if (r.has("vicinity")) {

							vicinity = r.getString("vicinity");
							
							Log.d("res", "vicinity = " + vicinity);
							
							SharedPreferences sp = getSharedPreferences(
									"vicinity", 3);
							SharedPreferences.Editor edit = sp.edit();
							edit.putString("vic", vicinity);
							edit.commit();
						}
					}

				} catch (Exception e) {
					Log.e("log_tag", "Error converting result " + e.toString());
				}
				Looper.loop();

			}

		});

		trd.start();
		openDialog();

	}

	private Handler closeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (pDialog != null)
				pDialog.dismiss();

		}
	};

	public void openDialog() {
		// Open the dialog
		pDialog = new ProgressDialog(AddBusiness.this);
		pDialog.setMessage(Html
				.fromHtml("<b>Please wait...</b><br/>Determining your location"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		closeHandler.sendEmptyMessageDelayed(0, 12000);
		// show toast

	}

	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;
		}
		return false;
	}

	private void promptUserToCreateGoogleAccount(String dialogTitle,
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

	public String firstGoogleMailAccount() {
		String USER_GOOGLE_MAIL = "";
		AccountManager acm = AccountManager.get(_this);

		Account[] accounts = acm.getAccountsByType("com.google");

		if (accounts.length > 0) {

			USER_GOOGLE_MAIL = accounts[0].name.toString();
		}

		return USER_GOOGLE_MAIL;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_business, menu);
		return true;
	}
	
	private String encodeURL(String url) {
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

}
