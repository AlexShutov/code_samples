import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class SampleWithTilesOverlay extends Activity {

	private TilesOverlay mTilesOverlay;
	private MapView mapView;
	private MapTileProviderBasic mProvider;
	private RelativeLayout rl;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup base map
		rl = new RelativeLayout(this);

		this.mapView = new MapView(this);
		this.mapView.setTilesScaledToDpi(true);
		rl.addView(this.mapView, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		this.mapView.setBuiltInZoomControls(true);

		// zoom to the netherlands
		this.mapView.getController().setZoom(7);
		this.mapView.getController().setCenter(new GeoPoint(51500000, 5400000));

		// Add tiles layer
		mProvider = new MapTileProviderBasic(getApplicationContext());
		mProvider.setTileSource(TileSourceFactory.FIETS_OVERLAY_NL);
		this.mTilesOverlay = new TilesOverlay(mProvider, this.getBaseContext());
		this.mapView.getOverlays().add(this.mTilesOverlay);

		this.setContentView(rl);
	}


	@Override
	public void onDestroy(){
		super.onDestroy();
		if (mapView !=null)
			mapView.onDetach();
		mapView =null;
		if (mProvider!=null)
			mProvider.detach();
		mProvider = null;
	}
}