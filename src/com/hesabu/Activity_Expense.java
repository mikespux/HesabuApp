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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Activity_Expense extends DashBoardActivity {
    /** Called when the activity is first created. */
	private int day, month, year;
	String Date;
	ProgressDialog prgDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setHeader(getString(R.string.Expense), true, true);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Adding Expense. Please wait...");
        prgDialog.setCancelable(false);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final EditText expense = (EditText) findViewById(R.id.item);
		final EditText amount = (EditText) findViewById(R.id.amount);
		Button btnsave = (Button) findViewById(R.id.save);
        btnsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String item = expense.getText().toString();
				String Amount = amount.getText().toString();
				Date=String.format("%d-%d-%d",year,month + 1,day);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_Expense.this);
		        String username = prefs.getString("user", "anon");
				
				if(item.equals("")||Amount.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter All fields", Toast.LENGTH_LONG).show();
					return;
				}
				
				Boolean success = true;
				try {
					DBHelper db = new DBHelper(
							getApplicationContext());
					db.AddExpense(item,Amount,Date,username);
					if (success) {
						
					
				
						Toast.makeText(Activity_Expense.this, "Expense Saved successfully!!",
								Toast.LENGTH_LONG).show();
						syncSQLiteMySQLDB();
						expense.setText("");
						amount.setText("");
						
					}
				} catch (Exception e) {
					success = false;

					if (success) {
						Toast.makeText(Activity_Expense.this, "Saving  Failed",
								Toast.LENGTH_LONG).show();
					}

				}

			}

		});
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsExpense.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsExpense.handleMenuOption(this,item);
	}
	public void syncSQLiteMySQLDB(){
        //Create AsycHttpClient object
	 final DBHelper db = new DBHelper(getApplicationContext());
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  db.getAllExpense();
        if(userList.size()!=0){
            if(db.dbSyncCountexpense() != 0){
                prgDialog.show();
                params.put("expenseJSON", db.composeJSONExpense());
                client.post("http://wascakenya.co.ke/synchesabu/insertexpense.php",params ,new AsyncHttpResponseHandler() {
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
                                db.updateSyncStatusexpense(obj.get("id").toString(),obj.get("status").toString());
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
}
