package com.addBusiness.addbiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CommonConnectionClass {
	static Context context;

	

	public CommonConnectionClass(Context context) {
		super();
		this.context = context;
	}

	public void commonConnectionMethod(final Hashtable<String, String> hashTable,
			final String url) {
		 
		
		Thread trd = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				Log.e("Registration", "gone to connectin class with url " + url);
				HttpResponse response = null;
				// code to do the HTTP request
				InputStream is = null;
				String result = "";

				// Creating HTTP client
				HttpClient httpClient = new DefaultHttpClient();
				// Creating HTTP Post
				HttpPost httpPost = new HttpPost(url);

				// Building post parameters
				// key and value pair

				int items_number = hashTable.size();// this will be the number of
													// NameValuePair
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(items_number);
				Log.e("Registration", Integer.toString(items_number));

				 Enumeration<String> enumer = hashTable.keys();
				 while (enumer.hasMoreElements()) {
				 String keyFromTable = (String) enumer.nextElement();
				 // get Returns the value to which the specified key is mapped,
				 // or null if this map contains no mapping for the key
				
				 nameValuePair.add(new BasicNameValuePair(keyFromTable,hashTable.get(keyFromTable)));
				 Log.e("Registration", "pairs added succesifully");
				
				 Log.e("Registration", keyFromTable + " = " +
				 hashTable.get(keyFromTable).length());
				 }

				//nameValuePair.add(new BasicNameValuePair("messag","Hi, trying Android HTTP post!"));

				// Url Encoding the POST parameters
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				} catch (UnsupportedEncodingException e) {
					// writing error to Log
					e.printStackTrace();
				}

				// Making HTTP Request
				Log.e("Registration", "go on...........");

				try {
					response = httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					// String htmlResponse = EntityUtils.toString(entity);

					// writing response to log
					Log.d("res", "1 " + response.toString());

					// Log.d("res", htmlResponse);

				} catch (Exception e) {
					// writing exception to log
					Log.d("res", "2 " + e.toString());
					// e.printStackTrace();
				}
				// catch (IOException e) {
				// // writing exception to log
				// e.printStackTrace();
				// Log.d("res", "3 " + e.toString());
				//
				// }

				// convert response to string
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
					
					//call the method that will return a value
					returnResponse(result);
					Toast.makeText(context, "You added" , Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Log.e("log_tag", "Error converting result " + e.toString());
				}

				
				
			}

			
			
		});
		
		trd.start();
		
		

	}
	
	/**
	 * Returns the first google mail account in that device
	 * @param void
	 * @return USER_GOOGLE_MAIL
	 */

	public static String firstGoogleMailAccount() {

		AccountManager acm = AccountManager.get(context);

		Account[] accounts = acm.getAccountsByType("com.google");

		String USER_GOOGLE_MAIL = "";
		if (accounts.length > 0) {

			USER_GOOGLE_MAIL = accounts[0].name.toString();
		}

		return USER_GOOGLE_MAIL;

	}
	
	
	private String returnResponse(String result) {
		
		return result;
		// TODO Auto-generated method stub
		
	}

}
