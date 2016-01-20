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

public class UtilsUser {
	
    public static boolean  inflateMenu(Activity activity, Menu menu) {
    	MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate( R.menu.user_menu, menu);   
		return true; 
    }
    
    public static boolean handleMenuOption(Activity activity, MenuItem item) {
    	Intent intent;
		switch(item.getItemId()) {

		case R.id.optUin :
			 intent = new Intent(activity,UpdateUser.class); 
			  activity.startActivity(intent);
			  break;
		
		case R.id.optPass :
			  intent = new Intent(activity, UpdatePass.class);
			  activity.startActivity(intent);
			  break;			  
			  
				  
		}
		return true;
    }
    
}

