package com.addBusiness.addbiz;

import java.util.List;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAccount extends Activity {
	CreateAccount _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
		final EditText txtCPassword = (EditText) findViewById(R.id.txtCPassword);
		
		final TextView lblAlreadyHaveAccount = (TextView) findViewById(R.id.lblAlreadyHaveAccount);
		Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
		final TextView lblError = (TextView) findViewById(R.id.lblError);

		// fetch a user email and automatically set it into the email field
		String emailToSet = firstGoogleMailAccount();
		txtEmail.setText(emailToSet);

		btnCreateAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// store account to database if it does not exist
				String password = txtPassword.getText().toString().trim();
				String Cpassword = txtCPassword.getText().toString().trim();
				String email = txtEmail.getText().toString().trim();

				if (email.equals("") || password.equals("")
						|| Cpassword.equals("")) {
					Log.e("db", "empty fields");
					lblError.setText(getResources().getString(R.string.all_fields_are_required));
					

				} else if (!Cpassword.equals(password)) {// empty field
					Log.e("db", "passwords not matching");
					
					lblError.setText(getResources().getString(R.string.passwords_do_not_match));
					// lblError.setTextColor(errorColor);
				} else {
					// proceed to database
					Log.e("db", "everything cool");
					DatabaseHandler db = new DatabaseHandler(_this);
					// insert a record into the database
					lblError.setText("");
					db.insertAccount(new com.addBusiness.addbiz.Account(email,password));

					Log.e("db", "record inserted into database");

					Log.e("db", "record inserted" + email + " " + password);
					
					Log.e("db", "reading");
					
					List<com.addBusiness.addbiz.Account> contacts = db.getAllContacts();   
					 
			        for (com.addBusiness.addbiz.Account cn : contacts) {
			            String log = "Id: "+cn.getId()+" ,Name: " + cn.getEmailAddress() + " ,Phone: " + cn.getPassword();
			                // Writing Contacts to log
			            Log.d("db", log);
			        }
			        

					// prompt user that his/er record has been added
					// Direct him to login now
					AlertDialog.Builder successAlert = new AlertDialog.Builder(
							_this);

					// successAlert Dialog Title
					successAlert.setTitle(getResources().getString(
							R.string.account_created));
					// successAlert Dialog Message
					successAlert.setMessage(getResources().getString(
							R.string.you_have_set_account));

					successAlert.setPositiveButton(
							getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								// On pressing OK button
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(_this,
											Login.class);

									_this.startActivity(intent);

								}
							});

					// on pressing cancel button
					successAlert.setNegativeButton(
							getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					successAlert.show();

				}

			}
		});

		lblAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// show login form
				Intent i = new Intent(_this, Login.class);
				_this.startActivity(i);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_account, menu);
		return true;
	}

	/**
	 * Returns the first google mail account in that device
	 * 
	 * @param void
	 * @return USER_GOOGLE_MAIL
	 */

	public String firstGoogleMailAccount() {

		AccountManager acm = AccountManager.get(_this);

		Account[] accounts = acm.getAccountsByType("com.google");

		String USER_GOOGLE_MAIL = "";
		if (accounts.length > 0) {

			USER_GOOGLE_MAIL = accounts[0].name.toString();
		}

		return USER_GOOGLE_MAIL;

	}

}
