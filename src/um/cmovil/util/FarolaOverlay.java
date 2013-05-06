package um.cmovil.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import um.cmovil.actividades.FarolaDialog;
import um.cmovil.modelo.ControladorFarolas;
import um.cmovil.modelo.recursos.Farola;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class FarolaOverlay extends ItemizedOverlay<OverlayItem> {
	private List<Drawable> mMarkers;
	private List<Farola> listaFarolas;
	private Context mapContext;
	private MapView mapView;

	public FarolaOverlay(Drawable marker, Context mapContext, MapView mapView) {
		super(boundCenterBottom(marker));
		this.mapContext = mapContext;
		this.mapView = mapView;
		listaFarolas = ControladorFarolas.getListaFarolas();

	}

	public void setItems(List<Farola> items, ArrayList<Drawable> drawables) {
		listaFarolas = items;
		mMarkers = drawables;

		populate();
	}

	protected OverlayItem createItem(int i) {

		OverlayItem item = new OverlayItem(listaFarolas.get(i).getGeoPoint(),
				null, null);

		if (listaFarolas.get(i).isEncendida())
			item.setMarker(boundCenterBottom(mMarkers.get(1)));
		else
			item.setMarker(boundCenterBottom(mMarkers.get(0)));

		return item;
	}

	public int size() {
		return listaFarolas.size();

	}

	protected boolean onTap(int i) {

		new FarolaDialog(mapContext, listaFarolas.get(i),
				new FarolaUpdateListener(i)).show();

		return true;
	}

	private class FarolaUpdateListener implements DownloadListener {
		private int itemIndex;

		public FarolaUpdateListener(int i){

			
			itemIndex = i;
			
		}
		@Override
		public void downloadOk(HttpResponse response) {
			// Actualizar la UI del cliente // Muestra en la pantalla la
			// respuesta del servidor

			
			if (listaFarolas.get(itemIndex).isEncendida())
				getItem(itemIndex).setMarker(boundCenterBottom(mMarkers.get(1)));
			else
				getItem(itemIndex).setMarker(boundCenterBottom(mMarkers.get(0)));
			
			mapView.postInvalidate();
		}

		@Override
		public void downloadFailed() {
		}
	}

}