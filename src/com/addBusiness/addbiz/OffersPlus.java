package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OffersPlus extends Activity {
	OffersPlus _this;
	private static final int DATE_DIALOG_ID = 999;
	private ImageButton btnSetDate;
	private EditText txtExpiryDate;
	private EditText txtPromotionalMessage;
	private Spinner spSelectProduct;
	private Spinner spTypeOfAddition;
	private TextView lblExpiryDate;
	private Button btnAddPromo;

	private int year;
	private int month;
	private int day;
	AddProduct addPro = null;

	public ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offers_plus);

		populateTypeOfAdditionSpinner();

		setCurrentDateOnView();

		addListenerImageDateButton();

		populateProductSpinner();

		addClickListenerToAddButton();

	}

	public void addClickListenerToAddButton() {
		btnAddPromo = (Button) findViewById(R.id.btnAddPromo);
		spSelectProduct = (Spinner) findViewById(R.id.spnSelectProduct);
		spTypeOfAddition = (Spinner) findViewById(R.id.spnTypeOfAddition);
		btnAddPromo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("Registration", "gone to http class with url 1");
				// TODO Auto-generated method stub
				txtExpiryDate = (EditText) findViewById(R.id.txtExpDate);
				txtPromotionalMessage = (EditText) findViewById(R.id.txtPromotionalMessage);
				String email = firstGoogleMailAccount();
				Log.e("Registration", "gone to http class with url 2");
				String expDate = txtExpiryDate.getText().toString().trim();

				String productName = spSelectProduct.getSelectedItem()
						.toString().trim();
				Log.e("Registration", "gone to http class with url 3");
				String typeOfAddition = spTypeOfAddition.getSelectedItem()
						.toString().trim();
				Log.e("Registration", "gone to http class with url 4");
				String message = txtPromotionalMessage.getText().toString()
						.trim();
				Log.e("Registration", "gone to http class with url 5");

				if (isOnline()) {

					httpMethod(email, expDate, productName, typeOfAddition,
							message);

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
					promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
							negativeBtnLabel, positiveBtnLabel, settingArea);

				}

			}
		});

	}

	public void httpMethod(final String email, final String expDate,
			final String productName, final String typeOfAddition,
			final String message) {
		Log.e("Registration", "gone to http class with url ");

		Thread trd = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.e("Registration",
						"gone to connectin class with url starting...1");
				String url = Utilities.url_addPromo;
				Looper.prepare();

				Log.e("Registration", "gone to connectin class with url " + url);

				HttpResponse response = null;
				// code to do the HTTP request
				InputStream is = null;
				// String result;
				Log.d("res", "in addListenerToButton func 5");
				// Creating HTTP client
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 13000);
				Log.d("res", "in addListenerToButton func 6");

				HttpClient httpClient = new DefaultHttpClient(httpParams);
				Log.d("res", "in addListenerToButton func 7");

				// Creating HTTP Post
				HttpPost httpPost = new HttpPost(url);

				Log.d("res", "in addListenerToButton func 8");
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
						5);
				Log.d("res", "in addListenerToButton func 7");
				nameValuePair.add(new BasicNameValuePair("email", email));
				nameValuePair.add(new BasicNameValuePair("expD", expDate));
				nameValuePair.add(new BasicNameValuePair("pName", productName));
				nameValuePair
						.add(new BasicNameValuePair("type", typeOfAddition));
				nameValuePair.add(new BasicNameValuePair("msg", message));

				Log.d("res", "in addListenerToButton func 9");
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				} catch (UnsupportedEncodingException e) {
					// writing error to Log
					e.printStackTrace();
				}

				try {
					response = httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					// writing response to log
					Log.d("res", "1 " + response.toString());

				} catch (ConnectTimeoutException ctEx) {

					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(_this, "Server not responding",
									Toast.LENGTH_LONG).show();
						}
					});

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

					final String result = sb.toString();

					Log.d("res", "4 " + result);

					pDialog.dismiss();

					// changes to gui can be done on the UI thread
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(_this, result, Toast.LENGTH_LONG)
									.show();

						}
					});

					Log.d("res", "in function populate 1");

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
		pDialog = new ProgressDialog(OffersPlus.this);
		pDialog.setMessage(Html
				.fromHtml("<b>Please wait...</b><br/>Adding your offer"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		closeHandler.sendEmptyMessageDelayed(0, 12000);
		// show toast

	}

	private void addListenerImageDateButton() {
		// TODO Auto-generated method stub
		btnSetDate = (ImageButton) findViewById(R.id.imgBtnCalender);
		btnSetDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);

			}
		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			// tvDisplayDate = (TextView) findViewById(R.id.tvDate);
			txtExpiryDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private void setCurrentDateOnView() {
		// TODO Auto-generated method stub

		txtExpiryDate = (EditText) findViewById(R.id.txtExpDate);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into EditText
		txtExpiryDate.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));

	}

	private void populateProductSpinner() {
		// TODO Auto-generated method stub
		Log.e("Registration", "gone to connectin class with url starting...1");
		// TODO Auto-generated method stub
		// connect to addBiz application server to collect all the
		// product
		// registered or added by this user
		// we require the email address as the id
		final String email = firstGoogleMailAccount();
		Log.e("Registration", "gone to connectin class with url starting...2");
		final String url = Utilities.url_registerBiz_to_populateSelectProduct;

		Log.e("Registration", "gone to connectin class with url starting...3");
		if (isOnline()) {
			Log.e("Registration",
					"gone to connectin class with url starting...4");
			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("Registration",
							"gone to connectin class with url starting...1");
					Looper.prepare();

					Log.e("Registration", "gone to connectin class with url "
							+ url);

					HttpResponse response = null;
					// code to do the HTTP request
					InputStream is = null;
					// String result;
					Log.d("res", "in addListenerToButton func 5");
					// Creating HTTP client
					final HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 13000);
					Log.d("res", "in addListenerToButton func 6");

					HttpClient httpClient = new DefaultHttpClient(httpParams);
					Log.d("res", "in addListenerToButton func 7");

					// Creating HTTP Post
					HttpPost httpPost = new HttpPost(url);

					Log.d("res", "in addListenerToButton func 8");
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
							1);
					Log.d("res", "in addListenerToButton func 7");
					nameValuePair.add(new BasicNameValuePair("email", email));

					Log.d("res", "in addListenerToButton func 9");
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(
								nameValuePair));
					} catch (UnsupportedEncodingException e) {
						// writing error to Log
						e.printStackTrace();
					}

					try {
						response = httpClient.execute(httpPost);

						HttpEntity entity = response.getEntity();

						is = entity.getContent();

						// writing response to log
						Log.d("res", "1 " + response.toString());

					} catch (ConnectTimeoutException ctEx) {

						Toast.makeText(getApplicationContext(),
								"Server not responding", Toast.LENGTH_LONG)
								.show();
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

						final String result = sb.toString();

						Log.d("res", "4 " + result);

						// changes to gui can be done on the UI thread
						runOnUiThread(new Runnable() {
							public void run() {

								String[] output = generateArray(result, ",");
								populateSelectProductSpinner(output);

							}
						});

						Log.d("res", "in function populate 1");

					} catch (Exception e) {
						Log.e("log_tag",
								"Error converting result " + e.toString());
					}
					Looper.loop();

				}

			});

			trd.start();

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

	public void populateSelectProductSpinner(String[] productArray) {
		Log.d("res", "in function populate 1");
		spSelectProduct = (Spinner) findViewById(R.id.spnSelectProduct);
		Log.d("res", "in function populate 2");
		ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, productArray);
		Log.d("res", "in function populate 4");
		// Drop down layout style
		productAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Log.d("res", "in function populate 5");
		spSelectProduct.setAdapter(productAdapter);
	}

	public void populateTypeOfAdditionSpinner() {

		String[] types = { getResources().getString(R.string.New_arrival),
				getResources().getString(R.string.Offer_or_promotion) };
		spTypeOfAddition = (Spinner) findViewById(R.id.spnTypeOfAddition);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types);
		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// attaching data adapter to spinner
		spTypeOfAddition.setAdapter(dataAdapter);
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

	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;
		}
		return false;
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

	public String[] generateArray(String original, String separator) {

		Vector<String> nodes = new Vector<String>();

		// Parse nodes into vector
		int index = original.indexOf(separator);
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement(original);

		// Create splitted string array
		String[] result = new String[nodes.size() - 1];

		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size() - 1; loop++) {
				result[loop] = (String) nodes.elementAt(loop);
				// System.out.println(result[loop]); - uncomment this line to
				// see the result on output console
			}
		}

		return result;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_offers_plus, menu);
		return true;
	}

}
