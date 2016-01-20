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

public class UtilsReports {
	
    public static boolean  inflateMenu(Activity activity, Menu menu) {
    	MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate( R.menu.ploss_menu, menu);   
		return true; 
    }
    
    public static boolean handleMenuOption(Activity activity, MenuItem item) {
    	Intent intent;
		switch(item.getItemId()) {

		case R.id.optPloss :
			 intent = new Intent(activity,ListProfitLoss.class); 
			  activity.startActivity(intent);
			  break;
		
		case R.id.optSearch :
			  intent = new Intent(activity, SearchPLoss.class);
			  activity.startActivity(intent);
			  break;			  
			  
		case R.id.optStockR  :
			  intent = new Intent(activity,Activity_ListStock.class);
			  activity.startActivity(intent);
			  break;			  
		}
		return true;
    }
    
}

