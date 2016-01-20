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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TransactionDetailsSale extends Activity {
	private String transId;
	private String accountId;
	private TextView textAcno;
	ProgressDialog prgDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_detailsale);
		
		transId = this.getIntent().getStringExtra("transid");
		Log.d("Account", "Trans id : " + transId);
		
		textAcno = (TextView) this.findViewById(R.id.textAcno);
		TextView textTransDate = (TextView) this.findViewById(R.id.textTransDate);
		TextView textTransType = (TextView) this.findViewById(R.id.textTransType);
		TextView textTransAmount = (TextView) this.findViewById(R.id.textTransAmount);
		TextView textChequeNo = (TextView) this.findViewById(R.id.textChequeNo);
		TextView textChequeParty = (TextView) this.findViewById(R.id.textChequeParty);
		TextView textChequeDetails = (TextView) this.findViewById(R.id.textChequeDetails);
		TextView textRemarks  = (TextView) this.findViewById(R.id.textTransRemarks);
		
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Deleting Transaction. Please wait...");
		prgDialog.setCancelable(false);
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor tran = db.rawQuery("select item,account_id,transdate,transamount,transtype,cheque_no,cheque_party,cheque_details, t.remarks from transactions t inner join stock a  on ( a._id = t.account_id) where t._id = ?",
				    new String[] {transId });
		
       		
		if (tran.moveToFirst()) {
			accountId  =  tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_ACCOUNT_ID));
			textAcno.setText( tran.getString(tran.getColumnIndex(Database.STOCK_NAME)));
			textTransDate.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_TRANSDATE)));
			textTransType.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_TRANSTYPE)));
			textTransAmount.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_TRANSAMOUNT)));
			textChequeNo.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_CHEQUE_NO)));
			textChequeParty.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_CHEQUE_PARTY)));
			textChequeDetails.setText( tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_CHEQUE_DETAILS)));
			textRemarks.setText(tran.getString(tran.getColumnIndex(Database.TRANSACTIONS_REMARKS)));
		}
		else
			Log.d("Accounts","No transaction found!");
		
		db.close();
		dbhelper.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return Utils.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  Utils.handleMenuOption(this,item);
	}
	
	public void deleteTransaction(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this transaction?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   syncDelete();
		               deleteCurrentTransaction();
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
	
         			
    public void deleteCurrentTransaction() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.TRANSACTIONS_TABLE_NAME, "_id=?", new String[] { transId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Transaction Deleted Successfully!", Toast.LENGTH_LONG).show();
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delet transaction!", Toast.LENGTH_LONG).show();
		} 
    	catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
    
    public void showAccountDetails(View v) {
        Intent intent = new Intent(this,UpdateStock.class);
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
    	 String selectQuery = "SELECT  * FROM transactions ";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
       // Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tid", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_ID)));
                map.put("stockid", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_ACCOUNT_ID)));
                map.put("date", cursor.getString(cursor
    					.getColumnIndex(Database. TRANSACTIONS_TRANSDATE)));
                map.put("transtype", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_TRANSTYPE)));
                map.put("quantity", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_TRANSAMOUNT)));
                map.put("price", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_CHEQUE_NO)));
                map.put("total", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_CHEQUE_PARTY)));
                map.put("remarks", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_REMARKS)));
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_USERID)));
    	          
               
                
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        
        return wordList;
    }
    
    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeDelJSONfromSQLite(String transId ){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
    	DBHelper dbhelper = new DBHelper(this);
    	transId = this.getIntent().getStringExtra("transid");
   	 String selectQuery = "SELECT  * FROM transactions where _id ='"+transId+"'";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tid", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_ID)));
             
                map.put("userid", cursor.getString(cursor
    					.getColumnIndex(Database.TRANSACTIONS_USERID)));
    	               
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
        String selectQuery = "SELECT  * FROM transactions where status = '"+"yes"+"'";
    	SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
       
        
        return count;
    }
    public void updateSyncStatus(String id, String status){
    	DBHelper dbhelper = null;
		SQLiteDatabase db = null;
		dbhelper = new DBHelper(this);
	   db = dbhelper.getReadableDatabase();
		   
        String updateQuery = "Update transactions set status = '"+ status +"' where _id="+"'"+ id +"'";
        Log.d("query",updateQuery);       
        db.execSQL(updateQuery);
  
        
    
    
    }
   
    public void syncDelete(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  getAllUsers();
        if(userList.size()!=0){
       
                prgDialog.show();
                params.put("deletetrans", composeDelJSONfromSQLite(transId));
                client.post("http://wascakenya.co.ke/synchesabu/deletetrans.php",params ,new AsyncHttpResponseHandler() {
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
}
