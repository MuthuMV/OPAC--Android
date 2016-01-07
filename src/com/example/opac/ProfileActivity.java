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
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	HttpClient httpclient;
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpEntity httpentity;
	ArrayList<NameValuePair> nameValuePairs;
	InputStream is;
	String result;
	SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		session = new SessionManager(getApplicationContext());		
		HashMap<String, String> user = session.getUserDetails();
        // username
        String name = user.get(SessionManager.KEY_NAME);
        
      //httppost
		try{
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://192.168.137.1:80/profile.php");
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user",name));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    httpresponse = httpclient.execute(httppost);
		    httpentity = httpresponse.getEntity();
	        is = httpentity.getContent();
		}
		catch(Exception e){
		     Log.e("log_tag", "Error in http connection "+e.toString());
			Toast.makeText(getBaseContext(), "http post error"+e, Toast.LENGTH_SHORT ).show();
		}
		
		//result conversion
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}
		catch(Exception e){
			Log.e("log_tag", "Error in converting result "+e.toString());
			Toast.makeText(getBaseContext(), "result conversion error"+e, Toast.LENGTH_SHORT ).show();
		}
		
		//parsing json data
		try{
			final ArrayList<String> ret_books = new ArrayList<String>();
			String returnString;
			String user_name = new String();
	        String dept = new String();
			//String[] returnbooks = new String[100];
	        JSONArray jArray = new JSONArray(result);
	        for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                //returnbooks[i] = json_data.getString("Reserved");
                returnString = json_data.getString("Reserved");	
                ret_books.add(returnString);
                user_name=json_data.getString("Name");
                dept=json_data.getString("Department");
	        }
	    	TextView books = (TextView) findViewById(R.id.tvbook);
	    	TextView username = (TextView) findViewById(R.id.tvuser);
	    	username.setText("Username:"+ " "+name+
										"\n"+ "Name:"+ " "+user_name+
										"\n"+ "Department:"+ " "+ dept);
	      	 for(int i=0; i<=ret_books.size(); i++){
	      		 String temp = (String) ret_books.get(i);
	      		 books.append(temp+"\n\t\t\t\t");
	      	 }
	   	
	        
		}catch(Exception e){
	        Log.e("log_tag", "Error parsing data "+e.toString());
			//Toast.makeText(getBaseContext(), "parsing error"+e, Toast.LENGTH_SHORT ).show();
		}
		
	 
	}
		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_profile, menu);
		return true;
	}

}
