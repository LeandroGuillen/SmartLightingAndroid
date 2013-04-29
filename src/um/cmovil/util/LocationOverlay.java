package um.cmovil.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationOverlay extends ItemizedOverlay<OverlayItem> {
	private List<GeoPoint> mItems;
	private List<Drawable> mMarkers;
	private Context mContext;

	public LocationOverlay(Context context, Drawable marker) {
		super(boundCenterBottom(marker));
		mContext = context;
	}

	public void setItems(ArrayList<GeoPoint> items,
			ArrayList<Drawable> drawables) {
		mItems = items;
		mMarkers = drawables;
		populate();
	}

	protected OverlayItem createItem(int i) {
		OverlayItem item = new OverlayItem(mItems.get(i), null, null);
		item.setMarker(boundCenterBottom(mMarkers.get(0)));
		return item;
	}

	public int size() {
		return mItems.size();
	}

	protected boolean onTap(int i) {
		
		//new FarolaDialog(this,)
		return true;
	}
}