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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends Activity {
	ProgressDialog prgDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Registering. Please wait...");
        prgDialog.setCancelable(false);
		final EditText txtFName = (EditText) findViewById(R.id.edtfname);
		final EditText txtBName = (EditText) findViewById(R.id.edtbname);
		final EditText txtBType = (EditText) findViewById(R.id.edtBtype);
		final EditText txtPNo = (EditText) findViewById(R.id.edtpno);
		final EditText txtLocation = (EditText) findViewById(R.id.edtloc);
		final EditText txtUserName = (EditText) findViewById(R.id.edtuser);
		final EditText txtPassword = (EditText) findViewById(R.id.edtpass);
		final EditText txtCPassword = (EditText) findViewById(R.id.edtcpass);
		Button back= (Button) findViewById(R.id.btnback);
		Button btnsignup = (Button) findViewById(R.id.btnsignup);
		syncSQLiteMySQLDBRegister();
    back.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent login =new Intent(RegisterActivity.this,Login.class);
		startActivity(login);
	}
	
    });
		btnsignup.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				String FName = txtFName.getText().toString();
				String BName = txtBName.getText().toString();
				String BType = txtBType.getText().toString();
				String PNo = txtPNo.getText().toString();
				String Location = txtLocation.getText().toString();
				String username = txtUserName.getText().toString();
				String password = txtPassword.getText().toString();
				String cpassword = txtCPassword.getText().toString();
				if(FName.equals("")||BName.equals("")||BType.equals("")||PNo.equals("")||Location.equals("")||username.equals("")||password.equals("")||cpassword.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter All fields", Toast.LENGTH_LONG).show();
					return;
				}
				 if(PNo.length() < 6 || PNo.length() > 13)
				    {
				        
					 Toast.makeText(getApplicationContext(), "Please enter a valid Phone Number", Toast.LENGTH_LONG).show();
					 return;
				    }
				if(!password.equals(cpassword))
				{
					Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
					return;
				}
				Boolean success = true;
				try {
					DBHelper db = new DBHelper(
							getApplicationContext());
					Cursor checkid =db.fetchUsername(username );
					if (checkid == null) {
						Toast.makeText(getApplicationContext(), "no username name saved",
						          Toast.LENGTH_SHORT).show();
					}
					else {
			    		startManagingCursor(checkid );
			    		
			    		//Check for duplicate id number
			    		if (checkid .getCount() > 0) {
			    		Toast.makeText(getApplicationContext(), "Username already exists",
			  			          Toast.LENGTH_SHORT).show();
			    			stopManagingCursor(checkid );
			    			checkid .close();
			    			return;
			    		}
			    		}
					db.AddUser(FName,BName,BType,PNo,Location ,username, password);
					if (success) {
						Toast.makeText(RegisterActivity.this, "You have been registered successfully!!",
								Toast.LENGTH_LONG).show();
						syncSQLiteMySQLDBRegister();
						SharedPreferences prefs = PreferenceManager
								.getDefaultSharedPreferences(RegisterActivity.this);
						
		    			  Editor edit = prefs.edit();
		    			  edit.putString("user",username);				    			  
		    			  edit.commit();
		    			  
		    			  edit.putString("pass",password);
		    			  edit.commit();
		    			  txtFName.setText("");
		    			  txtBName.setText("");
		    			  txtBType.setText("");
		    			  txtPNo.setText("");
		    			  txtLocation.setText("");
		    			  txtUserName.setText("");
		    			  txtPassword.setText("");
		    			  txtCPassword.setText("");
						Intent login =new Intent(RegisterActivity.this,HomeActivity.class);
						startActivity(login);
					}
				} catch (Exception e) {
					success = false;

					if (success) {
						Toast.makeText(RegisterActivity.this, "Registration  Failed",
								Toast.LENGTH_LONG).show();
					}

				}

			}

		});
	}
	 public void syncSQLiteMySQLDBRegister(){
	        //Create AsycHttpClient object
		 final DBHelper db = new DBHelper(getApplicationContext());
	        AsyncHttpClient client = new AsyncHttpClient();
	        RequestParams params = new RequestParams();
	        ArrayList<HashMap<String, String>> userList =  db.getAllUsers();
	        if(userList.size()!=0){
	            if(db.dbSyncCountUser() != 0){
	                prgDialog.show();
	                params.put("usersJSON", db.composeJSONUser());
	                client.post("http://wascakenya.co.ke/synchesabu/insertuser.php",params ,new AsyncHttpResponseHandler() {
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
	                                db.updateSyncStatusUser(obj.get("id").toString(),obj.get("status").toString());
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
	                Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
	            }
	        }else{
	                Toast.makeText(getApplicationContext(), "No data in SQLite DB, to perform Sync action", Toast.LENGTH_LONG).show();
	        }
	    }
}
