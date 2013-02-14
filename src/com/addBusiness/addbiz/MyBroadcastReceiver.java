package com.addBusiness.addbiz;





import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
	

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			String action = intent.getAction();
			if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {

				String registrationId = intent
						.getStringExtra("registration_id");
				String error = intent.getStringExtra("error");
				String unregistered = intent.getStringExtra("unregistered");

				// registration succeeded
				if (registrationId != null) {
					// store registration ID on shared preferences
					// notify 3rd-party server about the registered ID
					Log.e("Registration", registrationId);

					putIntoSharedPreference(registrationId, context,intent);
					
					
					//Intent i = new Intent(context,PreferenceIntentService.class);
					//Log.e("Registration", registrationId);
				}

				// unregistration succeeded
				if (unregistered != null) {
					// get old registration ID from shared preferences
					// notify 3rd-party server about the unregistered ID
					Log.e("Registration", unregistered);
				}

				// last operation (registration or unregistration) returned an
				// error;
				if (error != null) {
					if ("SERVICE_NOT_AVAILABLE".equals(error)) {
						// optionally retry using exponential back-off
						// (see Advanced Topics)
					} else {
						// Unrecoverable error, log it
						Log.i("Registration", "Received error: " + error);
					}
				}

			} else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {

				// server sent 2 key-value pairs, score and time
				String score = intent.getStringExtra("score");
				String time = intent.getStringExtra("time");
				// generates a system notification to display the score and time
			}

		} catch (Exception e) {
			
			Log.e("Registration", "at an exception............" + e.toString());
		}
	}

	public void putIntoSharedPreference(String registrationId, Context context, Intent intent){
		try{
			 Log.e("Registration", "gone to shared preference");
				//String regId = intent.getStringExtra("registration_id");				 
				SharedPreferences sp = context.getSharedPreferences(Utilities.REGISTRATION_ID_PREFERENCE,0);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("id", intent.getStringExtra("registration_id"));
				editor.commit();
				
				//register your device to server
				Log.e("Registration", "has commited shared preferences");
				
				
				 Log.e("Registration", "has commited in shared preference");
		}catch(Exception e){
			 Log.e("Registration", "caught an exception while in prefence: " + e.toString());
			
		}
		
		
	}
	
	

}