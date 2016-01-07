package com.example.opac;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting);
		ImageButton books = (ImageButton) findViewById(R.id.starting_book);
		ImageButton latest = (ImageButton) findViewById(R.id.starting_latest);
		ImageButton login = (ImageButton) findViewById(R.id.starting_login);
	    books.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Intent myIntent = new Intent(StartingActivity.this,BooksDisplayActivity.class);
	        	StartingActivity.this.startActivity(myIntent);
	        }
	});   
	    latest.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	Intent myIntent = new Intent(StartingActivity.this,LatestActivity.class);
        	StartingActivity.this.startActivity(myIntent);
        }
	    });
	   login.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Intent myIntent = new Intent(StartingActivity.this,MainActivity.class);
	        	StartingActivity.this.startActivity(myIntent);
	        }
	});  
	}
	    
	    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_starting, menu);
		return true;
	}

}
