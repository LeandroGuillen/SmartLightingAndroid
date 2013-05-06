package um.cmovil.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import um.cmovil.actividades.FarolaDialog;
import um.cmovil.actividades.adaptadores.FarolaAdapter;
import um.cmovil.modelo.recursos.Farola;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationOverlay extends ItemizedOverlay<OverlayItem> {
	private List<Drawable> mMarkers;
	private List<Farola> listaFarolas;
	private Context mContext;
	private FarolaAdapter farolaAdapter;
	private GeoPoint myLocation;

	public LocationOverlay(Context context, Drawable marker) {
		super(boundCenterBottom(marker));
		mContext = context;

		// listaFarolas = ControladorFarolas.getListaFarolas();
	}

	public void setItems(List<Farola> items, ArrayList<Drawable> drawables, GeoPoint myLocation) {
		listaFarolas = items;
		mMarkers = drawables;
		this.myLocation = myLocation; 

		populate();
	}

	protected OverlayItem createItem(int i) {
		OverlayItem item = new OverlayItem(listaFarolas.get(i).getGeoPoint(), null, null);
		item.setMarker(boundCenterBottom(mMarkers.get(0)));
		return item;
	}
	
/*	protected OverlayItem createMyItem(){
		OverlayItem item = new OverlayItem(myLocation, null, null);
		item.setMarker(boundCenterBottom(mMarkers.get(1)));
		return item;
	}
*/
	
	// TODO : Posibles soluciones: 1- A–adir otro overlay para el usuario. Combinar lista de farolas y punto de usuario
	
	
	
	
	public int size() {
		return listaFarolas.size();
	}

	protected boolean onTap(int i) {

		new FarolaDialog(mContext, listaFarolas.get(i), new FarolaUpdateListener())
				.show();

		return true;
	}

	private class FarolaUpdateListener implements DownloadListener {

		@Override
		public void downloadOk(HttpResponse response) {
			// Actualizar la UI del cliente
			farolaAdapter.notifyDataSetChanged();
			try {
				// Interpretar la respuesta
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();

				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONObject json = new JSONObject(tokener);

				// Muestra en la pantalla la respuesta del servidor

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void downloadFailed() {
		}
	}

}