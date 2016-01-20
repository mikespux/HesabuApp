package com.hesabu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UpdateUser extends Activity {
	private String accountId;
	private EditText editFname, editBName, editBType, editPhone,editLoc;
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_userinf);
		editFname = (EditText) this.findViewById(R.id.editFname);
		editBName = (EditText) this.findViewById(R.id.editBName);
		editBType = (EditText) this.findViewById(R.id.editBType);
		editPhone = (EditText) this.findViewById(R.id.editPhone);
		editLoc = (EditText) this.findViewById(R.id.editLoc);
		
		 
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Updating User. Please wait...");
		prgDialog.setCancelable(false);
	}
	
	
	
	

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UpdateUser.this);
     	accountId = prefs.getString("user", "anon");
		Log.d("Accounts", "Account Id : " + accountId);
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query(Database.USER_TABLE_NAME, null,
				" username = ?", new String[] { accountId }, null, null, null);
		//startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			editFname.setText(account.getString(account
					.getColumnIndex(Database.FNAME)));
			editBName.setText(account.getString(account
					.getColumnIndex(Database.BNAME)));
			editBType.setText(account.getString(account
					.getColumnIndex(Database.BTYPE)));
			editPhone.setText(account.getString(account
					.getColumnIndex(Database.PHONE)));
			editLoc.setText(account.getString(account
					.getColumnIndex(Database.LOCATION)));
			
		}
		account.close();
		db.close();
		dbhelper.close();

	}

	public void updateAccount(View v) {
		try {
			 if(editPhone.length() < 6 || editPhone.length() > 13)
			    {
			        
				 Toast.makeText(getApplicationContext(), "Please enter a valid Phone Number", Toast.LENGTH_LONG).show();
				 return;
			    }
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute insert command
		    HashMap<String, String> queryValues = new HashMap<String, String>();
            queryValues.put("fname", editFname.getText().toString());
            queryValues.put("bname", editBName.getText().toString());
            queryValues.put("btype", editBType.getText().toString());
            queryValues.put("phone", editPhone.getText().toString());
            queryValues.put("location", editLoc.getText().toString());
         
        
			ContentValues values = new ContentValues();
			values.put( Database.FNAME, queryValues.get("fname"));
			values.put( Database.BNAME, queryValues.get("bname"));
			values.put( Database.BTYPE, queryValues.get("btype"));
			values.put( Database.PHONE, queryValues.get("phone"));
			values.put( Database.LOCATION, queryValues.get("location"));
			

			long rows = db.update(Database.USER_TABLE_NAME, values,
					"username = ?", new String[] { accountId });

			db.close();
			if (rows > 0){
				Toast.makeText(this, "Updated User Information Successfully!",
						Toast.LENGTH_LONG).show();
				syncUpdate();
			}
			else{
				Toast.makeText(this, "Sorry! Could not update User!",
						Toast.LENGTH_LONG).show();}
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteAccount(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete this stock?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	  //  syncDelete();
			                deleteCurrentAccount();
			                
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
	}
	
	
         			
    public void deleteCurrentAccount() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.USER_TABLE_NAME, "_id=?", new String[] { accountId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "User Deleted Successfully!", Toast.LENGTH_LONG).show();
				
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delete Stock!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
    
    public void listAccountTransactions(View v) {
    	Intent intent = new Intent(this,Activity_AddStock.class);
    	intent.putExtra("accountid", accountId);
    	startActivity(intent);
	}
    /**
     * Get list of users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
    	DBHelper dbhelper = new DBHelper(this);
    	 String selectQuery = "SELECT  * FROM users ";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
       // Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("fname", cursor.getString(cursor
    					.getColumnIndex(Database.FNAME)));
                map.put("bname", cursor.getString(cursor
    					.getColumnIndex(Database.BNAME)));
                map.put("btype", cursor.getString(cursor
    					.getColumnIndex(Database.BTYPE)));
                map.put("pno", cursor.getString(cursor
    					.getColumnIndex(Database.PHONE)));
                map.put("location", cursor.getString(cursor
    					.getColumnIndex(Database.LOCATION)));
                map.put("username", cursor.getString(cursor
    					.getColumnIndex(Database.USERNAME_NAME)));
                map.put("password", cursor.getString(cursor
    					.getColumnIndex(Database.PASSWORD)));
    	          
               
                
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        
        return wordList;
    }
    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeJSONfromSQLite(String accountId){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
    	DBHelper dbhelper = new DBHelper(this);
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UpdateUser.this);
     	accountId = prefs.getString("user", "anon");
   	 String selectQuery = "SELECT  * FROM users where username ='"+accountId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("fname", cursor.getString(cursor
    					.getColumnIndex(Database.FNAME)));
                map.put("bname", cursor.getString(cursor
    					.getColumnIndex(Database.BNAME)));
                map.put("btype", cursor.getString(cursor
    					.getColumnIndex(Database.BTYPE)));
                map.put("pno", cursor.getString(cursor
    					.getColumnIndex(Database.PHONE)));
                map.put("location", cursor.getString(cursor
    					.getColumnIndex(Database.LOCATION)));
                map.put("username", cursor.getString(cursor
    					.getColumnIndex(Database.USERNAME_NAME)));
                map.put("password", cursor.getString(cursor
    					.getColumnIndex(Database.PASSWORD)));
    	          
    	               
                wordList.add(map);
            } while (cursor.moveToNext());
        }
       
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }
    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeDelJSONfromSQLite(String accountId){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
    	DBHelper dbhelper = new DBHelper(this);
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UpdateUser.this);
     	accountId = prefs.getString("user", "anon");
   	 String selectQuery = "SELECT  * FROM users where username ='"+accountId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                        map.put("username", cursor.getString(cursor
    	    					.getColumnIndex(Database.USERNAME_NAME)));
    	               
                wordList.add(map);
            } while (cursor.moveToNext());
        }
       
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }
    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync needed\n";
        }
        return msg;
    }
 
    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        DBHelper dbhelper = new DBHelper(this);
        String selectQuery = "SELECT  * FROM users where status = '"+"yes"+"'";
    	SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        count = cursor.getCount();
        
        return count;
    }
    public void updateSyncStatus(String id, String status){
    	DBHelper dbhelper = null;
		SQLiteDatabase db = null;
		dbhelper = new DBHelper(this);
	   db = dbhelper.getReadableDatabase();
		   
        String updateQuery = "Update users set status = '"+ status +"' where username="+"'"+ id +"'";
        Log.d("query",updateQuery);       
        db.execSQL(updateQuery);
  
        
    
    
    }
    public void syncUpdate(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  getAllUsers();
        if(userList.size()!=0){
       
                prgDialog.show();
                params.put("updateusers", composeJSONfromSQLite(accountId));
                client.post("http://wascakenya.co.ke/synchesabu/updateusers.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
 
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                        String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
           
        }else{
                Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }
   
}
