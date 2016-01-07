package com.example.opac;

import java.io.BufferedReader;
import java.io.IOException;
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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReservationActivity extends Activity {
	
	HttpClient httpclient;
	HttpPost httppost;
	HttpResponse httpresponse;
	HttpEntity httpentity;
	ArrayList<NameValuePair> nameValuePairs;
	InputStream is;
	String result;
	Button reserve;
	//SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SessionManager session = new SessionManager(ReservationActivity.this);
		setContentView(R.layout.activity_reservation); 
				
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		reserve = (Button) findViewById (R.id.reserve);
		
		Intent myIntent = new Intent(getIntent());
		String book = myIntent.getStringExtra("book");
	
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("title",book));
		

	
		
		//httppost
				try{
					httpclient = new DefaultHttpClient();
					httppost = new HttpPost("http://192.168.137.1:80/synopsis.php");
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
			        String isbn = new String();
			        String title = new String();
			        String author = new String();			        
			        String genre = new String();
			        String stock = new String();
			        //Toast.makeText(getBaseContext(), "before loop", Toast.LENGTH_SHORT ).show();
			        for(int i=0;i<jArray.length();i++){
			                JSONObject json_data = jArray.getJSONObject(i);
			               // returnString[i] = json_data.getString("Title");	
			                isbn=json_data.getString("ISBN");
			                 title=json_data.getString("Title");
			                author=json_data.getString("Author");
			                 genre=json_data.getString("Genre");
			                 stock=json_data.getString("Stock");			            
			        }
			        //Toast.makeText(getBaseContext(), isbn+title+author+synopsis+genre+stock, Toast.LENGTH_LONG ).show();
			    	TextView textview = (TextView) findViewById(R.id.tv);
					textview.setText("ISBN:"+ " "+isbn+
										"\n"+ "Title:"+ " "+title+
										"\n"+ "Author:"+ " "+ author+
										"\n"+ "Genre:"+ " "+ genre+
										"\n"+ "Stock:"+ " "+ stock);
					//Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
					final String res_f_stock = new String(stock);
					final String res_isbn = new String(isbn);
					final String res_book = new String(title);
					Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
				  reserve.setOnClickListener(new View.OnClickListener() {				        
				       
				        public void onClick(View v) {
				    		//Intent myIntent = new Intent(getIntent());				    		
				    		//Boolean log_check = myIntent.getBooleanExtra(USER_SERVICE, false);
				    		session.checkLogin();
				    		try{				    			
					    				nameValuePairs = new ArrayList<NameValuePair>();
					    				nameValuePairs.add(new BasicNameValuePair("isbn",res_isbn));
					    				nameValuePairs.add(new BasicNameValuePair("stock",res_f_stock));
					    				
					    				if(session.test_array(res_isbn)){									
					    					Toast.makeText(getBaseContext(), "You have already reserved this book", Toast.LENGTH_LONG ).show();
					    				}
					    				else{				    					
					    					httpclient = new DefaultHttpClient();
											httppost = new HttpPost("http://192.168.137.1:80/reserve.php");
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											httpresponse = httpclient.execute(httppost);
											session.res_array(res_isbn);
											Toast.makeText(getBaseContext(), "Book Reserved successfully", Toast.LENGTH_LONG ).show();
											
											//To store the username,book reserved
											HashMap<String, String> user = session.getUserDetails();
									        String name = user.get(SessionManager.KEY_NAME);
											nameValuePairs = new ArrayList<NameValuePair>();
						    				nameValuePairs.add(new BasicNameValuePair("username",name));
						    				nameValuePairs.add(new BasicNameValuePair("book",res_book));
						    				httpclient = new DefaultHttpClient();
											httppost = new HttpPost("http://192.168.137.1:80/reserved.php");
						    				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											httpresponse = httpclient.execute(httppost);
											
				    				}									
				    		}catch(Exception e){
								     Log.e("log_tag", "Error in http connection "+e.toString());
									Toast.makeText(getBaseContext(), "http post error"+e, Toast.LENGTH_SHORT ).show();
								}
				    		
				        	Intent resIntent = new Intent(ReservationActivity.this,HomeActivity.class);
				        	ReservationActivity.this.startActivity(resIntent);
				        }

						private String convertStreamToString(InputStream inputstream) {
							  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						        StringBuilder sb = new StringBuilder();
						 
						        String line = null;
						        try {
						            while ((line = reader.readLine()) != null) {
						                sb.append(line + "\n");
						            }
						        } catch (IOException e) {
						            e.printStackTrace();
						        } finally {
						            try {
						                is.close();
						            } catch (IOException e) {
						                e.printStackTrace();
						            }
						        }
						        return sb.toString();
						}
					    });
					
					
			        
				}catch(Exception e){
			        Log.e("log_tag", "Error parsing data "+e.toString());
					Toast.makeText(getBaseContext(), "parsing error"+e, Toast.LENGTH_SHORT ).show();
				}
			        
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reservation, menu);
		return true;
	}

}
