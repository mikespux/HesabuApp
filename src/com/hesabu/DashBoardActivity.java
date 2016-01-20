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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hesabuapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public abstract class DashBoardActivity extends Activity {
    /** Called when the activity is first created. */
	static final int CUSTOM_DIALOG_ID = 0;
	Button customDialog_Dismiss;
	private EditText subject;
	private EditText body;
	private int day, month, year;
	String Date;
	ProgressDialog prgDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Composing feedback. Please wait...");
        prgDialog.setCancelable(false);
    }
    
    public void setHeader(String title, boolean btnHomeVisible, boolean btnFeedbackVisible)
    {
    		ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
    		View inflated = stub.inflate();
          
    		TextView txtTitle = (TextView) inflated.findViewById(R.id.txtHeading);
    		txtTitle.setText(title);
    		
    		Button btnHome = (Button) inflated.findViewById(R.id.btnHome);
    		if(!btnHomeVisible)
    			btnHome.setVisibility(View.INVISIBLE);
    		
    		Button btnFeedback = (Button) inflated.findViewById(R.id.btnFeedback);
    		if(!btnFeedbackVisible)
    			btnFeedback.setVisibility(View.INVISIBLE);
    		
    }
    
    /**
     * Home button click handler
     * @param v
     */
    public void btnHomeClick(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    	intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	
    }
    
    /**
     * Feedback button click handler
     * @param v
     */
    @SuppressWarnings("deprecation")
	public void btnFeedbackClick(View v)
    {
    	 showDialog(CUSTOM_DIALOG_ID);
    }

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	 
	   private Button.OnClickListener customDialog_DismissOnClickListener  = new Button.OnClickListener(){	 
	 @SuppressWarnings("deprecation")
	@Override
	 public void onClick(View arg0) {
	  // TODO Auto-generated method stub
	  dismissDialog(CUSTOM_DIALOG_ID);
	 }
	    
	   };
	   private Button.OnClickListener customDialog_DismissOnClickListene  = new Button.OnClickListener(){	 
	 @Override
	 public void onClick(View arg0) {
	  // TODO Auto-generated method stub
		 sendEmail();
    	 // after sending the email, clear the fields
		
	  
	 }
	    
	   };
	   
	@Override
	protected Dialog onCreateDialog(int id) {
	 // TODO Auto-generated method stub
	 Dialog dialog = null;
	    switch(id) {
	    case CUSTOM_DIALOG_ID:
	     dialog = new Dialog(this); 
	     dialog.setContentView(R.layout.feedback);
	     dialog.setTitle("Feedback");
	    
	    
	      subject = (EditText) dialog.findViewById(R.id.subject);
	      body = (EditText) dialog.findViewById(R.id.body);
	      Button sendBtn = (Button) dialog.findViewById(R.id.sendEmail);
	    
	      
		     
		     
	     
//	     customDialog_EditText = (EditText)dialog.findViewById(R.id.dialogedittext);
//	     customDialog_TextView = (TextView)dialog.findViewById(R.id.dialogtextview);
//	     customDialog_Update = (Button)dialog.findViewById(R.id.dialogupdate);
	     customDialog_Dismiss = (Button)dialog.findViewById(R.id.btnback);
//	     
//	     customDialog_Update.setOnClickListener(customDialog_UpdateOnClickListener);
	     customDialog_Dismiss.setOnClickListener(customDialog_DismissOnClickListener);
	     sendBtn.setOnClickListener(customDialog_DismissOnClickListene);
//	     
//	     
	        break;
	    }
	    return dialog;
	} 
	  protected void sendEmail() {

	   
			String sub = subject.getText().toString();
			String message = body.getText().toString();
			Date=String.format("%d-%d-%d",year,month + 1,day);
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DashBoardActivity.this);
	        String username = prefs.getString("user", "anon");
			
			if(sub.equals("")||message.equals(""))
			{
				Toast.makeText(getApplicationContext(), "Please enter subject and message", Toast.LENGTH_LONG).show();
				return;
			}
			
			Boolean success = true;
			try {
				DBHelper db = new DBHelper(
						getApplicationContext());
				db.AddFeedback(sub,message,Date,username);
				if (success) {
					
				
			
					Toast.makeText(DashBoardActivity.this, "Feedback Sent successfully!!",
							Toast.LENGTH_LONG).show();
					 syncSQLiteMySQLDB();
					subject.setText("");
					body.setText("");
					
				}
			} catch (Exception e) {
				success = false;

				if (success) {
					Toast.makeText(DashBoardActivity.this, "Sending  Failed",
							Toast.LENGTH_LONG).show();
				}

			}
			}
	  
	  public void syncSQLiteMySQLDB(){
	        //Create AsycHttpClient object
		 final DBHelper db = new DBHelper(getApplicationContext());
	        AsyncHttpClient client = new AsyncHttpClient();
	        RequestParams params = new RequestParams();
	        ArrayList<HashMap<String, String>> userList =  db.getAllFeedBack();
	        if(userList.size()!=0){
	            if(db.dbSyncCountFeedBack() != 0){
	                prgDialog.show();
	                params.put("feedbackJSON", db.composeJSONFeedBack());
	                client.post("http://wascakenya.co.ke/synchesabu/insertfeedback.php",params ,new AsyncHttpResponseHandler() {
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
	                                db.updateSyncStatusFeedBack(obj.get("id").toString(),obj.get("status").toString());
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
	                Toast.makeText(getApplicationContext(), "No data in SQLite DB, to perform Sync action", Toast.LENGTH_LONG).show();
	        }
	    }
}