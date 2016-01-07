package com.example.opac;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {

	private TabHost mtabhost;
	SessionManager session;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		session = new SessionManager(getApplicationContext());		
		HashMap<String, String> user = session.getUserDetails();
        // username
        String name = user.get(SessionManager.KEY_NAME);
 
		
		mtabhost = getTabHost(); 
		TabHost.TabSpec spec;
		Intent intent;
		//Intent myIntent = new Intent(getIntent());
		//String username = myIntent.getStringExtra("user");
		TextView textview = (TextView) findViewById(R.id.tv);		
		textview.setText("Hi,"+ name+ " "+ "Welcome...");
		TextView exit = (TextView) findViewById(R.id.logout);
		exit.setText("LogOut");
		   exit.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	 session.logoutUser();
		        }
		}); 
		   TextView profile = (TextView) findViewById(R.id.profile);
			 profile.setText("Profile");
			 profile.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			        			Intent myIntent = new Intent(HomeActivity.this, ProfileActivity.class);
			        			HomeActivity.this.startActivity(myIntent);
			        	 }			        	 
			}); 
		   
		 
		
		//Books tab
		intent = new Intent(this, BooksActivity.class);
		spec = mtabhost.newTabSpec("books")
				.setIndicator("Books")
				.setContent(intent);
		mtabhost.addTab(spec);
		
		
		//Notification tab
		intent = new Intent(this, NotificationActivity.class);
		spec = mtabhost.newTabSpec("notification")
				.setIndicator("Notification")
				.setContent(intent);
		mtabhost.addTab(spec);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

}
