package dev.firstapp;

import android.app.Activity;
import android.os.Bundle;

//just open he about activity
public class About extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
	
}
