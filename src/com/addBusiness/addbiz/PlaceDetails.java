package com.addBusiness.addbiz;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

public class PlaceDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_details);
		
		//fetch data from an intent
		Intent intent = getIntent();
		
		String name = intent.getStringExtra("name");
		String  vicinity = intent.getStringExtra("vicinity");
		String types = intent.getStringExtra("types");
		
		
		HashMap<String, String> receivedMap = (HashMap<String, String>) intent.getSerializableExtra("place_detail_map");;
		
		String formatted_address = receivedMap.get("formatted_address");
		String url = receivedMap.get("url");
		String formatted_phone = receivedMap.get("formatted_phone");
		String website = receivedMap.get("website");
		
		TextView txtvname = (TextView)findViewById(R.id.name);
		TextView txtvVicinity = (TextView) findViewById(R.id.vicinity);
		TextView txtvtype = (TextView)findViewById(R.id.type);
		TextView txtvAddress = (TextView)findViewById(R.id.formatted_address);
		TextView txtvPhone = (TextView) findViewById(R.id.formatted_phone);
		TextView txtvUrl = (TextView)findViewById(R.id.url);
		TextView txtvWebsite = (TextView)findViewById(R.id.website);
		
		txtvname.setText("Name: " + name);
		txtvVicinity.setText("Vicinity: " + vicinity);
		txtvtype.setText("Of type: " + types);
		txtvAddress.setText("Address: " + formatted_address);
		
		txtvUrl.setText("Suggested Url: " + url);
		
		if(!website.equals("")){
			
			txtvWebsite.setText("Website: " + website);
		}
		
		if(!formatted_phone.equals("")){
			
			txtvPhone.setText("Phone: " + formatted_phone);
		}
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_place_details, menu);
		return true;
	}

}
