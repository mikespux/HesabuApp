package com.hesabu;

/**
 * @author Michael N.Orenge
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Login extends Activity {
	EditText txtUserName;
	EditText txtPassword;
	DBHelper dbUser;
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		 txtUserName = (EditText)findViewById(R.id.edtusername);
		 txtPassword = (EditText)findViewById(R.id.edtpassword);
		 prgDialog = new ProgressDialog(this);
	        prgDialog.setMessage("Synching user. Please wait...");
	        prgDialog.setCancelable(false);
	       syncSQLiteMySQLDB();
		Button btnLogin = (Button)findViewById(R.id.btnlogin);
		Button btnsignup = (Button)findViewById(R.id.btnsignup);
		btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String username = txtUserName.getText().toString();
				String password = txtPassword.getText().toString();
				if(username.equals("")||password.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					if(username.length() > 0 && password.length() >0)
					{
						dbUser = new DBHelper(Login.this);
						
						
						if(dbUser.Login(username, password))
						{
							// save user data
							SharedPreferences prefs = PreferenceManager
									.getDefaultSharedPreferences(Login.this);
							
			    			  Editor edit = prefs.edit();
			    			  edit.putString("user",username);				    			  
			    			  edit.commit();
			    			  
			    			  edit.putString("pass",password);
			    			  edit.commit();
							Toast.makeText(Login.this,"Successfully Logged In", Toast.LENGTH_LONG).show();
							 txtUserName.setText("");
			    			  txtPassword.setText("");
							Intent login =new Intent(Login.this,HomeActivity.class);
							startActivity(login);
						}else{
							Toast.makeText(Login.this,"Invalid Username/Password", Toast.LENGTH_LONG).show();
						}
						dbUser.close();
					}
					
				}catch(Exception e)
				{
					Toast.makeText(Login.this,e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
			
		});
		btnsignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent signup= new Intent(Login.this,RegisterActivity.class);
				startActivity(signup);
				
				
			}
		});
	}
    @Override
	public void onBackPressed() {
		//Display alert message when back button has been pressed
		backButtonHandler();
		return;
	}
	public void onStart() {
		super.onStart();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
        String uname = prefs.getString("user", "");
        String pass = prefs.getString("pass", "");
		 txtUserName.setText(uname);
		  txtPassword.setText(pass);
		String username = txtUserName.getText().toString();
		String password = txtPassword.getText().toString();
		
		try{
			if(username.length() > 0 && password.length() >0)
			{
				DBHelper dbUser = new DBHelper(Login.this);
				
				
				if(dbUser.Login(username, password))
				{
					
					
					
					 txtUserName.setText("");
	    			  txtPassword.setText("");
					Intent login =new Intent(Login.this,HomeActivity.class);
					startActivity(login);
				}else{
					
				}
				dbUser.close();
			}
			
		}catch(Exception e)
		{
			Toast.makeText(Login.this,e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void backButtonHandler() {
		
						  Intent intent = new Intent(Intent.ACTION_MAIN);
					        intent.addCategory(Intent.CATEGORY_HOME);
					        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					        startActivity(intent);
						finish();
						//System.exit(0);
	}
	
	// Method to Sync MySQL to SQLite DB
		public void syncSQLiteMySQLDB() {
			// Create AsycHttpClient object
			AsyncHttpClient client = new AsyncHttpClient();
			// Http Request Params Object
			RequestParams params = new RequestParams();
			// Show ProgressBar
			prgDialog.show();
			// Make Http call to getusers.php
			client.post("http://wascakenya.co.ke/synchesabu/getusers.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// Hide ProgressBar
						prgDialog.hide();
						// Update SQLite DB with response sent by getusers.php
						updateSQLite(response);
					}
					// When error occured
					@Override
					public void onFailure(int statusCode, Throwable error, String content) {
						// TODO Auto-generated method stub
						// Hide ProgressBar
						prgDialog.hide();
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
						} else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getApplicationContext(), "[No Internet Connection]",
									Toast.LENGTH_LONG).show();
						}
					}
			});
		}
		
		@SuppressWarnings("deprecation")
		public void updateSQLite(String response){
			ArrayList<HashMap<String, String>> usersynclist;
			usersynclist = new ArrayList<HashMap<String, String>>();
			// Create GSON object
			Gson gson = new GsonBuilder().create();
			try {
				// Extract JSON array from the response
				JSONArray arr = new JSONArray(response);
				System.out.println(arr.length());
				// If no of array elements is not zero
				if(arr.length() != 0){
					// Loop through each array element, get JSON object which has userid and username
					for (int i = 0; i < arr.length(); i++) {
						// Get JSON object
						JSONObject obj = (JSONObject) arr.get(i);
						System.out.println(obj.get("fname"));
						System.out.println(obj.get("bname"));
						System.out.println(obj.get("btype"));
						System.out.println(obj.get("pno"));
						System.out.println(obj.get("location"));
						System.out.println(obj.get("username"));
						System.out.println(obj.get("password"));
						

						DBHelper dbhelper = new DBHelper(this); 
						SQLiteDatabase db = dbhelper.getWritableDatabase();
		                Log.d("Account","Got Writable database");
		                
		                queryValues = new HashMap<String, String>();
		                queryValues.put("fname", obj.get("fname").toString());
		                queryValues.put("bname", obj.get("bname").toString());
		                queryValues.put("btype", obj.get("btype").toString());
		                queryValues.put("pno",obj.get("pno").toString());
		                queryValues.put("location", obj.get("location").toString());
		                queryValues.put("username", obj.get("username").toString());
		                queryValues.put("password", obj.get("password").toString());
		                queryValues.put("status","yes");
		          
		                
		                DBHelper dbh = new DBHelper(
								getApplicationContext());
						Cursor checkid =dbh.fetchUsername(obj.get("username").toString() );
						if (checkid == null) {
							//Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT).show();
						}
						else {
				    		startManagingCursor(checkid );
				    		
				    		//Check for duplicate id number
				    		if (checkid .getCount() > 0) {
				    	//	Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT).show();
				    			stopManagingCursor(checkid );
				    			checkid .close();
				    			return;
				    		}
				    		}
						// DB QueryValues Object to insert into SQLite
						
		                ContentValues values = new ContentValues();
						values.put( Database.FNAME, queryValues.get("fname"));
						values.put( Database.BNAME, queryValues.get("bname"));
						values.put( Database.BTYPE, queryValues.get("btype"));
						values.put( Database.PHONE, queryValues.get("pno"));
						values.put( Database.LOCATION, queryValues.get("location"));
						values.put( Database.USERNAME_NAME, queryValues.get("username"));
						values.put( Database.PASSWORD, queryValues.get("password"));
						values.put( Database.USTATUS, queryValues.get("status"));
						
						long rows = db.insert(Database.USER_TABLE_NAME, null, values);
						
						if ( rows > 0)  {
						    Toast.makeText(this, "Fetching User done...Successfully! ",	Toast.LENGTH_LONG).show();
						    HashMap<String, String> map = new HashMap<String, String>();
							// Add status for each User in Hashmap
							map.put("Id", obj.get("username").toString());
							map.put("status", "0");
							usersynclist.add(map);
						    
						    
					
						}
						else
							Toast.makeText(this, "Sorry! Could not Fetch User!",	Toast.LENGTH_LONG).show();
						
						
					}
					// Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
					updateMySQLSyncSts(gson.toJson(usersynclist));
					// Reload the Main Activity
					reloadActivity();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Method to inform remote MySQL DB about completion of Sync activity
		public void updateMySQLSyncSts(String json) {
			System.out.println(json);
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("syncsts", json);
			// Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
			client.post("http://wascakenya.co.ke/synchesabu/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Toast.makeText(getApplicationContext(),	"Sync Successfully Done.", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onFailure(int statusCode, Throwable error, String content) {
						Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
				}
			});
		}
		// Reload MainActivity
		public void reloadActivity() {
			Intent objIntent = new Intent(getApplicationContext(), Login.class);
			startActivity(objIntent);
		}
	
}
