package com.example.opac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {

	EditText username, password;
	Button login;
	String user, pass; 
	HttpClient httpclient;
	HttpPost httppost;
	ArrayList<NameValuePair> namevaluepairs =  new ArrayList<NameValuePair>();

	HttpResponse httpresponse;
	HttpEntity httpentity;	
	
	AlertDialogManager alert = new AlertDialogManager();
	 
    // Session Manager Class
    SessionManager session;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialise();
	}
	
	private void initialise(){
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		session = new SessionManager(getApplicationContext());
		//forgot = (Button) findViewById(R.id.forgot);
		login.setOnClickListener(this);
		//forgot.setOnClickListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
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

	@Override
	public void onClick(View v) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		// TODO Auto-generated method stub
		//creating Default http client
		httpclient = new DefaultHttpClient();

		//creating http post with url to php 
		httppost = new HttpPost("http://192.168.137.1:80/user.php");

		//assigning the input details to strings
		user = username.getText().toString();
		pass = password.getText().toString();
		
		//String tempuser = "[a-z]";
		//if(user.matches(tempuser) && pass.matches(tempuser)){
		
		try{
			//placing the strings arrays
			namevaluepairs.add(new BasicNameValuePair("username", user));
			namevaluepairs.add(new BasicNameValuePair("password", pass));
			//Toast.makeText(getBaseContext(), "LoginSuccessful", Toast.LENGTH_SHORT).show();

			//adding arraylist to post
			httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

			//assigning form container(httpclient) to response
			httpresponse = httpclient.execute(httppost);
			
			//checking status code
			if(httpresponse.getStatusLine().getStatusCode() == 200){
				
				//assigning response entity to httpentity
				httpentity = httpresponse.getEntity();
				
				//
				if(httpentity != null){
					
					//passing the data from httpentity to new stream
					InputStream inputstream = httpentity.getContent();
					
					//creating json object with converted data as parameter
					JSONObject jsonresponse = new JSONObject(convertStreamToString(inputstream));
					//assigning json response to mysql table fields
					String retuser = jsonresponse.getString("username");
					String retpass = jsonresponse.getString("password");
					
					 if(user.length() > 0 && pass.length() > 0){
		                    // For testing puspose username, password is checked with sample data
		                    // username = test
		                    // password = test
						 
							if(user.equals(retuser) && pass.equals(retpass)){
								
								session.createLoginSession(user, pass);								
								Toast.makeText(getBaseContext(), "LoginSuccessful", Toast.LENGTH_SHORT).show();
								Intent myIntent = new Intent(MainActivity.this,HomeActivity.class);						 
								myIntent.putExtra("user", user);
								//myIntent.putExtra(USER_SERVICE,true);
								MainActivity.this.startActivity(myIntent); 
							}
							else
							{
		                        // username / password doesn't match
		                        alert.showAlertDialog(MainActivity.this, "Login failed..", "Username/Password is incorrect", false);
							}
		                }else{
		                    // user didn't entered username or password
		                    // Show alert asking him to enter the details
		                    alert.showAlertDialog(MainActivity.this, "Login failed..", "Please enter username and password", false);
		                	 
		                }
					

					//login validation
					/*if(user.equals(retuser) && pass.equals(retpass)){
						
						//creating a shared preference
						SharedPreferences sp = getSharedPreferences("logindetails",0);
						
						//editing the shared preference
						SharedPreferences.Editor spedit = sp.edit();
						spedit.putString("username", user);
						spedit.putString("password", pass);
						
						//closing the editor
						spedit.commit();
						Toast.makeText(getBaseContext(), "LoginSuccessful", Toast.LENGTH_SHORT).show();
						Intent myIntent = new Intent(MainActivity.this,HomeActivity.class);						 
						myIntent.putExtra("user", user);
						myIntent.putExtra(USER_SERVICE,true);
						MainActivity.this.startActivity(myIntent); 
					}else{
						Toast.makeText(getBaseContext(), "Invalid login", Toast.LENGTH_SHORT ).show();
					}*/
				}
			}
				
			
		}catch(Exception e){
			//e.printStackTrace();
			//Toast.makeText(getBaseContext(), "Login Unsuccessful"+ e, Toast.LENGTH_LONG ).show();
			alert.showAlertDialog(MainActivity.this, "Login failed..", "Please enter correct username and password", false);

		}
	//}else{
		//Toast.makeText(getBaseContext(), "Check your login details", Toast.LENGTH_LONG ).show();
	//}
		
	}

}
