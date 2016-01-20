package com.hesabu;
/**
 * @author Michael N.Orenge
 * 
 */
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.hesabuapp.R;

public class SearchPLoss extends DashBoardActivity {
	private EditText editFromDate,editToDate;
	private int fromDay, fromMonth, fromYear;
	
	private int toDay, toMonth, toYear;
	private final int FROM_DATE_DIALOG = 1;
	private final int TO_DATE_DIALOG = 2;
	private Spinner editFromAmount,editToAmount;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_plloss);
	    setHeader(getString(R.string.SearchPL), true, true);
		editFromDate = (EditText) this.findViewById(R.id.editFromDate);
		editToDate = (EditText) this.findViewById(R.id.editToDate);
		
		editFromAmount = (Spinner) this.findViewById(R.id.editFromAmount);
		Database.populateAccounts(editFromAmount);
		editToAmount = (Spinner) this.findViewById(R.id.editToAmount);
		
		 // get the current date
        final Calendar c = Calendar.getInstance();
        fromYear = toYear = c.get(Calendar.YEAR);
        fromMonth  = toMonth = c.get(Calendar.MONTH);
        toDay = c.get(Calendar.DAY_OF_MONTH);
        
        fromDay  = 1;  // from is set to 1st of the current month 
        
        updateToDateDisplay();
        updateFromDateDisplay();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return UtilsReports.inflateMenu(this,menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		return  UtilsReports.handleMenuOption(this,item);
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
		   Intent intent = new Intent(this, ListPLoss.class);
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
