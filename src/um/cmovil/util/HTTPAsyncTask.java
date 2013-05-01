package um.cmovil.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import um.cmovil.modelo.Controlador;
import android.content.Context;
import android.os.AsyncTask;

public class HTTPAsyncTask extends AsyncTask<HTTPRequest, String, HttpResponse> {

	private DownloadListener downloadListener;

	@Override
	protected HttpResponse doInBackground(HTTPRequest... obj) {
		HTTPRequest httpreq = obj[0];
		Context appContext = httpreq.getContext();
		downloadListener = httpreq.getDownloadListener();
		String URL = httpreq.getURL();
		String userAgent = Controlador.getUserAgent();
		String key = Controlador.getKey();

		HttpResponse response = null;

		if (Utils.isNetworkOk(appContext)) {
			try {

				String time = Utils
						.getCurrentDateAsStringInRFC1123Format(Utils.GMT_TIMEZONE);

				HttpGet get = new HttpGet(URL);
				get.addHeader("Accept", "application/json");
				get.addHeader("User-Agent", userAgent);
				get.addHeader(HTTP.DATE_HEADER, time);
				get.addHeader("Content-Length", "0");

				if (!Controlador.getCookie().isEmpty())

					get.addHeader("Cookie", Controlador.getCookie());

				else

					get.addHeader("apiKey", Utils.md5(Utils.md5(key) + time));

				HttpClient client = new DefaultHttpClient();
				response = client.execute(get);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return response;
	}

	@Override
	protected void onPostExecute(HttpResponse response) {
		if (response != null)
			downloadListener.downloadOk(response);
		else
			downloadListener.downloadFailed();
	}
}