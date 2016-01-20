package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hesabuapp.R;

public class StockMenu extends Activity {
	
Button addstock,liststock,buyprod,sellprod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_menu);
		
		addstock=(Button) findViewById(R.id.addstock);
		liststock=(Button) findViewById(R.id.liststock);
		buyprod=(Button) findViewById(R.id.buyprod);
		sellprod=(Button) findViewById(R.id.sellprod);
		
		addstock.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Activity_AddStock.class);
				startActivity(intent);
			}
		});
		liststock.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Activity_ListStock.class);
				startActivity(intent);
			}
		});
		buyprod.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), AddBuy.class);
				startActivity(intent);
			}
		});
		sellprod.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), AddSales.class);
				startActivity(intent);
			}
		});
	}

}
