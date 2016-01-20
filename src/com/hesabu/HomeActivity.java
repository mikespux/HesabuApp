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

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HomeActivity extends DashBoardActivity {
    /** Called when the activity is first created. */
	HashMap<String, String> queryValues;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setHeader(getString(R.string.HomeActivityTitle), false, true);
   
        syncSQLiteMySQLDB();
        syncSQLiteMySQLDBExpense();
        syncSQLiteMySQLDBProduction();
        syncSQLiteMySQLDBTrans();
        syncSQLiteMySQLDBFeedBack();
        syncSQLiteMySQLDBStock();
        syncSQLiteMySQLDBSales();
        syncSQLiteMySQLDBAddExpense();
    }
    @Override
	public void onBackPressed() {
		//Display alert message when back button has been pressed
		backButtonHandler();
		return;
	}
    /**
     * Button click handler on Main activity
     * @param v
     */
    public void onButtonClicker(View v)
    {
    	Intent intent;
    	
    	switch (v.getId()) {
		case R.id.addstock:
			intent = new Intent(this, StockMenu.class);
			startActivity(intent);
			break;

		
		case R.id.buyprod:
			intent = new Intent(this, AddBuy.class);
			startActivity(intent);
			break;
			
	
			
		case R.id.sales:
			intent = new Intent(this,RecentTransMenu.class);
			startActivity(intent);
			break;	
		case R.id.expense:
			intent = new Intent(this, ExpenseMenu.class);
			startActivity(intent);
			break;
		
		case R.id.listexpenses:
			intent = new Intent(this, ProductionMenu.class);
			startActivity(intent);
			break;			
			
		case R.id.profitloss:
			intent = new Intent(this, ListProfitLoss.class);
			startActivity(intent);
			break;
	
		case R.id.searchtrans:
			intent = new Intent(this, SearchMenu.class);
			startActivity(intent);
			break;
			
		case R.id.userinfo:
			intent = new Intent(this,UserMenu.class);
			startActivity(intent);
			break;
		
		case R.id.Logout:
			intent = new Intent(this, Login.class);
			startActivity(intent);
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(HomeActivity.this);
		    			Editor edit = prefs.edit();
		     edit.remove("user");
		     edit.remove("pass");
		     edit.commit();
			break;
			
		
		default:
			break;
		}
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsUser.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsUser.handleMenuOption(this,item);
	}
	

	public void backButtonHandler() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				HomeActivity.this);
		// Setting Dialog Title
		alertDialog.setTitle("Close Hesabu?");
		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want to close the application?");
		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.dialog_icon);
		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						  Intent intent = new Intent(Intent.ACTION_MAIN);
					        intent.addCategory(Intent.CATEGORY_HOME);
					        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					        startActivity(intent);
						finish();
						//System.exit(0);
					}
				});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event
						dialog.cancel();
					}
				});
		// Showing Alert Message
		alertDialog.show();
	}
	// Method to Sync Stock MySQL to SQLite DB
			public void syncSQLiteMySQLDB() {
				// Create AsycHttpClient object
				AsyncHttpClient client = new AsyncHttpClient();
				// Http Request Params Object
				RequestParams params = new RequestParams();
				// Show ProgressBar
				//prgDialog.show();
				// Make Http call to getusers.php
				client.post("http://wascakenya.co.ke/synchesabu/getstock.php", params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							// Hide ProgressBar
							//prgDialog.hide();
							// Update SQLite DB with response sent by getusers.php
							updateSQLite(response);
						}
						// When error occured
						@Override
						public void onFailure(int statusCode, Throwable error, String content) {
							// TODO Auto-generated method stub
							// Hide ProgressBar
							//prgDialog.hide();
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
							System.out.println(obj.get("item"));
							System.out.println(obj.get("quantity"));
							System.out.println(obj.get("bprice"));
							System.out.println(obj.get("sprice"));
							System.out.println(obj.get("value"));
							System.out.println(obj.get("date"));
							System.out.println(obj.get("userid"));
						
							

							DBHelper dbhelper = new DBHelper(this); 
							SQLiteDatabase db = dbhelper.getWritableDatabase();
			                Log.d("Account","Got Writable database");
			                
			                queryValues = new HashMap<String, String>();
			                queryValues.put("item", obj.get("item").toString());
			                queryValues.put("quantity", obj.get("quantity").toString());
			                queryValues.put("bprice", obj.get("bprice").toString());
			                queryValues.put("sprice",obj.get("sprice").toString());
			                queryValues.put("value",obj.get("value").toString());
			                queryValues.put("date", obj.get("date").toString());
			                queryValues.put("userid", obj.get("userid").toString());
			                queryValues.put("status","yes");
			          
			                
			                DBHelper dbh = new DBHelper(
									getApplicationContext());
							Cursor checkid =dbh.fetchProduct(obj.get("item").toString(),obj.get("userid").toString() );
							if (checkid == null) {
								Toast.makeText(getApplicationContext(), "no product saved",
								          Toast.LENGTH_SHORT).show();
							}
							else {
					    		startManagingCursor(checkid );
					    		
					    		//Check for duplicate id number
					    		if (checkid .getCount() > 0) {
					    		
					    			checkid .close();
					    			return;
					    		}
					    		}
							// DB QueryValues Object to insert into SQLite
							
			                ContentValues values = new ContentValues();
							values.put( Database.STOCK_NAME, queryValues.get("item"));
							values.put( Database.STOCK_QUANTITY, queryValues.get("quantity"));
							values.put( Database.STOCK_BPRICE, queryValues.get("bprice"));
							values.put( Database.STOCK_SPRICE, queryValues.get("sprice"));
							values.put( Database.STOCK_VALUE, queryValues.get("value"));
							values.put( Database.STOCK_DATER, queryValues.get("date"));
							values.put( Database.STOCK_USERID, queryValues.get("userid"));
							values.put( Database.SSTATUS, queryValues.get("status"));
							
							
							long rows = db.insert(Database.STOCK_TABLE_NAME, null, values);
							
							if ( rows > 0)  {
							    Toast.makeText(this, "Fetching Stock done...Successfully! ",	Toast.LENGTH_LONG).show();
							    HashMap<String, String> map = new HashMap<String, String>();
								// Add status for each User in Hashmap
								map.put("Id", obj.get("item").toString());
								map.put("status", "0");
								usersynclist.add(map);
							    
							    this.finish();
						
							}
							else
								Toast.makeText(this, "Sorry! Could not Fetch item!",	Toast.LENGTH_LONG).show();
							
							
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
				client.post("http://wascakenya.co.ke/synchesabu/updatesyncstsstock.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						Toast.makeText(getApplicationContext(),	"Sync Successfully done.", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(int statusCode, Throwable error, String content) {
							Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
					}
				});
			}
			
			// Method to Sync Expense MySQL to SQLite DB
						public void syncSQLiteMySQLDBExpense() {
							// Create AsycHttpClient object
							AsyncHttpClient client = new AsyncHttpClient();
							// Http Request Params Object
							RequestParams params = new RequestParams();
							// Show ProgressBar
						//	prgDialog.show();
							// Make Http call to getusers.php
							client.post("http://wascakenya.co.ke/synchesabu/getexpense.php", params, new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										// Hide ProgressBar
										//prgDialog.hide();
										// Update SQLite DB with response sent by getusers.php
										updateSQLiteExpense(response);
									}
									// When error occured
									@Override
									public void onFailure(int statusCode, Throwable error, String content) {
										// TODO Auto-generated method stub
										// Hide ProgressBar
										//prgDialog.hide();
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
						public void updateSQLiteExpense(String response){
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
										System.out.println(obj.get("name"));
										System.out.println(obj.get("amount"));
										System.out.println(obj.get("date"));
										System.out.println(obj.get("userid"));
									
										

										DBHelper dbhelper = new DBHelper(this); 
										SQLiteDatabase db = dbhelper.getWritableDatabase();
						                Log.d("Account","Got Writable database");
						                
						                queryValues = new HashMap<String, String>();
						                queryValues.put("name", obj.get("name").toString());
						                queryValues.put("amount", obj.get("amount").toString());
						                queryValues.put("date", obj.get("date").toString());
						                queryValues.put("userid", obj.get("userid").toString());
						                queryValues.put("status","yes");
						          
						                
						                DBHelper dbh = new DBHelper(
												getApplicationContext());
										Cursor checkid =dbh.fetchExpense(obj.get("name").toString(),obj.get("userid").toString() );
										if (checkid == null) {
											Toast.makeText(getApplicationContext(), "no expense saved",
											          Toast.LENGTH_SHORT).show();
										}
										else {
								    		startManagingCursor(checkid );
								    		
								    		//Check for duplicate id number
								    		if (checkid .getCount() > 0) {
								    		
								    			stopManagingCursor(checkid );
								    			checkid .close();
								    			return;
								    		}
								    		}
										// DB QueryValues Object to insert into SQLite
										
						                ContentValues values = new ContentValues();
										values.put( Database.EXPENSE_NAME, queryValues.get("name"));
										values.put( Database.EXPENSE_AMOUNT, queryValues.get("amount"));
										values.put( Database.EXPENSE_DATE, queryValues.get("date"));
										values.put( Database.EXPENSE_USERID, queryValues.get("userid"));
										values.put( Database.ESTATUS, queryValues.get("status"));
										
										
										long rows = db.insert(Database.EXPENSE_TABLE_NAME, null, values);
										
										if ( rows > 0)  {
										    Toast.makeText(this, "Fetching Expense done...Successfully! ",	Toast.LENGTH_LONG).show();
										    HashMap<String, String> map = new HashMap<String, String>();
											// Add status for each User in Hashmap
											map.put("Id", obj.get("name").toString());
											map.put("status", "0");
											usersynclist.add(map);
										    
										    this.finish();
									
										}
										else
											Toast.makeText(this, "Sorry! Could not Fetch Expense!",	Toast.LENGTH_LONG).show();
										
										
									}
									// Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
									updateMySQLSyncStsExpense(gson.toJson(usersynclist));
									// Reload the Main Activity
									//reloadActivity();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						// Method to inform remote MySQL DB about completion of Sync activity
						public void updateMySQLSyncStsExpense(String json) {
							System.out.println(json);
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put("syncsts", json);
							// Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
							client.post("http://wascakenya.co.ke/synchesabu/updatesyncstsexpense.php", params, new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(String response) {
									Toast.makeText(getApplicationContext(),	"Sync Successfully done.", Toast.LENGTH_LONG).show();
								}

								@Override
								public void onFailure(int statusCode, Throwable error, String content) {
										Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
								}
							});
						}
						// Method to Sync Production MySQL to SQLite DB
						public void syncSQLiteMySQLDBProduction() {
							// Create AsycHttpClient object
							AsyncHttpClient client = new AsyncHttpClient();
							// Http Request Params Object
							RequestParams params = new RequestParams();
							// Show ProgressBar
						//	prgDialog.show();
							// Make Http call to getusers.php
							client.post("http://wascakenya.co.ke/synchesabu/getproduction.php", params, new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										// Hide ProgressBar
										//prgDialog.hide();
										// Update SQLite DB with response sent by getusers.php
										updateSQLiteProduction(response);
									}
									// When error occured
									@Override
									public void onFailure(int statusCode, Throwable error, String content) {
										// TODO Auto-generated method stub
										// Hide ProgressBar
										//prgDialog.hide();
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
						public void updateSQLiteProduction(String response){
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
										System.out.println(obj.get("name"));
										System.out.println(obj.get("amount"));
										System.out.println(obj.get("date"));
										System.out.println(obj.get("userid"));
									
										

										DBHelper dbhelper = new DBHelper(this); 
										SQLiteDatabase db = dbhelper.getWritableDatabase();
						                Log.d("Account","Got Writable database");
						                
						                queryValues = new HashMap<String, String>();
						                queryValues.put("name", obj.get("name").toString());
						                queryValues.put("amount", obj.get("amount").toString());
						                queryValues.put("date", obj.get("date").toString());
						                queryValues.put("userid", obj.get("userid").toString());
						                queryValues.put("status","yes");
						          
						                
						                DBHelper dbh = new DBHelper(
												getApplicationContext());
										Cursor checkid =dbh.fetchProduction(obj.get("name").toString(),obj.get("userid").toString() );
										if (checkid == null) {
											Toast.makeText(getApplicationContext(), "no expense saved",
											          Toast.LENGTH_SHORT).show();
										}
										else {
								    		startManagingCursor(checkid );
								    		
								    		//Check for duplicate id number
								    		if (checkid .getCount() > 0) {
								    		
								    			stopManagingCursor(checkid );
								    			checkid .close();
								    			return;
								    		}
								    		}
										// DB QueryValues Object to insert into SQLite
										
						                ContentValues values = new ContentValues();
										values.put( Database.PRODUCTION_NAME, queryValues.get("name"));
										values.put( Database.PRODUCTION_AMOUNT, queryValues.get("amount"));
										values.put( Database.PRODUCTION_DATE, queryValues.get("date"));
										values.put( Database.PRODUCTION_USERID, queryValues.get("userid"));
										values.put( Database.PSTATUS, queryValues.get("status"));
										
										
										long rows = db.insert(Database.PRODUCTION_TABLE_NAME, null, values);
										
										if ( rows > 0)  {
										    Toast.makeText(this, "Fetching Production done...Successfully! ",	Toast.LENGTH_LONG).show();
										    HashMap<String, String> map = new HashMap<String, String>();
											// Add status for each User in Hashmap
											map.put("Id", obj.get("name").toString());
											map.put("status", "0");
											usersynclist.add(map);
										    
										    this.finish();
									
										}
										else
											Toast.makeText(this, "Sorry! Could not Fetch Production!",	Toast.LENGTH_LONG).show();
										
										
									}
									// Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
									updateMySQLSyncStsProduction(gson.toJson(usersynclist));
									// Reload the Main Activity
									//reloadActivity();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						// Method to inform remote MySQL DB about completion of Sync activity
						public void updateMySQLSyncStsProduction(String json) {
							System.out.println(json);
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put("syncsts", json);
							// Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
							client.post("http://wascakenya.co.ke/synchesabu/updatesyncstsproduction.php", params, new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(String response) {
									Toast.makeText(getApplicationContext(),	"Sync Successfully done.", Toast.LENGTH_LONG).show();
								}

								@Override
								public void onFailure(int statusCode, Throwable error, String content) {
										Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
								}
							});
						}
			
						// Method to Sync Trans MySQL to SQLite DB
						public void syncSQLiteMySQLDBTrans() {
							// Create AsycHttpClient object
							AsyncHttpClient client = new AsyncHttpClient();
							// Http Request Params Object
							RequestParams params = new RequestParams();
							// Show ProgressBar
							//prgDialog.show();
							// Make Http call to getusers.php
							client.post("http://wascakenya.co.ke/synchesabu/gettrans.php", params, new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										// Hide ProgressBar
										//prgDialog.hide();
										// Update SQLite DB with response sent by getusers.php
										updateSQLiteTrans(response);
									}
									// When error occured
									@Override
									public void onFailure(int statusCode, Throwable error, String content) {
										// TODO Auto-generated method stub
										// Hide ProgressBar
										//prgDialog.hide();
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
						public void updateSQLiteTrans(String response){
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
										System.out.println(obj.get("tid"));
										System.out.println(obj.get("stockid"));
										System.out.println(obj.get("transdate"));
										System.out.println(obj.get("transtype"));
										System.out.println(obj.get("quantity"));
										System.out.println(obj.get("price"));
										System.out.println(obj.get("total"));
										System.out.println(obj.get("remarks"));
										System.out.println(obj.get("userid"));
									
										

										DBHelper dbhelper = new DBHelper(this); 
										SQLiteDatabase db = dbhelper.getWritableDatabase();
						                Log.d("Account","Got Writable database");
						                
						                queryValues = new HashMap<String, String>();
						                queryValues.put("tid", obj.get("tid").toString());
						                queryValues.put("stockid", obj.get("stockid").toString());
						                queryValues.put("transdate", obj.get("transdate").toString());
						                queryValues.put("transtype", obj.get("transtype").toString());
						                queryValues.put("quantity", obj.get("quantity").toString());
						                queryValues.put("price", obj.get("price").toString());
						                queryValues.put("total", obj.get("total").toString());
						                queryValues.put("remarks", obj.get("remarks").toString());
						                queryValues.put("userid", obj.get("userid").toString());
						                queryValues.put("status","yes");
						          
						                
						                DBHelper dbh = new DBHelper(
												getApplicationContext());
										Cursor checkid =dbh.fetchTrans(obj.get("tid").toString(),obj.get("userid").toString());
										if (checkid == null) {
											Toast.makeText(getApplicationContext(), "no transaction saved",
											          Toast.LENGTH_SHORT).show();
										}
										else {
								    		startManagingCursor(checkid );
								    		
								    		//Check for duplicate id number
								    		if (checkid .getCount() > 0) {
								    		
								    			stopManagingCursor(checkid );
								    			checkid .close();
								    			return;
								    		}
								    		}
										// DB QueryValues Object to insert into SQLite
										
						                ContentValues values = new ContentValues();
										values.put( Database.TRANSACTIONS_ID, queryValues.get("tid"));
										values.put( Database.TRANSACTIONS_ACCOUNT_ID, queryValues.get("stockid"));
										values.put( Database.TRANSACTIONS_TRANSDATE, queryValues.get("transdate"));
										values.put( Database.TRANSACTIONS_TRANSTYPE, queryValues.get("transtype"));
										values.put( Database.TRANSACTIONS_TRANSAMOUNT, queryValues.get("quantity"));
										values.put( Database.TRANSACTIONS_CHEQUE_NO, queryValues.get("price"));
										values.put( Database.TRANSACTIONS_CHEQUE_PARTY , queryValues.get("total"));
										values.put( Database.TRANSACTIONS_REMARKS , queryValues.get("remarks"));
										values.put( Database.TRANSACTIONS_USERID , queryValues.get("userid"));
										values.put( Database.TSTATUS, queryValues.get("status"));
										
										
										long rows = db.insert(Database.TRANSACTIONS_TABLE_NAME, null, values);
										
										if ( rows > 0)  {
										    Toast.makeText(this, "Fetching Transactions done...Successfully! ",	Toast.LENGTH_LONG).show();
										    HashMap<String, String> map = new HashMap<String, String>();
											// Add status for each User in Hashmap
											map.put("Id", obj.get("userid").toString());
											map.put("status", "0");
											usersynclist.add(map);
										    
										    this.finish();
									
										}
										else
											Toast.makeText(this, "Sorry! Could not Fetch Transaction!",	Toast.LENGTH_LONG).show();
										
										
									}
									// Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
									updateMySQLSyncStsTrans(gson.toJson(usersynclist));
									// Reload the Main Activity
									//reloadActivity();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						// Method to inform remote MySQL DB about completion of Sync activity
						public void updateMySQLSyncStsTrans(String json) {
							System.out.println(json);
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put("syncsts", json);
							// Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
							client.post("http://wascakenya.co.ke/synchesabu/updatesyncststrans.php", params, new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(String response) {
									Toast.makeText(getApplicationContext(),	"Sync Successfully done.", Toast.LENGTH_LONG).show();
								}

								@Override
								public void onFailure(int statusCode, Throwable error, String content) {
										Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
								}
							});
						}
						// Reload MainActivity
						public void reloadActivity() {
							Intent objIntent = new Intent(getApplicationContext(), HomeActivity.class);
							startActivity(objIntent);
						}
						
						
						 public void syncSQLiteMySQLDBFeedBack(){
						        //Create AsycHttpClient object
							 final DBHelper db = new DBHelper(getApplicationContext());
						        AsyncHttpClient client = new AsyncHttpClient();
						        RequestParams params = new RequestParams();
						        ArrayList<HashMap<String, String>> userList =  db.getAllFeedBack();
						        if(userList.size()!=0){
						            if(db.dbSyncCountFeedBack() != 0){
						                //prgDialog.show();
						                params.put("feedbackJSON", db.composeJSONFeedBack());
						                client.post("http://wascakenya.co.ke/synchesabu/insertfeedback.php",params ,new AsyncHttpResponseHandler() {
						                    @Override
						                    public void onSuccess(String response) {
						                        System.out.println(response);
						                      //  prgDialog.hide();
						                        try {
						                            JSONArray arr = new JSONArray(response);
						                            System.out.println(arr.length());
						                            for(int i=0; i<arr.length();i++){
						                                JSONObject obj = (JSONObject)arr.get(i);
						                                System.out.println(obj.get("id"));
						                                System.out.println(obj.get("status"));
						                                db.updateSyncStatusFeedBack(obj.get("id").toString(),obj.get("status").toString());
						                            }
						                           // Toast.makeText(getApplicationContext(), "feedback successfull!", Toast.LENGTH_LONG).show();
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
						                Toast.makeText(getApplicationContext(), "Sync Succesfully done!", Toast.LENGTH_LONG).show();
						            }
						        }else{
						               // Toast.makeText(getApplicationContext(), "No data in SQLite DB, to perform Sync action", Toast.LENGTH_LONG).show();
						        }
						    }
						 
						
						 
						 public void syncSQLiteMySQLDBStock(){
						        //Create AsycHttpClient object
							  final DBHelper db = new DBHelper(getApplicationContext());
						        AsyncHttpClient client = new AsyncHttpClient();
						        RequestParams params = new RequestParams();
						        ArrayList<HashMap<String, String>> userList =  db.getAllStock();
						        if(userList.size()!=1){
						            if(db.dbSyncCount() != 1){
						                //prgDialog.show();
						                params.put("stocksJSON", db.composeJSONStock());
						                client.post("http://wascakenya.co.ke/synchesabu/insertstock.php",params ,new AsyncHttpResponseHandler() {
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
						                                db.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
						                            }
						                         //   Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();
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
						                Toast.makeText(getApplicationContext(), "Sync Succesfully done!", Toast.LENGTH_LONG).show();
						            }
						        }else{
						                //Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
						        }
						    }
						 
						 public void syncSQLiteMySQLDBSales(){
						        //Create AsycHttpClient object
							 final DBHelper db = new DBHelper(getApplicationContext());
						        AsyncHttpClient client = new AsyncHttpClient();
						        RequestParams params = new RequestParams();
						        ArrayList<HashMap<String, String>> userList =  db.getAllTrans();
						        if(userList.size()!=0){
						            if(db.dbSyncCountTrans() != 0){
						               // prgDialog.show();
						                params.put("transJSON", db.composeJSONTrans());
						                client.post("http://wascakenya.co.ke/synchesabu/inserttrans.php",params ,new AsyncHttpResponseHandler() {
						                    @Override
						                    public void onSuccess(String response) {
						                        System.out.println(response);
						                      //  prgDialog.hide();
						                        try {
						                            JSONArray arr = new JSONArray(response);
						                            System.out.println(arr.length());
						                            for(int i=0; i<arr.length();i++){
						                                JSONObject obj = (JSONObject)arr.get(i);
						                                System.out.println(obj.get("id"));
						                                System.out.println(obj.get("status"));
						                                db.updateSyncStatusTrans(obj.get("id").toString(),obj.get("status").toString());
						                            }
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
						                Toast.makeText(getApplicationContext(), "Sync Successfully done!", Toast.LENGTH_LONG).show();
						            }
						        }else{
						               // Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
						        }
						    }
						 public void syncSQLiteMySQLDBAddExpense(){
						        //Create AsycHttpClient object
							 final DBHelper db = new DBHelper(getApplicationContext());
						        AsyncHttpClient client = new AsyncHttpClient();
						        RequestParams params = new RequestParams();
						        ArrayList<HashMap<String, String>> userList =  db.getAllExpense();
						        if(userList.size()!=0){
						            if(db.dbSyncCountexpense() != 0){
						               // prgDialog.show();
						                params.put("expenseJSON", db.composeJSONExpense());
						                client.post("http://wascakenya.co.ke/synchesabu/insertexpense.php",params ,new AsyncHttpResponseHandler() {
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
						                                db.updateSyncStatusexpense(obj.get("id").toString(),obj.get("status").toString());
						                            }
						                            //Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();
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
						                Toast.makeText(getApplicationContext(), "Sync Successfully done!", Toast.LENGTH_LONG).show();
						            }
						        }else{
						             //   Toast.makeText(getApplicationContext(), "No data in SQLite DB,to perform Sync action", Toast.LENGTH_LONG).show();
						        }
						    }
						 
}

