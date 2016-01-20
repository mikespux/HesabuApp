package com.hesabu;
/**
 * @author Michael N.Orenge
 * 
 */
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hesabuapp.R;

public class UpdateALert extends Activity {
	private String accountId;
	private EditText editSprice;
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_alert);

	
		editSprice = (EditText) this.findViewById(R.id.editSprice);
		
		
	}
	
	
	
	

	@Override
	public void onStart() {
		super.onStart();
		accountId = this.getIntent().getStringExtra("accountid");
		Log.d("Accounts", "Account Id : " + accountId);
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor account = db.query(Database.ALERT_TABLE_NAME , null,
				" _id = ?", new String[] { accountId }, null, null, null);
		//startManagingCursor(accounts);
		if (account.moveToFirst()) {
			// update view
			
		
			editSprice.setText(account.getString(account
					.getColumnIndex(Database.ALERT_AMOUNT)));
			
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
          
           
            queryValues.put("pstation", editSprice.getText().toString());
         
        
			ContentValues values = new ContentValues();

		
			values.put( Database.ALERT_AMOUNT, queryValues.get("pstation"));
			
			

			long rows = db.update(Database.ALERT_TABLE_NAME, values,
					"_id = ?", new String[] { accountId });

			db.close();
			if (rows > 0){
				Toast.makeText(this, "Updated Alert Successfully!",
						Toast.LENGTH_LONG).show();
			//	syncUpdate();
			}
			else{
				Toast.makeText(this, "Sorry! Could not update Alert!",
						Toast.LENGTH_LONG).show();}
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteAccount(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete this Alert?")
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
			int rows = db.delete(Database.ALERT_TABLE_NAME, "_id=?", new String[] { accountId});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Alert Deleted Successfully!", Toast.LENGTH_LONG).show();
				
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delete Alert!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
    
   
   
    
}
