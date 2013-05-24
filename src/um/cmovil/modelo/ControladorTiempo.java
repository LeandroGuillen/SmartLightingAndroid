package um.cmovil.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import um.cmovil.modelo.recursos.Tiempo;
import android.annotation.SuppressLint;

public class ControladorTiempo {

	private static List<Tiempo> listaTiempos = new LinkedList<Tiempo>();

	@SuppressLint("SimpleDateFormat")
	public static void addTiempo(HttpResponse response) throws UnsupportedEncodingException, IllegalStateException, IOException, JSONException, ParseException {
		Tiempo t = new Tiempo();

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		JSONTokener tokener = new JSONTokener(builder.toString());
		JSONArray array = new JSONArray(tokener);

		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);

			t.setTemperatura(obj.getInt("temperatura"));
			t.setVientoMedio(obj.getInt("vientoMedio"));
			t.setVientoRacha(obj.getInt("vientoRacha"));
			t.setPrecipitaciones(obj.getInt("precipitaciones"));
			t.setNubes(obj.getInt("nubes"));
			t.setHumedad(obj.getInt("humedad"));
			t.setPresion(obj.getInt("presion"));
			t.setFecha(obj.getLong("fecha"));

			listaTiempos.add(t);
		}
	}

	/**
	 * Obtiene una lista ordenada por fecha de los diez últimos tiempos
	 * obtenidos.
	 * 
	 * @return
	 */
	public static List<Tiempo> getUltimosTiempos() {
		return listaTiempos;
	}

	public static Tiempo getUltimoTiempo() {
		return listaTiempos.get(listaTiempos.size()-1);
	}
}
