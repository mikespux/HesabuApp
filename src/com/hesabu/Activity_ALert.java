package com.hesabu;

/**
 * @author Michael N.Orenge
 * 
 */

import java.util.Calendar;
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

public class Activity_ALert extends DashBoardActivity {
    /** Called when the activity is first created. */
	private int day, month, year;
	String Date;
	ProgressDialog prgDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        setHeader("Alert", true, true);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Synching Alert. Please wait...");
        prgDialog.setCancelable(false);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
       
		final EditText amount = (EditText) findViewById(R.id.amount);
		Button btnsave = (Button) findViewById(R.id.save);
        btnsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				String Amount = amount.getText().toString();
				Date=String.format("%d-%d-%d",year,month + 1,day);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_ALert.this);
		        String username = prefs.getString("user", "anon");
				
				if(Amount.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter alert", Toast.LENGTH_LONG).show();
					return;
				}
				
				Boolean success = true;
				try {
					DBHelper db = new DBHelper(
							getApplicationContext());
					db.AddAlert(Amount,username);
					if (success) {
						
					
				
						Toast.makeText(Activity_ALert.this, "Alert Saved successfully!!",
								Toast.LENGTH_LONG).show();
						
						amount.setText("");
						
					}
				} catch (Exception e) {
					success = false;

					if (success) {
						Toast.makeText(Activity_ALert.this, "Saving  Failed",
								Toast.LENGTH_LONG).show();
					}

				}

			}

		});
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsStockmenu.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsStockmenu.handleMenuOption(this,item);
	}
	
}
