package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hesabuapp.R;

public class UserMenu extends Activity {
	
Button userinfo,changepass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_menu);
		
		userinfo=(Button) findViewById(R.id.userinfo);
		changepass=(Button) findViewById(R.id.changepass);
		
		userinfo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), UpdateUser.class);
				startActivity(intent);
			}
		});
		changepass.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), UpdatePass.class);
				startActivity(intent);
			}
		});
		
	}

}
