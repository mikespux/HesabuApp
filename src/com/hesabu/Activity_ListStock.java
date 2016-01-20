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

public class Activity_ListStock extends DashBoardActivity {
    /** Called when the activity is first created. */
	ListView listStock;
	TextView btotal,stotal,value;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liststock);
        setHeader(getString(R.string.FroyoActivityTitle), true, true);
        stotal=(TextView) findViewById(R.id.txtst);
        
        btotal=(TextView) findViewById(R.id.txtbt);
       value=(TextView) findViewById(R.id.txtVal);
        
        listStock = (ListView) this.findViewById(R.id.listStock);
		listStock.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
				TextView  textAccountId = (TextView) selectedView.findViewById(R.id.textAccountId);
				Log.d("Accounts", "Selected Account Id : " + textAccountId.getText().toString());
				Intent intent = new Intent(Activity_ListStock.this, UpdateStock.class);
				intent.putExtra("accountid", textAccountId.getText().toString());
				startActivity(intent);
			}
		});
		
		 
		total();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsStock.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsStock.handleMenuOption(this,item);
	}
	public void total(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_ListStock.this);
        String username = prefs.getString("user", "anon");
		DBHelper db = new DBHelper(getApplicationContext());
		int sales = db.totalSprice(username);
		int buying = db.totalBprice(username);
		int value1 = db.totalValue(username);
		DecimalFormat format=new DecimalFormat("#,###.00");
		String btotal1  = String.valueOf(format.format(buying));
		String stotal1  = String.valueOf(format.format(sales));
		String val  = String.valueOf(format.format(value1));
		btotal.setText(" B Price Total: "+btotal1 );
		stotal.setText(" S Price Total: "+stotal1 );
		value.setText("Stock Value: "+val );
	}
    @Override 
	public void onStart() {
		super.onStart();
		try {
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_ListStock.this);
	        String username = prefs.getString("user", "anon");
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor  accounts = db.query( false, Database.STOCK_TABLE_NAME,null, "uid" + "='" + username + "'",null,null,null,null,null,null);
			
			String from [] = { Database.STOCK_ID, Database.STOCK_NAME, Database.STOCK_QUANTITY,Database.STOCK_BPRICE, Database.STOCK_SPRICE , Database.STOCK_VALUE};
			int to [] = { R.id.textAccountId,R.id.textStation, R.id.textFname, R.id.bprice, R.id.textBalance,R.id.value};
			
			@SuppressWarnings("deprecation")
		
			SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.stock, accounts,from,to);
			
		    ListView listStock = (ListView) this.findViewById( R.id.listStock);
		    
		    
		    listStock.setAdapter(ca);
		    dbhelper.close();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
			
		}
    }
    
}
