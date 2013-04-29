package um.cmovil.actividades;

import java.util.ArrayList;
import java.util.List;

import um.cmovil.R;
import um.cmovil.modelo.ControladorFarolas;
import um.cmovil.modelo.recursos.Farola;
import um.cmovil.util.LocationOverlay;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapViewActivity extends MapActivity {

	LocationManager manager;
	Location currentLocation;
	String selectedProvider;

	TextView locationView;
	MapView map;
	MapController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		map = (MapView) findViewById(R.id.map);
		controller = map.getController();

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Ask the user to enable GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Location Manager");
			builder.setMessage("We would like to use your location, but GPS is currently disabled.\n"
					+ "Would you like to change these settings now?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Launch settings, allowing user to make a change
							Intent i = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(i);
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// No location service, no Activity
							finish();
						}
					});
			builder.create().show();
		}

		// Two arrays, one for the location and other for the images

		ArrayList<GeoPoint> locations = new ArrayList<GeoPoint>();
		ArrayList<Drawable> images = new ArrayList<Drawable>();

		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = manager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		int lat, lng;
		if (location != null) {

			// Convert to microdegrees
			lat = (int) (location.getLatitude() * 1000000);
			lng = (int) (location.getLongitude() * 1000000);
			Toast.makeText(this, lat + " " + lng, Toast.LENGTH_LONG).show();

		} else {
			// Default to Google HQ
			lat = 37427222;
			lng = -122099167;
		}

		/*
		 * EJEMPLO PARA MOSTRAR UNA FAROLA*
		 */

		// TODO :_ Interfaz para recoger datos de farolas y a–adirlas a
		// locations.

		List<Farola> listaFarolas = ControladorFarolas.getListaFarolas();

		for (Farola farola : listaFarolas) {

			locations.add(farola.getGeoPoint());
		}

		// Google HeadQuarters 37.427,-122.099 set the icon of Google
		locations.add(new GeoPoint(lat, lng));
		images.add(getResources().getDrawable(R.drawable.bombilla_off));
		
		// Call the auxiliary class "LocationOverlay" based on ItemizedOverlay
		LocationOverlay myOverlay = new LocationOverlay(this, getResources()
				.getDrawable(R.drawable.bombilla_off));
		myOverlay.setItems(locations, images);

		// Add to the map the overlay
		map.getOverlays().add(myOverlay);

		// Register for updates
		int minTime = 100;
		float minDistance = 0;
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
				minDistance, listener);
		GeoPoint mapCenter = new GeoPoint(lat, lng);
		controller.setCenter(mapCenter);
		controller.setZoom(15);

	}

	// Required abstract method, return false
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			currentLocation = location;
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			controller.setCenter(point);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};

}