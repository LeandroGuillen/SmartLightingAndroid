package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.Controlador;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	SharedPreferences formStore;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Controlador.setUserAgent("Jara");
		Controlador.setKey("TestKey");
		Controlador.setServer("192.168.1.134:8080");

		// File defined in PREFS_NAME
		formStore = getSharedPreferences(PREFS_NAME, 0);

		Toast.makeText(MainActivity.this, formStore.getString("Cookie", ""),
				Toast.LENGTH_LONG).show();

	}

	public void verListaFarolas(View view) {
		Intent intent = new Intent(this, FarolaListActivity.class);
		startActivity(intent);
	}

}
