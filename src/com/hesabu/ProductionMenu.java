package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hesabuapp.R;

public class ProductionMenu extends Activity {
	
Button addpcost,listpcost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_menu);
		
		addpcost=(Button) findViewById(R.id.pcost);
		listpcost=(Button) findViewById(R.id.listpcost);
		
		addpcost.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Activity_Production.class);
				startActivity(intent);
			}
		});
		listpcost.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Activity_ListProduction.class);
				startActivity(intent);
			}
		});
		
	}

}
