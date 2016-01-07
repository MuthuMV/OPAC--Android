package com.example.opac;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceActivity;
 
public class SessionManager extends PreferenceActivity {
    // Shared Preferences
    SharedPreferences pref;
 
    // Editor for Shared preferences
    Editor editor;
 
    // Context
    Context _context;
 
    // Shared pref mode
    int PRIVATE_MODE = 0;
 
    // Sharedpref file name
    private static final String PREF_NAME = "Sharedpref_filename";
 
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
 
    // User name (make variable public to access from outside)
    public static  String KEY_NAME = "name";
 
    // Email address (make variable public to access from outside)
    public static  String KEY_EMAIL = "email";
    
    private static final String IS_RES = "IsReserved";
    public static  String book = "not reserved";
   public static ArrayList<String> ar = new ArrayList<String>();
    public static String[] res_book;
    public static int array_size;

 
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
 
    /**
     * Create login session
     * */
    public void createLoginSession(String username, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
 
        // Storing name in pref
        editor.putString(KEY_NAME, username);
 
        // Storing email in pref
        editor.putString(KEY_EMAIL, password);
 
        // commit changes
        editor.commit();
    }   
 
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 
            // Staring Login Activity
            _context.startActivity(i);
        }
 
    }
 
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
 
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
 
        // return user
        return user;
    }
 
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
 
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, StartingActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 
        // Staring Login Activity
        _context.startActivity(i);
    }
    

    
    //passing books array
    
    public void res_array(String s){
    	ar.add(s);
    	for(int i=0;i<ar.size();i++)
    	{
    	         editor.putString("val"+i,ar.get(i));
    	}
    	 editor.putInt("array_size",ar.size());
    	 editor.commit();
    }
   
    //retrieving books array
    public boolean test_array(String s){
    	int size=pref.getInt("array_size",0);
    	ArrayList<String> myarray=new ArrayList<String>();
    	for(int j=0;j<=size;j++)
    	{
    	          myarray.add(pref.getString("val"+j, null));
    	}
    	return myarray.contains(s);
    }
    

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}