package um.cmovil.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import um.cmovil.actividades.adaptadores.FarolaAdapter;
import um.cmovil.modelo.recursos.Farola;

import com.google.android.maps.GeoPoint;

public class Controlador {
	private static String userAgent;
	private static String key;
	private static String server;
	private static Map<String, Farola> farolas = new HashMap<String, Farola>();
	private static String cookie;

	public static String getServer() {
		return server;
	}

	public static void setServer(String server) {
		Controlador.server = server;
	}

	public static String getUserAgent() {
		return userAgent;
	}

	// FIXME : User-Agent parece ser siempre machacado para colocar el
	// navegador. La cabecera deber�a ser "User";
	public static void setUserAgent(String userAgent) {
		Controlador.userAgent = userAgent;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Controlador.key = key;
	}

	public static void addFarola(Farola f) {
		farolas.put(f.getNombre(), f);
	}

	public static Farola getFarola(String nombre) {
		return farolas.get(nombre);
	}

	public static List<Farola> getListaFarolas() {
		List<Farola> misFarolas = new LinkedList<Farola>();
		misFarolas.addAll(farolas.values());
		return misFarolas;
	}

	public static void addFarolasFromJSON(HttpResponse response, FarolaAdapter adapter) throws JSONException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		JSONTokener tokener = new JSONTokener(builder.toString());
		JSONArray lista = new JSONArray(tokener);

		adapter.clear();

		for (int i = 0; i < lista.length(); i++) {
			JSONObject json = lista.getJSONObject(i);

			String nombre = json.getString("nombre");
			boolean encendida = json.getBoolean("encendida");
			int dim = json.getInt("dim");
			int lon = json.getInt("lon");
			int lat = json.getInt("lat");

			Farola f = new Farola(nombre, encendida, dim);
			f.setGeoPoint(new GeoPoint(lat, lon));

			addFarola(f);
			adapter.add(f);
		}

		adapter.notifyDataSetChanged();
	}

	public static void setCookie(String cookie) {
		Controlador.cookie = cookie;
	}

	public static String getCookie() {
		return Controlador.cookie;
	}

}
