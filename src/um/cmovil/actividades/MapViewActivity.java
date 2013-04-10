package um.cmovil.actividades;

import um.cmovil.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = (MapView) findViewById(R.id.map);
		controller = map.getController();
		map.setSatellite(true);

		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

		// Get a cached location, if it exists
		currentLocation = manager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// Register for updates
		int minTime = 10;
		float minDistance = 0;
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
				minDistance, listener);

		int lat, lng;
		if (currentLocation != null) {
			// Convert to microdegrees
			lat = (int) (currentLocation.getLatitude() * 1000000);
			lng = (int) (currentLocation.getLongitude() * 1000000);

			Toast.makeText(getApplicationContext(),
					("lat: " + lat + "    " + "long: " + lng),
					Toast.LENGTH_LONG).show();
		} else {
			// Default to Google HQ
			lat = 38024076;
			lng = -1173600;
		}
		GeoPoint mapCenter = new GeoPoint(lat, lng);
		controller.setCenter(mapCenter);
		controller.setZoom(30);
	}

	// Required abstract method, return false

	protected boolean isRouteDisplayed() {
		return false;
	}

	private LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			currentLocation = location;
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
