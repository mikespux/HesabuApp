package com.hesabu;
/**
 * @author Michael N.Orenge
 * 
 */
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hesabuapp.R;

public class ListPLoss extends Activity {
	ListView listTransactions,listExpenses,ListProduction;
	String  fromDate,toDate,fromAmount,toAmount;
	String  condition = " 1 = 1 ";
	TextView gross,totalexpenses,netprofit,txtCOS,txtSales,heading,txtinc,txtexp,Csales,solditems;
	String Date;
	DecimalFormat format;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_profit);
		format=new DecimalFormat("#,###.00");
		fromDate = this.getIntent().getStringExtra("fromdate");
		toDate = this.getIntent().getStringExtra("todate");
		fromAmount = this.getIntent().getStringExtra("fromamount");
		toAmount = this.getIntent().getStringExtra("toamount");
		listTransactions = (ListView) this.findViewById(R.id.listTransactions);
		listExpenses = (ListView) this.findViewById(R.id.listExpenses);
		ListProduction = (ListView) this.findViewById(R.id.listProduction);
		
		Calendar.getInstance();
		new SimpleDateFormat("MMM dd, yyyy");
	        
	        Date=toDate;
		gross=(TextView) findViewById(R.id.gross);
		totalexpenses=(TextView) findViewById(R.id.totalexp);
		netprofit=(TextView) findViewById(R.id.netprofit);
		solditems=(TextView) findViewById(R.id.solditems);
		txtCOS=(TextView) findViewById(R.id.txtCOS);
		txtSales=(TextView) findViewById(R.id.txtSale);
		heading=(TextView) findViewById(R.id.heading);
		txtexp=(TextView) findViewById(R.id.txtExp);
		txtinc=(TextView) findViewById(R.id.txtInc);
		Csales=(TextView) findViewById(R.id.CSales);
		addGross();
		addExpenses();
		netProfit();
		getHeading();
		
		// form condition based on input
		if ( fromDate.length() > 0)
			  condition += " and  transdate   >= '" + fromDate + "'";
		
		if ( toDate.length() > 0)
			  condition += " and  transdate <= '" + toDate + "'";
		
		

				
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsReports.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsReports.handleMenuOption(this,item);
	}
	
	public void addGross(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
        String username = prefs.getString("user", "anon");
		DBHelper db = new DBHelper(getApplicationContext());
		int sales = db.totalSales1(username,fromDate,toDate);
		int buying = db.totalCOS1(username,fromDate,toDate);
		int solditem = db.totalBuying1(username,fromDate,toDate);
		int production = db.totalProduction1(username,fromDate,toDate);
		String ttl  = String.valueOf(format.format(sales-(buying + solditem)));
		String sale  = String.valueOf(format.format(sales));
		String cos  = String.valueOf(format.format(buying+production));
		String sold  = String.valueOf(format.format(solditem));
		
		gross.setText("Ksh."+cos );
		gross.setPaintFlags(gross.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		solditems.setText("Ksh."+sold );
		solditems.setPaintFlags(solditems.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		txtSales.setText("Ksh."+sale);
		txtSales.setPaintFlags(txtSales.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		Csales.setText("Ksh."+ttl);
		Csales.setPaintFlags(Csales.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		
	
	}
	public void addExpenses(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
        String username = prefs.getString("user", "anon");
		DBHelper db = new DBHelper(getApplicationContext());
		int expense = db.totalExpense1(username,fromDate,toDate);
		db.totalCOS1(username,fromDate,toDate);
		
		String ttl  = String.valueOf(format.format(expense));
		
		totalexpenses.setText("Ksh." +ttl);
		totalexpenses.setPaintFlags(totalexpenses.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
	}
	public void getHeading(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
        String username = prefs.getString("user", "anon");
		DBHelper db = new DBHelper(getApplicationContext());
		String headingf = db.getHeading(username);
		txtinc.setPaintFlags(heading.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		txtexp.setPaintFlags(heading.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		
		heading.setPaintFlags(heading.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
		heading.setText( headingf+" Profit and Loss as at "+Date  );
	}
	public void netProfit(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
        String username = prefs.getString("user", "anon");
		DBHelper db = new DBHelper(getApplicationContext());
		int expense = db.totalExpense1(username,fromDate,toDate);
		int production = db.totalProduction1(username,fromDate,toDate);
		int sales = db.totalSales1(username,fromDate,toDate);
		int buying = db.totalCOS1(username,fromDate,toDate);
		int solditem = db.totalBuying1(username,fromDate,toDate);
		int profit;
		profit=sales-(buying+expense+solditem+production);
		if(profit<0){
		
		String ttl  = String.valueOf(format.format(profit));
		netprofit.setText("(Ksh."+ttl +")" +" Loss");
		}else if(profit==0)
		{
			String ttl  = String.valueOf(format.format(profit));
			netprofit.setPaintFlags(netprofit.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
			netprofit.setText("Ksh."+ttl);
		}
		else{
			String ttl  = String.valueOf(format.format(profit));
			netprofit.setPaintFlags(netprofit.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
			netprofit.setText("(Ksh."+ttl +")" +" Profit");
			
		}
	}


	@Override 
	public void onStart() {
		super.onStart();
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
	        String username = prefs.getString("user", "anon");
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor trans = db.rawQuery("select t.uid, item,sum(quantity),sum(transamount),transtype,sum(cheque_no),sum(cheque_party),sum(cheque_no-cheque_no) as diff from transactions t inner join stock a  on ( a._id = t.account_id) where " + condition +" and t.uid='"+ username +"' and transtype ='Selling'  GROUP BY transtype,item  order by transtype desc LIMIT 10",null);
			if ( trans.getCount() == 0 )
				this.findViewById(R.id.textError).setVisibility(View.VISIBLE);
			else
				this.findViewById(R.id.textError).setVisibility(View.INVISIBLE);
			
			ArrayList<Map<String,String>> listTrans = new ArrayList<Map<String,String>>();
            while ( trans.moveToNext()) {
            	// get trans details for display
            	LinkedHashMap<String,String> tran = new LinkedHashMap<String,String>();
            
            	tran.put("acno",  trans.getString(trans.getColumnIndex("item")));
                tran.put("transtype",trans.getString(trans.getColumnIndex("transtype")));
            	tran.put("transamount",trans.getString(trans.getColumnIndex("sum(transamount)")));
            	tran.put("transremarks",trans.getString(trans.getColumnIndex("sum(cheque_party)")));
            	tran.put("transdate",trans.getString(trans.getColumnIndex("diff")));
            	
            	String chequeno = trans.getString(trans.getColumnIndex("sum(cheque_no)"));
            	String transDetails = "Cash";
            	if (! chequeno.trim().equals(""))
            		    transDetails = chequeno;
            	
            	tran.put("transdetails",transDetails);
            	listTrans.add(tran);
            }
            trans.close();
            db.close();
		    dbhelper.close();
		    
		    SimpleAdapter  adapter = new SimpleAdapter(this,
		    		listTrans, 
		    		R.layout.income, 
		    		new String [] { "acno", "transdate",  "transdetails", "transtype", "transamount" ,"transremarks"},
		    		new  int [] {   R.id.textAcno, R.id.textTransDate,  R.id.textTransDetails, R.id.textTransType, R.id.textTransAmount, R.id.textTransRemarks});
		    
		    listTransactions.setAdapter(adapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
	        String username = prefs.getString("user", "anon");
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor trans = db.rawQuery("select  uid,name,sum(amount) from expense where " + condition +" and uid='"+ username +"'  GROUP BY name    LIMIT 10",null);
			if ( trans.getCount() == 0 )
				this.findViewById(R.id.textError).setVisibility(View.VISIBLE);
			else
				this.findViewById(R.id.textError).setVisibility(View.INVISIBLE);
			
			ArrayList<Map<String,String>> listTrans = new ArrayList<Map<String,String>>();
            while ( trans.moveToNext()) {
            	// get trans details for display
            	LinkedHashMap<String,String> tran = new LinkedHashMap<String,String>();
            
            	tran.put("name",  trans.getString(trans.getColumnIndex("name")));
                tran.put("amount",trans.getString(trans.getColumnIndex("sum(amount)")));
            	
            	
            	
            	
            	listTrans.add(tran);
            }
            trans.close();
            db.close();
		    dbhelper.close();
		    
		    SimpleAdapter  adapter = new SimpleAdapter(this,
		    		listTrans, 
		    		R.layout.expenses, 
		    		new String [] {    "name", "amount" },
		    		new  int [] {    R.id.textTransType, R.id.textTransRemarks});
		    
		    listExpenses.setAdapter(adapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListPLoss.this);
	        String username = prefs.getString("user", "anon");
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			Cursor trans = db.rawQuery("select  uid,name,sum(amount) from production where " + condition +" and uid='"+ username +"'  GROUP BY name    LIMIT 10",null);
			if ( trans.getCount() == 0 )
				this.findViewById(R.id.textError).setVisibility(View.VISIBLE);
			else
				this.findViewById(R.id.textError).setVisibility(View.INVISIBLE);
			
			ArrayList<Map<String,String>> listTrans = new ArrayList<Map<String,String>>();
            while ( trans.moveToNext()) {
            	// get trans details for display
            	LinkedHashMap<String,String> tran = new LinkedHashMap<String,String>();
            
            	tran.put("name",  trans.getString(trans.getColumnIndex("name")));
                tran.put("amount",trans.getString(trans.getColumnIndex("sum(amount)")));
            	
            	
            	
            	
            	listTrans.add(tran);
            }
            trans.close();
            db.close();
		    dbhelper.close();
		    
		    SimpleAdapter  adapter = new SimpleAdapter(this,
		    		listTrans, 
		    		R.layout.production, 
		    		new String [] {    "name", "amount" },
		    		new  int [] {    R.id.textTransType, R.id.textTransRemarks});
		    
		    ListProduction.setAdapter(adapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}