package um.cmovil.modelo;

public class Controlador {
	private static String userAgent;
	private static String key;
	private static String server;
	private static Integer port;

	public static String getServer() {
		return server;
	}

	public static void setServer(String server) {
		Controlador.server = server;
	}

	public static Integer getPort() {
		return port;
	}

	public static void setPort(Integer port) {
		Controlador.port = port;
	}

	public static String getUserAgent() {
		return userAgent;
	}

	public static void setUserAgent(String userAgent) {
		Controlador.userAgent = userAgent;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Controlador.key = key;
	}

}
