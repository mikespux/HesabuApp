package com.hesabu;
/**
 * @author Michael N.Orenge
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddSales extends DashBoardActivity implements OnItemSelectedListener {
	private Spinner spinnerAccounts;
	private TextView textTransDate,not;
	private int day, month, year;
	private final int DATE_DIALOG = 1;
	EditText editTransAmount,editChequeNo,editChequeParty,InStock,SpinQunt;
	private SQLiteDatabase newDB;
	DBHelper dbHelper;
	String partyName;
	private EditText et;
	String accountId;
	ArrayList<String> dbdata=new ArrayList<String>();
	ArrayAdapter<String> adapter;

	private ArrayList<String> array_sort= new ArrayList<String>();
	int textlength=0;

	String Tamount;
	DBHelper db;
	ProgressDialog prgDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_sales);
		
		 setHeader(getString(R.string.Sell), true, true);
		 prgDialog = new ProgressDialog(this);
	        prgDialog.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
	        prgDialog.setCancelable(false);
  et = (EditText) findViewById(R.id.editText1);
		spinnerAccounts = (Spinner) this.findViewById(R.id.spinnerAccounts);
		SpinQunt = (EditText) this.findViewById(R.id.SpinQunt);
		
		DataFromDB();//method to get data from sqlite database
		CheckFromSpinner();//method to check data from spinner
		
		textTransDate = (TextView) this.findViewById(R.id.textTransDate);
		not = (TextView) this.findViewById(R.id.not);
		 editTransAmount = (EditText) this.findViewById(R.id.editBSprice);
		 editChequeNo = (EditText) this.findViewById(R.id.editQty);
		 editChequeParty = (EditText) this.findViewById(R.id.editTamount);
		 InStock = (EditText) this.findViewById(R.id.InStock);
		 spinnerAccounts.setOnItemSelectedListener(this);
		

		SpinQunt.setOnEditorActionListener(new OnEditorActionListener(){
				// TODO Auto-generated method stub
				

				@Override
				public boolean onEditorAction(TextView arg0, int arg1,
						KeyEvent arg2) {
					// TODO Auto-generated method stub
				
					return false;
				}
				
		});
	 
		 
		// Instantiates a TextWatcher, to observe your EditTexts' value changes
		// and trigger the result calculation
		TextWatcher textWatcher = new TextWatcher() {
		    public void afterTextChanged(Editable s) {
		    	if(editTransAmount.getText().length()>0 && editChequeNo.getText().length()>0){
		    	calculateResult();
		    	
		    	}
		    }
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){
		    	
		    	
		    }
		};
		
		// Adds the TextWatcher as TextChangedListener to both EditTexts
		editTransAmount.addTextChangedListener(textWatcher);
		editChequeNo.addTextChangedListener(textWatcher);
		
		 // get the current date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();
	}
	// The function called to calculate and display the result of the multiplication
	private void calculateResult() throws NumberFormatException {
	  // Gets the two EditText controls' Editable values
	  Editable editableValue1 = editTransAmount.getText(),
	           editableValue2 = editChequeNo.getText();

	  // Initializes the double values and result
	  double value1 = 0.0,
	         value2 = 0.0,
	         result;

	  // If the Editable values are not null, obtains their double values by parsing
	
	    value1 = Double.parseDouble(editableValue1.toString());

	  
	    value2 = Double.parseDouble(editableValue2.toString());

	  // Calculates the result
	  result = value1 * value2;

	  // Displays the calculated result
	  editChequeParty.setText(String.valueOf(result));
	}
	private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int pYear,int pMonth, int pDay) {
                    year = pYear;
                    month = pMonth;
                    day = pDay;
                    updateDateDisplay();
                }
            };

            
        	@Override
        	public boolean onCreateOptionsMenu(Menu menu) {
        		return Utils.inflateMenu(this,menu);
        	}
        	
        	@Override 
        	public boolean onOptionsItemSelected(MenuItem item) {
        		return  Utils.handleMenuOption(this,item);
        	}
	@Override 
	public void onStart() {
		super.onStart();
		
	}

	@SuppressWarnings("deprecation")
	public void showDateDialog(View v) {
		  showDialog(DATE_DIALOG);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		
	    switch (id) {
	    case DATE_DIALOG:
	        return new DatePickerDialog(this,
	                    dateSetListener, year, month, day);
	    }
	    return null;
	}
	
	
	
	
	private void updateDateDisplay() {
            // Month is 0 based so add 1
	        textTransDate.setText( String.format("%d-%d-%d",year,month + 1,day));
	}
	 
	public void addTransaction(View v) {
		// get access to views
		
	
	     String prod=(String) spinnerAccounts.getSelectedItem();
         db = new DBHelper(getApplicationContext());
         Cursor c = db.getAccId(prod);
			int j = 0;
		 accountId = c.getString(j);
				RadioButton  radioDeposit = (RadioButton) this.findViewById(R.id.radioDeposit);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddSales.this);
		        String username = prefs.getString("user", "anon");
				EditText editChequeDetails = (EditText) this.findViewById(R.id.editChequeDetails);
				EditText editRemarks = (EditText) this.findViewById(R.id.editRemarks);
				Tamount = editTransAmount.getText().toString();
				
				double stock= Double.parseDouble(InStock.getText().toString());
				double quantity= Double.parseDouble(editTransAmount.getText().toString());
				
				
				if(prod.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please Select Item", Toast.LENGTH_LONG).show();
					return;
				}
				if(quantity ==0.0){
					Toast.makeText(this, "Sorry could not save, because quantity is 0.0!", Toast.LENGTH_LONG).show();
				}else{
				if(stock >= quantity){
				boolean done = Database.addTransaction(this,
						accountId,
						radioDeposit.isChecked() ? "Buying" : "Selling",   // transaction type 
						textTransDate.getText().toString(),
						editTransAmount.getText().toString(),
						editChequeNo.getText().toString(),
						editChequeParty.getText().toString(),
						editChequeDetails.getText().toString(),
						editRemarks.getText().toString(),
				         username);
				

				try {
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ( done ){
					
					Toast.makeText(this,"Transaction Added!", Toast.LENGTH_LONG).show();
					syncSQLiteMySQLDBSales();
					syncUpdate();
					editTransAmount.setText("");
					editChequeNo.setText("");
					editChequeParty.setText("");
					editRemarks.setText("");
					et.setText("");}
				else{
					Toast.makeText(this, "Sorry Could Not Add Transaction!", Toast.LENGTH_LONG).show();}
				
				}else
				{
					
					Toast.makeText(this, "Sorry could not save, because stock is less than quantity!", Toast.LENGTH_LONG).show();
				}
				}
	} 
	public void loadSpinner(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddSales.this);
        String username = prefs.getString("user", "anon");
        spinnerAccounts = (Spinner)findViewById(R.id.spinnerAccounts);
		db = new DBHelper(getApplicationContext());
		List<String> cowlist = db.getProductList(username);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cowlist);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerAccounts.setAdapter(dataAdapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		
		
		
		
		//save user data
		 String product =parent.getItemAtPosition(position).toString();	
		

		 db = new DBHelper(getApplicationContext());
			Cursor c = db.getSprice(product);
					int j = 0;
					 editChequeNo.setText(c.getString(j));
			
					 Cursor d = db.getInStock(product);
					 int f = 0;
					 InStock.setText(d.getString(f));
					 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddSales.this);
				        String username = prefs.getString("user", "anon");
					 int alert = db.totalAlert(username);					 
					 double s= Double.parseDouble(InStock.getText().toString());
					 String aler  = String.valueOf(alert);
					 if(s< alert){
					 
					 
					 not.setText("Warning!! Your " +product + " stock Is below:"+aler +" , Add more stock in the store");

					 not.setVisibility(View.VISIBLE);
					 
					 }else
					 {
						 not.setVisibility(View.GONE);
						 
					 }
		SharedPreferences ct = PreferenceManager
				.getDefaultSharedPreferences(AddSales.this);
		Editor edit = ct.edit();
		edit.putString("category", product);
		edit.commit();
				

	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	private void DataFromDB() {
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddSales.this);
	        String username = prefs.getString("user", "anon");
		dbHelper=new DBHelper(AddSales.this);
		newDB=dbHelper.getWritableDatabase();
		Cursor c=newDB.rawQuery("select item from stock where uid ='" + username + "'", null);
		if(c!=null)
		{
		if(c.moveToFirst())
		{
		do{
		partyName=c.getString(c.getColumnIndex("item"));
		dbdata.add(partyName);

		}while(c.moveToNext());
		}
		}

		adapter=new ArrayAdapter<String>(AddSales.this,android.R.layout.simple_list_item_1,dbdata);
		//lv.setAdapter(adapter);

		adapter=new ArrayAdapter<String>(AddSales.this,android.R.layout.simple_list_item_1,dbdata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAccounts.setAdapter(adapter);
		}

		private void CheckFromSpinner() {
		et.addTextChangedListener(new TextWatcher()
		{
		public void afterTextChanged(Editable s)
		{
		// Abstract Method of TextWatcher Interface.
		}
		public void beforeTextChanged(CharSequence s,
		int start, int count, int after)
		{
		// Abstract Method of TextWatcher Interface.
		}
		public void onTextChanged(CharSequence s,
		int start, int before, int count)
		{
		textlength = et.getText().length();
		array_sort.clear();
		for (int i = 0; i < dbdata.size(); i++)
		{
		if (textlength <= dbdata.get(i).length())
		{
		if(et.getText().toString().equalsIgnoreCase(
		(String)
		dbdata.get(i).subSequence(0,
		textlength)))
		{
		array_sort.add(dbdata.get(i));
		}
		}
		}

		spinnerAccounts.setAdapter(new ArrayAdapter<String>(AddSales.this, android.R.layout.simple_spinner_dropdown_item,array_sort));

		}



		});

		}
		public void syncSQLiteMySQLDBSales(){
	        //Create AsycHttpClient object
		 final DBHelper db = new DBHelper(getApplicationContext());
	        AsyncHttpClient client = new AsyncHttpClient();
	        RequestParams params = new RequestParams();
	        ArrayList<HashMap<String, String>> userList =  db.getAllTrans();
	        if(userList.size()!=0){
	            if(db.dbSyncCountTrans() != 0){
	                prgDialog.show();
	                params.put("transJSON", db.composeJSONTrans());
	                client.post("http://wascakenya.co.ke/synchesabu/inserttrans.php",params ,new AsyncHttpResponseHandler() {
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
	                                db.updateSyncStatusTrans(obj.get("id").toString(),obj.get("status").toString());
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
	                Toast.makeText(getApplicationContext(), "Sync Successfully done!", Toast.LENGTH_LONG).show();
	            }
	        }else{
	                Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
	        }
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
}
