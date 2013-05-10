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
import um.cmovil.modelo.Controlador;
import um.cmovil.modelo.ControladorFarolas;
import um.cmovil.modelo.recursos.Farola;
import um.cmovil.util.DownloadListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FarolaListActivity extends Activity {
	private FarolaAdapter farolaAdapter;
	LocationManager manager;

	Location currentLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_farola_list);

		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Toast.makeText(FarolaListActivity.this,
				"lat" + Controlador.getLatitude(), Toast.LENGTH_LONG).show();

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Ask the user to enable GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Location Manager");
			builder.setMessage("We would like to use your location, but GPS is currently disabled.\n"
					+ "Would you like to change these settings now?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Launch settings, allowing user to make a change
							Intent i = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(i);
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// No location service, no Activity
							finish();
						}
					});
			builder.create().show();

		}
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = manager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {

			Controlador.setLatitude(location.getLatitude() * 1000000);
			Controlador.setLongitude(location.getLongitude() * 1000000);

		} 
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0,
				listener);

		prepararLista();
	}

	private void prepararLista() {

		// We get the ListView component from the layout
		final ListView lv = (ListView) findViewById(R.id.listViewFarolas);

		farolaAdapter = new FarolaAdapter(this,
				ControladorFarolas.getListaFarolas());
		lv.setAdapter(farolaAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Farola f = (Farola) lv.getAdapter().getItem(position);
				new FarolaDialog(FarolaListActivity.this, f,
						new FarolaUpdateListener()).show();
			}
		});

	}

	// TODO : Hacer publica
	private class FarolaUpdateListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			// Actualizar la UI del cliente
			farolaAdapter.notifyDataSetChanged();
			try {
				// Interpretar la respuesta
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();

				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONObject json = new JSONObject(tokener);

				// Muestra en la pantalla la respuesta del servidor
				Toast.makeText(FarolaListActivity.this,
						json.getString("status"), Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				Toast.makeText(FarolaListActivity.this, "Error de E/S",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(FarolaListActivity.this,
						"Error al decodificar la respuesta JSON",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}

		@Override
		public void downloadFailed() {
			Toast.makeText(FarolaListActivity.this,
					"No se pudo guardar la farola", Toast.LENGTH_SHORT).show();
		}
	}

	private LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			Controlador.setLatitude(location.getLatitude() * 1E6);
			Controlador.setLongitude(location.getLongitude() * 1E6);
			farolaAdapter.notifyDataSetChanged();
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};
}
