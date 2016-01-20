package com.hesabu;
/**
 * @author Michael N.Orenge
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.hesabuapp.R;

public class SearchTransactions extends DashBoardActivity {
	private EditText editFromDate,editToDate;
	private int fromDay, fromMonth, fromYear;
	private SQLiteDatabase newDB;
	DBHelper dbHelper;
	String partyName;
	ArrayList<String> dbdata=new ArrayList<String>();
	ArrayAdapter<String> adapter;

	int textlength=0;
	private int toDay, toMonth, toYear;
	private final int FROM_DATE_DIALOG = 1;
	private final int TO_DATE_DIALOG = 2;
	private Spinner editFromAmount,editToAmount;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_transactions);
	    setHeader(getString(R.string.Search), true, true);
		editFromDate = (EditText) this.findViewById(R.id.editFromDate);
		editToDate = (EditText) this.findViewById(R.id.editToDate);
		
		editFromAmount = (Spinner) this.findViewById(R.id.editFromAmount);
		//Database.populateAccounts(editFromAmount);
		editToAmount = (Spinner) this.findViewById(R.id.editToAmount);
		
		 // get the current date
        final Calendar c = Calendar.getInstance();
        fromYear = toYear = c.get(Calendar.YEAR);
        fromMonth  = toMonth = c.get(Calendar.MONTH);
        toDay = c.get(Calendar.DAY_OF_MONTH);
        
        fromDay  = 1;  // from is set to 1st of the current month 
        DataFromDB();//method to get data from sqlite database
	
        updateToDateDisplay();
        updateFromDateDisplay();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return Utils.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  Utils.handleMenuOption(this,item);
	}
	
	private void DataFromDB() {
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SearchTransactions.this);
	        String username = prefs.getString("user", "anon");
		dbHelper=new DBHelper(SearchTransactions.this);
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

		adapter=new ArrayAdapter<String>(SearchTransactions.this,android.R.layout.simple_list_item_1,dbdata);
		//lv.setAdapter(adapter);

		adapter=new ArrayAdapter<String>(SearchTransactions.this,android.R.layout.simple_list_item_1,dbdata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		editFromAmount.setAdapter(adapter);
		}

		private DatePickerDialog.OnDateSetListener fromDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int pYear,int pMonth, int pDay) {
                    fromYear = pYear;
                    fromMonth = pMonth;
                    fromDay = pDay;
                    updateFromDateDisplay();
                }
            };
          
   	private DatePickerDialog.OnDateSetListener toDateSetListener =
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int pYear,int pMonth, int pDay) {
                            toYear = pYear;
                            toMonth = pMonth;
                            toDay = pDay;
                            updateToDateDisplay();
                        }
                    };

	@SuppressWarnings("deprecation")
	public void showFromDateDialog(View v) {
		  showDialog(FROM_DATE_DIALOG);
	}
	
	@SuppressWarnings("deprecation")
	public void showToDateDialog(View v) {
		  showDialog(TO_DATE_DIALOG);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case FROM_DATE_DIALOG:
	        return new DatePickerDialog(this,
	                    fromDateSetListener, fromYear, fromMonth, fromDay);
	    case TO_DATE_DIALOG:
	        return new DatePickerDialog(this,
	                    toDateSetListener, toYear, toMonth, toDay);
	    }
	    return null;
	}
	
	private void updateToDateDisplay() {
            // Month is 0 based so add 1
	        editToDate.setText( String.format("%d-%d-%d",toYear,toMonth + 1,toDay));
	}
	private void updateFromDateDisplay() {
        // Month is 0 based so add 1
        editFromDate.setText( String.format("%d-%d-%d",fromYear,fromMonth + 1,fromDay));
}
	 
	public void searchTransactions(View v) {
		   Intent intent = new Intent(this, ListTransactions.class);
		   intent.putExtra("fromdate", editFromDate.getText().toString());
		   intent.putExtra("todate", editToDate.getText().toString());
		   intent.putExtra("fromamount", editFromAmount.getSelectedItem().toString());
		   intent.putExtra("toamount", editToAmount.getSelectedItem().toString());
		   startActivity(intent);
	}
	
	public void clearFields(View v) {
		
           editFromDate.setText("");
           editToDate.setText("");
           editFromAmount.setTag("");
           editToAmount.setTag("");
	} 
	

}
