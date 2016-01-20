package com.hesabu;

/**
 * @author Michael N.Orenge
 * 
 */

import java.text.DecimalFormat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hesabuapp.R;

public class Activity_ListAlert extends DashBoardActivity {
    /** Called when the activity is first created. */
	ListView listStock;
	TextView totalexpenses;
	DecimalFormat format;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listalert);
        setHeader("List Expense", true, true);
        format=new DecimalFormat("#,###.00");
        totalexpenses=(TextView) findViewById(R.id.txtexp);
        
        listStock = (ListView) this.findViewById(R.id.listStock);
		listStock.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
				TextView  textAccountId = (TextView) selectedView.findViewById(R.id.textAccountId);
				Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
				Intent intent = new Intent(Activity_ListAlert.this, UpdateALert.class);
				intent.putExtra("accountid", textAccountId.getText().toString());
				startActivity(intent);
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
    @Override 
	public void onStart() {
		super.onStart();
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_ListAlert.this);
	        String username = prefs.getString("user", "anon");
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor  accounts = db.query( false, Database.ALERT_TABLE_NAME,null, "uid" + "='" + username + "'",null,null,null,null,null,null);
			
			String from [] = { Database.ALERT_ID , Database.ALERT_AMOUNT };
			int to [] = { R.id.textAccountId,R.id.bprice};
			
			@SuppressWarnings("deprecation")
			SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.alert, accounts,from,to);
			
		    ListView listStock = (ListView) this.findViewById( R.id.listStock);
		    listStock.setAdapter(ca);
		    dbhelper.close();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
