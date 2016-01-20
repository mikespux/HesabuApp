package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hesabuapp.R;

public class SearchMenu extends Activity {
	
Button searcht,searchpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_menu);
		
		searcht=(Button) findViewById(R.id.searcht);
		searchpl=(Button) findViewById(R.id.searchpl);
		
		searcht.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SearchTransactions.class);
				startActivity(intent);
			}
		});
		searchpl.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SearchPLoss.class);
				startActivity(intent);
			}
		});
		
	}

}
