package um.cmovil.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import um.cmovil.modelo.recursos.Tiempo;
import android.annotation.SuppressLint;

public class ControladorTiempo {

	// private static int MAX_TIEMPOS = 10;
	// private static Queue<Tiempo> listaTiempos = new LinkedList<Tiempo>();
	// private static Iterator<Tiempo> primero;
	private static Tiempo ultimo;

	@SuppressLint("SimpleDateFormat")
	public static void addTiempo(HttpResponse response) throws UnsupportedEncodingException, IllegalStateException, IOException, JSONException, ParseException {
		Tiempo t = new Tiempo();

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		JSONTokener tokener = new JSONTokener(builder.toString());
		JSONObject obj = new JSONObject(tokener);

		t.setTemperatura(obj.getInt("temperatura"));
		t.setVientoMedio(obj.getInt("vientoMedio"));
		t.setVientoRacha(obj.getInt("vientoRacha"));
		t.setPrecipitaciones(obj.getInt("precipitaciones"));
		t.setNubes(obj.getInt("nubes"));
		t.setHumedad(obj.getInt("humedad"));
		t.setPresion(obj.getInt("presion"));
		t.setFecha(obj.getLong("fecha"));

		putUltimoTiempo(t);

	}

	// /**
	// * Obtiene una lista ordenada por fecha de los diez últimos tiempos
	// * obtenidos.
	// *
	// * @return
	// */
	// public static List<Tiempo> getUltimosTiempos() {
	// List<Tiempo> nueva = new LinkedList<Tiempo>();
	//
	// for (int i = 0; i < MAX_TIEMPOS; i++) {
	// Tiempo t = listaTiempos.get((tiempoUltimo + i) % MAX_TIEMPOS);
	// nueva.add(t);
	// }
	//
	// return nueva;
	// }

	public static Tiempo getUltimoTiempo() {
		return ultimo;
	}

	private static void putUltimoTiempo(Tiempo t) {
		ultimo = t;
		// if (listaTiempos.isEmpty()) {
		// listaTiempos.add(t);
		// listaTiempos.
		// primero = t;
		// } else {
		// if (listaTiempos.size() < MAX_TIEMPOS) {
		// listaTiempos.add(t);
		// } else {
		//
		// }
		// }
		// listaTiempos.remove(primero);
	}
}
