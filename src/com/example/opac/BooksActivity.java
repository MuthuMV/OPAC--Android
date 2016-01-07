package com.example.opac;


import org.apache.http.client.methods.HttpPost;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;


public class BooksActivity extends Activity {

	HttpClient httpclient;
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpEntity httpentity;
	ArrayList<NameValuePair> nameValuePairs;
	InputStream is;
	String result = "";
	private ListView listView;
	EditText inputSearch;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_books);
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("stock","5"));
		listView = (ListView) findViewById(R.id.list);
		listView.setTextFilterEnabled(true);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		//httppost
		try{
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://192.168.137.1:80/books.php");
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
			//String[] returnString = new String[100];
	        JSONArray jArray = new JSONArray(result);	
	        ArrayList<String> items = new ArrayList<String>();
	        for(int i=0;i<jArray.length();i++){
	                JSONObject json_data = jArray.getJSONObject(i);
	               // returnString[i] = json_data.getString("Title");	
	                String book=json_data.getString("Title");
	                items.add(book);
	        }
	        
	        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, items);
	        listView.setAdapter(adapter);
	        inputSearch.addTextChangedListener(new TextWatcher() {	        	 
	        	 @Override
	             public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	                 // When user changed the Text
	        		 
	        		 ((ArrayAdapter<String>) BooksActivity.this.adapter).getFilter().filter(cs);
	             }
	  
	             @Override
	             public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                     int arg3) {
	                 // TODO Auto-generated method stub
	  
	             }
	  
	             @Override
	             public void afterTextChanged(Editable arg0) {
	                 // TODO Auto-generated method stub
	     
	             }
	        });
	        listView.setOnItemClickListener(new OnItemClickListener()
	        {
	            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
	            { 
	            	Intent resIntent = new Intent(BooksActivity.this,ReservationActivity.class);
	            	LinearLayout ll = (LinearLayout) arg1;
	            	TextView tv = (TextView) ll.findViewById(R.id.product_name);
	            	final String book = tv.getText().toString();
	            	//String book = ((TextView)arg1).getText().toString();
	            	resIntent.putExtra("book",book);
		        	BooksActivity.this.startActivity(resIntent);
	            }
	        });
		}catch(JSONException e){
	        Log.e("log_tag", "Error parsing data "+e.toString());
			Toast.makeText(getBaseContext(), "parsing error"+e, Toast.LENGTH_SHORT ).show();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_books, menu);
		return true;
	}

}
