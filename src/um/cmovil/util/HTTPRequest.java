package um.cmovil.util;

import android.content.Context;

public class HTTPRequest {
	private Context context;
	private String server;
	private Integer port;
	private String resource;
	private DownloadListener dl;
	private String userAgent;
	private String key;

	public HTTPRequest(Context context, String server, Integer port, String resource, DownloadListener donwloadListener, String userAgent, String key) {
		this.context = context;
		this.server = server;
		this.port = port;
		this.resource = resource;
		this.dl = donwloadListener;
		this.userAgent = userAgent;
		this.key = key;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getKey() {
		return key;
	}

	public String getResource() {
		return resource;
	}

	public Integer getPort() {
		return port;
	}

	public Context getContext() {
		return context;
	}

	public String getServer() {
		return server;
	}

	public DownloadListener getDownloadListener() {
		return dl;
	}

	public String getURL() {
		return "http://" + server + ":" + port + resource;
	}

}
