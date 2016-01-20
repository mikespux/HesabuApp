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

public class UpdateStock extends Activity {
	private String accountId;
	private EditText editItem, editQuantity, editBprice, editSprice;
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_stock);
		editItem = (EditText) this.findViewById(R.id.editItem);
		editQuantity = (EditText) this.findViewById(R.id.editQuantity);
		editBprice = (EditText) this.findViewById(R.id.editBSprice);
		editSprice = (EditText) this.findViewById(R.id.editSprice);
		
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Updating Stock. Please wait...");
		prgDialog.setCancelable(false);
	}
	
	
	
	

	@Override
	public void onStart() {
		super.onStart();
		accountId = this.getIntent().getStringExtra("accountid");
		Log.d("Accounts", "Account Id : " + accountId);
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query(Database.STOCK_TABLE_NAME, null,
				" _id = ?", new String[] { accountId }, null, null, null);
		//startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			editItem.setText(account.getString(account
					.getColumnIndex(Database.STOCK_NAME)));
			editQuantity.setText(account.getString(account
					.getColumnIndex(Database.STOCK_QUANTITY)));
			editBprice.setText(account.getString(account
					.getColumnIndex(Database.STOCK_BPRICE)));
			editSprice.setText(account.getString(account
					.getColumnIndex(Database.STOCK_SPRICE)));
			
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
		     double value;
		
	            double quant= Double.parseDouble(editQuantity.getText().toString());
				double bprice= Double.parseDouble(editBprice.getText().toString());
	             value=quant*bprice;
	             String val=String.valueOf(value);
		    HashMap<String, String> queryValues = new HashMap<String, String>();
            queryValues.put("pacno", editItem.getText().toString());
            queryValues.put("pcno", editQuantity.getText().toString());
            queryValues.put("pfname", editBprice.getText().toString());
            queryValues.put("pstation", editSprice.getText().toString());
            queryValues.put("value",val);
        
			ContentValues values = new ContentValues();
			values.put( Database.STOCK_NAME, queryValues.get("pacno"));
			values.put( Database.STOCK_QUANTITY, queryValues.get("pcno"));
			values.put( Database.STOCK_BPRICE, queryValues.get("pfname"));
			values.put( Database.STOCK_SPRICE, queryValues.get("pstation"));
			values.put( Database.STOCK_VALUE, queryValues.get("value"));
			

			long rows = db.update(Database.STOCK_TABLE_NAME, values,
					"_id = ?", new String[] { accountId });

			db.close();
			if (rows > 0){
				Toast.makeText(this, "Updated Stock Successfully!",
						Toast.LENGTH_LONG).show();
			syncUpdate();
			}
			else{
				Toast.makeText(this, "Sorry! Could not update Stock!",
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
			int rows = db.delete(Database.STOCK_TABLE_NAME, "_id=?", new String[] { accountId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Stock Deleted Successfully!", Toast.LENGTH_LONG).show();
				
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
     * Get list of stock from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
    	DBHelper dbhelper = new DBHelper(this);
    	 String selectQuery = "SELECT  * FROM stock ";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
       // Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("item", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_NAME)));
                map.put("quantity", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_QUANTITY)));
                map.put("bprice", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_BPRICE)));
                map.put("sprice", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_SPRICE)));
                map.put("value", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_VALUE)));
                map.put("date", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_DATER)));
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_USERID)));
    	          
               
                
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
   	 String selectQuery = "SELECT  * FROM stock where _id ='"+accountId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("item", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_NAME)));
                map.put("quantity", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_QUANTITY)));
                map.put("bprice", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_BPRICE)));
                map.put("sprice", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_SPRICE)));
                map.put("value", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_VALUE)));
                map.put("date", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_DATER)));
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.STOCK_USERID)));
    	          
    	               
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
   	 String selectQuery = "SELECT  * FROM stock where _id ='"+accountId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                        map.put("item", cursor.getString(cursor
    	    					.getColumnIndex(Database.STOCK_NAME)));
                        map.put("userid", cursor.getString(cursor
            					.getColumnIndex(Database.STOCK_USERID)));
    	               
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
        String selectQuery = "SELECT  * FROM stock where status = '"+"yes"+"'";
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
		   
        String updateQuery = "Update stock set status = '"+ status +"' where item="+"'"+ id +"'";
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
                params.put("updatestock", composeJSONfromSQLite(accountId));
                client.post("http://wascakenya.co.ke/synchesabu/updatestock.php",params ,new AsyncHttpResponseHandler() {
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
                Toast.makeText(getApplicationContext(), "No data in SQLite DB, to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }
    public void syncDelete(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  getAllUsers();
        if(userList.size()!=0){
       
              //  prgDialog.show();
                params.put("deletestock", composeDelJSONfromSQLite(accountId));
                client.post("http://wascakenya.co.ke/synchesabu/deletestock.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                       // prgDialog.hide();
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
                Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }
}
