package um.cmovil.modelo;

public class Controlador {
	private static String userAgent;
	private static String key;
	private static String server;

	private static String cookie = "";
	private static double latitude = 0.0;
	private static double longitude = 0.0;
	
	private static HashType hashType;

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

	public static void setCookie(String cookie) {
		Controlador.cookie = cookie;
	}

	public static String getCookie() {
		return Controlador.cookie;
	}

	public static double getLatitude() {

		return latitude;
	}

	public static double getLongitude() {
		return longitude;
	}

	public static void setLatitude(double d) {
		Controlador.latitude = d;

	}

	public static void setLongitude(double d) {
		Controlador.longitude = d;
	}

	public static HashType getHashType() {
		return hashType;
	}

	public static void setHashType(HashType hashType) {
		Controlador.hashType = hashType;
	}

}
