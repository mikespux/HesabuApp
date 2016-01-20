package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hesabuapp.R;

public class RecentTransMenu extends Activity {
	
Button recentp,recentS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recent_menu);
		
		recentp=(Button) findViewById(R.id.recentp);
		recentS=(Button) findViewById(R.id.recentS);
		
		recentp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ListRecentPurchases.class);
				startActivity(intent);
			}
		});
		recentS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ListRecentSales.class);
				startActivity(intent);
			}
		});
		
	}

}
