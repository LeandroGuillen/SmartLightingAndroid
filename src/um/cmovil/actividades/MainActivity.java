package um.cmovil.actividades;

import um.cmovil.R;
import um.cmovil.modelo.Controlador;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Controlador.setUserAgent("Jara");
		Controlador.setKey("TestKey");
		Controlador.setServer("192.168.1.55:8080");

	}

	public void verListaFarolas(View view) {
		Intent intent = new Intent(this, FarolaListActivity.class);
		startActivity(intent);
	}

}
