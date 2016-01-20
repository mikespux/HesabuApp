package com.hesabu;
/**
 * @author Michael N.Orenge
 * 
 */
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hesabuapp.R;

public class UtilsExpense {
	
    public static boolean  inflateMenu(Activity activity, Menu menu) {
    	MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate( R.menu.expense_menu, menu);   
		return true; 
    }
    
    public static boolean handleMenuOption(Activity activity, MenuItem item) {
    	Intent intent;
		switch(item.getItemId()) {

		case R.id.optExpense :
			 intent = new Intent(activity,Activity_Expense.class); 
			  activity.startActivity(intent);
			  break;
		
		case R.id.optListExpense :
			  intent = new Intent(activity, Activity_ListExpense.class);
			  activity.startActivity(intent);
			  break;			  
			  
				  
		}
		return true;
    }
    
}

