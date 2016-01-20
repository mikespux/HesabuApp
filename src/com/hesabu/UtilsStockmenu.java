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

public class UtilsStockmenu {
	
    public static boolean  inflateMenu(Activity activity, Menu menu) {
    	MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate( R.menu.stock_menu1, menu);   
		return true; 
    }
    
    public static boolean handleMenuOption(Activity activity, MenuItem item) {
    	Intent intent;
		switch(item.getItemId()) {

		case R.id.optBp :
			 intent = new Intent(activity,AddBuy.class); 
			  activity.startActivity(intent);
			  break;
		
		case R.id.optListExpense :
			  intent = new Intent(activity, Activity_ALert.class);
			  activity.startActivity(intent);
			  break;			  
			  
		case R.id.optListAlert :
			  intent = new Intent(activity, Activity_ListAlert.class);
			  activity.startActivity(intent);
			  break;	
		}
		return true;
    }
    
}

