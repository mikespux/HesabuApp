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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class UpdateExpense extends Activity {
	private String accountId;
	private EditText editItem, editSprice;
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_expense);
		editItem = (EditText) this.findViewById(R.id.editItem);
	
		editSprice = (EditText) this.findViewById(R.id.editSprice);
		
		  
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Updating Expense. Please wait...");
		prgDialog.setCancelable(false);
	}
	
	
	
	

	@Override
	public void onStart() {
		super.onStart();
		accountId = this.getIntent().getStringExtra("accountid");
		Log.d("Accounts", "Account Id : " + accountId);
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query(Database.EXPENSE_TABLE_NAME , null,
				" _id = ?", new String[] { accountId }, null, null, null);
		//startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			editItem.setText(account.getString(account
					.getColumnIndex(Database.EXPENSE_NAME)));
		
			editSprice.setText(account.getString(account
					.getColumnIndex(Database.EXPENSE_AMOUNT)));
			
		}
		account.close();
		db.close();
		dbhelper.close();

	}

	public void updateAccount(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute insert command
		    HashMap<String, String> queryValues = new HashMap<String, String>();
            queryValues.put("pacno", editItem.getText().toString());
           
            queryValues.put("pstation", editSprice.getText().toString());
         
        
			ContentValues values = new ContentValues();
			values.put( Database.EXPENSE_NAME, queryValues.get("pacno"));
		
			values.put( Database.EXPENSE_AMOUNT, queryValues.get("pstation"));
			
			

			long rows = db.update(Database.EXPENSE_TABLE_NAME, values,
					"_id = ?", new String[] { accountId });

			db.close();
			if (rows > 0){
				Toast.makeText(this, "Updated Expense Successfully!",
						Toast.LENGTH_LONG).show();
				syncUpdate();
			}
			else{
				Toast.makeText(this, "Sorry! Could not update Expense!",
						Toast.LENGTH_LONG).show();}
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteAccount(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete this expense?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	    syncDelete();
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
			int rows = db.delete(Database.EXPENSE_TABLE_NAME, "_id=?", new String[] { accountId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Expense Deleted Successfully!", Toast.LENGTH_LONG).show();
				
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delete Expense!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
    
    public void listAccountTransactions(View v) {
    	Intent intent = new Intent(this,Activity_Expense.class);
    	intent.putExtra("accountid", accountId);
    	startActivity(intent);
	}
    /**
     * Get list of stock from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
    	DBHelper dbhelper = new DBHelper(this);
    	 String selectQuery = "SELECT  * FROM expense ";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
       // Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_NAME)));
                map.put("amount", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_AMOUNT)));
                map.put("date", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_DATE)));
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_USERID)));
               
                
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
    	accountId = this.getIntent().getStringExtra("accountid");
   	 String selectQuery = "SELECT  * FROM expense where _id ='"+accountId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_NAME)));
                map.put("amount", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_AMOUNT)));
                map.put("date", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_DATE)));
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_USERID)));
    	          
    	               
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
    	accountId = this.getIntent().getStringExtra("accountid");
   	 String selectQuery = "SELECT  * FROM expense where _id ='"+accountId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_NAME)));
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.EXPENSE_USERID)));
    	               
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
        String selectQuery = "SELECT  * FROM expense where status = '"+"yes"+"'";
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
		   
        String updateQuery = "Update expense set status = '"+ status +"' where name="+"'"+ id +"'";
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
                params.put("updateexpense", composeJSONfromSQLite(accountId));
                client.post("http://wascakenya.co.ke/synchesabu/updateexpense.php",params ,new AsyncHttpResponseHandler() {
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
                            Toast.makeText(getApplicationContext(), "[No Internet Connection]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
           
        }else{
                Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }
    public void syncDelete(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  getAllUsers();
        if(userList.size()!=0){
       
                //prgDialog.show();
                params.put("deleteexpense", composeDelJSONfromSQLite(accountId));
                client.post("http://wascakenya.co.ke/synchesabu/deleteexpense.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        //prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Toast.makeText(getApplicationContext(), "Delete Successfull!", Toast.LENGTH_LONG).show();
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
                       // prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "[No Internet Connection]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
           
        }else{
                Toast.makeText(getApplicationContext(), "No data in SQLite DB, to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }
    
}
