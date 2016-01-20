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

public class UtilsStock {
	
    public static boolean  inflateMenu(Activity activity, Menu menu) {
    	MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate( R.menu.stock_menu, menu);   
		return true; 
    }
    
    public static boolean handleMenuOption(Activity activity, MenuItem item) {
    	Intent intent;
		switch(item.getItemId()) {

		case R.id.optExpense :
			 intent = new Intent(activity,Activity_AddStock.class); 
			  activity.startActivity(intent);
			  break;
		
		case R.id.optListExpense :
			  intent = new Intent(activity, Activity_ListStock.class);
			  activity.startActivity(intent);
			  break;			  
			  
		case R.id.optRecentSales :
			  intent = new Intent(activity,ListRecentSales.class);
			  activity.startActivity(intent);
			  break;
		case R.id.optRecentPurchase :
			  intent = new Intent(activity,ListRecentPurchases.class);
			  activity.startActivity(intent);
			  break;	
		}
		return true;
    }
    
}

