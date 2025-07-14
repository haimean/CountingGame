package vn.edu.fpt.countinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String CHOICES = "pref_numberOfChoices";


    private boolean preferencesChanged = true; // did preferences change?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {
            // now that the default preferences have been set,
            // initialize QuizFragment and start the quiz
            GameFragment gameFragment = (GameFragment) getFragmentManager().findFragmentById(R.id.quizFragment);
            gameFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
//            gameFragment.resetQuiz();
            preferencesChanged = false;
        }
    } // end method onStart

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu); // inflate the menu
        return true;
    }

    // displays SettingsActivity when running on a phone
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    private OnSharedPreferenceChangeListener preferenceChangeListener = new OnSharedPreferenceChangeListener() {
        // called when the user changes the app's preferences
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            preferencesChanged = true; // user changed app settings
            GameFragment gameFragment = (GameFragment) getFragmentManager().findFragmentById(R.id.quizFragment);

            if (key.equals(CHOICES)) // # of choices to display changed
            {
                gameFragment.updateGuessRows(sharedPreferences);
//                gameFragment.resetGame();
            }else{

            }
            Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
        } // end method onSharedPreferenceChanged
    }; // end anonymous inner class

}