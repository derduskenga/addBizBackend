package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterBizData extends Activity {

	GetGPSLocation gps;
	ProgressDialog pDialog;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_biz_data);
		

		final Button btnRegisterBiz = (Button) findViewById(R.id.btnRegisterBizLocation);
		final AlertDialog al =  new AlertDialog.Builder(this).create();

		btnRegisterBiz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// create class object

				EditText txtBizDescription = (EditText) findViewById(R.id.txtBizDescription);
				TextView lblBizDescription = (TextView) findViewById(R.id.lblBizDescritpion);

				gps = new GetGPSLocation(RegisterBizData.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {

					final double latitude = gps.getLatitude();
					final double longitude = gps.getLongitude();
					Log.e("Registration", latitude + " and " + longitude);
					// Get other user details and add the business locations
					final String bizDescription = txtBizDescription.getText()
							.toString().trim();
					final String type = "other";
					String lang = Utilities.LANG;
					final int accuracy = 15;
					 Thread t = new Thread(new Runnable(){
						 String url="https://maps.googleapis.com/maps/api/place/add/json?sensor=false&key=AIzaSyBXLNJygLtodLbRKKOHPgAFeqw6FPmIoRY";
						 String json = "{\"location\":{\"lat\": " + latitude
									+ ",\"lng\":" + longitude + "}"
									+ ",\"accuracy\":" + accuracy
									+ ",\"name\":\"" + bizDescription
									+ "\"," + "\"types\":[\"" + type
									+ "\"],\"language\":\"en\"}";
						 InputStream is;
						 String result;
						 String userResponse;
				            public void run() {
				                Looper.prepare(); //For Preparing Message Pool for the child Thread
				                HttpClient client = new DefaultHttpClient();
				                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
				                HttpResponse response;
				                //JSONObject json = new JSONObject();
				                Log.e("Registration", "gone to connectin class with url " + url);

				                try {
				                    HttpPost post = new HttpPost(url);
//				                    json.put("email", email);
//				                    json.put("password", pwd);
				                    StringEntity se = new StringEntity( json);  
				                    se.setContentType((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				                    post.setEntity(se);
				                    Log.e("Registration", "gone to connectin class with url ");
				                    response = client.execute(post);
				                    
				                    Log.e("Registration", "gone to connectin class with url " + response);

				                    /*Checking response */
				                    if(response!=null){
				                         is = response.getEntity().getContent(); //Get the data in the entity
				                        Log.e("Registration", "gone to connectin class with url " + is);
				                        
				                        HttpEntity entity = response.getEntity();

				    					is = entity.getContent();

				    					// String htmlResponse = EntityUtils.toString(entity);

				    					// writing response to log
				    					Log.d("res", "1 " + response.toString());

				                    }

				                } catch(Exception e) {
				                    e.printStackTrace();
				                    //createDialog("Error", "Cannot Estabilish Connection");
				                }
				            	try {
									BufferedReader reader = new BufferedReader(new InputStreamReader(
											is, "iso-8859-1"), 8);
									StringBuilder sb = new StringBuilder();
									String line = null;
									while ((line = reader.readLine()) != null) {
										sb.append(line + "\n");
									}
									is.close();
									result = sb.toString();
									Log.d("res", "4 " + result);
								
									JSONObject jsonObj = new JSONObject(result);
									
									if(jsonObj.getString("status").equals("OK")){
										 userResponse = "Your business location was added to google maps";
										 
									}else{
										 userResponse  = "Operation failed, try again";
									}
									
									
									
									pDialog.dismiss();
									//al.cancel();
									Toast.makeText(getApplicationContext(), userResponse, Toast.LENGTH_LONG).show();
									
									
									
								} catch (Exception e) {
									Log.e("log_tag", "Error converting result " + e.toString());
								}

				                Looper.loop(); //Loop in the message queue
				            }
				        });

				        t.start(); 
				        
				        pDialog= new ProgressDialog(RegisterBizData.this);
						pDialog.setMessage(Html.fromHtml("<b>Adding...</b><br/>Adding your location..."));
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(false);
			            pDialog.show();

				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register_biz_data, menu);
		return true;
	}
	
	

}
