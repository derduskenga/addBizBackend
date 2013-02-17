package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PlacesList extends Activity {

	GetGPSLocation gps;
	// Progress dialog
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places_list);
		ListView lv = (ListView) findViewById(R.id.list);

		Intent intent = getIntent();
		ArrayList<HashMap<String, String>> placesListItems = (ArrayList<HashMap<String, String>>) intent
				.getSerializableExtra("list_of_hashMap");

		ListAdapter adapter = new SimpleAdapter(PlacesList.this,
				placesListItems, R.layout.list_item,
				new String[] { "reference", "lat", "lng", "types", "vicinity",
						"name" }, new int[] { R.id.reference, R.id.lat,
						R.id.lng, R.id.type, R.id.vicinity, R.id.name });

		// Adding data into listview
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString().trim();
				final String reference = ((TextView) view
						.findViewById(R.id.reference)).getText().toString()
						.trim();
				final String lat = ((TextView) view.findViewById(R.id.lat))
						.getText().toString().trim();
				final String lng = ((TextView) view.findViewById(R.id.lng))
						.getText().toString().trim();
				final String types = ((TextView) view.findViewById(R.id.type))
						.getText().toString().trim();
				final String vicinity = ((TextView) view
						.findViewById(R.id.vicinity)).getText().toString()
						.trim();

				// TODO Auto-generated method stub
				// if (!distance.equals("") && !name.equals("")) {

				gps = new GetGPSLocation(PlacesList.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {

					final String url = "https://maps.googleapis.com/maps/api/place/details/"
							+ "json?reference="
							+ reference
							+ "&sensor=true&key=AIzaSyAttUIUmoIsdmm7K7p_"
							+ "Xe0ZkvztzciDbpk";

					Thread trd = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Looper.prepare();
							Log.e("Registration",
									"gone to connectin class with url " + url);
							HttpResponse response = null;
							// code to do the HTTP request
							InputStream is = null;
							String result = "";
							JSONObject jsonObj = null;

							// Creating HTTP client
							final HttpParams httpParams = new BasicHttpParams();
							HttpConnectionParams.setConnectionTimeout(
									httpParams, 30000);
							HttpClient httpClient = new DefaultHttpClient(
									httpParams);
							// Creating HTTP Post
							HttpPost httpPost = new HttpPost(url);
							Log.e("Registration", "go on...........");

							try {
								response = httpClient.execute(httpPost);

								HttpEntity entity = response.getEntity();

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
										new InputStreamReader(is, "iso-8859-1"),
										8);
								StringBuilder sb = new StringBuilder();
								String line = null;
								while ((line = reader.readLine()) != null) {
									sb.append(line + "\n");
								}
								is.close();
								result = sb.toString();
								Log.d("res", "4 " + result);
								// call the method that will return a value
								jsonObj = new JSONObject(result);
								String status = jsonObj.getString("status");

								if (status.equals("OK")) {

									HashMap<String, String> placeDetails = jsonParserForDetails(jsonObj);

									Log.d("res", "4 we got ok");

									Intent showPlaceDetails = new Intent(
											getApplicationContext(),
											PlaceDetails.class);
									showPlaceDetails.putExtra("name", name);
									showPlaceDetails.putExtra("vicinity",
											vicinity);
									showPlaceDetails.putExtra("types", types);
									showPlaceDetails.putExtra(
											"place_detail_map", placeDetails);

									// dismiss dialog here
									pDialog.dismiss();
									startActivity(showPlaceDetails);

								} else if (status.equals("ZERO_RESULTS")
										|| status.equals("REQUEST_DENIED")
										|| status.equals("INVALID_REQUEST")) {
									pDialog.dismiss();
									Toast.makeText(
											PlacesList.this,
											"No result, refine your search criterion",
											Toast.LENGTH_LONG).show();
									Log.d("res", "4 we got ok n we r in it");

								} else if (status.equals("OVER_QUERY_LIMIT")) {
									pDialog.dismiss();
									Toast.makeText(
											getApplicationContext(),
											"Similar applications have made too many requests to the server",
											Toast.LENGTH_LONG).show();

								} else {
									pDialog.dismiss();
									Toast.makeText(
											getApplicationContext(),
											"An error ocured, please try again",
											Toast.LENGTH_LONG).show();
								}

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
					//
					// pDialog = new ProgressDialog(PlacesList.this);
					// pDialog.setMessage(Html.fromHtml("<b></b><br/>Loading Place Details..."));
					// pDialog.setIndeterminate(false);
					// pDialog.setCancelable(false);
					// pDialog.show();

				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
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
		pDialog = new ProgressDialog(PlacesList.this);
		pDialog.setMessage(Html
				.fromHtml("<b></b><br/>Loading Place Details..."));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();

		// Close it after 2 seconds
		closeHandler.sendEmptyMessageDelayed(0, 32000);
	}

	private HashMap<String, String> jsonParserForDetails(JSONObject jsonObj) {

		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			JSONObject r = jsonObj.getJSONObject("result");

			String formatted_address = r.getString("formatted_address");
			String url_detail = r.getString("url");
			String international_phone = "";
			String website = "";
			if (r.has("international_phone_number")) {
				international_phone = r.getString("international_phone_number");

			} else if (r.has("formatted_phone_number")) {
				international_phone = r.getString("formatted_phone_number");
			}

			if (r.has("website")) {

				website = r.getString("website");
			}

			map.put("formatted_address", formatted_address);
			map.put("url", url_detail);
			map.put("formatted_phone", international_phone);
			map.put("website", website);

		} catch (JSONException jsonEx) {
			jsonEx.printStackTrace();
		}

		return map;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_places_list, menu);
		return true;
	}

}
