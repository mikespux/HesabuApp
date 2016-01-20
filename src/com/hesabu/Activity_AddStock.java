package com.hesabu;

/**
 * @author Michael N.Orenge
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Activity_AddStock extends DashBoardActivity {
    /** Called when the activity is first created. */
	
	Spinner rows;
	Button buttonAdd;
	LinearLayout container;
	int i=1;
 EditText textOut,quantity,buyingprice,sellingprice;
 ProgressDialog prgDialog;
 private int day, month, year;
	String Date;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstock);
        setHeader(getString(R.string.EclairActivityTitle), true, true);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Adding Stock. Please wait...");
        prgDialog.setCancelable(false);
        rows = (Spinner)findViewById(R.id.textin);
		buttonAdd = (Button)findViewById(R.id.add);
		container = (LinearLayout)findViewById(R.id.container);
		final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
		buttonAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				int count=Integer.parseInt(rows.getSelectedItem().toString());
				for(i=1;i<=count;i++)
				{
							
						LayoutInflater layoutInflater = 
						(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate(R.layout.row, null);
				 textOut = (EditText)addView.findViewById(R.id.item);
				 quantity = (EditText)addView.findViewById(R.id.quantity);
			 buyingprice = (EditText)addView.findViewById(R.id.buyingprice);
				 sellingprice = (EditText)addView.findViewById(R.id.sellingprice);
				
				
				Button buttonRemove = (Button)addView.findViewById(R.id.remove);
				Button buttonSave = (Button)addView.findViewById(R.id.save);
				buttonRemove.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						((LinearLayout)addView.getParent()).removeView(addView);
					}});
				
				
				buttonSave.setOnClickListener(new OnClickListener(){

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						String item= textOut.getText().toString();
						String quanty = quantity.getText().toString();
						String bprice = buyingprice.getText().toString();
						String sprice = sellingprice.getText().toString();
						
						
						
						Date=String.format("%d-%d-%d",year,month + 1,day);
						if(item.equals("")||quanty.equals("")||bprice.equals("")||sprice.equals(""))
						{ 
							Toast.makeText(getApplicationContext(), "Please enter All fields", Toast.LENGTH_LONG).show();
							return;
						}
						
						try {
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_AddStock.this);
					        String username = prefs.getString("user", "anon");
							DBHelper dbhelper = new DBHelper(getApplicationContext()); 
							SQLiteDatabase db = dbhelper.getWritableDatabase();
			                Log.d("Account","Got Writable database");
			            	Cursor checkid =dbhelper.fetchProduct(item,username );
							if (checkid == null) {
								Toast.makeText(getApplicationContext(), "no product name saved",
								          Toast.LENGTH_SHORT).show();
							}
							else {
					    		startManagingCursor(checkid );
					    		
					    		
					    		if (checkid .getCount() > 0) {
					    		Toast.makeText(getApplicationContext(), "Product Name already exists",
					  			          Toast.LENGTH_SHORT).show();
					    			stopManagingCursor(checkid );
					    			checkid .close();
					    			return;
					    		}
					    		}
							double quan=0.0,bpric=0.0, value;
							quan=Double.parseDouble(quanty);
							bpric=Double.parseDouble(bprice);
							value=quan*bpric;
							String result=String.valueOf(value);
							// execute insert command
			                HashMap<String, String> queryValues = new HashMap<String, String>();
			                queryValues.put("item", textOut.getText().toString());
			                queryValues.put("quantity", quantity.getText().toString());
			                queryValues.put("bprice", buyingprice.getText().toString());
			                queryValues.put("sprice", sellingprice.getText().toString());
			                queryValues.put("value", result);
			                queryValues.put("date", Date);
			                queryValues.put("username", username);
			                queryValues.put("status", "no");
			               
							ContentValues values = new ContentValues();
						
							values.put( Database.STOCK_NAME, queryValues.get("item"));
							values.put( Database.STOCK_QUANTITY, queryValues.get("quantity"));
							values.put( Database.STOCK_BPRICE, queryValues.get("bprice"));
							values.put( Database.STOCK_SPRICE, queryValues.get("sprice"));
							values.put( Database.STOCK_VALUE, queryValues.get("value"));
							values.put( Database.STOCK_DATER, queryValues.get("date"));
							values.put( Database.STOCK_USERID, queryValues.get("username"));
							values.put( Database.SSTATUS, queryValues.get("status"));
							
							
							
							
							long rows = db.insert(Database.STOCK_TABLE_NAME, null, values);
							
							db.close();
							if ( rows > 0)  {
							    Toast.makeText(getApplicationContext(), "Stock Added Successfully! ",	Toast.LENGTH_LONG).show();
							    
							    syncSQLiteMySQLDBStock();

							    textOut.setText("");
							    quantity.setText("");
							    buyingprice.setText("");
							    sellingprice.setText("");
								
							 
							}
							else
								Toast.makeText(getApplicationContext(), "Sorry! Could not add Stock!",	Toast.LENGTH_LONG).show();
							
						} catch (Exception ex) {
							Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
						}
						
						
					}});
				
				container.addView(addView);
				
				
				}
			}});
		
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsStockmenu.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsStockmenu.handleMenuOption(this,item);
	}

	 public void syncSQLiteMySQLDBStock(){
	        //Create AsycHttpClient object
		  final DBHelper db = new DBHelper(getApplicationContext());
	        AsyncHttpClient client = new AsyncHttpClient();
	        RequestParams params = new RequestParams();
	        ArrayList<HashMap<String, String>> userList =  db.getAllStock();
	        if(userList.size()!=1){
	            if(db.dbSyncCount() != 1){
	                prgDialog.show();
	                params.put("stocksJSON", db.composeJSONStock());
	                client.post("http://wascakenya.co.ke/synchesabu/insertstock.php",params ,new AsyncHttpResponseHandler() {
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
	                                db.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
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
	                Toast.makeText(getApplicationContext(), "Sync Succesfully done!", Toast.LENGTH_LONG).show();
	            }
	        }else{
	                Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
	        }
	    }
}
