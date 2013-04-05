package um.cmovil.actividades;

import java.util.LinkedList;
import java.util.List;

import um.cmovil.R;
import um.cmovil.actividades.adaptadores.FarolaAdapter;
import um.cmovil.modelo.recursos.Farola;
import um.cmovil.util.DownloadListener;
import um.cmovil.util.HTTPAsyncTask;
import um.cmovil.util.HTTPRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class FarolaListActivity extends Activity {
	FarolaAdapter farolaAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_farola_list);
		
		prepararLista();

		HTTPRequest statusRequest = new HTTPRequest(this, "/temp/temp0", new MyDownloadListener());
		new HTTPAsyncTask().execute(statusRequest);
	}

	private void prepararLista() {

		// We get the ListView component from the layout
		ListView lv = (ListView) findViewById(R.id.listViewFarolas);

		// TODO Farolas de prueba: Obtener lista de farolas del servidor
		List<Farola> misFarolas = new LinkedList<Farola>();
		misFarolas.add(new Farola(false, 20));
		misFarolas.add(new Farola(false, 30));
		misFarolas.add(new Farola(true, 64));
		misFarolas.add(new Farola(false, 19));

		farolaAdapter = new FarolaAdapter(this, misFarolas);
		lv.setAdapter(farolaAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.farola_list, menu);
		return true;
	}

	private class MyDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(Object result) {
			Context context = (Context) FarolaListActivity.this;

			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Information");
			dialog.setMessage((String) result);
			dialog.setPositiveButton("OK", null);
			dialog.show();
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(FarolaListActivity.this, "No se pudo realizar la conexión", Toast.LENGTH_SHORT).show();
		}
	}
}
