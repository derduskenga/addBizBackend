package com.addBusiness.addbiz;

import java.util.List;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Help_options extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_options);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		
		//Toast.makeText(this, id, Toast.LENGTH_LONG).show();
		
		ListView lst_help_options= (ListView)findViewById(R.id.about_list);
		
		final String [] help_options  = {getResources().getString(R.string.title_activity_help_options),
				getResources().getString(R.string.feedback_list_option),
				getResources().getString(R.string.about_add_biz_list_option)};//{Help.Feedback,About add biz}
		
		
		
		//about arrayAdapter parameters
		// First paramenter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		
		ArrayAdapter<String> help_options_adapter = new ArrayAdapter<String>(this, 
																R.layout.activity_help_options,
																R.id.help_options_list_text,
																help_options);
		// Assign adapter to ListView
		lst_help_options.setAdapter(help_options_adapter);
		
//		lst_help_options.setOnHoverListener(new View.OnHoverListener() {
//			
//			@Override
//			public boolean onHover(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
		
		//set onitemclick listener on the listview
		
		lst_help_options.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			  {
				  String list_text_clicked = ((TextView)view.findViewById(R.id.help_options_list_text)).getText().toString();
				  
				  if(list_text_clicked.equals(getResources().getString(R.string.title_activity_help_options))){//"Help"
					  
					  //let the reader see your help
					  
				  }else if (list_text_clicked.equals(getResources().getString(R.string.feedback_list_option))){// "Feedback"
					  //provide a user with some place to submit feedback
					  
				  }else if(list_text_clicked.equals(getResources().getString(R.string.about_add_biz_list_option))){//About add biz
					  new AlertDialog.Builder(Help_options.this)
						.setTitle(R.string.about_add_biz_list_option)
						.setMessage(R.string.about_details)
						.setNeutralButton(R.string.ok, null)
						.show();
						return;
					  
					  
				  }
			  }
			});
		
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_help_options, menu);
		return true;
	}

}
