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

public class Utils {
	
    public static boolean  inflateMenu(Activity activity, Menu menu) {
    	MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate( R.menu.common_menu, menu);   
		return true; 
    }
    
    public static boolean handleMenuOption(Activity activity, MenuItem item) {
    	Intent intent;
		switch(item.getItemId()) {
		case R.id.optSell :
			  intent = new Intent(activity,AddSales.class);
			  activity.startActivity(intent);
			  break;
		case R.id.optBuy :
			 intent = new Intent(activity,AddBuy.class); 
			  activity.startActivity(intent);
			  break;
			  
	
		case R.id.optSearchTransactions :
			intent = new Intent(activity,SearchTransactions.class);
			  activity.startActivity(intent);
			  break;
		case R.id.optListStock :
			  intent = new Intent(activity, ListRecentSales.class);
			  activity.startActivity(intent);
			  break;			  
			  
		case R.id.optRecentTransactions :
			  intent = new Intent(activity,ListRecentPurchases.class);
			  activity.startActivity(intent);
			  break;			  
		}
		return true;
    }
    
}

