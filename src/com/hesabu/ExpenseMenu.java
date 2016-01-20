package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hesabuapp.R;

public class ExpenseMenu extends Activity {
	
Button addexp,listex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_menu);
		
		addexp=(Button) findViewById(R.id.addexp);
		listex=(Button) findViewById(R.id.listex);
		
		addexp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Activity_Expense.class);
				startActivity(intent);
			}
		});
		listex.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Activity_ListExpense.class);
				startActivity(intent);
			}
		});
		
	}

}
