package vn.edu.fpt.countinggame;

import android.preference.PreferenceFragment;
import android.os.Bundle;
public class SettingsFragment extends PreferenceFragment {
    // creates preferences GUI from preferences.xml file in res/xml

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences); // load from XML
    }
} // end class SettingsFragment
