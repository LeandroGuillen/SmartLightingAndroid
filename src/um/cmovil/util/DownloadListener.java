package um.cmovil.util;

import org.apache.http.HttpResponse;

public interface DownloadListener {
	public void downloadOk(HttpResponse result);
	public void downloadFailed();
}
