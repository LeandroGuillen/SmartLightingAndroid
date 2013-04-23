package um.cmovil.actividades;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.http.HttpResponse;
import org.json.JSONException;

import um.cmovil.R;
import um.cmovil.modelo.Controlador;
import um.cmovil.modelo.ControladorFarolas;
import um.cmovil.modelo.ControladorTiempo;
import um.cmovil.modelo.recursos.Tiempo;
import um.cmovil.util.DownloadListener;
import um.cmovil.util.HTTPAsyncTask;
import um.cmovil.util.HTTPRequest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	SharedPreferences formStore;
	public static final String PREFS_NAME = "MyPrefsFile";
	/**
	 * Verdadero - Estamos leyendo farolas para mostrar la lista en pantalla
	 * Falso - Estamos leyendo farolas por cualquier otro motivo
	 */
	private boolean listaFarolasIntentRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		formStore = getSharedPreferences(PREFS_NAME, 0);

		Controlador.setServer(formStore.getString("server", ""));
		TextView tvServer = (TextView) findViewById(R.id.maServidor);
		tvServer.setText(Controlador.getServer());

		obtenerDatosTiempo();

		Toast.makeText(MainActivity.this, formStore.getString("Cookie", ""),
				Toast.LENGTH_LONG).show();

		Toast.makeText(MainActivity.this, formStore.getString("Cookie", ""), Toast.LENGTH_LONG).show();

		listaFarolasIntentRunning = false;
	}

	public void obtenerDatosTiempo() {
		// Cargar datos del tiempo
		HTTPRequest statusRequest = new HTTPRequest(this, "/resources/weather",
				new TiempoDownloadListener());
		new HTTPAsyncTask().execute(statusRequest);

	}

	public void verListaFarolas(View view) {
		// Cuando quieres ver la lista de farolas es cuando se descargan del
		// servidor. Solo actualizar si realmente se necesita. El controlador
		// implementa un temporizador actualmente.
		if (ControladorFarolas.necesitoActualizar()) {
			listaFarolasIntentRunning = true;
			HTTPRequest request = new HTTPRequest(this, "/resources/streetlight/testlist", new FarolasDownloadListener());
			new HTTPAsyncTask().execute(request);
		} else {
			// Lanzar la actividad nueva
			Intent intent = new Intent(MainActivity.this,
					FarolaListActivity.class);
			startActivity(intent);
		}
	}

	public void verMapa(View view) {
		// Cuando quieres ver la lista de farolas es cuando se descargan del
		// servidor. Solo actualizar si realmente se necesita. El controlador
		// implementa un temporizador actualmente.
		if (ControladorFarolas.necesitoActualizar()) {
			HTTPRequest request = new HTTPRequest(this, "/resources/streetlight/testlist", new FarolasDownloadListener());
			new HTTPAsyncTask().execute(request);
		} else {
			// Lanzar la actividad nueva
			Intent intent = new Intent(this, MapViewActivity.class);
			startActivity(intent);
		}
	}

	private class FarolasDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			try {
				// Actualizar datos locales
				ControladorFarolas.addFarolasFromJSON(response);

				// Solo si queriamos mostrar la lista de farolas debemos mostrar
				// la actividad de ListaFarolas
				if (listaFarolasIntentRunning) {
					// Lanzar la actividad nueva
					Intent intent = new Intent(MainActivity.this, FarolaListActivity.class);
					startActivity(intent);

					listaFarolasIntentRunning = false;
				}

			} catch (JSONException e) {
				Toast.makeText(
						MainActivity.this,
						"Error al formar el JSON de los datos recibidos del servidor",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						MainActivity.this,
						"Error de E/S al formar el JSON de los datos recibidos del servidor",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(MainActivity.this,
					"No se pudo realizar la conexión", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private class TiempoDownloadListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			String mensaje = "Recibidos los datos meteorologicos del servidor";
			try {
				ControladorTiempo.addTiempo(response);

				Tiempo t = ControladorTiempo.getUltimoTiempo();

				TextView tvTemp = (TextView) findViewById(R.id.maTemperatura);
				TextView tvVm = (TextView) findViewById(R.id.maVientoMedio);
				TextView tvVr = (TextView) findViewById(R.id.maVientoRachas);
				TextView tvPrec = (TextView) findViewById(R.id.maPrecipitaciones);
				TextView tvPres = (TextView) findViewById(R.id.maPresion);
				TextView tvHum = (TextView) findViewById(R.id.maHumedad);
				TextView tvNub = (TextView) findViewById(R.id.maNubes);

				tvTemp.setText(t.getTemperatura() + " ºC");
				tvVm.setText(t.getVientoMedio() + " km/h");
				tvVr.setText(t.getVientoRacha() + " km/h");
				tvPrec.setText(t.getPrecipitaciones() + " mm");
				tvPres.setText(t.getPresion() + " hPa");
				tvHum.setText(t.getHumedad() + "%");
				tvNub.setText(t.getNubes() + "%");

			} catch (UnsupportedEncodingException e) {
				mensaje = "Error al recibir los datos sobre el tiempo";
				e.printStackTrace();
			} catch (IllegalStateException e) {
				mensaje = "Error al recibir los datos sobre el tiempo";
				e.printStackTrace();
			} catch (IOException e) {
				mensaje = "Error al recibir los datos sobre el tiempo: E/S";
				e.printStackTrace();
			} catch (JSONException e) {
				mensaje = "Error al recibir los datos sobre el tiempo: JSON mal formado";
				e.printStackTrace();
			} catch (ParseException e) {
				mensaje = "Error al recibir los datos sobre el tiempo: formato de fecha mal formado";
				e.printStackTrace();
			} finally {
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void downloadFailed() {
			Toast.makeText(MainActivity.this,
					"No se pudo realizar la conexión", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
