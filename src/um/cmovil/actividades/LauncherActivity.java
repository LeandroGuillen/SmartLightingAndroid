package um.cmovil.actividades;

import um.cmovil.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LauncherActivity extends Activity {

	SharedPreferences formStore;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);

	}

	protected void onStart() {
super.onStart();
		formStore = getSharedPreferences(PREFS_NAME, 0);

		if (formStore.getString("Cookie", "").equals("")) {

			logIn();
		} else {
			mainActivity();
		}

		
		
		
	}

	private void logIn() {
		// Lanzar la actividad nueva
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	private void mainActivity() {
		// Lanzar la actividad nueva
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
