package uk.ac.horizon.tableware;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TWPreferenceActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}
	
}
