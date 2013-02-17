package com.addBusiness.addbiz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Login extends Activity {
	Login _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final EditText txtEmail =  (EditText) findViewById(R.id.txtEmail);
		final EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
		 Button btnLogin = (Button)findViewById(R.id.btnLogin);
		final TextView lblCreateAccount = (TextView)findViewById(R.id.lblCreateAccount);
		final TextView  lblForgotPassword = (TextView)findViewById(R.id.lblForgotPassword);
		
		String emailToSet = CommonConnectionClass.firstGoogleMailAccount().toString();
		Log.e("db", "starting...right away 1");
		txtEmail.setText(emailToSet);
		Log.e("db", "starting...right away 2 ");
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stube
				//fetch credentials and login
				Log.e("db", "starting...right away in");
				String email = txtEmail.getText().toString().trim();
				String password = txtPassword.getText().toString().trim();
				
				if(email.equals("") || password.equals("")){//empty fields
					//prompt the user that the fiels are empty
					Log.e("db", "starting...first loop");
					Toast.makeText(_this, "some empty field", Toast.LENGTH_LONG).show();
					
				}else{
					//Log.e("db", "starting...");
					DatabaseHandler db = new DatabaseHandler(_this);
					//Log.e("db", "goin on 1.....");
					
					int flag = db.getAccount(new com.addBusiness.addbiz.Account(email,password));
					
					//Log.e("db", "goin on 2....");
					
					if(flag>0){//correct credentials
						//Log.e("db", "goin on 3.....");
						Toast.makeText(_this, "Correct credentilas", Toast.LENGTH_LONG).show();
						//update our LOGIN_STATUS preference to true
						updateLoginSharedPreferenceToTrue();
						//direct to BusinessOwnerTabs.java
						Intent intent = new Intent(_this, BusinessOwnerTabs.class);
						_this.startActivity(intent);
						
					}else{//wrong credentials
						
						Toast.makeText(_this, "Wrong credentilas", Toast.LENGTH_LONG).show();
						
					}
				}
				
			}
		});
		
		lblCreateAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//load create account form
				Intent iCA = new Intent(_this,CreateAccount.class);
				_this.startActivity(iCA);
			}
		});
		
		lblForgotPassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent iFP = new Intent(_this,ForgotPassword.class);
				_this.startActivity(iFP);
				
			}
		});
	}
	
	public void updateLoginSharedPreferenceToTrue(){
		
		Log.e("status", "updating shared preference to true after successiful login");
		SharedPreferences sp = getSharedPreferences(Utilities.LOGIN_STATUS, 1);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("status", "true");
		editor.commit();
		
		Log.e("status", "has commited  shared preference true after successiful login");
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

}
