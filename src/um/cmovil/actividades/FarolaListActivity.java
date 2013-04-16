package um.cmovil.actividades;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import um.cmovil.R;
import um.cmovil.actividades.adaptadores.FarolaAdapter;
import um.cmovil.modelo.Controlador;
import um.cmovil.modelo.recursos.Farola;
import um.cmovil.util.DownloadListener;
import um.cmovil.util.HTTPAsyncTask;
import um.cmovil.util.HTTPRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FarolaListActivity extends Activity {
	FarolaAdapter farolaAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_farola_list);

		prepararLista();

		HTTPRequest statusRequest = new HTTPRequest(this, "/resources/streetlight/testlist", new MyDownloadListener());
		new HTTPAsyncTask().execute(statusRequest);
	}

	private void prepararLista() {

		// We get the ListView component from the layout
		final ListView lv = (ListView) findViewById(R.id.listViewFarolas);

		farolaAdapter = new FarolaAdapter(this, Controlador.getListaFarolas());
		lv.setAdapter(farolaAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Farola f = (Farola) lv.getAdapter().getItem(position);

				FarolaDialog fd = new FarolaDialog(FarolaListActivity.this, f);
				fd.show();
			}
		});

	}

	private class MyDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			try {
				HttpEntity httpEntity = response.getEntity();
				String result = null;
				result = EntityUtils.toString(httpEntity);

				// Actualizar datos locales
				Controlador.addFarolasFromJSON(result);

				// Mostrar mensaje por pantalla
				Context context = (Context) FarolaListActivity.this;
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("Information");
				dialog.setMessage(result);
				dialog.setPositiveButton("OK", null);
				dialog.show();
			} catch (JSONException e) {
				Toast.makeText(FarolaListActivity.this, "Error al recibir los datos del servidor", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (ParseException e) {
				Toast.makeText(FarolaListActivity.this, "Error al recibir los datos del servidor", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(FarolaListActivity.this, "Error al recibir los datos del servidor", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(FarolaListActivity.this, "No se pudo realizar la conexión", Toast.LENGTH_SHORT).show();
		}
	}
}
