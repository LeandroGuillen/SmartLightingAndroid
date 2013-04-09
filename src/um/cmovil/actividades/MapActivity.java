package um.cmovil.actividades;

import um.cmovil.R;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapActivity extends Activity {

	
	


    MapView map;
    MapController controller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = (MapView) findViewById(R.id.map);
		controller = map.getController();

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
		GeoPoint mapCenter = new GeoPoint(lat, lng);
		controller.setCenter(mapCenter);
		controller.setZoom(15);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
