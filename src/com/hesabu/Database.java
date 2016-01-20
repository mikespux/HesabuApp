package com.hesabu;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

public class Database {
	public static final String STOCK_TABLE_NAME = "stock";
	public static final String STOCK_ID = "_id";
	public static final String STOCK_NAME = "item";
	public static final String STOCK_QUANTITY = "quantity";
	public static final String STOCK_BPRICE = "bprice";
	public static final String STOCK_SPRICE = "sprice";
	public static final String STOCK_VALUE = "value";
	public static final String STOCK_DATER = "date";
	public static final String STOCK_USERID = "uid";
	public static final String SSTATUS = "status";
	
	
	public static final String EXPENSE_TABLE_NAME = "expense";
	public static final String EXPENSE_ID = "_id";
	public static final String EXPENSE_NAME = "name";
    public static final String EXPENSE_AMOUNT = "amount";	
    public static final String EXPENSE_DATE = "transdate";
	public static final String EXPENSE_USERID = "uid";
	public static final String ESTATUS = "status";
	
	public static final String PRODUCTION_TABLE_NAME = "production";
	public static final String PRODUCTION_ID = "_id";
	public static final String PRODUCTION_NAME = "name";
    public static final String PRODUCTION_AMOUNT = "amount";	
    public static final String PRODUCTION_DATE = "transdate";
	public static final String PRODUCTION_USERID = "uid";
	public static final String PSTATUS = "status";

	public static final String ALERT_TABLE_NAME = "alert";
	public static final String ALERT_ID = "_id";
    public static final String ALERT_AMOUNT = "alert";	
   	public static final String ALERT_USERID = "uid";
	public static final String ASTATUS = "status";
	
	public static final String FEEDBACK_TABLE_NAME = "feedback";
	public static final String FEED_ID = "_id";
	public static final String FEED_SUB = "sub";	
    public static final String FEED_MESSAGE = "message";
    public static final String FEED_DATE = "feeddate";
   	public static final String FEED_USERID = "uid";
	public static final String FSTATUS = "status";
	
	
	public static final String USER_TABLE_NAME = "users";
	public static final String USER_ID = "_id";
	public static final String FNAME = "fname";
	public static final String BNAME = "bname";
	public static final String BTYPE = "btype";
	public static final String PHONE = "pno";
	public static final String LOCATION = "location";
	public static final String USERNAME_NAME = "username";
	public static final String PASSWORD = "password";
	public static final String USTATUS = "status";
	
	
	
	
	public static final String TRANSACTIONS_TABLE_NAME = "transactions";
	public static final String TRANSACTIONS_ID = "_id";
	public static final String TRANSACTIONS_ACCOUNT_ID = "account_id";
	public static final String TRANSACTIONS_TRANSDATE = "transdate";
	public static final String TRANSACTIONS_TRANSTYPE = "transtype";
	public static final String TRANSACTIONS_TRANSAMOUNT = "transamount";
	public static final String TRANSACTIONS_CHEQUE_NO = "cheque_no";
	public static final String TRANSACTIONS_CHEQUE_PARTY = "cheque_party";
	public static final String TRANSACTIONS_CHEQUE_DETAILS = "cheque_details";
	public static final String TRANSACTIONS_REMARKS  = "remarks";
	public static final String TRANSACTIONS_USERID= "uid";
	public static final String TSTATUS = "status";

	public static Account cursorToAccount(Cursor accounts) {
		Account account = new Account();
		account.setId( accounts.getString(accounts.getColumnIndex(Database.STOCK_ID)));
		account.setPname(accounts.getString(accounts.getColumnIndex(Database.STOCK_NAME)));
		account.setSprice( accounts.getString(accounts.getColumnIndex(Database.STOCK_SPRICE)));
		account.setBPrice( accounts.getString(accounts.getColumnIndex(Database.STOCK_BPRICE)));
        return account;
	}

