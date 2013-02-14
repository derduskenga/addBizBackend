package com.addBusiness.addbiz;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class BusinessOwnerTabs extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_owner_tabs);
		
		TabHost tabhost = getTabHost();
		
		//tab addBusiness
		TabSpec addBusinessSpec = tabhost.newTabSpec("Add business");
		// setting Title and Icon for the Tab
		addBusinessSpec.setIndicator("Add business");
		Intent addBusinessSpecIntent = new Intent(this, AddBusiness.class);
		addBusinessSpec.setContent(addBusinessSpecIntent);
		
		//tab addProduct
		TabSpec addProductApec = tabhost.newTabSpec("Add Product");
		addProductApec.setIndicator("Add Product");
		Intent addProductIntent = new Intent(this, AddProduct.class);
		addProductApec.setContent(addProductIntent);
		
		//tab offer plus
		TabSpec addOfferPlus = tabhost.newTabSpec("Add Offer Plus");
		addOfferPlus.setIndicator("Add offers plus");
		Intent addOfferPlusIntent = new Intent(this, OffersPlus.class);
		addOfferPlus.setContent(addOfferPlusIntent);
		
	
		
		// Adding all TabSpec to TabHost
		tabhost.addTab(addBusinessSpec);
		tabhost.addTab(addProductApec);
		tabhost.addTab(addOfferPlus);
		
		
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_business_owner_tabs, menu);
		return true;
	}

}
