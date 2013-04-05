package um.cmovil.util;

import um.cmovil.modelo.Controlador;
import android.content.Context;

public class HTTPRequest {
	private Context context;
	private String resource;
	private DownloadListener dl;

	public HTTPRequest(Context context, String resource, DownloadListener donwloadListener) {
		this.context = context;
		this.resource = resource;
		this.dl = donwloadListener;
	}

	public String getResource() {
		return resource;
	}

	public Context getContext() {
		return context;
	}

	public DownloadListener getDownloadListener() {
		return dl;
	}

	public String getURL() {
		return "http://" + Controlador.getServer() + ":" + Controlador.getPort() + resource;
	}

}
