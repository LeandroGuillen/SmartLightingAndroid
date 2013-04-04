package um.cmovil.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

public class HTTPAsyncTask extends AsyncTask<HTTPRequest, String, String> {

	private DownloadListener downloadListener;

	@Override
	protected String doInBackground(HTTPRequest... obj) {
		HTTPRequest httpreq = obj[0];
		Context appContext = httpreq.getContext();
		downloadListener = httpreq.getDownloadListener();
		String URL = httpreq.getURL();
		String userAgent = httpreq.getUserAgent();
		String key = httpreq.getKey();

		String result = null;

		if (Utils.isNetworkOk(appContext)) {
			try {
				String time = Utils.getCurrentDateAsStringInRFC1123Format(Utils.GMT_TIMEZONE);

				HttpGet get = new HttpGet(URL);
				get.addHeader("Accept", "application/json");
				get.addHeader("User-Agent", userAgent);
				get.addHeader(HTTP.DATE_HEADER, time);
				get.addHeader("apiKey", Utils.md5(key + time));
				get.addHeader("Content-Length", "0");

				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(get);

				// Get response
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null)
			downloadListener.downloadOk(result);
		else
			downloadListener.downloadFailed();
	}
}