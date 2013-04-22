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

		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = manager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		int lat, lng;
		if (location != null) {
			// Convert to microdegrees
			lat = (int) (location.getLatitude() * 1000000);
			lng = (int) (location.getLongitude() * 1000000);
		} else {
			// Default to Google HQ
			lat = 37427222;
			lng = -122099167;
		}
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
			updateMap();
		}

		private void updateMap() {
			if (currentLocation == null) {
				locationView.setText("Determining Your Location...");
			} else {
				GeoPoint mapCenter = new GeoPoint(
						(int) currentLocation.getLatitude() * 10000,
						(int) currentLocation.getLongitude() * 1000);

				controller.setCenter(mapCenter);
				controller.setZoom(15);

			}
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
