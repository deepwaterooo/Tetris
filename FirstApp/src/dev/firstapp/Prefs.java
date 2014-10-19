package dev.firstapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

//save actual screen and open another, when it returns, it will be on the same screen
public class Prefs extends PreferenceActivity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//addPreferencesFromResource(R.xml.setting);
		}
}
