package com.addBusiness.addbiz;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "userAccount";
    private static final String TABLE_NAME = "accounts";
    
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//creating table accounts
		Log.e("db", "database creating");
		String createTable = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_EMAIL + " TEXT, " + KEY_PASSWORD + " TEXT)";
		db.execSQL(createTable);
		Log.e("db", "database created");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
 
        // Create tables again
        onCreate(db);
		
	}

	public void insertAccount(com.addBusiness.addbiz.Account account){//we return a value of type Account class
			
		SQLiteDatabase db = this.getWritableDatabase();
		Log.e("db", "in insert record");
		ContentValues values = new ContentValues();
		values.put(KEY_EMAIL, account.getEmailAddress());
		values.put(KEY_PASSWORD, account.getPassword());
		long rowId = db.insert(TABLE_NAME, null, values);//insert() the row ID of the newly inserted row, or -1 if an error occurred
		Log.e("db", String.valueOf(rowId));
		
		db.close();
		
	}
	
	
	public int getAccount(com.addBusiness.addbiz.Account account)throws SQLException{
		Log.e("db", "in function 1");
		  SQLiteDatabase db=this.getReadableDatabase(); 
		 Log.e("db", "in function 2 ....");
		 String selection = KEY_EMAIL+ "= ? AND "+KEY_PASSWORD+" = ?";
		 String[] selectionArgs = {account.getEmailAddress(), account.getPassword()};
		 //String tableName = "YourTable";
		 Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
		 int result = c.getCount();
		 c.close();
		 return result;
	}
	
	
	
	 public List<com.addBusiness.addbiz.Account> getAllContacts() {
	        List<Account> contactList = new ArrayList<Account>();
	        // Select All Query
	        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	 
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	com.addBusiness.addbiz.Account contact = new com.addBusiness.addbiz.Account();
	                contact.setId(Integer.parseInt(cursor.getString(0)));
	                contact.setEmailAddress(cursor.getString(1));
	                contact.setPassword(cursor.getString(2));
	                // Adding contact to list
	                contactList.add(contact);
	            } while (cursor.moveToNext());
	        }
	 
	        // return contact list
	        return contactList;
	    }
}
