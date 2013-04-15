package um.cmovil.modelo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import um.cmovil.modelo.recursos.Farola;

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

	// FIXME : User-Agent parece ser siempre machacado para colocar el navegador. La cabecera deber’a ser "User";
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

	public static void setCookie(String cookie) {
		Controlador.cookie = cookie;
	}
	
	public static String getCookie(){
		return Controlador.cookie;
	}

}