	public  static void populateAccounts(Spinner spinnerAccounts) {
		Context context = spinnerAccounts.getContext();
		DBHelper dbhelper = new DBHelper(context);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor accounts = db.query(Database.STOCK_TABLE_NAME, null, null,null, null, null, null);
		ArrayList<Account> list = new ArrayList<Account>();

	    // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		while (accounts.moveToNext()) {
			Account account =  Database.cursorToAccount(accounts);
			list.add(account);
		}
		accounts.close();
		db.close();
		dbhelper.close();
		
		ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(context, android.R.layout.simple_spinner_item,list);
		spinnerAccounts.setAdapter(adapter);
	}
	
	public  static boolean updateAccountBalance(SQLiteDatabase db, String accountId, String transType, double amount, String transDate) {
	  try {
		if ( transType.equals("Buying"))  
		    db.execSQL( " update " + Database.STOCK_TABLE_NAME + " set quantity = quantity + " + amount + ",value= (quantity + " + amount + ") * bprice where " + Database.STOCK_ID + " = " + accountId);
        else
	     	db.execSQL( " update " + Database.STOCK_TABLE_NAME + " set quantity = quantity - " + amount + ",value= (quantity - " + amount + ") * bprice where " + Database.STOCK_ID + " = " + accountId);
        return true; 
	   }
	   catch(Exception ex) {
		  Log.d("Accounts", "Error in UpdateBalance : " + ex.getMessage());
		  return false; 
	   }
	}
	
	public static String getAccountId(Spinner spinnerAccounts) {
		
         Account account = (Account) spinnerAccounts.getSelectedItem();
         
         return account.getId();
	}
	

	
	
	public static String getDateFromDatePicker(DatePicker dp) {
		return dp.getYear() + "-" + dp.getMonth() + 1 +  "-" + dp.getDayOfMonth();
	}
	
	public static boolean addTransaction(Context context, String accountId, String transType, String transDate, String transAmount, String chequeNo, String chequeParty,
			   String chequeDetails, String remarks,String username) {
		
		DBHelper dbhelper = null;
		SQLiteDatabase db = null;
		try {
			dbhelper = new DBHelper(context);
			db = dbhelper.getWritableDatabase();
			db.beginTransaction();

			// execute insert command
			ContentValues values = new ContentValues();
			values.put(Database.TRANSACTIONS_ACCOUNT_ID, accountId);
			values.put(Database.TRANSACTIONS_TRANSDATE, transDate);
			values.put(Database.TRANSACTIONS_TRANSAMOUNT, transAmount);
			values.put(Database.TRANSACTIONS_CHEQUE_NO, chequeNo);
			values.put(Database.TRANSACTIONS_CHEQUE_PARTY, chequeParty);
			values.put(Database.TRANSACTIONS_CHEQUE_DETAILS,chequeDetails);
			values.put(Database.TRANSACTIONS_REMARKS, remarks);
			values.put(Database.TRANSACTIONS_USERID, username);
			values.put(Database.TRANSACTIONS_TRANSTYPE, transType);
			values.put(Database.TSTATUS, "no");

			long rowid = db.insert(Database.TRANSACTIONS_TABLE_NAME, null, values);
			Log.d("Accounts","Inserted into TRANSACTIONS " + rowid);
			if ( rowid != -1) {
				 // update Accounts Table 
				 boolean done = Database.updateAccountBalance(db,accountId,transType, Double.parseDouble(transAmount),transDate);
 			     Log.d("Accounts","Updated Account Balance");
				 if ( done ) {
					 db.setTransactionSuccessful();
					 db.endTransaction();
					 return true;
				 }
				 else {
					 db.endTransaction();
					 return false; 
				 }
			}
			else
				return false; 
	    }
		catch(Exception ex) {
			Log.d("Account", "Error in addTransaction -->" + ex.getMessage());
			return false; 
		}
		finally {
			if ( db != null && db.isOpen()) {
				   db.close();
			}
		}
   } // addTransaction 
	
}
