package um.cmovil.actividades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import um.cmovil.R;
import um.cmovil.actividades.adaptadores.FarolaAdapter;
import um.cmovil.modelo.ControladorFarolas;
import um.cmovil.modelo.recursos.Farola;
import um.cmovil.util.DownloadListener;
import android.app.Activity;
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
	}

	private void prepararLista() {

		// We get the ListView component from the layout
		final ListView lv = (ListView) findViewById(R.id.listViewFarolas);

		farolaAdapter = new FarolaAdapter(this, ControladorFarolas.getListaFarolas());
		lv.setAdapter(farolaAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Farola f = (Farola) lv.getAdapter().getItem(position);
				new FarolaDialog(FarolaListActivity.this, f, new FarolaUpdateListener()).show();
			}
		});

	}

	private class FarolaUpdateListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			// Actualizar la UI del cliente
			farolaAdapter.notifyDataSetChanged();
			try {
				// Interpretar la respuesta
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();

				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONObject json = new JSONObject(tokener);

				// Muestra en la pantalla la respuesta del servidor
				Toast.makeText(FarolaListActivity.this, json.getString("status"), Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				Toast.makeText(FarolaListActivity.this, "Error de E/S", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(FarolaListActivity.this, "Error al decodificar la respuesta JSON", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}

		@Override
		public void downloadFailed() {
			Toast.makeText(FarolaListActivity.this, "No se pudo guardar la farola", Toast.LENGTH_SHORT).show();
		}
	}
}
