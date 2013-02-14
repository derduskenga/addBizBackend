package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AroundMe extends Activity {
	GetGPSLocation gps;
	// Progress dialog
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_around_me);
		Button btnSearch = (Button) findViewById(R.id.btnSearchPlaces);

		final EditText txtDistance = (EditText) findViewById(R.id.txtDistance);
		final EditText txtName = (EditText) findViewById(R.id.txtNameOfPlace);

		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if (!distance.equals("") && !name.equals("")) {
				String name = txtName.getText().toString().trim();
				String distance = txtDistance.getText().toString().trim();

				gps = new GetGPSLocation(AroundMe.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {

					 double latitude_ = gps.getLatitude();
					 double longitude_ = gps.getLongitude();
					 int factor = 10000000;
					
					 int scaled_and_roundedLAT = (int)(latitude_ * factor +
					 0.5);
					 int scaled_and_roundedLONG = (int)(longitude_ * factor +
					 0.5);
					
					 final double latitude = (double)scaled_and_roundedLAT /
					 factor;
					 final double longitude = (double)scaled_and_roundedLONG /
					 factor;

//					final double latitude = -1.3123622;
//					final double longitude = 36.8129811;

					Log.e("Registration", distance + " " + name + " "
							+ latitude + " " + longitude);

					final String url = encodeURL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
							+ +latitude
							+ ","
							+ longitude
							+ "&radius="
							+ distance
							+ "&name="
							+ name
							+ "&sensor="
							+ "true&key=" + Utilities.API_KEY);

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
								pDialog.dismiss();
								jsonObj = new JSONObject(result);
								String status = jsonObj.getString("status");

								if (status.equals("OK")) {

									ArrayList<HashMap<String, String>> placesList = jsonParser(jsonObj);

									Log.d("res", "4 we got ok");

									Intent intent = new Intent(
											getApplicationContext(),
											PlacesList.class);
									intent.putExtra("list_of_hashMap",
											placesList);
									pDialog.dismiss();
									startActivity(intent);

								} else if (status.equals("ZERO_RESULTS")
										|| status.equals("REQUEST_DENIED")
										|| status.equals("INVALID_REQUEST")) {
									// pDialog.dismiss();
									Toast.makeText(
											AroundMe.this,
											"No result, refine your search criterion",
											Toast.LENGTH_LONG).show();
									Log.d("res", "4 we got ok n we r in it");

								} else if (status.equals("OVER_QUERY_LIMIT")) {
									// pDialog.dismiss();
									Toast.makeText(
											getApplicationContext(),
											"Similar applications have made too many requests to the server",
											Toast.LENGTH_LONG).show();

								} else {
									// pDialog.dismiss();
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

				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}
				// }

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
		pDialog = new ProgressDialog(AroundMe.this);
		pDialog.setMessage(Html.fromHtml("<b>Loading Places...</b>"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();

		// Close it after 2 seconds
		closeHandler.sendEmptyMessageDelayed(0, 29000);
	}

	/**
	 * receives a json object and parses it to put the results in a list for
	 * viewing
	 * 
	 * @param jsonObject
	 * @return void
	 */
	public ArrayList<HashMap<String, String>> jsonParser(JSONObject jsonObject) {

		// Hashmap for ListView
		ArrayList<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
		// results JSONArray
		JSONArray results = null;
		Log.d("res", "4 we got ok n we r in functiont");
		try {
			// Getting Array of results
			results = jsonObject.getJSONArray("results");
			
			Log.d("res", "array length " + results.length());
			// looping through All Contacts

			for (int i = 0; i < results.length(); i++) {

				// Log.d("res", "array length " + results.length());
				// we have got an object under the resultd array
				JSONObject r = results.getJSONObject(i);
				// Log.d("res", "array length 1 ");
				String reference = r.getString("reference");
				// Log.d("res", "array length go on 2 ");
				String name = r.getString("name");
				// Log.d("res", "array length go on 3 ");

				// String rating = r.getString("rating");
				// Log.d("res", "array length go 4 ");
				String vicinity = r.getString("vicinity");
				// //Log.d("res", "array length 5 ");
				// types is an array in the object r
				JSONArray t = r.getJSONArray("types");

				String types = t.getString(0);
				Log.d("res", "checking wheter type was fetched " + types);

				// geometry is again an object
				JSONObject geometry = r.getJSONObject("geometry");

				// again location is a json object
				JSONObject location = geometry.getJSONObject("location");
				String latitude = location.getString("lat");
				String longitude = location.getString("lng");

				// Log.d("res1", " 5 " + name);
				// Log.d("res1", " 5 " + vicinity);
				// Log.d("res1", " 5 " + types);
				// Log.d("res1", " 5 " + reference);
				// //Log.d("res1", " 5 " + rating);
				// Log.d("res1", " 5 " + latitude);
				// Log.d("res1", " 5 " + longitude);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put("name", name);
				map.put("vicinity", vicinity);
				// map.put("rating", rating);
				map.put("types", types);
				map.put("lat", latitude);
				map.put("lng", longitude);
				map.put("reference", reference);

				placesList.add(map);
				// create an intent and go to another activity
				// pass ArrayList of hashmap

			}

		} catch (JSONException jsonEx) {
			// TODO: handle exception
			jsonEx.printStackTrace();
		}

		return placesList;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_around_me, menu);
		return true;
	}

}
