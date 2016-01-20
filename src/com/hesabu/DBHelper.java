package com.hesabu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBHelper extends SQLiteOpenHelper {
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "hesabu.db";
   
	public DBHelper(Context ctx) {
		super(ctx, DB_NAME, null, DB_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
          createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
	
	public void createTables(SQLiteDatabase database) {
		String stock_table_sql = "create table " + Database.STOCK_TABLE_NAME + " ( " +
				Database.STOCK_ID 	+ " integer  primary key autoincrement," + 
				Database.STOCK_NAME + " TEXT," +
				Database.STOCK_QUANTITY + " TEXT," +
				Database.STOCK_BPRICE + " TEXT," +
				Database.STOCK_SPRICE + " TEXT," +
				Database.STOCK_VALUE + " TEXT," +
				Database.STOCK_DATER + " TEXT," +
				Database.STOCK_USERID + " TEXT," +
				Database.SSTATUS + " TEXT)";
		
		String user_table_sql = "create table " + Database.USER_TABLE_NAME + " ( " +
				Database.USER_ID 	+ " integer  primary key autoincrement," + 
				Database.FNAME + " TEXT," +
				Database.BNAME + " TEXT," +
				Database.BTYPE + " TEXT," +
				Database.PHONE + " TEXT," +
				Database.LOCATION + " TEXT," +
				Database.USERNAME_NAME + " TEXT," +
				Database.PASSWORD + " TEXT," +
				Database.USTATUS + " TEXT)";
		
		
				
		String transactions_table_sql = "create table " + Database.TRANSACTIONS_TABLE_NAME + " ( " +
				Database.TRANSACTIONS_ID 	+ " integer  primary key autoincrement," + 
				Database.TRANSACTIONS_ACCOUNT_ID + " TEXT," +
				Database.TRANSACTIONS_TRANSDATE + " TEXT," +
				Database.TRANSACTIONS_TRANSAMOUNT + " FLOAT," +
				Database.TRANSACTIONS_TRANSTYPE+ " TEXT," +
				Database.TRANSACTIONS_CHEQUE_NO + " TEXT," +
				Database.TRANSACTIONS_CHEQUE_PARTY + " TEXT," +
				Database.TRANSACTIONS_CHEQUE_DETAILS+ " TEXT," +
				Database.TRANSACTIONS_REMARKS+ " TEXT," +
				Database.TRANSACTIONS_USERID+ " TEXT," +
				Database.TSTATUS + " TEXT)";
		
		String expense_table_sql = "create table " + Database.EXPENSE_TABLE_NAME + " ( " +
				Database.EXPENSE_ID 	+ " integer  primary key autoincrement," + 
				Database.EXPENSE_NAME + " TEXT," +
				Database.EXPENSE_AMOUNT + " FLOAT," +
				Database.EXPENSE_DATE + " TEXT," +
				Database.EXPENSE_USERID + " TEXT," +
				Database.ESTATUS + " TEXT)";
		String production_table_sql = "create table " + Database.PRODUCTION_TABLE_NAME + " ( " +
				Database.PRODUCTION_ID 	+ " integer  primary key autoincrement," + 
				Database.PRODUCTION_NAME + " TEXT," +
				Database.PRODUCTION_AMOUNT + " FLOAT," +
				Database.PRODUCTION_DATE + " TEXT," +
				Database.PRODUCTION_USERID + " TEXT," +
				Database.PSTATUS + " TEXT)";
		String alert_table_sql = "create table " + Database.ALERT_TABLE_NAME + " ( " +
				Database.ALERT_ID 	+ " integer  primary key autoincrement," + 
				Database.ALERT_AMOUNT + " FLOAT," +
				Database.ALERT_USERID + " TEXT," +
				Database.ESTATUS + " TEXT)";
		
		String feedback_table_sql = "create table " + Database.FEEDBACK_TABLE_NAME + " ( " +
				Database.FEED_ID 	+ " integer  primary key autoincrement," + 
				Database.FEED_SUB + " TEXT," +
				Database.FEED_MESSAGE + " TEXT," +
				Database.FEED_DATE + " TEXT," +
				Database.FEED_USERID + " TEXT," +
				Database.FSTATUS + " TEXT)";
		
        try {
		   database.execSQL(stock_table_sql);
		   database.execSQL(user_table_sql);
		   database.execSQL(transactions_table_sql);
		   database.execSQL(expense_table_sql);
		   database.execSQL(production_table_sql);
		   database.execSQL(alert_table_sql);
		   database.execSQL(feedback_table_sql);
		   Log.d("Hesabu","Tables created!");
		   
        }
        catch(Exception ex) {
        	Log.d("Hesabu", "Error in DBHelper.onCreate() : " + ex.getMessage());
        }
	}

	public long AddUser(String fname, String bname, String btype,String phone, String location,String username, String password ) {
		SQLiteDatabase db = this.getReadableDatabase();
		 HashMap<String, String> queryValues = new HashMap<String, String>();
         queryValues.put("fname", fname);
         queryValues.put("bname", bname);
         queryValues.put("btype",btype);
         queryValues.put("phone", phone);
         queryValues.put("location", location);
         queryValues.put("username", username);
         queryValues.put("password",password);
         queryValues.put("status","no");
		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.FNAME, queryValues.get("fname"));
		initialValues.put(Database.BNAME, queryValues.get("bname"));
		initialValues.put(Database.BTYPE, queryValues.get("btype"));
		initialValues.put(Database.PHONE, queryValues.get("phone"));
		initialValues.put(Database.LOCATION, queryValues.get("location"));
		initialValues.put(Database.USERNAME_NAME, queryValues.get("username"));
		initialValues.put(Database.PASSWORD, queryValues.get("password"));
		initialValues.put(Database.USTATUS, queryValues.get("status"));
		return db.insert(Database.USER_TABLE_NAME, null, initialValues);

	}
	
	public long AddExpense(String name, String amount,String Date,String uid ) {
		SQLiteDatabase db = this.getReadableDatabase();
		 HashMap<String, String> queryValues = new HashMap<String, String>();
         queryValues.put("name", name);
         queryValues.put("amount", amount);
         queryValues.put("date",Date);
         queryValues.put("uid", uid);
         queryValues.put("status","no");
		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.EXPENSE_NAME, queryValues.get("name"));
		initialValues.put(Database.EXPENSE_AMOUNT, queryValues.get("amount"));
		initialValues.put(Database.EXPENSE_DATE, queryValues.get("date"));
		initialValues.put(Database.EXPENSE_USERID,queryValues.get("uid"));
		initialValues.put(Database.ESTATUS, queryValues.get("status"));
		return db.insert(Database.EXPENSE_TABLE_NAME, null, initialValues);

	}
	public long AddProduction(String name, String amount,String Date,String uid ) {
		SQLiteDatabase db = this.getReadableDatabase();
		 HashMap<String, String> queryValues = new HashMap<String, String>();
         queryValues.put("name", name);
         queryValues.put("amount", amount);
         queryValues.put("date",Date);
         queryValues.put("uid", uid);
         queryValues.put("status","no");
		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.PRODUCTION_NAME, queryValues.get("name"));
		initialValues.put(Database.PRODUCTION_AMOUNT, queryValues.get("amount"));
		initialValues.put(Database.PRODUCTION_DATE, queryValues.get("date"));
		initialValues.put(Database.PRODUCTION_USERID,queryValues.get("uid"));
		initialValues.put(Database.PSTATUS, queryValues.get("status"));
		return db.insert(Database.PRODUCTION_TABLE_NAME, null, initialValues);

	}
	
	public long AddFeedback(String sub, String message,String Date,String uid ) {
		SQLiteDatabase db = this.getReadableDatabase();
		 HashMap<String, String> queryValues = new HashMap<String, String>();
         queryValues.put("sub", sub);
         queryValues.put("message", message);
         queryValues.put("date",Date);
         queryValues.put("uid", uid);
         queryValues.put("status","no");
		ContentValues initialValues = new ContentValues();
		initialValues.put(Database.FEED_SUB, queryValues.get("sub"));
		initialValues.put(Database.FEED_MESSAGE, queryValues.get("message"));
		initialValues.put(Database.FEED_DATE, queryValues.get("date"));
		initialValues.put(Database.FEED_USERID,queryValues.get("uid"));
		initialValues.put(Database.FSTATUS, queryValues.get("status"));
		return db.insert(Database.FEEDBACK_TABLE_NAME, null, initialValues);

	}
	public long AddAlert( String amount,String uid ) {
		SQLiteDatabase db = this.getReadableDatabase();
		 HashMap<String, String> queryValues = new HashMap<String, String>();
         
         queryValues.put("amount", amount);
         queryValues.put("uid", uid);
         queryValues.put("status","no");
		ContentValues initialValues = new ContentValues();
	
		initialValues.put(Database.ALERT_AMOUNT, queryValues.get("amount"));
		initialValues.put(Database.ALERT_USERID,queryValues.get("uid"));
		initialValues.put(Database.ASTATUS, queryValues.get("status"));
		return db.insert(Database.ALERT_TABLE_NAME, null, initialValues);

	}
	public boolean Login(String username, String password) throws SQLException {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM " + Database.USER_TABLE_NAME
				+ " WHERE username=? AND password=?", new String[] { username,
				password });
		if (mCursor != null) {
			if (mCursor.getCount() > 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * getting a list of all the products to populate spinner
	 */
	public List<String> getProductList(String username) {
		List<String> plist = new ArrayList<String>();
		String selectQuery = "SELECT * FROM " + Database.STOCK_TABLE_NAME + " WHERE uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				plist.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return plist;
	}
	
	/**
	 * getting a heading 
	 */
	public String getHeading(String username) {
		String heading = new String();
		String selectQuery = "SELECT bname FROM " + Database.USER_TABLE_NAME + " WHERE username ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				heading=(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return heading;
	}
	/**
	 * cursor for viewing the Buying Price
	 */
	public Cursor getBprice(String product) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = new String[] {Database.STOCK_BPRICE };
		Cursor c = db.query(Database.STOCK_TABLE_NAME, allColumns,"item" + "='" + product + "'", null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	/**
	 * getting a Product
	 */
	public String getIDNo(String idno) {
		String IDNo = new String();
		String selectQuery = "SELECT item FROM " + Database.STOCK_TABLE_NAME + " WHERE item ='" + idno + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				IDNo=(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return IDNo;
	}
	
	public Cursor fetchProduct(String idno,String uname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.STOCK_TABLE_NAME, 
				new String[] { "_id", "item","uid" }, 
				"item" + "='" + idno + "'AND " + 
				"uid" + "='" + uname + "'", null, null, null, null);
		
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	public Cursor fetchExpense(String _id,String uname) {
	
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.EXPENSE_TABLE_NAME, 
				new String[] { "_id", "name", "uid" }, 
				"name" + "='" + _id + "' AND " + 
				"uid" + "='" + uname + "'", null, null, null, null);
		
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	public Cursor fetchProduction(String _id,String uname) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.PRODUCTION_TABLE_NAME, 
				new String[] { "_id", "name", "uid" }, 
				"name" + "='" + _id + "' AND " + 
				"uid" + "='" + uname + "'", null, null, null, null);
		
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	public Cursor fetchTrans(String _id,String uname) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.TRANSACTIONS_TABLE_NAME, 
				new String[] { "_id", "account_id", "uid" }, 
				"_id" + "='" + _id + "' AND " + 
				"uid" + "='" + uname + "'", null, null, null, null);
		
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	public Cursor fetchUsername(String uname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor myCursor = db.query(Database.USER_TABLE_NAME, 
				new String[] { "_id", "username" }, 
				"username" + "='" + uname + "'", null, null, null, null);
		
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	
	/**
	 * cursor for viewing the Selling Price
	 */
	public Cursor getSprice(String product) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = new String[] {Database.STOCK_SPRICE };
		Cursor c = db.query(Database.STOCK_TABLE_NAME, allColumns,"item" + "='" + product + "'", null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	/**
	 * cursor for viewing the Selling Price
	 */
	public Cursor getAccId(String product) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = new String[] {Database.STOCK_ID  };
		Cursor c = db.query(Database.STOCK_TABLE_NAME, allColumns,"item" + "='" + product + "'", null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	/**
	 * cursor for viewing the Selling Price
	 */
	public Cursor getInStock(String product) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = new String[] {Database.STOCK_QUANTITY  };
		Cursor c = db.query(Database.STOCK_TABLE_NAME, allColumns,"item" + "='" + product + "'", null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	/**
	 * adding all cost of sales
	 */
	public int totalCOS(String username,String Date) {
		int cos = 0;
		String selectQuery = "SELECT account_id,stock._id,bprice,transamount,transtype FROM stock,transactions WHERE  transactions.uid ='" + username + "'and transdate='"+ Date +"' and stock. _id=account_id and transtype ='Selling' ";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				cos = cos + (cursor.getInt(2)*cursor.getInt(3));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return cos;
	}

	

	/**
	 * adding all buying
	 */
	public int totalCOS1(String username,String fromdate,String todate) {
		int cos = 0;
		String selectQuery = "SELECT account_id,stock._id,bprice,transamount,transtype FROM stock,transactions WHERE  transactions.uid ='" + username + "' and stock. _id=account_id and transtype ='Selling' AND transdate >='" + fromdate + "'  AND transdate <='" + todate + "' ";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				cos = cos + (cursor.getInt(2)*cursor.getInt(3));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return cos;
	}
	/**
	 * adding all buying
	 */
	public int totalAlert(String username) {
		int income = 0;
		String selectQuery = "SELECT sum(alert) FROM " + Database.ALERT_TABLE_NAME + " WHERE  uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all buying
	 */
	public int totalBprice(String username) {
		int income = 0;
		String selectQuery = "SELECT sum(bprice) FROM " + Database.STOCK_TABLE_NAME + " WHERE  uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all se;;ing
	 */
	public int totalSprice(String username) {
		int income = 0;
		String selectQuery = "SELECT sum(sprice) FROM " + Database.STOCK_TABLE_NAME + " WHERE  uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	public int totalValue(String username) {
		int income = 0;
		String selectQuery = "SELECT sum(value) FROM " + Database.STOCK_TABLE_NAME + " WHERE  uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	
	public int totalQuant(String username,String Date ) {
		int income = 0;
		String selectQuery = "SELECT sum(transamount) FROM " + Database.TRANSACTIONS_TABLE_NAME + " WHERE  uid ='" + username + "' and transtype ='Selling' and transdate='"+ Date +"'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	
	public int totalQuant1(String username,String Date,String Date1 ) {
		int income = 0;
		String selectQuery = "SELECT sum(transamount) FROM " + Database.TRANSACTIONS_TABLE_NAME + " WHERE  uid ='" + username + "' and transtype ='Selling' and transdate >='" + Date + "'  AND transdate <='" +Date1 + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * 
	 * adding all buying
	 */
	public int totalBuying(String username,String Date) {
		int income = 0;
		String selectQuery = "SELECT sum(cheque_party) FROM " + Database.TRANSACTIONS_TABLE_NAME + " WHERE transtype ='Buying' AND uid ='" + username + "' and transdate='"+ Date +"'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all sales
	 */
	public int totalSales(String username,String Date) {
		int income = 0;
		String selectQuery = "SELECT sum(cheque_party) FROM " + Database.TRANSACTIONS_TABLE_NAME + " WHERE transtype ='Selling' AND uid ='" + username + "' and transdate='"+ Date +"'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all expenses
	 */
	public int totalExpense1(String username,String Date) {
		int income = 0;
		String selectQuery = "SELECT sum(amount) FROM " + Database.EXPENSE_TABLE_NAME + " WHERE uid ='" + username + "' and transdate='"+ Date +"'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	
	/**
	 * adding all expenses
	 */
	public int totalExpense(String username) {
		int income = 0;
		String selectQuery = "SELECT sum(amount) FROM " + Database.EXPENSE_TABLE_NAME + " WHERE uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all expenses
	 */
	public int totalProduction(String username) {
		int income = 0;
		String selectQuery = "SELECT sum(amount) FROM " + Database.PRODUCTION_TABLE_NAME + " WHERE uid ='" + username + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	
	/**
	 * adding all expenses
	 */
	public int totalProduction(String username,String Date) {
		int income = 0;
		String selectQuery = "SELECT sum(amount) FROM " + Database.PRODUCTION_TABLE_NAME + " WHERE uid ='" + username + "' and transdate='"+ Date +"'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all buying
	 */
	public int totalBuying1(String username,String fromdate,String todate) {
		int income = 0;
		String selectQuery = "SELECT sum(cheque_party) FROM " + Database.TRANSACTIONS_TABLE_NAME + " WHERE transtype ='Buying' AND uid ='" + username + "' AND transdate >='" + fromdate + "'  AND transdate <='" + todate + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all sales
	 */
	public int totalSales1(String username,String fromdate,String todate) {
		int income = 0;
		String selectQuery = "SELECT sum(cheque_party) FROM " + Database.TRANSACTIONS_TABLE_NAME + " WHERE transtype ='Selling' AND uid ='" + username + "' AND transdate >='" + fromdate + "'  AND transdate <='" + todate + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all expenses
	 */
	public int totalExpense1(String username,String fromdate,String todate) {
		int income = 0;
		String selectQuery = "SELECT sum(amount) FROM " + Database.EXPENSE_TABLE_NAME + " WHERE uid ='" + username + "' AND transdate >='" + fromdate + "'  AND transdate <='" + todate + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	/**
	 * adding all expenses
	 */
	public int totalProduction1(String username,String fromdate,String todate) {
		int income = 0;
		String selectQuery = "SELECT sum(amount) FROM " + Database.PRODUCTION_TABLE_NAME + " WHERE uid ='" + username + "' AND transdate >='" + fromdate + "'  AND transdate <='" + todate + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				income = income + cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return income;
	}
	
	
	    /**
		 * sync users
		 */
		 public ArrayList<HashMap<String, String>> getAllUsers() {
		        ArrayList<HashMap<String, String>> wordList;
		        wordList = new ArrayList<HashMap<String, String>>();
		    	
		    	 String selectQuery = "SELECT  * FROM users where status = '"+"no"+"'";
		    	 SQLiteDatabase db = this.getReadableDatabase();
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
		    public String composeJSONUser(){
		        ArrayList<HashMap<String, String>> wordList;
		        wordList = new ArrayList<HashMap<String, String>>();
		 
		   	 String selectQuery = "SELECT  * FROM users  where status = '"+"no"+"'";
		   	SQLiteDatabase db = this.getReadableDatabase();
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
		     * Get Sync status of SQLite
		     * @return
		     */
		    public String getSyncStatusUser(){
		        String msg = null;
		        if(this.dbSyncCountUser() == 0){
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
		    public int dbSyncCountUser(){
		        int count = 0;
		     String selectQuery = "SELECT  * FROM users where status = '"+"no"+"'";
		    	SQLiteDatabase db = this.getReadableDatabase();
		        Cursor cursor = db.rawQuery(selectQuery, null);
		        count = cursor.getCount();
		        
		        
		        return count;
		    }
		    public void updateSyncStatusUser(String id, String status){
		    	
				
				SQLiteDatabase db = this.getReadableDatabase();
				   
		        String updateQuery = "Update users set status = '"+ status +"' where username='"+ id +"'";
		        Log.d("query",updateQuery);       
		        db.execSQL(updateQuery);
		  	       		    
		   	    }
		    
		    /**
			 * sync product
			 */
			 public ArrayList<HashMap<String, String>> getAllStock() {
			        ArrayList<HashMap<String, String>> wordList;
			        wordList = new ArrayList<HashMap<String, String>>();
			    	
			    	 String selectQuery = "SELECT * FROM stock where status = '"+"no"+"' ";
			    	 SQLiteDatabase db = this.getReadableDatabase();
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
			        
			        return wordList;
			    }
			    /**
			     * Compose JSON out of SQLite records
			     * @return
			     */
			    public String composeJSONStock(){
			        ArrayList<HashMap<String, String>> wordList;
			        wordList = new ArrayList<HashMap<String, String>>();
			 
			   	 String selectQuery = "SELECT  * FROM stock where status = '"+"no"+"'";
			   	SQLiteDatabase db = this.getReadableDatabase();
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
			     String selectQuery = "SELECT  * FROM stock where status = '"+"no"+"'";
			    	SQLiteDatabase db = this.getReadableDatabase();
			        Cursor cursor = db.rawQuery(selectQuery, null);
			        count = cursor.getCount();
			      
			        
			        return count;
			    }
			    public void updateSyncStatus(String id, String status){
			    	
					
					SQLiteDatabase db = this.getReadableDatabase();
					   
			        String updateQuery = "Update stock set status = '"+ status +"' where item='"+ id +"'";
			        Log.d("query",updateQuery);       
			        db.execSQL(updateQuery);
			  
			        
			    
			    
			    }
			    
			    /**
				 * sync expense
				 */
				 public ArrayList<HashMap<String, String>> getAllExpense() {
				        ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				    	
				    	 String selectQuery = "SELECT * FROM expense where status = '"+"no"+"' ";
				    	 SQLiteDatabase db = this.getReadableDatabase();
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
				        
				        return wordList;
				    }
				    /**
				     * Compose JSON out of SQLite records
				     * @return
				     */
				    public String composeJSONExpense(){
				        ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				 
				   	 String selectQuery = "SELECT  * FROM expense where status = '"+"no"+"'";
				   	SQLiteDatabase db = this.getReadableDatabase();
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
				     * Get Sync status of SQLite
				     * @return
				     */
				    public String getSyncStatusexpense(){
				        String msg = null;
				        if(this.dbSyncCountexpense() == 0){
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
				    public int dbSyncCountexpense(){
				        int count = 0;
				     String selectQuery = "SELECT  * FROM expense where status = '"+"no"+"'";
				    	SQLiteDatabase db = this.getReadableDatabase();
				        Cursor cursor = db.rawQuery(selectQuery, null);
				        count = cursor.getCount();
				      
				        
				        return count;
				    }
				    public void updateSyncStatusexpense(String id, String status){
				    	
						
						SQLiteDatabase db = this.getReadableDatabase();
						   
				        String updateQuery = "Update expense set status = '"+ status +"' where name='"+ id +"'";
				        Log.d("query",updateQuery);       
				        db.execSQL(updateQuery);
				  
				        
				    
				    
				    }
				    /**
					 * sync production
					 */
					 public ArrayList<HashMap<String, String>> getAllProduction() {
					        ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					    	
					    	 String selectQuery = "SELECT * FROM production where status = '"+"no"+"' ";
					    	 SQLiteDatabase db = this.getReadableDatabase();
							Cursor cursor = db.rawQuery(selectQuery, null);
					       
					        if (cursor.moveToFirst()) {
					            do {
					                HashMap<String, String> map = new HashMap<String, String>();
					                
					                map.put("name", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_NAME)));
					                map.put("amount", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_AMOUNT)));
					                map.put("date", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_DATE)));
					                map.put("userid", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_USERID)));
					               
					                
					                wordList.add(map);
					            } while (cursor.moveToNext());
					        }
					        
					        return wordList;
					    }
					    /**
					     * Compose JSON out of SQLite records
					     * @return
					     */
					    public String composeJSONProduction(){
					        ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					 
					   	 String selectQuery = "SELECT  * FROM production where status = '"+"no"+"'";
					   	SQLiteDatabase db = this.getReadableDatabase();
							Cursor cursor = db.rawQuery(selectQuery, null);
					        
					        if (cursor.moveToFirst()) {
					            do {
					                HashMap<String, String> map = new HashMap<String, String>();
					                
					                map.put("name", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_NAME)));
					                map.put("amount", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_AMOUNT)));
					                map.put("date", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_DATE)));
					                map.put("userid", cursor.getString(cursor
					    					.getColumnIndex(Database.PRODUCTION_USERID)));
					    	               
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
					    public String getSyncStatusproduction(){
					        String msg = null;
					        if(this.dbSyncCountproduction() == 0){
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
					    public int dbSyncCountproduction(){
					        int count = 0;
					     String selectQuery = "SELECT  * FROM production where status = '"+"no"+"'";
					    	SQLiteDatabase db = this.getReadableDatabase();
					        Cursor cursor = db.rawQuery(selectQuery, null);
					        count = cursor.getCount();
					      
					        
					        return count;
					    }
					    public void updateSyncStatusproduction(String id, String status){
					    	
							
							SQLiteDatabase db = this.getReadableDatabase();
							   
					        String updateQuery = "Update production set status = '"+ status +"' where name='"+ id +"'";
					        Log.d("query",updateQuery);       
					        db.execSQL(updateQuery);
					  
					        
					    
					    
					    }
				    /**
					 * sync transactions
					 */
					 public ArrayList<HashMap<String, String>> getAllTrans() {
					        ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					    	
					    	 String selectQuery = "SELECT  * FROM transactions where status = '"+"no"+"'";
					    	 SQLiteDatabase db = this.getReadableDatabase();
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
					    public String composeJSONTrans(){
					        ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					 
					   	 String selectQuery = "SELECT  * FROM transactions where status = '"+"no"+"'";
					   	SQLiteDatabase db = this.getReadableDatabase();
							Cursor cursor = db.rawQuery(selectQuery, null);
					        
					        if (cursor.moveToFirst()) {
					            do { HashMap<String, String> map = new HashMap<String, String>();
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
					        Gson gson = new GsonBuilder().create();
					        //Use GSON to serialize Array List to JSON
					        return gson.toJson(wordList);
					    }
					
					    /**
					     * Get Sync status of SQLite
					     * @return
					     */
					    public String getSyncStatusTrans(){
					        String msg = null;
					        if(this.dbSyncCountTrans() == 0){
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
					    public int dbSyncCountTrans(){
					        int count = 0;
					     String selectQuery = "SELECT  * FROM transactions where status = '"+"no"+"'";
					    	SQLiteDatabase db = this.getReadableDatabase();
					        Cursor cursor = db.rawQuery(selectQuery, null);
					        count = cursor.getCount();
					        
					        
					        return count;
					    }
					    public void updateSyncStatusTrans(String id, String status){
					    	
							
							SQLiteDatabase db = this.getReadableDatabase();
							   
					        String updateQuery = "Update transactions set status = '"+ status +"' where uid='"+ id +"'";
					        Log.d("query",updateQuery);       
					        db.execSQL(updateQuery);
					  	       		    
					   	    }
					    
					    /**
						 * sync feedback
						 */
						 public ArrayList<HashMap<String, String>> getAllFeedBack() {
						        ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						    	
						    	 String selectQuery = "SELECT * FROM feedback where status = '"+"no"+"' ";
						    	 SQLiteDatabase db = this.getReadableDatabase();
								Cursor cursor = db.rawQuery(selectQuery, null);
						       
						        if (cursor.moveToFirst()) {
						            do {
						                HashMap<String, String> map = new HashMap<String, String>();
						                
						                map.put("subject", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_SUB)));
						                map.put("description", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_MESSAGE)));
						                map.put("date", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_DATE)));
						                map.put("userid", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_USERID)));
						               
						                
						                wordList.add(map);
						            } while (cursor.moveToNext());
						        }
						        
						        return wordList;
						    }
						    /**
						     * Compose JSON out of SQLite records
						     * @return
						     */
						    public String composeJSONFeedBack(){
						        ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						 
						   	 String selectQuery = "SELECT  * FROM feedback where status = '"+"no"+"'";
						   	SQLiteDatabase db = this.getReadableDatabase();
								Cursor cursor = db.rawQuery(selectQuery, null);
						        
						        if (cursor.moveToFirst()) {
						            do {
						                HashMap<String, String> map = new HashMap<String, String>();
						                map.put("subject", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_SUB)));
						                map.put("description", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_MESSAGE)));
						                map.put("date", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_DATE)));
						                map.put("userid", cursor.getString(cursor
						    					.getColumnIndex(Database.FEED_USERID)));
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
						    public String getSyncStatusFeedBack(){
						        String msg = null;
						        if(this.dbSyncCountFeedBack() == 0){
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
						    public int dbSyncCountFeedBack(){
						        int count = 0;
						     String selectQuery = "SELECT  * FROM feedback where status = '"+"no"+"'";
						    	SQLiteDatabase db = this.getReadableDatabase();
						        Cursor cursor = db.rawQuery(selectQuery, null);
						        count = cursor.getCount();
						      
						        
						        return count;
						    }
						    public void updateSyncStatusFeedBack(String id, String status){
						    	
								
								SQLiteDatabase db = this.getReadableDatabase();
								   
						        String updateQuery = "Update feedback set status = '"+ status +"' where uid='"+ id +"'";
						        Log.d("query",updateQuery);       
						        db.execSQL(updateQuery);
						  
						        
						    
						    
						    }
					    
			    
}
