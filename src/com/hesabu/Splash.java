package com.hesabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hesabuapp.R;

public class Splash extends Activity{

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.splash);
			
			Thread timer = new Thread(){
				public void run(){
					try{
						sleep(2000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}finally{
						Intent intent = new Intent(getApplicationContext(), Login.class);
				    	startActivity(intent);
					}
			 	}
				
			};
			timer.start();
		}

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
			finish();
		}
	}
	
